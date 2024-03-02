package com.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.response.Response;
import com.wallet.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@PostMapping
	public ResponseEntity<Response<UserDTO>> create(@Valid @RequestBody UserDTO userDto, BindingResult result) {
		Response<UserDTO> response = new Response<UserDTO>();
		User user = service.save(new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail()));
		
		response.setData(new UserDTO(user.getId(), user.getUsername(), userDto.getPassword(), userDto.getEmail()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
