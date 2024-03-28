package com.wallet.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.wallet.dto.WalletItemDTO;
import com.wallet.util.enums.TypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "WALLET_ITENS")
public class WalletItem implements Serializable {

	private static final long serialVersionUID = -5920493416299280592L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "wallet", referencedColumnName = "id")
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Wallet wallet = new Wallet();

	@NotNull
	private Date data;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TypeEnum type;

	@NotNull
	private String description;

	@Column(name = "iten_value", nullable = false)
	private BigDecimal value;

	public WalletItem() {}

	public WalletItem(Wallet wallet, @Valid Date date, @Valid TypeEnum type, @Valid String description,
			@Valid BigDecimal value, @Valid Long id) {
		super();
		this.id = id;
		this.data = date;
		this.type = type;
		this.description = description;
		this.value = value;
		this.wallet = wallet;
	}

	public WalletItem(@Valid Wallet wallet, Date date, TypeEnum type, String description, BigDecimal value) {
		super();
		this.data = date;
		this.type = type;
		this.description = description;
		this.value = value;
		this.wallet = wallet;
	}

	public WalletItem(@Valid WalletItemDTO dto) {
		Wallet wallet = new Wallet();
		wallet.setId(dto.getWallet());
		
		this.id = dto.getId();
		this.data = dto.getDate();
		this.type = TypeEnum.getEnum(dto.getType());
		this.description = dto.getDescription();
		this.value = dto.getValue();
		this.wallet = wallet;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Date getDate() {
		return data;
	}

	public void setDate(Date date) {
		this.data = date;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
