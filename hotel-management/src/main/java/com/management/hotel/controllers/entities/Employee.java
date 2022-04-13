package com.management.hotel.controllers.entities;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.management.hotel.controllers.model.Gender;
import com.management.hotel.controllers.model.IdProofType;
import com.management.hotel.controllers.model.UserRoles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="employee")
public class Employee {
	@Id
	@GeneratedValue
	private BigInteger empId;
	private String name;
	private String email;
	private BigInteger phone;
	@Enumerated(value = EnumType.STRING)
	private Gender gender=Gender.Male;
	private int postCode;
	private String country="India";
	@Enumerated(value = EnumType.STRING)
	private IdProofType idProofType=IdProofType.Driving_Licince;
	private String idNumber;
	private String address;
	@ManyToOne
	@JoinColumn(name = "admin_user_id")
	private User admin;
	@OneToOne
	@JoinColumn(name = "reception_user_id")
	private User reception;
	public User toReception() {
		return new User(null, name, email, this.getReception().getPassword(), phone, null, UserRoles.RECEPTION, admin);
	}
	
}
