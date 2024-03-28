package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.wallet.entity.Wallet;
import com.wallet.repository.WalletRepository;

@SpringBootTest
@ActiveProfiles("test")
public class WalletServiceTest {

	@MockBean
	WalletRepository repo;
	
	@Autowired
	WalletService service;
		
	private static final String NAME = "Carteira Teste";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
	
	@Test
	public void save() {
		BDDMockito.given(repo.save(Mockito.any(Wallet.class))).willReturn(new Wallet(1L, NAME, VALUE));
		
		Wallet wallet = service.save(new Wallet());		
		
		assertNotNull(wallet);
		assertEquals(wallet.getName(), NAME);
		assertEquals(wallet.getValue(), VALUE);
	}
	
}
	