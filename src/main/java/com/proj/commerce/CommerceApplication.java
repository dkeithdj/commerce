package com.proj.commerce;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.proj.commerce.models.User;
import com.proj.commerce.repositories.UserRepository;

@SpringBootApplication
public class CommerceApplication extends JFrame {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(CommerceApplication.class,
				args);
		UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
		User foo = new User("foo", "foo01");
		User bar = new User("bar", "bar01");
		User baz = new User("baz", "baz01");
		userRepository.saveAll(List.of(foo, bar, baz));

		App cards = new App();

		// JFrame f = new JFrame();
		// JLabel l = new JLabel();

		List<JPanel> listings = new ArrayList<JPanel>();
		userRepository.findAll()
				.forEach(user -> listings.add(cards.card("40D1080Side.png", user)));
		cards.listings(listings);
		// l.setBounds(0, 0, 200, 200);
		// f.add(l);
		// f.setSize(new Dimension(200, 200));
		// f.setVisible(true);
		// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// @Bean
	// public CommandLineRunner commandLineRunner(UserRepository userRepository) {
	// return args -> {
	// UserInfo foo = new UserInfo("foo", "foo01");
	// userRepository.save(foo);
	// userRepository.findUser("foo").forEach(System.out::println);
	// // userRepository.findAll().forEach(System.out::println);
	// };
	// }

}
