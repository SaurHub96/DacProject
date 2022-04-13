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

import com.management.hotel.controllers.model.RoomStatus;
import com.management.hotel.controllers.model.RoomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="room")
public class Room {
	@Id
	@GeneratedValue
	private BigInteger roomId;
	private int roomNumber;
	private int floorNumber;
	@Enumerated(value = EnumType.STRING)
	private RoomType roomType;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@Enumerated(value = EnumType.STRING)
	private RoomStatus roomStatus;
	private int pricePerDay;
}
