package com.management.hotel.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.management.hotel.controllers.dao.CustomerDAO;
import com.management.hotel.controllers.dao.RoomDAO;
import com.management.hotel.controllers.entities.Customer;
import com.management.hotel.controllers.entities.Room;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.RoomStatus;
import com.management.hotel.controllers.model.RoomType;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.UserService;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private RoomDAO roomDAO;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerDAO customerDAO;

	@GetMapping
	public ModelAndView addRoomView(@RequestParam("roomId") BigInteger roomId) {
		User user = userService.getLogedUser();
		ModelAndView modelAndView = new ModelAndView("room/add-room");
		Room room = (Room) roomDAO.findByRoomIdAndUser(roomId, user)
				.orElse(new Room(null, 0, 0, RoomType.Single, null, RoomStatus.Available, 0));;
		modelAndView.getModel().put("room", room);
		modelAndView.getModel().put("roomTypes", Arrays.asList(RoomType.values()));
		return modelAndView;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> addRoom(@RequestBody Room room) {
		Map<String, Object> result = new HashMap<>();
		User user = userService.getLogedUser();
		try {
			if (user != null && room.getRoomNumber() != 0 && room.getFloorNumber() != 0 && room.getPricePerDay() != 0
					&& room.getRoomType() != null) {
				Room roomExist = roomDAO.findByRoomNumberAndUser(room.getRoomNumber(), user);
				if (roomExist == null) {
					room.setRoomStatus(RoomStatus.Available);
					room.setRoomId(null);
					room.setUser(user);
					room = roomDAO.save(room);
					result.put("room", room);
					result.put(MessageConstant.success, "Room is Added");
				} else {
					roomExist.setFloorNumber(room.getFloorNumber());
					roomExist.setPricePerDay(room.getPricePerDay());
					roomExist.setRoomType(room.getRoomType());
					roomExist = roomDAO.save(roomExist);
					result.put("room", roomExist);
					result.put(MessageConstant.success, "Room is Updated");
				}

			} else {
				result.put(MessageConstant.error, "Invalid input");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}

	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> deleteRoom(@RequestBody Room room) {
		Map<String, Object> result = new HashMap<>();
		User user = userService.getLogedUser();
		try {
			room = roomDAO.findById(room.getRoomId()).orElse(null);
			if (room != null && user != null && room.getUser().getUserId().equals(room.getUser().getUserId())) {
				roomDAO.delete(room);
				result.put(MessageConstant.success, room.getRoomNumber() + " Deleted Successfully");
			} else {
				result.put(MessageConstant.error, "Selected Room Not Present");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}

	@GetMapping("/roomdashbord")
	public ModelAndView room(@RequestParam("customerId") BigInteger customerId) {
		ModelAndView modelAndView = new ModelAndView("room/roomdashbord");
		if (!BigInteger.ZERO.equals(customerId)) {
			Customer customer = customerDAO.findById(customerId).orElse(null);
			modelAndView.getModel().put("bookCust", customer);
		}
		return modelAndView;
	}

	@GetMapping("/search")
	public ModelAndView searchRoom(@RequestParam("type") String type, @RequestParam("value") String value,
			@RequestParam("customerId") BigInteger customerId) {
		ModelAndView modelAndView = new ModelAndView("room/search-room");
		List<Room> rooms = new ArrayList<>();
		User user = userService.getLogedUser();
		user = UserRoles.ADMIN.equals(user.getRole()) ? user : user.getAdminUser();
		if (user != null) {
                if("RoomType".equalsIgnoreCase(type) && value!=null && !value.trim().isEmpty()) {
				
			    }else if("".equalsIgnoreCase(type) && value!=null && !value.trim().isEmpty()) {
				
			    }else if("".equalsIgnoreCase(type) && value!=null && !value.trim().isEmpty()) {
				
			    }else {}
				switch (type) {
				case "RoomType":
					rooms = BigInteger.ZERO.equals(customerId)
							? roomDAO.findAllByRoomTypeAndUser(RoomType.valueOf(value), user)
							: roomDAO.findAllByRoomTypeAndRoomStatusAndUser(RoomType.valueOf(value),
									RoomStatus.Available, user);
					break;
				case "RoomNumber":
					rooms = BigInteger.ZERO.equals(customerId)
							? roomDAO.findAllByRoomNumberAndUser(Integer.parseInt(value.trim()), user)
							: roomDAO.findAllByRoomNumberAndRoomStatusAndUser(Integer.parseInt(value.trim()),
									RoomStatus.Available, user);
					break;
				case "FloorNumber":
					rooms = BigInteger.ZERO.equals(customerId)
							? roomDAO.findAllByFloorNumberAndUser(Integer.parseInt(value.trim()), user)
							: roomDAO.findAllByFloorNumberAndRoomStatusAndUser(Integer.parseInt(value.trim()),
									RoomStatus.Available, user);
					break;
				default:
					rooms = BigInteger.ZERO.equals(customerId) ? roomDAO.findAll()
							: roomDAO.findAllByRoomStatus(RoomStatus.Available);
					break;
				}
			}
	     
		if (!BigInteger.ZERO.equals(customerId)) {
			modelAndView.getModel().put("customerId", customerId);
		}
		modelAndView.getModel().put("rooms", rooms);
		return modelAndView;
	}
}
