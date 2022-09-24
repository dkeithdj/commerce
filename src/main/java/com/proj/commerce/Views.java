package com.proj.commerce;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.ocpsoft.prettytime.PrettyTime;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;
import com.proj.commerce.repositories.ClientRepository;
import com.proj.commerce.service.ProductService;
import com.proj.commerce.ui.UI;
import com.proj.commerce.ui.UI.CustomButton;
import com.proj.commerce.ui.UI.CustomLabel;

import ch.qos.logback.core.status.Status;
import net.miginfocom.swing.MigLayout;

import com.github.rjeschke.txtmark.Processor;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class Views extends JFrame {

    // @Autowired
    private ClientRepository clientRepository = (ClientRepository) UtilBean.getBean(ClientRepository.class);
    private ProductService productService = (ProductService) UtilBean.getBean(ProductService.class);
    // @Autowired
    // private ApplicationContext context;

    // @Autowired
    // private ConfigurableApplicationContext applicationContext;

    private List<Product> products = productService.fetchProductList();
    private List<Client> users;
    private Client loggedInClient;

    private JPanel navPnl;

    private JLabel title;
    private JButton account;
    private JButton sell;
    private JButton orders;
    private JButton loginButton;
    private JButton registerButton;
    private JButton logoutButton;

    // Card assembly per products

    public void setUsers(List<Client> users) {
        this.users = users;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    private JPanel bodyPnl;
    private File file = null;

    // TODO make dimensions store in a variable
    // @Transactional
    private int wrapSize = 4;

    private JPanel mainPnl = new JPanel(new BorderLayout());
    private PrettyTime relativeTime = new PrettyTime();

    public JPanel card() {
        // grouped product panel
        MigLayout layout = new MigLayout(String.format(" wrap %d, alignx center", wrapSize));

        JPanel productsPnl = new UI().new CustomPanel();
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

                    // pnl.setVisible(false);

                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    cardPnl.setBorder(BorderFactory.createLineBorder(new UI().purple0, 3));
                    // System.out.println(pnl.getWidth() + "\t" + pnl.getHeight());

                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    cardPnl.setBorder(BorderFactory.createLineBorder(new UI().bg1, 1));
                }

            };

            CustomLabel itemDesc = new UI().new CustomLabel();
            itemDesc.setHtmlText(String.format(
                    "<div class='container'><hr><h2 class='title'>%s</h2><p class='value'>%.2f USD</p><br><p class='seller'>%s</p><hr><div class='divide'><p class='date'>%s</p></div></div>",
                    product.getTitle(), product.getPrice(), product.getClient().getUsername(),
                    relativeTime.format(product.getDate())));

            cardPnl.add(itemImage, "grow, alignx center");
            cardPnl.add(itemDesc, "grow");

            cardPnl.addMouseListener(mm);

            cardPnl.setBackground(null);
            cardPnl.setMaximumSize(new Dimension(200, 350));
            cardPnl.setPreferredSize(new Dimension(200, 350));
            cardPnl.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 135), 1));
            cardPnl.setBackground(new UI().bg1);
            cardList.add(cardPnl);

        });
        addComponentListener(new ComponentAdapter() {

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
        navPnl = new UI().new CustomPanel();
        navPnl.setLayout(new MigLayout("", "[][][]"));
        navPnl.setBackground(new UI().bg2);

        title = new UI().new CustomLabel("<html><font size=14>Commerce</html>");
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                products = productService.fetchProductList();
                redirect(card());
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                title.setForeground(new UI().fg0);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                title.setForeground(new UI().fg1);
            }

        });
        account = new UI().new CustomButton();
        account.setBackground(new UI().blue0);
        account.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(account());
            }

        });

        sell = new UI().new CustomButton("Sell");
        sell.setBackground(new UI().blue1);

        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(productForm());
            }

        });

        orders = new UI().new CustomButton("My Orders");
        orders.setBackground(new UI().purple0);

        orders.addActionListener(new ActionListener() {

            // TODO order function
            @Override
            public void actionPerformed(ActionEvent e) {
                // redirect(productForm());
                bodyPnl.removeAll();
                // bodyPnl.add(newProductForm());
                mainPnl.revalidate();
                mainPnl.repaint();
                // listingPnl.setComponentZOrder(loginForm(), 5);

            }

        });

        loginButton = new UI().new CustomButton("Login");
        loginButton.setBackground(new UI().green0);

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(loginForm());

            }

        });
        logoutButton = new UI().new CustomButton("Logout");
        logoutButton.setBackground(new UI().red);

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

        registerButton = new UI().new CustomButton("Register");
        registerButton.setBackground(new UI().orange0);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                redirect(registerForm());
            }

        });

        navPnl.add(title);

        navPnl.add(loginButton, "push,al right");
        navPnl.add(registerButton);
        return navPnl;

    }

    public JPanel loginForm() {
        JPanel pnl = new UI().new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new UI().new CustomLabel("Username: ");
        JTextField usernameIn = new UI().new CustomTextField(15);

        JLabel password = new UI().new CustomLabel("Password: ");
        JPasswordField passwordIn = new UI().new CustomPasswordField(15);

        JLabel status = new UI().new CustomLabel("");

        JButton login = new UI().new CustomButton("Login");
        login.setBackground(new UI().green0);
        login.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clientRepository = (ClientRepository) UtilBean.getBean(ClientRepository.class);
                loggedInClient = clientRepository.findByUsernameAndPassword(usernameIn.getText(),
                        String.valueOf(passwordIn.getPassword()));
                if (loggedInClient != null) {
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
                    login.setBackground(new UI().red);
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
        JPanel pnl = new UI().new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new UI().new CustomLabel("Username: ");
        JTextField usernameIn = new UI().new CustomTextField(15);

        JLabel password = new UI().new CustomLabel("Password: ");
        JPasswordField passwordIn = new UI().new CustomPasswordField(15);

        JLabel confirmPassword = new UI().new CustomLabel("Confirm Password: ");
        JPasswordField confirmPasswordIn = new UI().new CustomPasswordField(15);

        JLabel status = new UI().new CustomLabel("");

        JButton registerButton = new UI().new CustomButton("Register");
        registerButton.setBackground(new UI().orange0);
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

    public JPanel product(Product product) {
        JPanel productPnl = new UI().new CustomPanel();
        productPnl.setLayout(new MigLayout(" wrap, align center", "[][]", "[][]"));
        JLabel itemImage = new JLabel();
        try {

            BufferedImage thumbnail = ImageIO.read(new File(product.getImage()));
            Dimension maxSize = new Dimension(500, 500);
            BufferedImage resizedThumbnail = Scalr.resize(thumbnail, Method.QUALITY, maxSize.width, maxSize.height);
            ImageIcon ss = new ImageIcon(resizedThumbnail);
            itemImage.setIcon(ss);
            itemImage.setMaximumSize(maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CustomLabel description = new UI().new CustomLabel();
        description.setHtmlText(
                String.format(
                        "<h1 class='title'>%s</h1><div class='desc-align'><p class='description'>%s</p></div><br>",
                        product.getTitle(), product.getDescription()));

        CustomLabel price = new UI().new CustomLabel();
        price.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new UI().bg1));
        price.setHtmlText(String.format(
                "<h2 class='value'>$%.2f</h2><h3>Seller: <b>%s</b></h3>",
                product.getPrice(), loggedInClient.getUsername()));

        CustomLabel available = new UI().new CustomLabel();
        available.setHtmlText(String.format("<p class='value'><b>%d</b> Available</p>", product.getStocks()));

        CustomLabel info = new UI().new CustomLabel();
        info.setHtmlText(String.format("<p class='description'>Opened on %s</p>",
                product.getDate().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"))));
        info.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new UI().bg1));

        JSpinner quantity = new UI().new CustomSpinner(new SpinnerNumberModel(1, 1, product.getStocks(), 1));
        JButton buy = new UI().new CustomButton("Buy");
        buy.setBackground(new UI().green1);
        quantity.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                // if (Integer.parseInt(quantity.getValue().toString()) == 0) {

                // buy.setEnabled(false);
                // } else {
                // buy.setEnabled(true);
                // }
            }

        });

        // TODO design order
        productPnl.add(itemImage);
        productPnl.add(description, "aligny top,span 1 2");
        productPnl.add(price, "growx,skip 2");
        productPnl.add(available, "skip, split 3,al right");
        productPnl.add(quantity);
        productPnl.add(buy, "wrap, al right");
        productPnl.add(info, "skip, growx");
        return productPnl;
    }

    public JPanel productForm() {
        JPanel productForm = new UI().new CustomPanel();
        productForm.setLayout(new MigLayout("wrap, alignx center", "[][]"));
        JLabel title = new UI().new CustomLabel("Title: ");
        JTextField titleIn = new UI().new CustomTextField(20);
        JLabel description = new UI().new CustomLabel("Description: ");
        JTextArea descriptionIn = new JTextArea(10, 25);
        descriptionIn.setLineWrap(true);
        JLabel descriptionNote = new UI().new CustomLabel("(Supports Markdown Syntax)");
        JLabel price = new UI().new CustomLabel("Price: ");
        JTextField priceIn = new UI().new CustomTextField(15);
        JLabel stock = new UI().new CustomLabel("Stock: ");
        JTextField stockIn = new UI().new CustomTextField(15);

        JLabel image = new UI().new CustomLabel("Image: ");
        JFileChooser imageIn = new JFileChooser("~");
        imageIn.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "png", "jpg", "jpeg", "gif");
        imageIn.addChoosableFileFilter(filter);

        File destination = new File("./data/" + loggedInClient.getId());

        JButton imageButton = new UI().new CustomButton("Open");
        JLabel status = new UI().new CustomLabel("None");

        JButton sell = new UI().new CustomButton("Sell");
        sell.setBackground(new UI().blue1);

        imageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int retVal = imageIn.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    file = imageIn.getSelectedFile();
                    status.setText(file.getName());
                    imageButton.setBackground(new UI().green1);
                } else {
                    status.setText("None");
                    file = null;
                }
            }
        });

        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");
                List<String> components = new ArrayList<>();
                components.addAll(
                        List.of(titleIn.getText(), descriptionIn.getText(), priceIn.getText(), stockIn.getText()));
                String MDtoHTML = Processor.process(descriptionIn.getText());
                try {
                    int c = 0;
                    for (String component : components) {
                        if (component.isBlank()) {
                            System.out.println(c);
                            c++;
                        }
                    }
                    if (c == 0) {
                        FileUtils.forceMkdir(destination);
                        BufferedImage thumbnail = ImageIO.read(file);
                        Dimension maxSize = new Dimension(500, 500);
                        BufferedImage resizedThumbnail = Scalr.resize(thumbnail, Method.QUALITY, maxSize.width,
                                maxSize.height);

                        String newImagePath = new File(destination, file.getName()).toString();
                        ImageIO.write(resizedThumbnail, "png", new File(destination, file.getName().toString()));

                        Product newProduct = new Product(titleIn.getText(), MDtoHTML,
                                Double.parseDouble(priceIn.getText()),
                                newImagePath,
                                Integer.parseInt(stockIn.getText()));

                        newProduct.setClient(loggedInClient);

                        productService.saveProduct(newProduct);

                        products = productService.fetchProductList();
                        redirect(card());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NullPointerException n1) {
                    imageButton.setBackground(new UI().red);
                    n1.printStackTrace();
                } catch (NumberFormatException num1) {
                    price.setForeground(new UI().red);
                    stock.setForeground(new UI().red);
                    // num1.printStackTrace();
                }
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
        productForm.add(status);
        productForm.add(sell, "skip, al right");

        return productForm;
    }

    public JPanel account() {
        products = productService.fetchProductListByClient(loggedInClient.getId());
        JPanel accountPnl = new UI().new CustomPanel();
        accountPnl.setLayout(new BorderLayout());
        JPanel accountNavPnl = new UI().new CustomPanel();
        accountNavPnl.setBackground(new Color(52, 54, 78));
        accountNavPnl.setLayout(new MigLayout("", "[][]"));
        JLabel totalListings = new UI().new CustomLabel("Total listings: " + products.size());
        JLabel walletAmount = new UI().new CustomLabel("Wallet: " + loggedInClient.getWallet());
        JButton addAmount = new UI().new CustomButton("+");
        addAmount.setBackground(new UI().green0);

        accountNavPnl.add(totalListings);
        accountNavPnl.add(walletAmount, "push, al right");
        accountNavPnl.add(addAmount);

        accountPnl.add(accountNavPnl, BorderLayout.NORTH);
        accountPnl.add(card(), BorderLayout.CENTER);
        return accountPnl;

    }

    public void listings() {
        bodyPnl = new UI().new CustomPanel();
        bodyPnl.setLayout(new BorderLayout());

        mainPnl.add(nav(), BorderLayout.NORTH);
        bodyPnl.add(card(), BorderLayout.CENTER);

        mainPnl.add(bodyPnl, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(mainPnl);

        add(scroll);
        pack();
        setVisible(true);
        setSize(1000, 600);
        setMinimumSize(new Dimension(880, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void redirect(JPanel panel) {
        bodyPnl.removeAll();
        bodyPnl.add(panel);
        mainPnl.revalidate();
        mainPnl.repaint();
    }

}
