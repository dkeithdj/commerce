package com.proj.commerce.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class UI {
  public final Color BG0 = new Color(36, 40, 59);
  public final Color BG1 = new Color(65, 72, 104);
  public final Color BG2 = new Color(26, 27, 38);
  public final Color PURPLE0 = new Color(187, 154, 247);
  public final Color PURPLE1 = new Color(122, 162, 247);
  public final Color BLUE0 = new Color(125, 207, 255);
  public final Color BLUE1 = new Color(42, 195, 222);
  public final Color GREEN0 = new Color(115, 218, 202);
  public final Color GREEN1 = new Color(158, 206, 106);
  public final Color ORANGE0 = new Color(224, 175, 104);
  public final Color ORANGE1 = new Color(255, 158, 100);
  public final Color RED = new Color(247, 118, 142);
  public final Color FG0 = new Color(169, 177, 214);
  public final Color FG1 = new Color(192, 202, 245);

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
      setBackground(BG0);
    }

  }

  public class CustomButton extends JButton {
    public CustomButton() {
      setBackground(FG0);
      setForeground(BG0);
      setFocusPainted(false);
      setFont(new Font("Tahoma", Font.PLAIN, 12));

    }

    public CustomButton(String text) {
      setText(text);
      setBackground(FG0);
      setForeground(BG0);
      setFocusPainted(false);
      setFont(new Font("Tahoma", Font.PLAIN, 12));

    }

  }

  public class CustomLabel extends JLabel {
    public CustomLabel() {
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }

    public void setHtmlText(String htmlText) {
      setText(
          String.format("<html><style>%s</style><body>%s</body></html>", styleSheet(), htmlText));
    }

    public CustomLabel(String text) {
      setText(text);
      setForeground(FG1);
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }

  }

  public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
      setColumns(columns);
      setBackground(BG1);
      setForeground(FG1);
      setCaretColor(BLUE0);
      setBorder(BorderFactory.createLineBorder(BG0, 0));
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }
  }

  public class CustomPasswordField extends JPasswordField {
    public CustomPasswordField(int columns) {
      setColumns(columns);
      setBackground(BG1);
      setForeground(FG1);
      setCaretColor(BLUE0);
      setBorder(BorderFactory.createLineBorder(BG0, 0));
      setFont(new Font("Tahoma", Font.PLAIN, 14));
    }
  }

  public class CustomSpinner extends JSpinner {

    public CustomSpinner(SpinnerNumberModel model) {
      setModel(model);
      JComponent spinnerEditor = getEditor();
      JFormattedTextField spinnerFormat = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
      spinnerFormat.setColumns(3);
      spinnerFormat.setEditable(false);
      spinnerFormat.setFont(new Font("Tahoma", Font.PLAIN, 15));
      spinnerFormat.setSize(500, 300);
      int n = spinnerEditor.getComponentCount();
      for (int i = 0; i < n; i++) {
        Component c = spinnerEditor.getComponent(i);
        if (c instanceof JFormattedTextField) {
          c.setBackground(BG1);
          c.setForeground(FG1);

        }
        if (c instanceof JButton) {
          c.setBackground(BG1);
          c.setForeground(FG1);

        }
      }
    }

  }

  public ActionListener redirectListener(JPanel mainPnl, JPanel bodyPnl, JPanel pnl) {
    ActionListener al = new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        bodyPnl.removeAll();
        bodyPnl.add(pnl);
        mainPnl.revalidate();
        mainPnl.repaint();
      }
    };
    return al;
  }

}
