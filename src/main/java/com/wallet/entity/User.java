package com.wallet.entity;

import java.io.Serializable;

import com.wallet.dto.UserDTO;
import com.wallet.util.Bcrypt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = 1693850165739564098L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String password;
	@Column(name="name", nullable = false)
	private String username;

	private String email;

	public User() {
	}
	
	public User(String username, String password, String email) {
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public User(Long id, String username, String password, String email) {
		this.id = id;
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public User(@Valid UserDTO userDto) {
		this.id = userDto.getId();
		this.password = Bcrypt.getHash(userDto.getPassword());
		this.username = userDto.getUsername();
		this.email = userDto.getEmail();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
