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
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.management.hotel.controllers.dao.EmployeeDAO;
import com.management.hotel.controllers.dao.UserDAO;
import com.management.hotel.controllers.entities.Customer;
import com.management.hotel.controllers.entities.Employee;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.Gender;
import com.management.hotel.controllers.model.IdProofType;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.UserService;
import com.management.hotel.services.Validations;

@Controller
@RequestMapping("/emp")
public class EmployeeController {
	@Autowired
	private EmployeeDAO employeeDAO;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping("/empdashbord")
	public String empdashboard() {
		return "emp/empdashbord";
	}
	@GetMapping
	public ModelAndView getEmp(@RequestParam("empId") BigInteger empId) {
		ModelAndView modelAndView = new ModelAndView("emp/add-emp");
		User user = userService.getLogedUser();
		Employee emp=employeeDAO.findById(empId).orElse(new Employee());
		modelAndView.getModel().put("genders", Arrays.asList(Gender.values()));
		modelAndView.getModel().put("countries", Validations.countries);
		modelAndView.getModel().put("idProofTypes", Arrays.asList(IdProofType.values()));
		modelAndView.getModel().put("emp", emp);
		return modelAndView;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Object> addCustomer(@RequestBody Employee emp) {
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		try {
			Validations.isValid("name", emp.getName(), result);
			Validations.isValid("phone", ""+emp.getPhone(), result);
			Validations.isValid("email", emp.getEmail(), result);
			Validations.isValid("country", emp.getCountry(), result);
			Validations.isValid("address", emp.getAddress(), result);
			Validations.isValid("idNumber", emp.getIdNumber(), result);
			Validations.isValid("postCode", ""+emp.getPostCode(), result);
			String reception="";
			if(user!=null && !result.containsKey(MessageConstant.validation)) {
				Employee existEmp=emp.getEmpId()!=null?employeeDAO.findById(emp.getEmpId()).orElse(null):null;
				if(existEmp!=null && existEmp.getAdmin().getUserId().equals(user.getUserId())) {
					emp.setAdmin(user);
					if(emp.getReception()!=null && emp.getReception().getPassword()!=null) {
						User receptionUser=emp.toReception();
						reception=" As a Reception";
						if(existEmp.getReception()!=null) {
							receptionUser.setUserId(existEmp.getReception().getUserId());
							reception=" with reception Update";
						}
						receptionUser.setPassword(passwordEncoder.encode(receptionUser.getPassword()));
						receptionUser.setHotelName(user.getHotelName());
						receptionUser=userDAO.save(receptionUser);
						emp.setReception(receptionUser);
					}else {
						emp.setReception(existEmp.getReception());
					}
					employeeDAO.save(emp);
					result.put("emp", emp);
					result.put(MessageConstant.success,"Employee update success");
				}else {
					emp.setEmpId(null);
					emp.setAdmin(user);
					if(emp.getReception()!=null && emp.getReception().getPassword()!=null) {
						User receptionUser=emp.toReception();
						receptionUser.setHotelName(user.getHotelName());
						receptionUser.setPassword(passwordEncoder.encode(receptionUser.getPassword()));
						receptionUser=userDAO.save(receptionUser);
						emp.setReception(receptionUser);
						reception=" As a Reception";
					}
					employeeDAO.save(emp);
					result.put("emp", emp);
					result.put(MessageConstant.success,"Employye added success"+reception);
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
	public Map<String,Object> dleteEmployee(@RequestBody Employee emp) {
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		try {
			String reception="";
			emp=employeeDAO.findById(emp.getEmpId()).orElse(null);
			if(user!=null && user.getUserId().equals(emp.getAdmin().getUserId())) {
				employeeDAO.delete(emp);
				if(emp.getReception()!=null) {
					userDAO.delete(emp.getReception());
					reception="With Reception";
				}
				result.put(MessageConstant.success, "Emplyee Deleted "+reception);
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
	@GetMapping("/search")
	public ModelAndView searchRoom(@RequestParam("type") String type, @RequestParam("value") String value) {
		ModelAndView modelAndView = new ModelAndView("emp/search-emp");
		List<Employee> emps = new ArrayList<>();
		User user = userService.getLogedUser();
		if (user != null) {
			if (value != null && !value.trim().isEmpty()) {
				switch (type) {
				case "name":
					emps = employeeDAO.findAllByAdminAndNameContainsOrderByEmpIdDesc(user, value);
					break;

				default:
					emps = employeeDAO.findAllByAdminOrderByEmpIdDesc(user);
					break;

				}
			} else {
				emps = employeeDAO.findAllByAdminOrderByEmpIdDesc(user);
			}
		}
		modelAndView.getModel().put("emps", emps);
		return modelAndView;
	}
}
