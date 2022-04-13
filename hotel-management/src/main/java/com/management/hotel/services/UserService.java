package com.management.hotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.management.hotel.controllers.dao.UserDAO;
import com.management.hotel.controllers.entities.User;
import com.management.hotel.controllers.model.SecureUser;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userDAO.findByEmail(username); 
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
		return new SecureUser(user);
	}
	
	public User getLogedUser() {
		User user=null;
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			if(auth.getPrincipal() instanceof SecureUser) {
				SecureUser secureUser=(SecureUser)auth.getPrincipal();
				user=secureUser.getUser();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
