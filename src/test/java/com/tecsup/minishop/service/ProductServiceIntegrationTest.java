package com.tecsup.minishop.service;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
@ActiveProfiles("test")
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    // ---------------------------------------------------------------
    // TEST 1: Guardar producto válido
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe guardar un producto válido correctamente")
    void shouldSaveValidProduct() {
        // ARRANGE
        Product input = Product.builder()
                .name("Auriculares Sony")
                .price(320.00)
                .stock(15)
                .build();

        Product expected = Product.builder()
                .id(1L)
                .name("Auriculares Sony")
                .price(320.00)
                .stock(15)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(expected);

        // ACT
        Product result = productService.save(input);

        // ASSERT
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Auriculares Sony");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ---------------------------------------------------------------
    // TEST 2: Precio cero o negativo debe lanzar excepción
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción cuando el precio es cero o negativo")
    void shouldThrowExceptionWhenPriceIsInvalid() {
        // ARRANGE
        Product product = Product.builder()
                .name("Producto inválido")
                .price(0.0)
                .stock(5)
                .build();

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.save(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio debe ser mayor a cero");

        // El repositorio NUNCA debe ser llamado si la validación falla
        verify(productRepository, never()).save(any());
    }

    // ---------------------------------------------------------------
    // TEST 3: Stock negativo debe lanzar excepción
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción cuando el stock es negativo")
    void shouldThrowExceptionWhenStockIsNegative() {
        // ARRANGE
        Product product = Product.builder()
                .name("Producto sin stock")
                .price(100.00)
                .stock(-1)
                .build();

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.save(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El stock no puede ser negativo");
    }

    // ---------------------------------------------------------------
    // TEST 4: Retornar todos los productos
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe retornar todos los productos")
    void shouldReturnAllProducts() {
        // ARRANGE
        List<Product> products = List.of(
                Product.builder().id(1L).name("Producto A").price(100.0).stock(5).build(),
                Product.builder().id(2L).name("Producto B").price(200.0).stock(3).build()
        );
        when(productRepository.findAll()).thenReturn(products);

        // ACT
        List<Product> result = productService.findAll();

        // ASSERT
        assertThat(result).hasSize(2);
        verify(productRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------------
    // TEST 5: ID inexistente debe lanzar RuntimeException
    // ---------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar excepción cuando el producto no existe por ID")
    void shouldThrowExceptionWhenProductNotFound() {
        // ARRANGE
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Producto no encontrado con id: 99");
    }
}
