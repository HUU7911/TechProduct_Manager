package com.techstore.service;

import com.techstore.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void addProduct_success() {

        Product product = new Product(
                "iPhone 15",
                "Điện thoại",
                25000000,
                10,
                "Apple",
                "iPhone mới"
        );

        productService.addProduct(product);

        List<Product> products = productService.getAllProducts();

        assertTrue(products.contains(product));
    }

    @Test
    void addProduct_nullProduct() {

        assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(null);
        });
    }

    @Test
    void addProduct_emptyName() {

        Product product = new Product(
                "",
                "Laptop",
                20000000,
                5,
                "Dell",
                "Laptop Dell"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(product);
        });
    }

    @Test
    void addProduct_negativePrice() {

        Product product = new Product(
                "Laptop",
                "Laptop",
                -1000,
                5,
                "HP",
                "Sai giá"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(product);
        });
    }

    @Test
    void getProductById_success() {

        Product product = new Product(
                "Galaxy S24",
                "Điện thoại",
                22000000,
                8,
                "Samsung",
                "Flagship"
        );

        productService.addProduct(product);

        Product result = productService.getProductById(product.getId());

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
    }

    @Test
    void getProductById_notFound() {

        assertThrows(NoSuchElementException.class, () -> {
            productService.getProductById("INVALID_ID");
        });
    }

    @Test
    void updateProduct_success() {

        Product product = new Product(
                "Macbook",
                "Laptop",
                30000000,
                3,
                "Apple",
                "Macbook Pro"
        );

        productService.addProduct(product);

        product.setPrice(35000000);

        productService.updateProduct(product);

        Product updated = productService.getProductById(product.getId());

        assertEquals(35000000, updated.getPrice());
    }

    @Test
    void updateProduct_notFound() {

        Product product = new Product(
                "Fake",
                "Laptop",
                1000,
                1,
                "Fake",
                "Fake"
        );

        assertThrows(NoSuchElementException.class, () -> {
            productService.updateProduct(product);
        });
    }

    @Test
    void deleteProduct_success() {

        Product product = new Product(
                "Airpods",
                "Tai nghe",
                5000000,
                10,
                "Apple",
                "Tai nghe không dây"
        );

        productService.addProduct(product);

        productService.deleteProduct(product.getId());

        assertThrows(NoSuchElementException.class, () -> {
            productService.getProductById(product.getId());
        });
    }

    @Test
    void deleteProduct_notFound() {

        assertThrows(NoSuchElementException.class, () -> {
            productService.deleteProduct("INVALID_ID");
        });
    }

    @Test
    void searchProducts_success() {

        Product product = new Product(
                "Dell XPS",
                "Laptop",
                40000000,
                4,
                "Dell",
                "Laptop cao cấp"
        );

        productService.addProduct(product);

        List<Product> results = productService.searchProducts("Dell");

        assertFalse(results.isEmpty());
    }

    @Test
    void sortByPriceAsc_success() {

        List<Product> products = productService.sortByPriceAsc();

        assertFalse(products.isEmpty());

        for (int i = 0; i < products.size() - 1; i++) {
            assertTrue(products.get(i).getPrice()
                    <= products.get(i + 1).getPrice());
        }
    }

    @Test
    void sortByPriceDesc_success() {

        List<Product> products = productService.sortByPriceDesc();

        assertFalse(products.isEmpty());

        for (int i = 0; i < products.size() - 1; i++) {
            assertTrue(products.get(i).getPrice()
                    >= products.get(i + 1).getPrice());
        }
    }

    @Test
    void totalCount_success() {

        int count = productService.totalCount();

        assertTrue(count > 0);
    }

    @Test
    void totalValue_success() {

        double total = productService.totalValue();

        assertTrue(total > 0);
    }

    @Test
    void countByCategory_success() {

        long count = productService.countByCategory("Laptop");

        assertTrue(count >= 0);
    }
}