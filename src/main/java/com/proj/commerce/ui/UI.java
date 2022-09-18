package com.proj.commerce.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UI {
  public final Color bg0 = new Color(36, 40, 59);
  public final Color bg1 = new Color(65, 72, 104);
  public final Color bg2 = new Color(26, 27, 38);
  public final Color purple0 = new Color(187, 154, 247);
  public final Color purple1 = new Color(122, 162, 247);
  public final Color blue0 = new Color(125, 207, 255);
  public final Color blue1 = new Color(42, 195, 222);
  public final Color green0 = new Color(115, 218, 202);
  public final Color green1 = new Color(158, 206, 106);
  public final Color orange0 = new Color(224, 175, 104);
  public final Color orange1 = new Color(255, 158, 100);
  public final Color red = new Color(247, 118, 142);
  public final Color fg0 = new Color(169, 177, 214);
  public final Color fg1 = new Color(192, 202, 245);

  public final String styleSheet() {
    Path stylesLoc = Path.of("./static/styles.css");
    // String styles = "";
    try {
      return Files.readString(stylesLoc);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return "";

  }

  public class CustomPanel extends JPanel {
    public CustomPanel() {
      setBackground(bg0);
    }

  }

  public class CustomButton extends JButton {
    public CustomButton() {
      setBackground(fg0);
      setForeground(bg0);
      setFocusPainted(false);
      setFont(new Font("Tahoma", Font.PLAIN, 12));

    }

    public CustomButton(String text) {
      setText(text);
      setBackground(fg0);
      setForeground(bg0);
      setFocusPainted(false);
      setFont(new Font("Tahoma", Font.PLAIN, 12));

    }

  }

  public class CustomLabel extends JLabel {
    public CustomLabel() {
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }

    public void setHtmlText(String htmlText) {
      setText(String.format("<html><style>%s</style><body>%s</body></html>", styleSheet(), htmlText));
    }

    public CustomLabel(String text) {
      setText(text);
      setForeground(fg1);
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }

  }

  public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
      setColumns(columns);
      setBackground(new UI().bg1);
      setForeground(new UI().fg1);
      setCaretColor(new UI().blue0);
      setBorder(BorderFactory.createLineBorder(bg0, 0));
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }
  }

  public class CustomPasswordField extends JPasswordField {
    public CustomPasswordField(int columns) {
      setColumns(columns);
      setBackground(new UI().bg1);
      setForeground(new UI().fg1);
      setCaretColor(new UI().blue0);
      setBorder(BorderFactory.createLineBorder(bg0, 0));
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }
  }

}
