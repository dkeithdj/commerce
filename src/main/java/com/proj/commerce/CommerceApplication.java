package com.proj.commerce;

import java.util.List;

import javax.swing.JFrame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;
import com.proj.commerce.repositories.ProductRepository;
import com.proj.commerce.repositories.ClientRepository;

@SpringBootApplication
public class CommerceApplication extends JFrame {
	// @Autowired
	// private UserRepository userRepository;
	// @Autowired
	// private ProductRepository productRepository;

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(CommerceApplication.class,
				args);
		// SpringApplication.run(CommerceApplication.class,
		// args);

		ClientRepository clientRepository = configurableApplicationContext.getBean(ClientRepository.class);
		ProductRepository productRepository = configurableApplicationContext.getBean(ProductRepository.class);
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

		Views view = new Views();
		view.setProducts(productRepository.findAll());
		view.setUsers(clientRepository.findAll());
		view.listings();

	}

}
