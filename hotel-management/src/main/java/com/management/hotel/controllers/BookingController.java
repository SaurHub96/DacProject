package com.management.hotel.controllers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.management.hotel.controllers.dao.BookingDAO;
import com.management.hotel.controllers.dao.BookingOrderDAO;
import com.management.hotel.controllers.dao.CustomerDAO;
import com.management.hotel.controllers.dao.RoomDAO;
import com.management.hotel.controllers.entities.Booking;
import com.management.hotel.controllers.entities.BookingOrders;
import com.management.hotel.controllers.entities.Customer;
import com.management.hotel.controllers.entities.Room;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.BookCutomerRoom;
import com.management.hotel.controllers.model.CustomerStatus;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.PaidBy;
import com.management.hotel.controllers.model.RoomStatus;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.BookingService;
import com.management.hotel.services.NumberToWords;
import com.management.hotel.services.UserService;
import com.management.hotel.services.Validations;

@Controller
@RequestMapping("/booking")
public class BookingController {
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private RoomDAO roomDAO;
	@Autowired
	private UserService userService;
	@Autowired
	private BookingDAO bookingDAO;
	@Autowired
	private BookingOrderDAO bookingOrderDAO;

	@GetMapping
	public ModelAndView loadBookingDetailsOf(@RequestParam("bookingId") BigInteger bookingId, @RequestParam("type") String type) {
		ModelAndView modelAndView = new ModelAndView("pay".equals(type)?"booking/view-booking-pay":"booking/view-booking");
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		if (user != null && bookingId != null && !BigInteger.ZERO.equals(bookingId)) {
			Booking booking = bookingDAO.findByIdAndUser(bookingId, user).orElse(null);
			if (booking != null) {
				if("pay".equals(type)) {
					List<BookingOrders> orders=bookingOrderDAO.findAllByBooking(booking);
					booking.setAmount(Double.valueOf(BookingService.getBilledAmonut(booking,orders)));
					bookingDAO.save(booking);
					if(orders!=null && !orders.isEmpty()) {
						modelAndView.getModel().put("orderTotal", BookingService.getOrderTotal(orders));
						modelAndView.getModel().put("orders", orders);
					}
					modelAndView.getModel().put("roomTime",BookingService.getRoomBilingTime(booking));
					modelAndView.getModel().put("roomTotal",BookingService.getBilledAmonut(booking,null));
					int amount=(int)booking.getAmount().longValue();
					modelAndView.getModel().put("finalTotalInWord",NumberToWords.getWord(amount)+" Only");
				}
				modelAndView.getModel().put("currentTime",new Date());
				modelAndView.getModel().put("booking", booking);
			}
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		modelAndView.getModel().put("dateTimeFormat", BookingService.localDateTimeFormat);
		modelAndView.getModel().put("cuerrentDate", calendar.getTime());
		calendar.add(Calendar.DATE, 1);
		modelAndView.getModel().put("maxCheckOut", calendar.getTime());
		modelAndView.getModel().put("paidBys", Arrays.asList(PaidBy.values()));
		return modelAndView;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> bookRoom(@RequestBody BookCutomerRoom bookCutomerRoom) {
		Map<String, Object> result = new HashMap<>();
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			Validations.isValid("long", "customerId", bookCutomerRoom.getCustomerId().toString(), result);
			Validations.isValid("long", "roomId", bookCutomerRoom.getRoomId().toString(), result);
			if (!result.containsKey(MessageConstant.validation)) {
				Customer customer = customerDAO.findByCustomerIdAndUser(bookCutomerRoom.getCustomerId(), user)
						.orElse(null);
				Room room = roomDAO
						.findByRoomIdAndRoomStatusAndUser(bookCutomerRoom.getRoomId(), RoomStatus.Available, user)
						.orElse(null);
				if (customer != null && room != null) {
					room.setRoomStatus(RoomStatus.Booked);
					customer.setCustomerStatus(CustomerStatus.Booked);
					Booking booking = new Booking();
					booking.setCheckIn(new Date());
					booking.setCustomer(customer);
					booking.setRoom(room);
					customerDAO.save(customer);
					roomDAO.save(room);
					bookingDAO.save(booking);
					result.put(MessageConstant.success, "Booking Success");
					result.put("customer", customer);
					result.put("room", room);
				} else {
					result.put(MessageConstant.error, "Invalid cutomer of room");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> update(@RequestBody BookCutomerRoom bookingReq) {
		Map<String, Object> result = new HashMap<>();
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			Validations.isValid("long", "customerId", bookingReq.getBookingId().toString(), result);
			Validations.isValid("long", "roomNumber", bookingReq.getRoomNumber()+"", result);
			if (!result.containsKey(MessageConstant.validation)) {
				Booking booking=bookingDAO.findByIdAndUser(bookingReq.getBookingId(), user).orElse(null);
				if(booking!=null) {
					Room bookedRoom=booking.getRoom();
					if(bookingReq.getRoomNumber()!=0 && booking.getRoom().getRoomNumber()!=bookingReq.getRoomNumber()) {
						List<Room> room = roomDAO.findAllByRoomNumberAndRoomStatusAndUser(bookingReq.getRoomNumber(), RoomStatus.Available, user);
						if(room!=null && !room.isEmpty()) {
							bookedRoom.setRoomStatus(RoomStatus.Available);
							Room newBookedRoom=room.get(0);
							newBookedRoom.setRoomStatus(RoomStatus.Booked);
							booking.setRoom(newBookedRoom);
							roomDAO.save(bookedRoom);
							roomDAO.save(newBookedRoom);
							result.put(MessageConstant.success, "Booking Update Success");
						}else {
							result.put(MessageConstant.error, "Invalid Room Number");
						}
					}
					
					Date checkIn=bookingReq.getCheckInDate();
					booking.setCheckIn(checkIn!=null?checkIn:booking.getCheckIn());
					booking.setRemark(bookingReq.getRemark()!=null && !bookingReq.getRemark().trim().isEmpty()?bookingReq.getRemark():"");
					
					Date checkOut=bookingReq.getCheckOutDate();
					if(checkOut!=null) {
						if(checkOut.after(checkIn)) {
							booking.setCheckOut(checkOut);
							List<BookingOrders> orders=bookingOrderDAO.findAllByBooking(booking);
							booking.setAmount(Double.valueOf(BookingService.getBilledAmonut(booking,orders)));
							result.put(MessageConstant.success, "Booking Update Success= "+booking.getAmount());
						}else {
							result.put(MessageConstant.error, "Invalid Checkout Date");
						}
						
					}else if(booking.getCheckOut()!=null  && booking.getAmount()!=null && !Double.valueOf(0).equals(booking.getAmount())
							&& bookingReq.getPaidDetails()!=null && !bookingReq.getPaidDetails().isEmpty()) {
						booking.setPaidBy(bookingReq.getPaidBy());
						booking.setPaidDetails(bookingReq.getPaidDetails());
						booking.setPaymentDate(new Date());
						Customer customer=booking.getCustomer();
						customer.setCustomerStatus(CustomerStatus.NotBooked);
						customerDAO.save(customer);
						Room room=booking.getRoom();
						room.setRoomStatus(RoomStatus.Available);
						roomDAO.save(room);
						result.put(MessageConstant.success, "Paiment success= "+booking.getAmount());
					}
					bookingDAO.save(booking);
					if(result.isEmpty()) {
						result.put(MessageConstant.success, "Booking Update Success");
					}
				}else {
					result.put(MessageConstant.error, "Invalid Booking detial");
				}
				
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
	public Map<String, Object> delete(@RequestBody BookCutomerRoom bookingReq) {
		Map<String, Object> result = new HashMap<>();
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			Validations.isValid("long", "bookingId", bookingReq.getBookingId().toString(), result);
			if (!result.containsKey(MessageConstant.validation)) {
				Booking booking=bookingDAO.findByIdAndUser(bookingReq.getBookingId(), user).orElse(null);
				if(booking!=null && booking.getPaidDetails()==null) {
					Room room=booking.getRoom();
					Customer customer=booking.getCustomer();
					customer.setCustomerStatus(CustomerStatus.NotBooked);
					room.setRoomStatus(RoomStatus.Available);
					roomDAO.save(room);
					customerDAO.save(customer);
					bookingDAO.delete(booking);
				}else {
					result.put(MessageConstant.error, "Invalid Booking detials");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}

	@GetMapping("/search")
	public ModelAndView loadBookingDetailsOf(@RequestParam("type") String type,@RequestParam("value") String value) {
		ModelAndView modelAndView = new ModelAndView("booking/search-booking");
		List<Booking> bookings = null;
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		switch (type) {
		case "roomNumber": {
			bookings = bookingDAO.findAllByRoomNumberAndUser(Integer.parseInt(value), user);
			break;
		}
		case "name": {
			bookings = bookingDAO.findAllByNameStartWithAndUser(value + "%", user);
			break;
		}
		case "customerId": {
			bookings = bookingDAO.findAllByCustomerIdAndUser(new BigInteger(value), user);
			break;
		}
		default: {
			bookings = bookingDAO.findAll(Sort.by(Arrays.asList(Order.desc("checkIn"), Order.desc("bookingId"))));
			break;
		}
		}
		modelAndView.getModel().put("bookings", bookings);
		return modelAndView;
	}

	@RequestMapping("/bookingdashbord")
	public ModelAndView bookingDashbord(@RequestParam("customerId") long customerId ) {
		ModelAndView modelAndView=new ModelAndView("booking/bookingdashbord");
		modelAndView.getModel().put("customerId", customerId);
		return modelAndView;
	}

}
