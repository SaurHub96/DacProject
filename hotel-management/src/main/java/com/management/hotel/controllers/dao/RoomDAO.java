package com.management.hotel.controllers.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.hotel.controllers.entities.Room;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.RoomStatus;
import com.management.hotel.controllers.model.RoomType;

@Repository
public interface RoomDAO extends JpaRepository<Room, BigInteger> {
	public List<Room> findAllByRoomNumberAndUser(int roomNumber,User user);
	public Room findByRoomNumberAndUser(int roomNumber,User user);
	public List<Room> findAllByRoomTypeAndUser(RoomType roomType,User user);
	public List<Room> findAllByFloorNumberAndUser(int floorNmber,User user);
	public Optional<Room> findByRoomIdAndUser(BigInteger roomId, User user);
	public Optional<Room> findByRoomIdAndRoomStatusAndUser(BigInteger roomId, RoomStatus available, User user);
	
	public List<Room> findAllByRoomStatus(RoomStatus available);
	public List<Room> findAllByRoomTypeAndRoomStatusAndUser(RoomType valueOf, RoomStatus available, User user);
	public List<Room> findAllByRoomNumberAndRoomStatusAndUser(int parseInt, RoomStatus available, User user);
	public List<Room> findAllByFloorNumberAndRoomStatusAndUser(int parseInt, RoomStatus available, User user);
	
	
}
