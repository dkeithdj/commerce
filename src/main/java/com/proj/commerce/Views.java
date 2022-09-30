package com.proj.commerce;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
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
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.ocpsoft.prettytime.PrettyTime;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;
import com.proj.commerce.models.Order;
import com.proj.commerce.repositories.ClientRepository;
import com.proj.commerce.repositories.OrderRepository;
import com.proj.commerce.repositories.ProductRepository;
import com.proj.commerce.service.OrderService;
import com.proj.commerce.service.ProductService;
import com.proj.commerce.ui.UI;
import com.proj.commerce.ui.UI.CustomButton;
import com.proj.commerce.ui.UI.CustomLabel;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import ch.qos.logback.core.status.Status;
import net.miginfocom.swing.MigLayout;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatLineBorder;
import com.github.rjeschke.txtmark.Processor;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class Views extends UI {

    private ClientRepository clientRepository = (ClientRepository) UtilBean.getBean(ClientRepository.class);
    private OrderRepository orderRepository = (OrderRepository) UtilBean.getBean(OrderRepository.class);
    private ProductService productService = (ProductService) UtilBean.getBean(ProductService.class);
    private OrderService orderService = (OrderService) UtilBean.getBean(OrderService.class);

    private List<Product> products = productService.fetchProductListByStocks();
    private List<Client> users;
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

    private JPanel mainPnl = new JPanel(new BorderLayout());
    private PrettyTime relativeTime = new PrettyTime();

    public Views() {
        bodyPnl = new CustomPanel();
        bodyPnl.setLayout(new BorderLayout());

        mainPnl.add(nav(), BorderLayout.NORTH);
        bodyPnl.add(card(), BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(bodyPnl);

        // mainPnl.add(bodyPnl, BorderLayout.CENTER);
        mainPnl.add(scroll, BorderLayout.CENTER);
        f.add(mainPnl);
        f.setTitle("Commerce");
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

        // JPanel productsPnl = new JPanel();
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
                        redirect(product(product));
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

            cardPnl.addMouseListener(mm);

            // cardPnl.putClientProperty(FlatClientProperties.STYLE, "background: #414868");
            // cardPnl.setBackground(null);
            cardPnl.setMaximumSize(new Dimension(200, 350));
            cardPnl.setPreferredSize(new Dimension(200, 350));
            cardPnl.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16), BG0, 1, 12));
            // cardPnl.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 135), 1));
            // cardPnl.setBackground(BG1);
            cardList.add(cardPnl);

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

    public JPanel nav() {
        navPnl = new CustomPanel();
        navPnl.setLayout(new MigLayout("", "[][][]"));
        // navPnl.setBackground(BG2);

        // title = new CustomLabel("<html><font size=14>Commerce</html>");
        title = new CustomLabel("Home");
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h00");
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                products = productService.fetchProductListByStocks();
                redirect(card());
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
        // account.setBackground(BLUE0);
        account.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(account());
            }

        });

        sell = new CustomButton("Sell");
        // sell.setBackground(BLUE1);

        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(productForm(null, false));
            }

        });

        orders = new CustomButton("My Orders");
        // orders.setBackground(PURPLE0);

        orders.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // List<Product> productOrders = new ArrayList<>();
                // clientOrders.forEach(order -> {
                // order.getProducts().forEach(product -> productOrders.add(product));
                // });
                // clientOrders.forEach(order -> {
                // productOrders.add(order.getProducts());
                // });
                myOrder = true;
                products = orderService.fetchProductsByClientId(loggedInClient.getId());
                redirect(card());
                // productOrders.clear();

            }

        });

        loginButton = new CustomButton("Login");
        // loginButton.setBackground(GREEN0);

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(loginForm());

            }

        });
        logoutButton = new CustomButton("Logout");
        // logoutButton.setBackground(RED);

        logoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInClient = null;
                navPnl.removeAll();
                navPnl.add(title);
                navPnl.add(loginButton, "push, al right");
                navPnl.add(registerButton);

                products = productService.fetchProductList();
                redirect(card());
            }

        });

        registerButton = new CustomButton("Register");
        // registerButton.setBackground(ORANGE0);
        registerButton.addActionListener(new UI().redirectListener(mainPnl, bodyPnl, registerForm()));
        // registerButton.addActionListener(new ActionListener() {

        // @Override
        // public void actionPerformed(ActionEvent e) {
        // redirect(registerForm());
        // }

        // });

        navPnl.add(title);

        navPnl.add(loginButton, "push,al right");
        navPnl.add(registerButton);
        return navPnl;

    }

    public JPanel loginForm() {
        JPanel pnl = new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new CustomLabel("Username: ");
        // JTextField usernameIn = new CustomTextField(15);
        JTextField usernameIn = new JTextField(15);

        JLabel password = new CustomLabel("Password: ");
        // JPasswordField passwordIn = new CustomPasswordField(15);
        JPasswordField passwordIn = new JPasswordField(15);

        JLabel status = new CustomLabel("");

        JButton login = new CustomButton("Login");
        // login.setBackground(GREEN0);
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

                        products = productService.fetchProductList();
                        redirect(card());
                    } else {
                        status.setText("Username or password is incorrect");
                        login.setBackground(RED);
                    }
                } else {
                    status.setText("Input fields are empty.");
                    login.setBackground(RED);

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
        // registerButton.setBackground(ORANGE0);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (String.valueOf(passwordIn.getPassword()).equals(String.valueOf(confirmPasswordIn.getPassword()))) {
                    Client client = new Client(usernameIn.getText(), String.valueOf(passwordIn.getPassword()), 0);
                    clientRepository.save(client);
                    redirect(loginForm());
                } else {
                    status.setText("Password doesn't match, try again.");
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

    boolean myOrder = false;
    JSpinner quantity = new JSpinner();

    public JPanel product(Product product) {
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
        // buy.setBackground(GREEN1);

        buy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int orderQuantity = (Integer) quantity.getValue();
                double totalAmount = product.getPrice() * orderQuantity;
                if (loggedInClient.getWallet() >= totalAmount) {
                    // int quantity = Integer.parseInt(quantity.getValue().toString());
                    Order order = new Order(loggedInClient);
                    // Product product = product;
                    // Client loggedInClient = loggedInClient;
                    Client updateSeller = clientRepository.findById(product.getClient().getId()).get();

                    order.setProducts(List.of(product));
                    order.setQuantity(orderQuantity);

                    product.setStocks(product.getStocks() - orderQuantity);
                    loggedInClient.setWallet(loggedInClient.getWallet() - totalAmount);
                    updateSeller.setWallet(updateSeller.getWallet() + totalAmount);

                    // TODO fix doubled buy
                    clientRepository.saveAll(List.of(loggedInClient, updateSeller));
                    productService.updateProduct(product, product.getId());
                    orderRepository.save(order);
                    products = productService.fetchProductListByStocks();
                    // products = orderService.fetchProductsByClientId(loggedInClient.getId());
                    // TODO add joption confirm
                    redirect(card());
                } else {
                    JOptionPane.showMessageDialog(f, "Insufficient funds", null, JOptionPane.WARNING_MESSAGE);
                    // System.out.println("Insufficient funds");
                    // TODO put insufficient funds
                }

            }

        });

        JButton edit = new CustomButton("Edit");
        // edit.setBackground(BLUE0);
        edit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(productForm(product, true));
            }
        });
        JButton delete = new CustomButton("Delete");
        // delete.setBackground(RED);
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(f,
                        "This will be deleted and cannot be recovered, continue?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.OK_OPTION) {
                    productService.deleteProductById(product.getId());
                    products = productService.fetchProductListByStocks();
                    redirect(card());
                }

            }

        });

        // TODO design order
        productPnl.add(itemImage);
        productPnl.add(description, "aligny top,span 1 2");
        productPnl.add(price, "growx,skip 2");
        productPnl.add(available, "skip, split 3,al right");

        if (loggedInClient.getId() == product.getClient().getId()) {
            productPnl.add(edit);
            productPnl.add(delete);
            // TODO edit, delete button

        } else if (myOrder) {
            // } else if (orderRepository.findByClientId(loggedInClient.getId())) {
            orderRepository.findByClientId(loggedInClient.getId());
            Order currentOrder = orderRepository.findByClientIdAndProductsId(loggedInClient.getId(), product.getId());
            if (currentOrder != null) {

                available.setHtmlText(
                        String.format("Ordered <b>%d</b> items on <b>%s</b>", currentOrder.getQuantity(),
                                currentOrder.getOrderTime().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"))));
                myOrder = false;
            }
            myOrder = false;
        } else {
            // TODO som bug: kant buy on first click on product
            productPnl.add(quantity);
            productPnl.add(buy, "wrap, al right");

        }

        productPnl.add(info, "skip, growx");
        return productPnl;
    }

    public JPanel productForm(Product product, boolean isEdit) {
        List<String> text = new ArrayList<>(List.of("test", "", "", ""));

        JButton sell = new CustomButton("Sell");
        // sell.setBackground(BLUE1);
        JFileChooser imageIn = new JFileChooser("~");
        JLabel imgStatus = new CustomLabel("None");
        JLabel status = new CustomLabel("");

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
                    // imageButton.setBackground(GREEN1);
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
                            // break;
                            System.out.println(c);
                            c++;
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
                                System.out.println("deleting");
                                new File(product.getImage()).delete();
                                // newImagePath = new File(destination, file.getName()).toString();
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
                            redirect(card());

                        }

                    }
                } catch (IOException e1) {
                    // imageButton.setBackground(RED);
                } catch (NullPointerException n1) {
                    // imageButton.setBackground(RED);
                    // n1.printStackTrace();
                } catch (NumberFormatException num1) {
                    status.setText(num1.getMessage());
                    // status.setForeground(RED);
                    // price.setForeground(RED);
                    // stock.setForeground(RED);
                    // num1.printStackTrace();
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

    public JPanel account() {
        products = productService.fetchProductListByClient(loggedInClient.getId());

        JPanel accountPnl = new CustomPanel();
        accountPnl.setLayout(new BorderLayout());

        JPanel accountNavPnl = new CustomPanel();
        // accountNavPnl.setBackground(new Color(52, 54, 78));
        accountNavPnl.setLayout(new MigLayout("", "[][]"));

        JLabel totalListings = new CustomLabel("Total listings: " + products.size());
        JLabel totalOrders = new CustomLabel("Total Orders: " + clientOrders.size());

        JLabel walletAmount = new CustomLabel(String.format("Wallet: %.2f", loggedInClient.getWallet()));

        JButton addAmount = new CustomButton("+");
        // addAmount.setBackground(GREEN0);
        addAmount.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(JOptionPane.showInputDialog(f, "Cash-In Amount"));
                Client update = loggedInClient;
                update.setWallet(update.getWallet() + amount);
                clientRepository.save(update);
                walletAmount.setText(String.format("Wallet: %.2f", loggedInClient.getWallet()));
                mainPnl.revalidate();
                mainPnl.repaint();

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

    // public JPanel orders() {
    // JPanel ordersPnl = new CustomPanel();
    // ordersPnl.setLayout(new MigLayout());

    // List<Product> productOrders = new ArrayList<>();
    // clientOrders.forEach(order -> {
    // order.getProducts().forEach(product -> productOrders.add(product));
    // });
    // // clientOrders.forEach(order -> {
    // // productOrders.add(order.getProducts());
    // // });
    // myOrder = true;
    // products = productOrders;
    // redirect(card());
    // return ordersPnl;

    // }
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
