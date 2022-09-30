package com.proj.commerce;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;

@SpringBootApplication
public class CommerceApplication extends JFrame {
	// @Autowired
	// private UserRepository userRepository;
	// @Autowired
	// private ProductRepository productRepository;

	public static void main(final String[] args) {
		SpringApplication.run(CommerceApplication.class,
				args);
		// SpringApplication.run(CommerceApplication.class,
		// args);

		// ClientRepository clientRepository =
		// configurableApplicationContext.getBean(ClientRepository.class);
		// ProductRepository productRepository =
		// configurableApplicationContext.getBean(ProductRepository.class);
		// Client foo = new Client("foo", "foo01", 10000);
		// Client bar = new Client("bar", "bar01", 100000);
		// Client baz = new Client("baz", "baz01", 100000);

		// Product keyb = new Product("40D Keyboard", "A 40% mechanical keyboard",
		// 2400d, "40D1080Side.png", 1);
		// Product anotherkeyb = new Product("Kurk", "A kurk mechanical keyboard",
		// 2400d, "40D1080Side.png", 1);
		// Product frog = new Product("Froge", "A Frog mechanical keyboard", 2500d,
		// "40D1080Side.png", 1);
		// Product greg = new Product("Greg", "A greg mechanical keyboard", 6900d,
		// "40D1080Side.png", 1);
		// Product pho = new Product("Pho", "A pho mechanical keyboard", 4200d,
		// "40D1080Side.png", 1);
		// Product tadpole = new Product("Tadpole", "A tadpole mechanical keyboard",
		// 69420d, "40D1080Side.png", 1);

		// foo.addProduct(keyb);
		// foo.addProduct(anotherkeyb);
		// bar.addProduct(frog);
		// baz.addProduct(greg);
		// baz.addProduct(pho);
		// baz.addProduct(tadpole);
		// clientRepository.saveAll(List.of(foo, bar, baz));

		// Product case40 = new Product("40D Case", "A 40D Keyboard case", 100d,
		// "40DnoPCB.PNG", 1);
		// Product pcb = new Product("Mona v.1.1", "A Mona PCB for mechanical
		// keyboards", 100d, "reference.png", 2);
		// case40.setClient(clientRepository.findById(2L).get());
		// pcb.setClient(clientRepository.findById(2L).get());

		// productRepository.save(case40);
		// productRepository.save(pcb);
		// CoreManagers.initialize();
		FlatLaf.registerCustomDefaultsSource("com.proj.commerce.ui");
		// FlatDarkLaf.setup();
		// FlatGruvboxDarkHardIJTheme.setup();
		FlatAtomOneDarkContrastIJTheme.setup();
		// FlatAtomOneLightContrastIJTheme.setup();
		// UIManager.put("Panel.arc", 999);

		new Views();

		// view.setProducts(productRepository.findAll());
		// view.setUsers(clientRepository.findAll());
	}

}
