package com.proj.commerce;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.UserInfo;
import com.proj.commerce.repositories.ProductRepository;
import com.proj.commerce.repositories.UserRepository;

@SpringBootApplication
public class CommerceApplication extends JFrame {
	// @Autowired
	// private UserRepository userRepository;
	// @Autowired
	// private ProductRepository productRepository;

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(CommerceApplication.class,
				args);
		UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
		ProductRepository productRepository = configurableApplicationContext.getBean(ProductRepository.class);
		UserInfo foo = new UserInfo("foo", "foo01");
		UserInfo bar = new UserInfo("bar", "bar01");
		UserInfo baz = new UserInfo("baz", "baz01");

		Product keyb = new Product("40D Keyboard", "A 40% mechanical keyboard", 2400d, "40D1080Side.png", 1);
		Product anotherkeyb = new Product("Kurk", "A kurk mechanical keyboard", 2400d, "40D1080Side.png", 1);
		Product frog = new Product("Froge", "A Frog mechanical keyboard", 2500d, "40D1080Side.png", 1);
		Product greg = new Product("Greg", "A greg mechanical keyboard", 6900d, "40D1080Side.png", 1);
		Product pho = new Product("Pho", "A pho mechanical keyboard", 4200d, "40D1080Side.png", 1);

		foo.addProduct(keyb);
		foo.addProduct(anotherkeyb);
		bar.addProduct(frog);
		baz.addProduct(greg);
		baz.addProduct(pho);

		userRepository.saveAll(List.of(foo, bar, baz));

		Views view = new Views();

		view.setProducts(productRepository.findAll());
		view.listings();
		// JFrame f = new JFrame();
		// JLabel l = new JLabel();

		// List<JPanel> listings = new ArrayList<JPanel>();
		// productRepository.findAll();
		// .forEach(product -> listings.add(cards.card(product)));
		// cards.listings(listings);
	}

}
