package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.service.UserService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

	private static final Long ID = 1L;
	private static final String USERNAME = "test1";
	private static final String PASSWORD = "test123";
	private static final String EMAIL = "test@test.com";
	private static final String URL = "/user";

	@MockBean
	UserService service;

	@Autowired
	MockMvc mvc;
	
	@Test
	public void testSave() throws JsonProcessingException, Exception {
		
		BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());
		
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayLoad(ID, USERNAME, PASSWORD, EMAIL))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.id").value(ID))
			.andExpect(jsonPath("$.data.username").value(USERNAME))
			.andExpect(jsonPath("$.data.password").doesNotExist())
			.andExpect(jsonPath("$.data.email").value(EMAIL));
	}
	
	@Test
	public void testSaveInvalidUser() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayLoad(ID, USERNAME, PASSWORD,  "email"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors[0]").value("Email inválido"));
	}

	public User getMockUser() {		
		return new User(ID, USERNAME, PASSWORD, EMAIL);
	}

	public String getJsonPayLoad(Long id, String username, String password, String email) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(new UserDTO(id, username, password, email));
	}
}
