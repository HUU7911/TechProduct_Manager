package com.techstore.entity;

import java.awt.Color;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String brand;
    private String description;
    private Color headerColor;

    public Product() {}

    public Product(String name, String category, double price, int quantity, String brand, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name; this.category = category; this.price = price;
        this.quantity = quantity; this.brand = brand; this.description = description;
        this.headerColor = generateRandomColor();
    }

    public Product(String id, String name, String category, double price, int quantity,
                   String brand, String description, Color headerColor) {
        this.id = id; this.name = name; this.category = category; this.price = price;
        this.quantity = quantity; this.brand = brand; this.description = description;
        this.headerColor = headerColor != null ? headerColor : generateRandomColor();
    }

    private Color generateRandomColor() {
        int r = (int)(Math.random() * 80) + 20;
        int g = (int)(Math.random() * 80) + 20;
        int b = (int)(Math.random() * 120) + 100;
        return new Color(r, g, b);
    }

    public String toTxtLine() {
        return id + "|" + name + "|" + category + "|" + price + "|" + quantity
                + "|" + brand + "|" + description + "|" + headerColor.getRGB();
    }

    public static Product fromTxtLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 7) return null;
            Product prod = new Product();
            prod.id = p[0]; prod.name = p[1]; prod.category = p[2];
            prod.price = Double.parseDouble(p[3]);
            prod.quantity = Integer.parseInt(p[4]);
            prod.brand = p[5]; prod.description = p[6];
            prod.headerColor = p.length > 7 ? new Color(Integer.parseInt(p[7])) : new Color(50, 50, 180);
            return prod;
        } catch (Exception e) { return null; }
    }
}

