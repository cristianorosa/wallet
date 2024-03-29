package com.wallet.entity;

import java.io.Serializable;

import com.wallet.dto.UserWalletDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "USERS_WALLET")
public class UserWallet implements Serializable {

	private static final long serialVersionUID = 1693850165739564098L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name = "users", referencedColumnName = "id")
	@ManyToOne(fetch=FetchType.LAZY)
	private User users = new User();
	
	@JoinColumn(name = "wallet", referencedColumnName = "id")
	@ManyToOne(fetch=FetchType.LAZY)
	private Wallet wallet = new Wallet();

	public UserWallet() {}
	
	public UserWallet(@Valid UserWalletDTO dto) {
		this.id = dto.getId();
		this.users = new User();
		this.users.setId(dto.getUsers());
		this.wallet = new Wallet();
		this.wallet.setId(dto.getWallet());
	}

	public UserWallet(@Valid Long id, Long user, Long wallet) {
		super();
		this.id = id;
		this.users = new User();
		this.users.setId(user);
		this.wallet = new Wallet();
		this.wallet.setId(wallet);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
}
