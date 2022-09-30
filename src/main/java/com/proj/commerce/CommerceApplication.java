package com.proj.commerce;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;

@SpringBootApplication
public class CommerceApplication extends JFrame {

  public static void main(final String[] args) {
    SpringApplication.run(CommerceApplication.class,
        args);
    FlatLaf.registerCustomDefaultsSource("com.proj.commerce.ui");
    FlatAtomOneDarkContrastIJTheme.setup();
    int arc = 8;
    UIManager.put("Button.arc", arc);
    UIManager.put("Component.arc", arc);
    UIManager.put("CheckBox.arc", arc);
    UIManager.put("TextComponent.arc", arc);

    new Views();

  }

}
