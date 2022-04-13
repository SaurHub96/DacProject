package com.management.hotel.controllers.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.management.hotel.controllers.entities.Customer;
import com.management.hotel.controllers.entities.User;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, BigInteger> {

	public Optional<Customer> findByCustomerIdAndUser(BigInteger customerId, User user);
	
	public List<Customer> findAllByNameStartingWithOrderByCustomerId(String value);

	public List<Customer> findAllByEmailStartingWithOrderByCustomerId(String value);

	public List<Customer> findAllByPhoneStartingWithOrderByCustomerId(String value);

	public List<Customer> findAllByIdNumberStartingWithOrderByCustomerId(String value);
	
	public List<Customer> findAllByCountryStartingWithOrderByCustomerId(String value);

}
