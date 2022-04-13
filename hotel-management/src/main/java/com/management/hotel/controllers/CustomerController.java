package com.management.hotel.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
import com.management.hotel.controllers.entities.Customer;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.CustomerStatus;
import com.management.hotel.controllers.model.Gender;
import com.management.hotel.controllers.model.IdProofType;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.UserService;
import com.management.hotel.services.Validations;


@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private UserService userService;

	@GetMapping
	public ModelAndView getCustomer(@RequestParam("customerId") BigInteger customerId) {
		ModelAndView modelAndView = new ModelAndView("customer/add-customer");
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		Customer customer = (Customer) customerDAO.findByCustomerIdAndUser(customerId, user)
				.orElse(new Customer().withUser(user));
		modelAndView.getModel().put("genders", Arrays.asList(Gender.values()));
		modelAndView.getModel().put("countries", Validations.countries);
		modelAndView.getModel().put("idProofTypes", Arrays.asList(IdProofType.values()));
		modelAndView.getModel().put("cust", customer);
		return modelAndView;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Object> addCustomer(@RequestBody Customer customer) {
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			Validations.isValid("name", customer.getName(), result);
			Validations.isValid("phone", ""+customer.getPhone(), result);
			Validations.isValid("email", customer.getEmail(), result);
			Validations.isValid("country", customer.getCountry(), result);
			Validations.isValid("address", customer.getAddress(), result);
			Validations.isValid("idNumber", customer.getIdNumber(), result);
			Validations.isValid("postCode", ""+customer.getPostCode(), result);
			if(user!=null && !result.containsKey(MessageConstant.validation)) {
				Customer existCustomer=customer.getCustomerId()!=null?customerDAO.findById(customer.getCustomerId()).orElse(null):null;
				if(existCustomer!=null) {
					customer.setUser(user);
					customerDAO.save(customer);
					result.put("customer", customer);
					result.put(MessageConstant.success,"Customer update success!");
				}else {
					customer.setCustomerId(null);
					customer.setUser(user);
					customerDAO.save(customer);
					result.put("customer", customer);
					result.put(MessageConstant.success,"Customer added success");
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
	public Map<String,Object> dleteCustomer(@RequestBody Customer customer) {
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		try {
			customer=customerDAO.findById(customer.getCustomerId()).orElse(null);
			if(user!=null && customer.getCustomerId()!=null && user.getUserId().equals(customer.getUser().getUserId())) {
				customerDAO.delete(customer);
				result.put(MessageConstant.success, "Customer Deleted");
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

	@RequestMapping("/customerdashbord")
	public String customer() {
		return "customer/customerdashbord";
	}

	@GetMapping("/search")
	public ModelAndView searchRoom(@RequestParam("type") String type, @RequestParam("value") String value) {
		ModelAndView modelAndView = new ModelAndView("customer/search-customer");
		List<Customer> customers = new ArrayList<>();
		User user = userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		if (user != null) {
			if (value != null && !value.trim().isEmpty()) {
				switch (type) {
				case "name":
					customers = customerDAO.findAllByNameStartingWithOrderByCustomerId(value);
					break;
				case "email":
					customers = customerDAO.findAllByEmailStartingWithOrderByCustomerId(value);
					break;
				case "phone":
					customers = customerDAO.findAllByPhoneStartingWithOrderByCustomerId(value);
					break;
				case "idNumber":
					customers = customerDAO.findAllByIdNumberStartingWithOrderByCustomerId(value);
					break;
				case "country":
					customers = customerDAO.findAllByCountryStartingWithOrderByCustomerId(value);
					break;

				}
			} else {
				customers = customerDAO.findAll(Pageable.ofSize(100).getSort().by(Direction.DESC, "customerId"));
			}
		}
		modelAndView.getModel().put("customers",customers);
		modelAndView.getModel().put("bookedStatus", CustomerStatus.Booked);
		return modelAndView;
	}
}
