package com.proj.commerce;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.ocpsoft.prettytime.PrettyTime;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;
import com.proj.commerce.models.Order;
import com.proj.commerce.repositories.ClientRepository;
import com.proj.commerce.repositories.OrderRepository;
import com.proj.commerce.service.OrderService;
import com.proj.commerce.service.ProductService;
import com.proj.commerce.ui.UI;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import net.miginfocom.swing.MigLayout;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatLineBorder;

public class Views extends UI {

    private ClientRepository clientRepository = (ClientRepository) UtilBean.getBean(ClientRepository.class);
    private OrderRepository orderRepository = (OrderRepository) UtilBean.getBean(OrderRepository.class);
    private ProductService productService = (ProductService) UtilBean.getBean(ProductService.class);
    private OrderService orderService = (OrderService) UtilBean.getBean(OrderService.class);

    private List<Product> products = productService.fetchProductListByStocks();
    private Client loggedInClient;
    private List<Order> clientOrders;

    private JFrame f = new JFrame();

    private JPanel navPnl;

    private JLabel title;
    private JButton account;
    private JButton sell;
    private JButton orders;
    private JButton loginButton;
    private JButton registerButton;
    private JButton logoutButton;

    private JPanel bodyPnl;
    private File file = null;

    private int wrapSize = 4;
    private boolean myOrder = false;

    private JPanel mainPnl = new JPanel(new BorderLayout());
    private PrettyTime relativeTime = new PrettyTime();

    public Views() {
        bodyPnl = new CustomPanel();
        bodyPnl.setLayout(new BorderLayout());

        mainPnl.add(nav(), BorderLayout.NORTH);
        bodyPnl.add(home(), BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(bodyPnl);
        scroll.putClientProperty(FlatClientProperties.STYLE_CLASS, "scroll");

        mainPnl.add(scroll, BorderLayout.CENTER);

        Image icon = Toolkit.getDefaultToolkit().getImage("./data/icon.png");

        f.add(mainPnl);
        f.setTitle("Commerce");
        f.setIconImage(icon);
        f.pack();
        f.setVisible(true);
        f.setSize(1000, 600);
        f.setMinimumSize(new Dimension(880, 400));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel card() {
        // grouped product panel
        MigLayout layout = new MigLayout(String.format(" wrap %d, alignx center", wrapSize));

        JPanel productsPnl = new CustomPanel();
        productsPnl.setLayout(layout);

        // per product panel

        List<JPanel> cardList = new ArrayList<>();
        products.forEach(product -> {
            JLabel itemImage = new JLabel();
            JPanel cardPnl = new JPanel(
                    new MigLayout(" fill,  insets panel, wrap, alignx center", "", "[50%][50%]"));
            try {
                BufferedImage thumbnail = ImageIO.read(new File(product.getImage()));
                Dimension maxSize = new Dimension(180, 180);
                BufferedImage resizedThumbnail = Scalr.resize(thumbnail, Method.BALANCED, maxSize.width,
                        maxSize.height);
                ImageIcon ss = new ImageIcon(resizedThumbnail);
                itemImage.setIcon(ss);
                itemImage.setMaximumSize(new Dimension(180, 180));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MouseAdapter mm = new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (loggedInClient != null) {
                        if (!myOrder) {
                            redirect(product(null, product));
                        } else {
                            redirect(product(cardList.indexOf(cardPnl), product));

                        }

                    } else {
                        redirect(loginForm());
                    }
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    cardPnl.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), FG1, 2, 20));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    cardPnl.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), BG0, 1, 12));
                }

            };

            CustomLabel itemDesc = new CustomLabel();
            itemDesc.setHtmlText(String.format(
                    "<div class='container'><hr><h2 class='title'>%s</h2><p class='value'>%.2f USD</p><br><p class='seller'>%s</p><hr><div class='divide'><p class='date'>%s</p></div></div>",
                    product.getTitle(), product.getPrice(), product.getClient().getUsername(),
                    relativeTime.format(product.getDate())));

            cardPnl.add(itemImage, "grow, alignx center");
            cardPnl.add(itemDesc, "grow");

            cardPnl.putClientProperty(FlatClientProperties.STYLE_CLASS, "roundedPanel");
            cardPnl.setMaximumSize(new Dimension(200, 350));
            cardPnl.setPreferredSize(new Dimension(200, 350));
            cardPnl.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), BG0, 1, 12));
            cardList.add(cardPnl);

            cardPnl.addMouseListener(mm);
        });
        f.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                if (c.getWidth() > 1000)
                    wrapSize = 5;
                else
                    wrapSize = 4;
                layout.setLayoutConstraints(String.format("wrap %d, alignx center", wrapSize));
                productsPnl.setLayout(layout);
            }

        });
        cardList.forEach(card -> {
            productsPnl.add(card);
        });

        return productsPnl;
    }

    public JPanel home() {

        JPanel productsMainPnl = new CustomPanel();
        productsMainPnl.setLayout(new BorderLayout());

        JPanel productsNavPnl = new CustomPanel();
        productsNavPnl.setLayout(new MigLayout("", "[][]"));
        productsNavPnl.putClientProperty(FlatClientProperties.STYLE_CLASS, "accPanel");

        JLabel search = new JLabel("Search: ");
        JTextField searchIn = new JTextField(30);
        JButton searchButton = new JButton(new ImageIcon("./data/find.png"));
        JButton searchReset = new JButton("Reset");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                products = productService.fetchProductListBySearch(searchIn.getText());
                redirect(home());
            }
        });
        searchReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                products = productService.fetchProductListByStocks();
                redirect(home());
            }
        });

        productsNavPnl.add(search);
        productsNavPnl.add(searchIn);
        productsNavPnl.add(searchButton);
        productsNavPnl.add(searchReset);

        productsMainPnl.add(productsNavPnl, BorderLayout.NORTH);
        productsMainPnl.add(card(), BorderLayout.CENTER);
        return productsMainPnl;
    }

    public JPanel account() {
        products = productService.fetchProductListByClient(loggedInClient.getId());

        JPanel accountPnl = new CustomPanel();
        accountPnl.setLayout(new BorderLayout());

        JPanel accountNavPnl = new CustomPanel();
        accountNavPnl.putClientProperty(FlatClientProperties.STYLE_CLASS, "accPanel");
        accountNavPnl.setLayout(new MigLayout("", "[][]"));

        String totalLi = String.format("Total Listings: **%d**", products.size());
        JLabel totalListings = new CustomLabel(String.format("<html>%s</html>", mdToHTML(totalLi)));

        String totalStr = String.format("Total Orders: **%d**", clientOrders.size());
        JLabel totalOrders = new CustomLabel(String.format("<html>%s</html>", mdToHTML(totalStr)));

        String wallAm = String.format("Wallet: **%.2f**", loggedInClient.getWallet());
        JLabel walletAmount = new CustomLabel(String.format("<html>%s</html>", mdToHTML(wallAm)));

        JButton addAmount = new CustomButton("+");
        addAmount.putClientProperty(FlatClientProperties.STYLE_CLASS, "green");
        addAmount.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(JOptionPane.showInputDialog(f, "Cash-In Amount"));
                    if (amount <= 0) {
                        throw new Exception("Cash-in amount must be greater than 0");
                    }
                    Client update = loggedInClient;
                    update.setWallet(update.getWallet() + amount);
                    clientRepository.save(update);
                    String wallAm = String.format("Wallet: **%.2f**", loggedInClient.getWallet());
                    walletAmount.setText(String.format("<html>%s</html>", mdToHTML(wallAm)));
                    mainPnl.revalidate();
                    mainPnl.repaint();

                } catch (NumberFormatException n1) {
                    JOptionPane.showMessageDialog(f, "Enter correct amount", "Invalid", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(f, e1.getMessage(), "Invalid", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        accountNavPnl.add(totalListings);
        accountNavPnl.add(totalOrders);
        accountNavPnl.add(walletAmount, "push, al right");
        accountNavPnl.add(addAmount);

        accountPnl.add(accountNavPnl, BorderLayout.NORTH);
        accountPnl.add(card(), BorderLayout.CENTER);
        return accountPnl;

    }

    public JPanel nav() {
        navPnl = new CustomPanel();
        navPnl.setLayout(new MigLayout("", "[][][]"));

        title = new CustomLabel("Home");
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h00");
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                myOrder = false;
                products = productService.fetchProductListByStocks();
                redirect(home());
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                title.setForeground(FG0);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                title.setForeground(FG1);
            }

        });
        account = new CustomButton();
        account.putClientProperty(FlatClientProperties.STYLE_CLASS, "blue");
        account.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(account());
            }

        });

        sell = new CustomButton("Sell");
        sell.putClientProperty(FlatClientProperties.STYLE_CLASS, "yellowD");

        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(productForm(null, false));
            }

        });

        orders = new CustomButton("My Orders");
        orders.putClientProperty(FlatClientProperties.STYLE_CLASS, "pink");

        orders.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                myOrder = true;
                products = orderService.fetchProductsByClientId(loggedInClient.getId());
                redirect(card());

            }

        });

        loginButton = new JButton("Login");
        loginButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "blue");
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(loginForm());

            }

        });
        logoutButton = new JButton("Logout");
        logoutButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "red");
        logoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInClient = null;
                clientOrders = null;
                products = null;
                navPnl.removeAll();
                navPnl.add(title);
                navPnl.add(loginButton, "push, al right");
                navPnl.add(registerButton);

                products = productService.fetchProductList();
                redirect(home());
            }

        });

        registerButton = new JButton("Register");
        registerButton.addActionListener(new UI().redirectListener(mainPnl, bodyPnl, registerForm()));
        registerButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "yellow");

        navPnl.add(title);

        navPnl.add(loginButton, "push,al right");
        navPnl.add(registerButton);
        return navPnl;

    }

    public JPanel loginForm() {
        JPanel pnl = new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new CustomLabel("Username: ");
        JTextField usernameIn = new JTextField(15);

        JLabel password = new CustomLabel("Password: ");
        JPasswordField passwordIn = new JPasswordField(15);

        JLabel status = new CustomLabel("");

        JButton login = new CustomButton("Login");
        login.putClientProperty(FlatClientProperties.STYLE_CLASS, "green");
        login.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clientRepository = (ClientRepository) UtilBean.getBean(ClientRepository.class);
                if (!usernameIn.getText().isBlank() && !(passwordIn.getPassword().length == 0)) {
                    loggedInClient = clientRepository.findByUsernameAndPassword(usernameIn.getText(),
                            String.valueOf(passwordIn.getPassword()));
                    if (loggedInClient != null) {
                        clientOrders = orderRepository.findByClientId(loggedInClient.getId());
                        navPnl.removeAll();

                        account.setText(loggedInClient.getUsername());
                        navPnl.add(title);
                        navPnl.add(account);
                        navPnl.add(sell);
                        navPnl.add(orders);
                        navPnl.add(logoutButton, "push, al right");

                        products = productService.fetchProductListByStocks();
                        redirect(home());
                    } else {
                        status.setText("Username or password is incorrect");
                        login.putClientProperty(FlatClientProperties.STYLE_CLASS, "red");
                        status.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");
                    }
                } else {
                    status.setText("Input fields are empty.");
                    login.putClientProperty(FlatClientProperties.STYLE_CLASS, "red");
                    status.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");
                }
            }
        });

        pnl.add(username);
        pnl.add(usernameIn, "wrap");
        pnl.add(password);
        pnl.add(passwordIn, "wrap");
        pnl.add(login, "wrap, skip, al right");
        pnl.add(status, " span, al center");

        return pnl;
    }

    public JPanel registerForm() {
        JPanel pnl = new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new CustomLabel("Username: ");
        JTextField usernameIn = new JTextField(15);

        JLabel password = new CustomLabel("Password: ");
        JPasswordField passwordIn = new JPasswordField(15);

        JLabel confirmPassword = new CustomLabel("Confirm Password: ");
        JPasswordField confirmPasswordIn = new JPasswordField(15);

        JLabel status = new CustomLabel("");

        JButton registerButton = new CustomButton("Register");
        registerButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "yellow");
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!usernameIn.getText().isBlank() && passwordIn.getPassword().length > 0
                        && confirmPasswordIn.getPassword().length > 0) {
                    if (String.valueOf(passwordIn.getPassword())
                            .equals(String.valueOf(confirmPasswordIn.getPassword()))) {
                        Client client = new Client(usernameIn.getText(), String.valueOf(passwordIn.getPassword()), 0);
                        clientRepository.save(client);
                        redirect(loginForm());
                    } else {
                        status.setText("Password doesn't match, try again.");
                        status.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");
                    }
                } else {
                    status.setText("Empty Fields!");
                    status.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");
                }
            }
        });

        pnl.add(username);
        pnl.add(usernameIn, "wrap");
        pnl.add(password);
        pnl.add(passwordIn, "wrap");
        pnl.add(confirmPassword);
        pnl.add(confirmPasswordIn, "wrap");
        pnl.add(registerButton, "wrap, skip, al right");
        pnl.add(status, "span, al center");

        return pnl;
    }

    JSpinner quantity = new JSpinner();

    public JPanel product(Integer index, Product product) {
        JPanel productPnl = new CustomPanel();
        productPnl.setLayout(new MigLayout(" wrap, align center", "[][]", "[][]"));
        JLabel itemImage = new JLabel(new ImageIcon(product.getImage()));
        itemImage.setMaximumSize(new Dimension(500, 500));

        CustomLabel description = new CustomLabel();
        description.setHtmlText(
                String.format(
                        "<h1 class='title'>%s</h1><div class='desc-align'><p class='description'>%s</p></div><br>",
                        product.getTitle(), product.getDescription()));

        CustomLabel price = new CustomLabel();
        price.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BG1));
        price.setHtmlText(String.format(
                "<h2 class='value'>$%.2f</h2><h3>Seller: <b>%s</b></h3>",
                product.getPrice(), product.getClient().getUsername()));

        CustomLabel available = new CustomLabel();
        available.setHtmlText(String.format("<p class='value'><b>%d</b> Available</p>", product.getStocks()));

        CustomLabel info = new CustomLabel();
        info.setHtmlText(String.format("<p class='description'>Opened on %s</p>",
                product.getDate().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"))));
        info.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BG1));

        try {
            quantity = new CustomSpinner(new SpinnerNumberModel(1, 1, product.getStocks(), 1));
        } catch (Exception e) {
        }
        JButton buy = new CustomButton("Buy");
        buy.putClientProperty(FlatClientProperties.STYLE_CLASS, "green");

        buy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int orderQuantity = (Integer) quantity.getValue();
                double totalAmount = product.getPrice() * orderQuantity;
                if (loggedInClient.getWallet() >= totalAmount) {
                    Order order = new Order(loggedInClient);
                    Client updateSeller = clientRepository.findById(product.getClient().getId()).get();

                    order.setProducts(product);
                    order.setQuantity(orderQuantity);

                    String str = String.format("Total Spendings: **%.2f**<br> Wallet Balance: **%.2f**<br>",
                            totalAmount,
                            loggedInClient.getWallet() - totalAmount);
                    int opt = JOptionPane.showConfirmDialog(f, String.format("<html>%s</html>", mdToHTML(str)),
                            "Confirm Buy", JOptionPane.YES_NO_OPTION);

                    if (opt == JOptionPane.YES_OPTION) {
                        product.setStocks(product.getStocks() - orderQuantity);
                        loggedInClient.setWallet(loggedInClient.getWallet() - totalAmount);
                        updateSeller.setWallet(updateSeller.getWallet() + totalAmount);

                        clientRepository.saveAll(List.of(loggedInClient, updateSeller));
                        productService.updateProduct(product, product.getId());
                        orderRepository.save(order);
                        products = productService.fetchProductListByStocks();

                        clientOrders = orderRepository.findByClientId(loggedInClient.getId());
                        redirect(home());
                    }
                } else {
                    JOptionPane.showMessageDialog(f, "Insufficient funds", null, JOptionPane.WARNING_MESSAGE);
                }

            }

        });

        JButton edit = new CustomButton("Edit");
        edit.putClientProperty(FlatClientProperties.STYLE_CLASS, "blue");
        edit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(productForm(product, true));
            }
        });
        JButton delete = new CustomButton("Delete");
        delete.putClientProperty(FlatClientProperties.STYLE_CLASS, "red");
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(f,
                        "This will be deleted and cannot be recovered, continue?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    productService.deleteProductById(product.getId());
                    products = productService.fetchProductListByStocks();
                    redirect(home());
                }

            }

        });

        productPnl.add(itemImage);
        productPnl.add(description, "aligny top,span 1 2");
        productPnl.add(price, "growx,skip 2");
        productPnl.add(available, "skip, split 3,al right");

        if (loggedInClient.getId() == product.getClient().getId()) {
            productPnl.add(edit);
            productPnl.add(delete);

        } else if (myOrder) {
            if (!clientOrders.isEmpty()) {
                Order thisOrder = clientOrders.get(index);
                available.setHtmlText(
                        String.format("Ordered <b>%d</b> items on <b>%s</b>",
                                thisOrder.getQuantity(),
                                thisOrder.getOrderTime().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"))));
            }
            myOrder = false;
        } else {
            productPnl.add(quantity);
            productPnl.add(buy, "wrap, al right");

        }

        productPnl.add(info, "skip, growx");
        return productPnl;
    }

    public JPanel productForm(Product product, boolean isEdit) {
        List<String> text = new ArrayList<>(List.of("", "", "", ""));

        JButton sell = new CustomButton("Sell");
        sell.putClientProperty(FlatClientProperties.STYLE_CLASS, "yellowD");

        JFileChooser imageIn = new JFileChooser("~");
        JLabel imgStatus = new CustomLabel("None");
        JLabel status = new CustomLabel("");
        status.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");

        if (isEdit) {
            text.clear();
            text.addAll(List.of(product.getTitle(), htmlToMd(product.getDescription()), product.getPrice() + "",
                    product.getStocks() + ""));

            imageIn.setSelectedFile(new File(product.getImage()));
            file = new File(product.getImage());

            sell.setText("Edit");
            imgStatus.setText(product.getImage());
        }

        JPanel productForm = new CustomPanel();
        productForm.setLayout(new MigLayout("wrap, alignx center", "[][]"));
        JLabel title = new CustomLabel("Title: ");
        JTextField titleIn = new JTextField(20);
        titleIn.setText(text.get(0));
        JLabel description = new CustomLabel("Description: ");
        JTextArea descriptionIn = new JTextArea(10, 25);
        descriptionIn.setLineWrap(true);
        descriptionIn.setText(text.get(1));
        JLabel descriptionNote = new CustomLabel("(Supports Markdown Syntax)");
        JLabel price = new CustomLabel("Price: ");
        JTextField priceIn = new CustomTextField(15);
        priceIn.setText(text.get(2));
        JLabel stock = new CustomLabel("Stock: ");
        JTextField stockIn = new CustomTextField(15);
        stockIn.setText(text.get(3));

        JLabel image = new CustomLabel("Image: ");
        imageIn.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "png", "jpg", "jpeg", "gif");
        imageIn.addChoosableFileFilter(filter);

        File destination = new File("./data/" + loggedInClient.getId());

        JButton imageButton = new CustomButton("Open");

        imageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int retVal = imageIn.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    file = imageIn.getSelectedFile();
                    imgStatus.setText(file.getName());
                    imageButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "green");
                }
            }
        });

        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> components = new ArrayList<>();
                components.addAll(
                        List.of(titleIn.getText(), descriptionIn.getText(), priceIn.getText(), stockIn.getText()));
                try {
                    int c = 0;
                    for (String component : components) {
                        if (component.isBlank()) {
                            c++;
                            throw new Exception("Empty fields!");
                        }
                    }
                    if (c == 0) {
                        String MDtoHTML = mdToHTML(descriptionIn.getText());

                        FileUtils.forceMkdir(destination);
                        Double price = Double.parseDouble(priceIn.getText());
                        int stock = Integer.parseInt(stockIn.getText());
                        if (price <= 0 || stock <= 0) {
                            throw new NumberFormatException("Inputs must be greater than 0");
                        }

                        if (isEdit) {
                            if (!file.toString().equals(product.getImage())) {
                                new File(product.getImage()).delete();
                                product.setImage(resizeImg(destination));
                            }
                            product.setTitle(titleIn.getText());
                            product.setDescription(MDtoHTML);
                            product.setPrice(Double.parseDouble(priceIn.getText()));
                            product.setStocks(Integer.parseInt(stockIn.getText()));

                            productService.updateProduct(product, product.getId());
                            products = productService.fetchProductListByClient(loggedInClient.getId());
                            redirect(account());

                        } else {

                            Product newProduct = new Product(titleIn.getText(), MDtoHTML,
                                    Double.parseDouble(priceIn.getText()),
                                    resizeImg(destination),
                                    Integer.parseInt(stockIn.getText()));

                            newProduct.setClient(loggedInClient);
                            productService.saveProduct(newProduct);
                            products = productService.fetchProductListByStocks();
                            redirect(home());

                        }

                    }
                } catch (IOException e1) {
                    imageButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "red");
                } catch (NullPointerException n1) {
                    status.setText("Must provide an image");
                    imageButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "red");
                } catch (NumberFormatException num1) {
                    status.setText(num1.getMessage());
                    price.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");
                    stock.putClientProperty(FlatClientProperties.STYLE_CLASS, "errorMsg");
                } catch (Exception e2) {
                    status.setText(e2.getMessage());
                }
            }

            private String resizeImg(File destination) throws IOException {
                String newImagePath;
                BufferedImage thumbnail = ImageIO.read(file);
                Dimension maxSize = new Dimension(500, 500);
                BufferedImage resizedThumbnail = Scalr.resize(thumbnail, Method.QUALITY, maxSize.width,
                        maxSize.height);

                newImagePath = new File(destination, file.getName()).toString();
                ImageIO.write(resizedThumbnail, "png", new File(destination, file.getName().toString()));
                return newImagePath;
            }

        });

        productForm.add(title);
        productForm.add(titleIn);
        productForm.add(description);
        productForm.add(descriptionIn);
        productForm.add(descriptionNote, "skip, al right");
        productForm.add(price);
        productForm.add(priceIn);
        productForm.add(stock);
        productForm.add(stockIn);
        productForm.add(image);
        productForm.add(imageButton, "split");
        productForm.add(imgStatus);
        productForm.add(sell, "skip, al right");
        productForm.add(status, "span, al center");

        return productForm;
    }

    private String mdToHTML(String markdownContent) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdownContent);
        return renderer.render(document);
    }

    private String htmlToMd(String description) {
        return FlexmarkHtmlConverter.builder().build().convert(description);
    }

    public void redirect(JPanel panel) {
        bodyPnl.removeAll();
        bodyPnl.add(panel);
        mainPnl.revalidate();
        mainPnl.repaint();
    }

}
