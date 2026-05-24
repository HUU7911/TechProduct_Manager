package com.techstore.mapper;

import com.techstore.entity.Product;
import java.util.List;

public interface ProductMapper {
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(String id);
    List<Product> getAllProducts();
    Product getProductById(String id);
    List<Product> searchProducts(String keyword);
    List<Product> sortByPriceAsc();
    List<Product> sortByPriceDesc();
    void saveToFile();
    void loadFromFile();
}
