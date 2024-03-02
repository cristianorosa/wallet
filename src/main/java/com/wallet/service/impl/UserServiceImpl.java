package com.wallet.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.entity.User;
import com.wallet.repository.UserRepository;
import com.wallet.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository repo;

	@Override
	public User save(User u) {		
		return repo.save(u);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> user = repo.findByEmail(email);
		return user;
	}

}
