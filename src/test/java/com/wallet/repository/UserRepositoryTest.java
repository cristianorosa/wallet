package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.wallet.entity.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
	
	private static final String EMAIL = "test@test.com";
	
	@Autowired
	UserRepository repo;
	
	@BeforeEach
	void init() {
		User u = new User();
		u.setUsername("teste123");
		u.setPassword("teste123");
		u.setEmail(EMAIL);
		
		User response = repo.save(u);
		assertNotNull(response);
	}
	
	@AfterEach
    void tearDown() {
		repo.deleteAll();
    }
	
	@Test
	public void testSave() {
		User u = new User();
		u.setUsername("teste124");
		u.setPassword("teste124");
		u.setEmail("teste124@test.com");
		
		User response = repo.save(u);
		assertNotNull(response);
	}
	
	@Test
	public void testFindByEmail() {
		Optional<User> response = repo.findByEmail(EMAIL);
		
		assertTrue(response.isPresent());
		assertEquals(response.get().getEmail(), EMAIL);
	}
	
}
