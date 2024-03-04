package com.wallet.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wallet.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

	private Long id;

	@Length(min = 3, max = 50, message = "O nome deve conter entre 3 e 50 caracteres")
	private String username;
	
	@Length(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
	@NotNull
	private String password;
	
	@Email(message = "Email inválido")
	private String email;

	public UserDTO() {
	}

	public UserDTO(Long id, String username, String password, String email) {
		super();
		this.id = id;
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
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
