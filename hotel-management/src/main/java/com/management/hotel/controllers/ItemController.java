package com.management.hotel.controllers;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.management.hotel.controllers.dao.OrderIteDAO;
import com.management.hotel.controllers.entities.OrderItem;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.MessageConstant;
import com.management.hotel.controllers.model.UserRoles;
import com.management.hotel.services.UserService;
import com.management.hotel.services.Validations;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private OrderIteDAO orderIteDAO;
	@Autowired
	private UserService userService;
	@GetMapping
	private ModelAndView itemDashboard() {
		ModelAndView modelAndView=new ModelAndView("item/itemdashbord");
		return modelAndView;
	}
	@GetMapping("/search")
	private ModelAndView searchItem(@RequestParam("type") String type,@RequestParam("value") String value) {
		ModelAndView modelAndView=new ModelAndView("item/search-item");
		List<OrderItem> orderItems=null;
		User user=userService.getLogedUser();
		user=UserRoles.ADMIN.equals(user.getRole())?user:user.getAdminUser();
		if(user!=null && null!=value && !value.trim().isEmpty() && "itemName".equals(type)) {
			orderItems=orderIteDAO.findAllByItemNameContainingAndUser(value,user);
		}else {
			orderItems=orderIteDAO.findAllByUser(user);
		}
		if(orderItems!=null && !orderItems.isEmpty()) {
			modelAndView.getModel().put("orderItems", orderItems);
		}
		return modelAndView;
	}
	@GetMapping("/{itemId}")
	private ModelAndView itemDashboard(@PathVariable("itemId") BigInteger itemId) {
		ModelAndView modelAndView=new ModelAndView("item/add-item");
		User user=userService.getLogedUser();
		OrderItem orderItem=orderIteDAO.findById(itemId).orElse(new OrderItem(null, null, 0,user));
		modelAndView.getModel().put("item", orderItem);
		return modelAndView;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> saveItem(@RequestBody OrderItem orderItem){
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		try {
			Validations.isValid("name", orderItem.getItemName(), result);
			Validations.isValid("double","price", orderItem.getPrice()+"", result);
			if(user!=null && !result.containsKey(MessageConstant.validation)) {
				OrderItem item=orderItem.getItemId()!=null?orderIteDAO.findById(orderItem.getItemId()).orElse(null):null;
				if(item!=null && user.getUserId().equals(item.getUser().getUserId())) {
					item.setItemName(orderItem.getItemName());
					item.setPrice(orderItem.getPrice());
					item=orderIteDAO.save(item);
					result.put("item", item);
					result.put(MessageConstant.success,"Item update success!");
				}else {
					orderItem.setItemId(null);
					orderItem.setUser(user);
					orderItem=orderIteDAO.save(orderItem);
					result.put("item", orderItem);
					result.put(MessageConstant.success,"Item added success");
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
	public Map<String, Object> deleteItem(@RequestBody OrderItem orderItem){
		Map<String, Object> result=new HashMap<>();
		User user=userService.getLogedUser();
		try {
			orderItem=orderItem.getItemId()!=null?orderIteDAO.findById(orderItem.getItemId()).orElse(null):null;
			if(user!=null && orderItem!=null && user.getUserId().equals(orderItem.getUser().getUserId())) {
				orderIteDAO.delete(orderItem);
				result.put(MessageConstant.success, "Item Deleted");
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
