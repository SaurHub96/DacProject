package com.management.hotel.controllers.dao;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.hotel.controllers.entities.User;

@Repository
public interface UserDAO extends JpaRepository<User, BigInteger> {
	public User findByEmail(String email);
}
