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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import com.proj.commerce.models.Product;

import net.miginfocom.swing.MigLayout;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class Views extends JFrame {

    private List<Product> products;
    // Card assembly per products

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // TODO make dimensions store in a variable
    // @Transactional
    int wrapSize = 4;
    int c = 0;

    public JPanel card() {
        // grouped product panel
        MigLayout layout = new MigLayout(String.format("debug, wrap %d, alignx center", wrapSize));
        JPanel productsPnl = new JPanel(layout);

        // per product panel

        List<JPanel> cardList = new ArrayList<>();

        products.forEach(product -> {
            JLabel itemImage = new JLabel();
            JPanel cardPnl = new JPanel(
                    new MigLayout("debug, fill,  insets panel, wrap, alignx center", "", "[50%][50%]"));
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
                    System.out.println(product.getId());
                    // pnl.setVisible(false);
                    // TODO get id of current card to be redirected to listing.id

                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    cardPnl.setBorder(BorderFactory.createLineBorder(Color.red, 5));
                    // System.out.println(pnl.getWidth() + "\t" + pnl.getHeight());

                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    cardPnl.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
                }

            };

            JLabel itemDesc = new JLabel();
            Path stylesLoc = Path.of("static/styles.css");
            String styles = "";
            try {
                styles = Files.readString(stylesLoc);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String text = String.format(
                    "<html><style>%s</style><body><div><h2>%s</h2></div><div class='details'><div><h4>%.2f</h4></div><div class='seller'></div>%s</div></body></html>",
                    styles, product.getTitle(), product.getPrice(), product.getUser().getUsername());
            itemDesc.setText(text);
            // itemDesc.setBackground(Color.orange);
            itemDesc.setFont(new Font("Arial", Font.PLAIN, 12));

            cardPnl.add(itemImage, "grow, alignx center");
            cardPnl.add(itemDesc, "grow");

            cardPnl.addMouseListener(mm);

            cardPnl.setBackground(null);
            cardPnl.setMaximumSize(new Dimension(200, 300));
            cardPnl.setPreferredSize(new Dimension(200, 300));
            cardPnl.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
            cardList.add(cardPnl);
            System.out.println(c);
            c++;

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
        JPanel pnl = new JPanel(new MigLayout("", "[][]"));
        JLabel title = new JLabel("<html><font size=14>Commerce</html>");
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("yes");
                // listingPnl.setVisible(true);
                // setContentPane(listingPnl);

                // System.out.println(product.getId());
                // listingPnl.setVisible(false);
                // TODO get id of current card to be redirected to listing.id

            }

        });
        JButton account = new JButton("{username}");
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // listingPnl.removeAll();
                // listingPnl.add(loginForm());
                // listingPnl.setComponentZOrder(loginForm(), 5);

            }

        });
        JButton logoutButton = new JButton("Logout");
        JButton registerButton = new JButton("Register");

        pnl.add(title);
        pnl.add(account);

        pnl.add(loginButton);
        pnl.add(registerButton);
        pnl.add(logoutButton);
        return pnl;

    }

    // private JPanel body;

    public void listings() {
        // JFrame f = new JFrame();
        // Change wrap from 4 cards to 5 depending on frame width
        // final MigLayout layout = new MigLayout(String.format("debug, wrap %d, alignx
        // center", wrapSize));
        // JPanel pnl = new JPanel(new MigLayout("debug, alignx center", "[][]"));
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel bodyPnl = new JPanel(new BorderLayout());

        // Listens to frame width changes
        // MouseAdapter mm = new MouseAdapter() {
        // @Override
        // public void mouseClicked(java.awt.event.MouseEvent e) {
        // listingPnl.setVisible(false);
        // System.out.println(f.getSize());
        // // System.out.println(product.getId());
        // // listingPnl.setVisible(false);
        // // TODO get id of current card to be redirected to listing.id

        // }

        // @Override
        // public void mouseEntered(java.awt.event.MouseEvent e) {
        // // pnl.setBorder(BorderFactory.createLineBorder(Color.red, 5));
        // // System.out.println(pnl.getWidth() + "\t" + pnl.getHeight());

        // }

        // @Override
        // public void mouseExited(java.awt.event.MouseEvent e) {
        // // listingPnl.setVisible(true);
        // // pnl.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        // }

        // };

        // list.forEach(li -> {
        // li.addMouseListener(mm);
        // listingPnl.add(li);
        // });
        mainPnl.add(nav(), BorderLayout.NORTH);
        bodyPnl.add(card(), BorderLayout.CENTER);
        bodyPnl.setForeground(Color.yellow);
        // listingPnl.add(card());
        mainPnl.add(bodyPnl, BorderLayout.CENTER);
        // card().addMouseListener(mm);

        mainPnl.setBackground(Color.cyan);
        JScrollPane scroll = new JScrollPane(mainPnl);

        add(scroll);
        pack();
        setVisible(true);
        setSize(1000, 600);
        setMinimumSize(new Dimension(880, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel loginForm() {
        JPanel pnl = new JPanel(new MigLayout("fill", "[][]", "[][]"));
        JLabel username = new JLabel("Username: ");
        JTextField usernameIn = new JTextField();

        JLabel password = new JLabel("Password: ");
        JPasswordField passwordIn = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });

        pnl.add(username);
        pnl.add(usernameIn);
        pnl.add(password);
        pnl.add(passwordIn);
        pnl.add(loginButton);

        return pnl;
    }
}
