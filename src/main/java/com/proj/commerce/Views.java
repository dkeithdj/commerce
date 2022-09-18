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
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.security.auth.login.AccountException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;
import com.proj.commerce.repositories.ProductRepository;
import com.proj.commerce.repositories.ClientRepository;
import com.proj.commerce.service.ProductService;
import com.proj.commerce.ui.UI;
import com.proj.commerce.ui.UI.CustomLabel;
import com.proj.commerce.ui.UI.CustomPanel;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.util.FileUtil;
import net.miginfocom.swing.MigLayout;
import com.proj.commerce.UtilBean;

import com.github.rjeschke.txtmark.Processor;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class Views extends JFrame {

    @Autowired
    private ProductService productService;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private List<Product> products;
    private List<Client> users;

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

    public JPanel card(List<Product> products) {
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
                // String keyb = "EventHorizonTemp.png";
                // String keyb = "kekw.png";
                String keyb = "40D1080Side.png";
                // String keyb = user.getProduct().get(0).getImage();
                BufferedImage thumbnail = ImageIO.read(new File(product.getImage()));
                Dimension maxSize = new Dimension(180, 180);
                BufferedImage resizedThumbnail = Scalr.resize(thumbnail, Method.QUALITY, maxSize.width, maxSize.height);
                ImageIcon ss = new ImageIcon(resizedThumbnail);
                itemImage.setIcon(ss);
                itemImage.setMaximumSize(new Dimension(180, 180));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MouseAdapter mm = new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    bodyPnl.removeAll();
                    bodyPnl.add(product(product));
                    mainPnl.revalidate();
                    mainPnl.repaint();
                    // pnl.setVisible(false);
                    // TODO get id of current card to be redirected to listing.id

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
            cardPnl.setMaximumSize(new Dimension(200, 300));
            cardPnl.setPreferredSize(new Dimension(200, 300));
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
        // productsPnl.setBackground(new Color(36, 40, 59));

        return productsPnl;
    }

    public JPanel nav() {
        JPanel pnl = new UI().new CustomPanel();
        pnl.setLayout(new MigLayout("", "[][][]"));
        pnl.setBackground(new UI().bg2);

        JLabel title = new UI().new CustomLabel("<html><font size=14>Commerce</html>");
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                bodyPnl.removeAll();
                bodyPnl.add(card(products));
                mainPnl.revalidate();
                mainPnl.repaint();
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
        JButton account = new UI().new CustomButton("{username}");
        account.setBackground(new UI().blue0);
        account.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                bodyPnl.removeAll();
                // productService = (ProductService) context.getBean("productService");
                productService = (ProductService) UtilBean.getBean(ProductService.class);
                // ClientRepository clientRepository = (ClientRepository)
                // UtilBean.getBean(ClientRepository.class);
                // ProductRepository producRepository = (ProductRepository)
                // UtilBean.getBean(ProductRepository.class);

                // List<Product> clientProducts = new ArrayList<>();
                // products.forEach(p -> {
                // if (p.getClient().getId() == 1L) {
                // clientProducts.add(p);
                // }
                // });
                // bodyPnl.add(card(clientProducts));
                bodyPnl.add(card(productService.fetchProductListByClient(2L)));
                // bodyPnl.add(card());
                mainPnl.revalidate();
                mainPnl.repaint();

            }

        });

        JButton sell = new UI().new CustomButton("Sell");
        sell.setBackground(new UI().blue1);

        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                bodyPnl.removeAll();
                bodyPnl.add(newProductForm(products.get(0).getClient()));
                mainPnl.revalidate();
                mainPnl.repaint();
                // listingPnl.setComponentZOrder(loginForm(), 5);

            }

        });

        JButton loginButton = new UI().new CustomButton("Login");
        loginButton.setBackground(new UI().green0);

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                bodyPnl.removeAll();
                bodyPnl.add(loginForm());
                mainPnl.revalidate();
                mainPnl.repaint();
                // listingPnl.setComponentZOrder(loginForm(), 5);

            }

        });
        JButton logoutButton = new UI().new CustomButton("Logout");
        logoutButton.setBackground(new UI().red);

        JButton registerButton = new UI().new CustomButton("Register");
        registerButton.setBackground(new UI().orange0);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                bodyPnl.removeAll();
                bodyPnl.add(registerForm());
                mainPnl.revalidate();
                mainPnl.repaint();
            }

        });

        pnl.add(title);
        pnl.add(account);
        pnl.add(sell);

        pnl.add(loginButton, "push, al right");
        pnl.add(registerButton);
        pnl.add(logoutButton);
        return pnl;

    }

    public void listings() {
        bodyPnl = new UI().new CustomPanel();
        bodyPnl.setLayout(new BorderLayout());

        mainPnl.add(nav(), BorderLayout.NORTH);
        bodyPnl.add(card(products), BorderLayout.CENTER);

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

    // TODO add login validation
    public JPanel loginForm() {
        JPanel pnl = new UI().new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new UI().new CustomLabel("Username: ");
        JTextField usernameIn = new UI().new CustomTextField(15);

        JLabel password = new UI().new CustomLabel("Password: ");
        JPasswordField passwordIn = new UI().new CustomPasswordField(15);

        JButton loginButton = new UI().new CustomButton("Login");
        loginButton.setBackground(new UI().green0);
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });

        pnl.add(username);
        pnl.add(usernameIn, "wrap");
        pnl.add(password);
        pnl.add(passwordIn, "wrap");
        pnl.add(loginButton, "skip, al right");

        return pnl;
    }

    public JPanel registerForm() {
        JPanel pnl = new UI().new CustomPanel();
        pnl.setLayout(new MigLayout("align center"));

        JLabel username = new UI().new CustomLabel("Username: ");
        JTextField usernameIn = new UI().new CustomTextField(15);

        JLabel password = new UI().new CustomLabel("Password: ");
        JPasswordField passwordIn = new UI().new CustomPasswordField(15);

        JLabel reEnterPassword = new UI().new CustomLabel("Re-enter Password: ");
        JPasswordField reEnterPasswordIn = new UI().new CustomPasswordField(15);

        JButton registerButton = new UI().new CustomButton("Register");
        registerButton.setBackground(new UI().orange0);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });

        pnl.add(username);
        pnl.add(usernameIn, "wrap");
        pnl.add(password);
        pnl.add(passwordIn, "wrap");
        pnl.add(reEnterPassword);
        pnl.add(reEnterPasswordIn, "wrap");
        pnl.add(registerButton, "skip, al right");

        return pnl;
    }

    public JPanel product(Product product) {
        JPanel productPnl = new UI().new CustomPanel();
        productPnl.setLayout(new MigLayout("wrap,align center", "[][]", "[][]"));
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
        description.setHtmlText(String.format("<h1 class='title'>%s</h1><p class='description'>%s</p><br>",
                product.getTitle(), product.getDescription()));
        CustomLabel price = new UI().new CustomLabel();
        price.setHtmlText(String.format(
                "<p class='value'>$%.2f</p><p class='value'>Stocks: %d</p><p class='description'>Opened on %s</p>",
                product.getPrice(),
                product.getStocks(), product.getDate().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"))));
        productPnl.add(itemImage);
        productPnl.add(description, "aligny top,span 1 2");
        productPnl.add(price, " skip 2");
        return productPnl;
    }

    public JPanel newProductForm(Client client) {
        // user.getProduct().get(0);
        JPanel productForm = new UI().new CustomPanel();
        productForm.setLayout(new MigLayout("wrap, alignx center", "[][]"));
        JLabel title = new UI().new CustomLabel("Title: ");
        JTextField titleIn = new UI().new CustomTextField(15);
        JLabel description = new UI().new CustomLabel("Description: ");
        JTextArea descriptionIn = new JTextArea(10, 16);
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

        File destination = new File("./data/" + client.getId());

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
                        String newImagePath = new File(destination, file.getName()).toString();
                        FileUtils.copyFileToDirectory(file, destination);
                        // productService
                        // .saveProduct(new Product(titleIn.getText(), MDtoHTML,
                        // Double.parseDouble(priceIn.getText()),
                        // destination.getAbsolutePath() + file.getName(),
                        // Integer.parseInt(stockIn.getText())));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NullPointerException n1) {
                    imageButton.setBackground(new UI().red);
                    // n1.printStackTrace();
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
}
