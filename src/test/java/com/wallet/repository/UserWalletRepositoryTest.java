package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.wallet.entity.User;
import com.wallet.entity.UserWallet;
import com.wallet.entity.Wallet;

@SpringBootTest
@ActiveProfiles("test")
public class UserWalletRepositoryTest {
	
	@Autowired
	UserWalletRepository repo;
	
	@Autowired
	WalletRepository repoWallet;
	
	@Autowired
	UserRepository repoUser;
	
	private Wallet wallet;
	private User user;
	
	@BeforeEach
	void init() {
		wallet = repoWallet.save(new Wallet(1l, "carteira test 1", new BigDecimal(2155.55)));
		assertNotNull(wallet);
		
		user = repoUser.save(new User(1l, "test@123", "user_test1","test@email.com"));
		assertNotNull(user);
	}
	
	@AfterEach
    void tearDown() {
		repo.deleteAll();
		repoUser.deleteAll();
		repoWallet.deleteAll();
    }
	
	@Test
	public void testSave() {		
		UserWallet userWallet = new UserWallet();
		userWallet.setUsers(user);
		userWallet.setWallet(wallet);
		
		UserWallet response = repo.save(userWallet);
		assertNotNull(response);
	}
	
}
