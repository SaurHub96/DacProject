package com.management.hotel.controllers.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.hotel.controllers.entities.Employee;
import com.management.hotel.controllers.entities.User;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, BigInteger> {

	List<Employee> findAllByAdminOrderByEmpIdDesc(User user);

	List<Employee> findAllByAdminAndNameContainsOrderByEmpIdDesc(User user, String value);

	List<Employee> findAllByReception(User user);
	
}
