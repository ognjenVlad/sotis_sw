package com.ftn.sotis.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sotis.DTOs.LoginDTO;
import com.ftn.sotis.entities.User;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.security.TokenUtils;
import com.ftn.sotis.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
		
	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
        	// Perform the authentication
        	UsernamePasswordAuthenticationToken token = 
        			new UsernamePasswordAuthenticationToken(
					loginDTO.getUsername(), loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload user details so we can generate token
            UserDetails details = userDetailsService.
            		loadUserByUsername(loginDTO.getUsername());
            return new ResponseEntity<String>(
            		tokenUtils.generateToken(details), HttpStatus.OK);
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
            return new ResponseEntity<String>("Invalid login", HttpStatus.BAD_REQUEST);
        }
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ADMIN_ROLE')")
	public ResponseEntity<User> register(@RequestBody User user) {
		User retVal = null;
		try {
			retVal = userService.registerUser(user);
		} catch (InvalidDataException | EntityAlreadyExistsException e) {
			return new ResponseEntity<User>(new User(),HttpStatus.BAD_REQUEST);
		}
		
        return new ResponseEntity<User>(retVal,HttpStatus.OK);
	}
	
	@RequestMapping( value = "/test", method = RequestMethod.GET)
	public ResponseEntity<String> register(){
		String s = null;
		ProcessBuilder builder = new ProcessBuilder("python","kst/sotishelper.py");
		
		try {
			Process p = builder.start();
			p.waitFor();
			
			 BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
	        
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return new ResponseEntity<String>("Successful register",HttpStatus.OK);
	}
}
