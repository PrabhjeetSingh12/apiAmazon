package com.apiScraping.api;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlipkartApiService {

    @Value("${flipkart.api.key}")
    private String apiKey;

    @Value("${amazon.api.key}")
    private String amazonApiKey;

    @Autowired
    private ProductRepository productRepository;

    public String getProductDetails(String pid) {
        try {
            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://real-time-flipkart-api.p.rapidapi.com/product-details?pid=" + pid))
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", "real-time-flipkart-api.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log the full response
            System.out.println("API Response: " + response.body());

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.body());

            // Extract relevant data
            String title = jsonResponse.getString("title"); // Correctly fetching 'title'
            int priceAsInt = jsonResponse.getInt("price"); // Fetching 'price' as Integer
            BigDecimal price = BigDecimal.valueOf(priceAsInt); // Converting Integer to BigDecimal

            // Save the product details in the database
            Product product = new Product();
            product.setProductId(pid);
            product.setName(title);  // Now using 'title' to set name
            product.setPrice(price);
            product.setCreatedAt(LocalDateTime.now()); // Set current date and time
            product.setSource("Flipkart"); // Set source

            productRepository.save(product);

            // Return the response body
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching product details";
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByProductId(String productId){
        return productRepository.findByProductId(productId);
    }

    // Method to fetch products by name
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }


    // Method to fetch products by partial name
    public List<Product> getProductsByPartialName(String partialName) {
        return productRepository.findByNameContainingIgnoreCase(partialName);
    }

    // Method to fetch Amazon product details
    public String getAmazonProductDetails(String asin) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://real-time-amazon-data.p.rapidapi.com/product-details?asin=" + asin + "&country=IN"))
                    .header("x-rapidapi-key", amazonApiKey)
                    .header("x-rapidapi-host", "real-time-amazon-data.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Log the full response
            System.out.println("Amazon API Response: " + response.body());

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.body());

            // Check for a status and data
            if (!jsonResponse.getString("status").equals("OK")) {
                System.out.println("Error from Amazon API: " + jsonResponse.getString("status"));
                return "Error fetching Amazon product details: " + jsonResponse.getString("status");
            }

            // Extract relevant data from the "data" field
            JSONObject data = jsonResponse.getJSONObject("data");

            // Extracting the title and price
            String name = data.getString("product_title");
            BigDecimal price = new BigDecimal(data.getString("product_price").replace(",", "").replace("₹", "").trim());

            // Save the product details in the database
            Product product = new Product();
            product.setProductId(asin);
            product.setName(name);
            product.setPrice(price);
            product.setCreatedAt(LocalDateTime.now());
            product.setSource("Amazon");

            productRepository.save(product);

            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching Amazon product details";
        }
    }

    public List<Product> getProductsByInitialAndColor(String initial, String color) {
        return productRepository.findByInitialAndColor(initial, color);
    }

}
