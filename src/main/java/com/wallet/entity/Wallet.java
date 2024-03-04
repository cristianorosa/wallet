package com.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.wallet.dto.WalletDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "WALLET")
public class Wallet implements Serializable {

	private static final long serialVersionUID = 1693850165739564098L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private BigDecimal value;

	public Wallet() {
	}

	public Wallet(@Valid WalletDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
		this.value = dto.getValue();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
