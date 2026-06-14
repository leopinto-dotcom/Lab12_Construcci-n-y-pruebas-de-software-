package com.tecsup.minishop.repository;

import com.tecsup.minishop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Limpiamos la BD antes de cada test para garantizar aislamiento
        productRepository.deleteAll();
    }

    // ---------------------------------------------------------------
    // TEST 1: Guardar producto y verificar asignación de ID automático
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe guardar un producto y asignarle ID automáticamente")
    void shouldSaveProductAndAssignId() {
        // ARRANGE
        Product product = Product.builder()
                .name("Laptop Lenovo")
                .price(2500.00)
                .stock(10)
                .build();

        // ACT
        Product saved = productRepository.save(product);

        // ASSERT
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Laptop Lenovo");
        assertThat(saved.getPrice()).isEqualTo(2500.00);
    }

    // ---------------------------------------------------------------
    // TEST 2: Buscar producto por ID existente
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe encontrar un producto por ID existente")
    void shouldFindProductById() {
        // ARRANGE
        Product product = productRepository.save(
                Product.builder().name("Mouse Logitech").price(85.00).stock(50).build()
        );

        // ACT
        Optional<Product> found = productRepository.findById(product.getId());

        // ASSERT
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mouse Logitech");
    }

    // ---------------------------------------------------------------
    // TEST 3: ID inexistente debe retornar Optional vacío
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe retornar vacío cuando el ID no existe")
    void shouldReturnEmptyWhenIdNotFound() {
        // ACT
        Optional<Product> found = productRepository.findById(999L);

        // ASSERT
        assertThat(found).isEmpty();
    }

    // ---------------------------------------------------------------
    // TEST 4: Búsqueda por nombre ignorando mayúsculas/minúsculas
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe buscar productos por nombre ignorando mayúsculas")
    void shouldFindProductsByNameIgnoringCase() {
        // ARRANGE
        productRepository.save(Product.builder().name("Teclado Mecánico").price(150.00).stock(20).build());
        productRepository.save(Product.builder().name("Teclado Membrana").price(45.00).stock(30).build());
        productRepository.save(Product.builder().name("Monitor Dell").price(800.00).stock(5).build());

        // ACT
        List<Product> result = productRepository.findByNameContainingIgnoreCase("teclado");

        // ASSERT
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getName)
                .containsExactlyInAnyOrder("Teclado Mecánico", "Teclado Membrana");
    }
}
