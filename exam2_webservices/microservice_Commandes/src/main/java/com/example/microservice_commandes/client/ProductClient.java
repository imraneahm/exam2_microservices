package com.example.microservice_commandes.client ;
import com.example.microservice_produits.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8081/products")
public interface ProductClient {
    @GetMapping("/{id}")
    Product getProduct(@PathVariable("id") Long id);
}
