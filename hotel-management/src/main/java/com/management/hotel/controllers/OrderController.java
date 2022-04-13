package com.management.hotel.controllers;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.management.hotel.controllers.dao.BookingDAO;
import com.management.hotel.controllers.dao.BookingOrderDAO;
import com.management.hotel.controllers.dao.EmployeeDAO;
import com.management.hotel.controllers.dao.OrderIteDAO;
import com.management.hotel.controllers.entities.Booking;
import com.management.hotel.controllers.entities.BookingOrders;
import com.management.hotel.controllers.entities.Employee;
import com.management.hotel.controllers.entities.OrderItem;
import com.management.hotel.controllers.entities.Room;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.Orders;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.UserService;
import com.management.hotel.services.Validations;

@Controller
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private BookingDAO bookingDAO;
	@Autowired
	private OrderIteDAO orderIteDAO;
	@Autowired
	private BookingOrderDAO bookingOrderDAO;
	@Autowired 
	private UserService userService;
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@GetMapping
	public ModelAndView ordersDashbord(@RequestParam("bookingId") BigInteger bookingId) {
		ModelAndView modelAndView=new ModelAndView("orders/ordersdashbord");
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		if(user!=null) {
			List<Booking> bookings=bookingDAO.findAllByUserAndPaidDetailsIsNull(user);
			if(bookings!=null && !bookings.isEmpty()) {
				modelAndView.getModel().put("bookings", bookings);
			}
			if(bookingId!=null && !BigInteger.ZERO.equals(bookingId)) {
				Booking booking=bookingDAO.findById(bookingId).orElse(null);
				if(booking!=null) {
					modelAndView.getModel().put("booking", booking);
				}
			}
		}
		return modelAndView;
	}
	@GetMapping("/search")
	public ModelAndView orders(@RequestParam("type") String type,@RequestParam("value") String value) {
		ModelAndView modelAndView=new ModelAndView("orders/search-order");
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		if( user!=null && "bookingId".equals(type)) {
			BigInteger bookingId=new BigInteger(value);
			Booking booking=bookingDAO.findByIdAndUser(bookingId,user).orElse(null);
			if(booking!=null) {
				List<BookingOrders> bookingOrders=bookingOrderDAO.findAllByBooking(booking);
				if(bookingOrders!=null && !bookingOrders.isEmpty()) {
					modelAndView.getModel().put("bookingOrders", bookingOrders);
				}
				modelAndView.getModel().put("booking",booking);
			}
		}
		return modelAndView;
	}
	
	@GetMapping("/{bookingId}/{orderId}")
	public ModelAndView addOrder(@PathVariable("bookingId") BigInteger bookingId, @PathVariable("orderId") BigInteger orderId) {
		ModelAndView modelAndView=new ModelAndView("orders/add-order");
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		if( user!=null && !BigInteger.ZERO.equals(bookingId)) {
			Booking booking=bookingDAO.findByIdAndUser(bookingId,user).orElse(null);
			if(booking!=null) {
				if(!BigInteger.ZERO.equals(orderId)) {
					BookingOrders bookingOrders=bookingOrderDAO.findById(orderId).orElse(null);
					if(bookingOrders!=null && booking.getBookingId().equals(bookingId)) {
						modelAndView.getModel().put("order",bookingOrders);
					}
				}
				modelAndView.getModel().put("booking",booking);
			}
			List<Employee> emps=employeeDAO.findAllByReception(null);
			List<OrderItem> orderItems = orderIteDAO.findAllByUser(user);
			modelAndView.getModel().put("emps",emps);
			modelAndView.getModel().put("items",orderItems);
		}
		return modelAndView;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> saveOrder(@RequestBody Orders orders){
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			if(user!=null) {
				OrderItem item=orderIteDAO.findById(orders.getItemId()).orElse(null);
				Booking booking=bookingDAO.findByIdAndUser(orders.getBookingId(), user).orElse(null);
				if(item!=null && booking!=null && user.getUserId().equals(item.getUser().getUserId())) {
					BookingOrders bookingOrders=orders.getOrderId()!=null && !BigInteger.ZERO.equals(orders.getOrderId())
													?bookingOrderDAO.findById(orders.getOrderId()).orElse(null):null;
					if(bookingOrders!=null && booking.getBookingId().equals(bookingOrders.getBooking().getBookingId())) {
						bookingOrders.setOrderItem(item);
						bookingOrders.setCount(orders.getCount());
						bookingOrders.setAmount(orders.getCount()*item.getPrice());
						bookingOrders=bookingOrderDAO.save(bookingOrders);
						if(orders.getEmpId()!=null && !BigInteger.ZERO.equals(orders.getEmpId())) {
							Employee emp=employeeDAO.findById(orders.getEmpId()).orElse(null);
							if(emp!=null) {
								bookingOrders.setEmployee(emp);
							}
						}
						result.put("order", bookingOrders);
						result.put(MessageConstant.success,"Order Updated Placed!");
						
					}else {
						Employee emp=null;
						if(orders.getEmpId()!=null && !BigInteger.ZERO.equals(orders.getEmpId())) {
							emp=employeeDAO.findById(orders.getEmpId()).orElse(null);
						}
						bookingOrders=new BookingOrders(null, booking, item, orders.getCount(), item.getPrice()*orders.getCount(), null, emp,new Date());
						bookingOrders=bookingOrderDAO.save(bookingOrders);
						result.put("order", bookingOrders);
						result.put(MessageConstant.success,"Order Placed!");
					}
				}else {
					result.put(MessageConstant.error,"Invalid Order data");
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}
	
	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> deleteOrder(@RequestBody Orders orders){
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			BookingOrders bookingOrders=bookingOrderDAO.findById(orders.getOrderId()).orElse(null);
			if(user!=null && bookingOrders!=null && user.getUserId().equals(bookingOrders.getBooking().getRoom().getUser().getUserId())) {
				bookingOrderDAO.delete(bookingOrders);
				result.put(MessageConstant.success, "Order Deleted Successfully");
			} else {
				result.put(MessageConstant.error, "Invalid input");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}
}
