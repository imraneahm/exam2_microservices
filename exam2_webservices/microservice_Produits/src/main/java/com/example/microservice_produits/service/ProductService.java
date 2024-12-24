package com.example.microservice_produits.service;

import com.example.microservice_produits.model.Product;
import com.example.microservice_produits.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Ajouter un produit
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Récupérer un produit par ID
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));
    }

    // Mettre à jour un produit
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProduct(id); // Vérifie que le produit existe
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

    // Supprimer un produit
    public void deleteProduct(Long id) {
        Product existingProduct = getProduct(id); // Vérifie que le produit existe avant de supprimer
        productRepository.delete(existingProduct);
    }

    // Récupérer tous les produits
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
