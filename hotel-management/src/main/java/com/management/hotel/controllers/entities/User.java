package com.management.hotel.controllers.entities;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.With;

import com.management.hotel.controllers.model.UserRoles;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity 
@Table(name="user")  
public class User {
	@Id
	@GeneratedValue
	private BigInteger userId;
	private String name;
	private String email;
	private String password;
	private BigInteger phone;
	private String hotelName;
	@Enumerated(value = EnumType.STRING)
	private UserRoles role;
	@ManyToOne
	@JoinColumn(name="admin_user_id")
	private User adminUser;
}
