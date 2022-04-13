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

import com.management.hotel.controllers.model.CustomerStatus;
import com.management.hotel.controllers.model.Gender;
import com.management.hotel.controllers.model.IdProofType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="customer")
public class Customer {
	@Id
	@GeneratedValue
	private BigInteger customerId;
	private String name;
	@Enumerated(value = EnumType.STRING)
	private Gender gender=Gender.Male;
	private String phone;
	private String email;
	private int postCode;
	private String country="India";
	@Enumerated(value = EnumType.STRING)
	private IdProofType idProofType=IdProofType.Driving_Licince;
	private String idNumber;
	private String address;
	@Enumerated(value = EnumType.STRING)
	private CustomerStatus customerStatus=CustomerStatus.NotBooked;
	@JoinColumn(name="userId")
	@ManyToOne
	@With
	private User user;
}
