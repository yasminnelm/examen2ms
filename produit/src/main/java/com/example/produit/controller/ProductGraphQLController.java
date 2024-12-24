package com.example.produit.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> products() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Product productById(@Argument Long id) {
        return productService.getProductById(id).orElse(null);
    }

    @MutationMapping
    public Product addProduct(@Argument String name, @Argument String description,
                              @Argument double price, @Argument int quantity) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        return productService.addProduct(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument String name,
                                 @Argument String description, @Argument Double price,
                                 @Argument Integer quantity) {
        Product productDetails = new Product();
        productDetails.setName(name);
        productDetails.setDescription(description);
        productDetails.setPrice(price);
        productDetails.setQuantity(quantity);
        return productService.updateProduct(id, productDetails);
    }

    @MutationMapping
    public boolean deleteProduct(@Argument Long id) {
        productService.deleteProduct(id);
        return true;
    }
}
