package com.example.microservice_commandes.repository;

import com.example.microservice_commandes.model.Order;
import com.example.microservice_produits.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
