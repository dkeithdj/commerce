package com.commerce;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.naming.LinkRef;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.Processor;

import net.miginfocom.swing.MigLayout;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        new App().listings();
        // card();
    }

    // Card assembly per products
    public JPanel card() {
        JFrame f = new JFrame();
        JPanel pnl = new JPanel(new BorderLayout());
        JPanel card = new JPanel();
        // JLabel thumbnail = new JLabel();
        // thumbnail.setIcon(new ImageIcon("kekw.png"));
        // String md = Processor
        // .process(
        // "![stuff](file:kekw.png){.img} \n # This is stuff {#head1} ***heh*** \n
        // **hehu** \n ",
        // Configuration.DEFAULT_SAFE);
        // System.out.println(md);

        // Processor.process("", Configuration.DEFAULT_SAFE);
        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);
        jep.setPreferredSize(new Dimension(100, 150));
        // jep.setContentType("text/html");
        HTMLEditorKit kit = new HTMLEditorKit();
        jep.setEditorKit(kit);
        StyleSheet style = kit.getStyleSheet();
        // String text = "<html><body><img src='file:40D1080Side.png'
        // alt='kekw'/></body></html>";
        String text = "<html><body><p>hey hi</p></body></html";
        style.addRule("img { height: 100px; width: 100px;}");
        Document doc = kit.createDefaultDocument();
        jep.setDocument(doc);
        jep.setText(text);
        // jep.setMaximumSize(new Dimension(400, 400));
        // pnl.add(thumbnail, BorderLayout.NORTH);
        JLabel description = new JLabel(text);
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(jep);

        pnl.add(scroll, BorderLayout.CENTER);
        pnl.setBackground(Color.BLUE);

        return pnl;
    }

    int wrapSize = 4;

    public void listings() {
        JFrame f = new JFrame();
        // Change wrap from 4 cards to 5 depending on frame width
        final MigLayout layout = new MigLayout(String.format("wrap %d, alignx center", wrapSize));
        final JPanel pnl = new JPanel(layout);

        // Listens to frame width changes
        f.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                if (c.getWidth() > 600)
                    wrapSize = 5;
                else
                    wrapSize = 4;
                layout.setLayoutConstraints(String.format("wrap %d, alignx center", wrapSize));
                pnl.setLayout(layout);
            }

        });

        pnl.add(card());
        pnl.add(card());
        pnl.add(card());
        pnl.add(card());
        pnl.add(card());
        JScrollPane scroll = new JScrollPane(pnl);
        f.add(scroll);
        f.setVisible(true);
        f.setSize(1000, 600);
        f.setMinimumSize(new Dimension(150 * 4, 400));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
