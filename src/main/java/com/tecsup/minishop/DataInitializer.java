package com.tecsup.minishop;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;

    public DataInitializer(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productService.findAll().isEmpty()) {
            productService.save(Product.builder().name("Laptop HP ProBook").price(1250.00).stock(15).build());
            productService.save(Product.builder().name("Mouse Logi MX Master 3S").price(99.90).stock(100).build());
            productService.save(Product.builder().name("Teclado Keychron K2").price(119.99).stock(50).build());
            productService.save(Product.builder().name("Monitor Dell UltraSharp 27").price(349.50).stock(20).build());
            productService.save(Product.builder().name("Audífonos Sony WH-1000XM4").price(279.00).stock(30).build());
            productService.save(Product.builder().name("Silla Ergonómica Sihoo").price(189.99).stock(12).build());
            productService.save(Product.builder().name("Pad de Escritorio Cuero").price(19.90).stock(200).build());
            productService.save(Product.builder().name("Webcam Logitech C920").price(79.99).stock(45).build());
            productService.save(Product.builder().name("Disco SSD Kingston 1TB").price(89.99).stock(80).build());
            productService.save(Product.builder().name("Memoria RAM Corsair 16GB").price(59.90).stock(120).build());
            System.out.println(">>> Base de datos inicializada con 10 productos para pruebas de carga.");
        }
    }
}
