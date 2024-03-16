// Problem-3:
//Come up with an approach for product configuration, where multiple products can be stored.
// Build an in-memory database or in-memory storage. We should be able to have product categories
// along with product descriptions and details. We should be able to store a wide range of types
// of products similar to Amazon, we should be able to implement efficient search of the products
// and flexible configuration of the products. In addition to in-memory storage, build an efficient
// textual search on any of the parameters (similar to search in Amazon).

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductConfigurationSystem {
    private Map<String, Category> categories;

    public ProductConfigurationSystem() {
        categories = new HashMap<>();
    }

    public void addCategory(String categoryName) {
        categories.put(categoryName, new Category(categoryName));
    }

    public void addProduct(String categoryName, String productName, String description, double price) {
        Category category = categories.get(categoryName);
        if (category != null) {
            category.addProduct(new Product(productName, description, price));
        }
    }

    public List<Product> searchProducts(String keyword) {
        return categories.values().stream()
                .flatMap(category -> category.getProducts().stream())
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ProductConfigurationSystem system = new ProductConfigurationSystem();

        system.addCategory("Electronics");
        system.addCategory("Books");

        system.addProduct("Electronics", "Laptop", "Thin and light laptop", 999.99);
        system.addProduct("Electronics", "Phone", "Smartphone with high-res camera", 599.99);
        system.addProduct("Books", "Novel", "Best-selling novel", 19.99);

        List<Product> electronicsProducts = system.searchProducts("Laptop");
        if (!electronicsProducts.isEmpty()) {
            System.out.println("Laptop products:");
            electronicsProducts.forEach(System.out::println);
        }
    }
}

class Category {
    private String name;
    private List<Product> products;

    public Category(String name) {
        this.name = name;
        products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}

class Product {
    private String name;
    private String description;
    private double price;

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
