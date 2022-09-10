package com.commerce;

import java.awt.BorderLayout;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.html.HTML;
import com.github.rjeschke.txtmark.Processor;

// Things to add
// txtmark - renders md syntax to html (good for formatting)
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        card();
    }

    public static void card() {
        JFrame f = new JFrame();
        JPanel pnl = new JPanel(new BorderLayout());
        JLabel thumbnail = new JLabel();
        thumbnail.setIcon(new ImageIcon("../kekw.png"));
        // thumbnail.setText("\nhi");
        // String text = "<html><h1>IZU Event Horizon</h1><br><p>1300usd</p><br><p>This
        // is a keycapset</p></html>";
        String md = Processor.process("# This is stuff \n **heh**");
        JLabel description = new JLabel("<html>" + md + "</html>");
        pnl.add(thumbnail, BorderLayout.NORTH);
        pnl.add(description);

        f.add(pnl);
        f.setVisible(true);
        f.setSize(300, 300);
        f.setLocation(300, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
