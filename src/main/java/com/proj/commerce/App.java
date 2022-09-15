package com.proj.commerce;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.LinkRef;
import javax.persistence.Entity;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.w3c.dom.events.MouseEvent;

import com.proj.commerce.models.User;

import net.miginfocom.swing.MigLayout;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class App {
    // public static void main(String[] args) {
    // System.out.println("Hello World!");
    // new App().listings();
    // // card();
    // }

    // Card assembly per products

    // TODO make dimensions store in a variable
    public JPanel card(String imgFile, User user) {
        final JPanel pnl = new JPanel(
                new MigLayout("debug,fill,  insets panel, wrap, alignx center", "", "[50%][50%]"));
        BufferedImage thumbnail;
        JLabel itemImage = new JLabel();
        try {
            // String keyb = "EventHorizonTemp.png";
            // String keyb = "kekw.png";
            thumbnail = ImageIO.read(new File(imgFile));
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
                System.out.println(user.getId());
                // TODO get id of current card to be redirected to listing.id

            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                pnl.setBorder(BorderFactory.createLineBorder(Color.red, 5));
                // System.out.println(pnl.getWidth() + "\t" + pnl.getHeight());

            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                pnl.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
            }

        };

        // jep.setContentType("text/html");
        // HTMLEditorKit kit = new HTMLEditorKit();
        // StyleSheet style = kit.getStyleSheet();
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
                "<html><style>%s</style><body><div><h2>IZU Rose</h2></div><div class='details'><div><h4>2, 300 USD</h4></div><div class='seller'></div>%s</div></body></html>",
                styles, user.getUsername());
        // String text = "<html><style>body {border: 2px solid red;} .details
        // {background-color: #ffc0cb; color: blue; text-align: right; border-color:
        // black; width: 140px; height: 100%;}</style><body><div><h2>IZU
        // Rose</h2></div><div class='details'><div><h4>2, 300 USD</h4></div><div
        // class='seller'>"
        // + user.getUsername() + "</div></div></body></html>";
        // style.addRule(".price {color: red}");
        // style.addRule(".seller {color: blue; text-align: right; border-color:
        // black;}");
        // style.addRule(".seller {background-color: #ffc0cb; width: 100px}");
        // style.addRule("body {background-color: grey; font-family: Arial, Helvetica,
        // sans-serif}");
        itemDesc.setText(text);
        // itemDesc.setBackground(Color.orange);
        itemDesc.setFont(new Font("Arial", Font.PLAIN, 12));

        pnl.addMouseListener(mm);
        pnl.setBackground(null);
        pnl.setMaximumSize(new Dimension(200, 300));
        pnl.setPreferredSize(new Dimension(200, 300));
        pnl.add(itemImage, "grow, alignx center");
        pnl.add(itemDesc, "grow");
        pnl.setBorder(BorderFactory.createLineBorder(Color.blue, 3));

        return pnl;
    }

    int wrapSize = 4;

    public void listings(List<JPanel> list) {
        JFrame f = new JFrame();
        // Change wrap from 4 cards to 5 depending on frame width
        final MigLayout layout = new MigLayout(String.format("wrap %d, alignx center", wrapSize));
        final JPanel pnl = new JPanel(layout);

        // Listens to frame width changes
        f.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                if (c.getWidth() > 1000)
                    wrapSize = 5;
                else
                    wrapSize = 4;
                layout.setLayoutConstraints(String.format("wrap %d, alignx center", wrapSize));
                pnl.setLayout(layout);
            }

        });
        list.forEach(li -> pnl.add(li));

        pnl.setBackground(Color.cyan);
        JScrollPane scroll = new JScrollPane(pnl);

        f.add(scroll);
        f.pack();
        f.setVisible(true);
        f.setSize(1000, 600);
        f.setMinimumSize(new Dimension(150 * 4, 400));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
