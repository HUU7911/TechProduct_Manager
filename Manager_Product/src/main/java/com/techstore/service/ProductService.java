package com.techstore.service;

import com.techstore.entity.Product;
import com.techstore.mapper.ProductMapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProductService implements ProductMapper {

    private final List<Product> products = new ArrayList<>();
    private static final String FILE_PATH = "data/products.txt";

    public ProductService() {
        new File("data").mkdirs();
        loadFromFile();
        if (products.isEmpty()) {
            generateSampleData();
            saveToFile();
        }
    }

    private void generateSampleData() {
        String[] cats = {"Điện thoại", "Laptop", "Tablet", "Tai nghe", "Đồng hồ thông minh", "Phụ kiện"};
        String[] brands = {"Apple", "Samsung", "Sony", "Dell", "HP", "Lenovo", "Asus", "Xiaomi", "Oppo", "Huawei"};
        String[] models = {"Pro Max", "Ultra", "Plus", "Lite", "Standard", "Edge", "Note", "S Series", "A Series", "Mini"};
        Random rnd = new Random();
        for (int i = 0; i < 1000; i++) {
            String cat = cats[rnd.nextInt(cats.length)];
            String brand = brands[rnd.nextInt(brands.length)];
            String model = models[rnd.nextInt(models.length)];
            String name = brand + " " + cat + " " + model + " " + (2020 + rnd.nextInt(5));
            double price = 1_000_000 + rnd.nextInt(49_000_000);
            int qty = 10 + rnd.nextInt(491);
            String desc = cat + " cao cấp từ " + brand + ", hiệu suất mạnh mẽ.";
            products.add(new Product(name, cat, price, qty, brand, desc));
        }
    }

    @Override
    public void addProduct(Product p) {
        if (p == null) throw new IllegalArgumentException("Sản phẩm không được null");
        if (p.getName() == null || p.getName().isBlank()) throw new IllegalArgumentException("Tên sản phẩm không được trống");
        if (p.getPrice() < 0) throw new IllegalArgumentException("Giá sản phẩm không hợp lệ");
        products.add(p);
        saveToFile();
    }

    @Override
    public void updateProduct(Product p) {
        if (p == null) throw new IllegalArgumentException("Sản phẩm không được null");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(p.getId())) {
                products.set(i, p); saveToFile(); return;
            }
        }
        throw new NoSuchElementException("Không tìm thấy sản phẩm ID: " + p.getId());
    }

    @Override
    public void deleteProduct(String id) {
        if (!products.removeIf(p -> p.getId().equals(id)))
            throw new NoSuchElementException("Không tìm thấy sản phẩm ID: " + id);
        saveToFile();
    }

    @Override
    public List<Product> getAllProducts() { return new ArrayList<>(products); }

    @Override
    public Product getProductById(String id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm"));
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) return getAllProducts();
        String kw = keyword.toLowerCase().trim();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(kw)
                        || p.getBrand().toLowerCase().contains(kw)
                        || p.getCategory().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortByPriceAsc() {
        return products.stream().sorted(Comparator.comparingDouble(Product::getPrice)).collect(Collectors.toList());
    }

    @Override
    public List<Product> sortByPriceDesc() {
        return products.stream().sorted(Comparator.comparingDouble(Product::getPrice).reversed()).collect(Collectors.toList());
    }

    @Override
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Product p : products) pw.println(p.toTxtLine());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu file sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;
        products.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Product p = Product.fromTxtLine(line);
                if (p != null) products.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file sản phẩm: " + e.getMessage());
        }
    }

    public long countByCategory(String cat) {
        return products.stream().filter(p -> p.getCategory().equals(cat)).count();
    }

    public double totalValue() {
        return products.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
    }

    public int totalCount() { return products.size(); }
}

