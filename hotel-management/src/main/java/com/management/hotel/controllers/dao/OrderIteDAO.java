package com.management.hotel.controllers.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.hotel.controllers.entities.OrderItem;
import com.management.hotel.controllers.entities.User;
@Repository
public interface OrderIteDAO extends JpaRepository<OrderItem, BigInteger> {

	List<OrderItem> findAllByItemNameContainingAndUser(String itemName, User user);

	List<OrderItem> findAllByUser(User user);
	
}
