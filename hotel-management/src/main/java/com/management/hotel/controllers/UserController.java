package com.management.hotel.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.management.hotel.controllers.dao.UserDAO;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.UserService;
import com.management.hotel.services.Validations;

@Controller
@RequestMapping
public class UserController {
	@Autowired
	private UserDAO userDAO; 
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/")
	public ModelAndView loginPage() {
		return login();
	}
	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView=new ModelAndView();
		if(userService.getLogedUser()!=null) {
			return getDashBord(modelAndView);
		}
		modelAndView.getModel().put("login", "login");
		modelAndView.setViewName("login");
		return modelAndView;
	}
	@RequestMapping("/contact")
	public ModelAndView logcontactin() {
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("contact");
		return modelAndView;
	}
	@RequestMapping("/about")
	public ModelAndView about() {
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("about");
		return modelAndView;
	}
	@PostMapping(path="/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Object> signup(@RequestBody User user) {
		Map<String, Object> result=new HashMap<>();
		try {
			Validations.isValid("name", user.getName(),result);
			Validations.isValid("email", user.getEmail(),result);
			Validations.isValid("password", user.getPassword(),result);
			Validations.isValid("phone", user.getPhone()+"",result);
			if(!result.containsKey(MessageConstant.validation)) {
				if(user.getHotelName()==null || user.getHotelName().trim().isEmpty()) {
					user.setHotelName("My Hotel");
				}
				user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
				user.setUserId(null);
				user.setRole(UserRoles.ADMIN);
				user=userDAO.save(user);
				if(user!=null) {
					result.put(MessageConstant.success, "SignUp success, please login");
					result.put("user", user);
				}else {
					result.put(MessageConstant.error, "Somthing is wrong, Please retry later");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			result.put(MessageConstant.exception, e.getMessage());
			result.put(MessageConstant.error, "Sorry Server problem please contact developers");
		}
		return result;
	}
	
	@RequestMapping("/dashbord")
	public ModelAndView dashbord() {
		ModelAndView modelAndView=new ModelAndView();
		return getDashBord(modelAndView);
	}
	
	public ModelAndView getDashBord(ModelAndView modelAndView) {		
		modelAndView.setViewName("dashbord");
		User user=userService.getLogedUser();
		if(user!=null) {
			modelAndView.getModel().put("user", user);
		}else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}
}
