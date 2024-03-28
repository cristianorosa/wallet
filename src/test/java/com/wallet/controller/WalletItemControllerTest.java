package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.TypeEnum;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WalletItemControllerTest {

	@MockBean
	WalletItemService service;

	@Autowired
	MockMvc mvc;

	private static final Long ID = 1l;
	private static final Date DATE = new Date();
	private static final LocalDate TODAY = LocalDate.now();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "Entrada de Saldo";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
	private static final String URL = "/wallet-item";

	@Test
	public void save() throws JsonProcessingException, Exception {

		BDDMockito.given(service.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());

		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayLoad(ID, DATE, TYPE, DESCRIPTION, VALUE))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.date").value(TODAY.format(dateFormatter())))
				.andExpect(jsonPath("$.data.type").value(TYPE.getValue()))
				.andExpect(jsonPath("$.data.description").value(DESCRIPTION))
				.andExpect(jsonPath("$.data.value").value(VALUE));
	}

	@Test
	private void findBetweenDates() throws Exception {
		List<WalletItem> walletItems = new ArrayList<WalletItem>();
		walletItems.add(getMockWalletItem());
		Page<WalletItem> page = new PageImpl<WalletItem>(walletItems);

		String startDate = TODAY.format(dateFormatter());
		String endtDate = TODAY.plusDays(5).format(dateFormatter());

		BDDMockito.given(service.findBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class),
				Mockito.anyInt())).willReturn(page);

		mvc.perform(MockMvcRequestBuilders.get(URL + "/" + ID + "1?startDate=" + startDate + "?endDate" + endtDate)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].id").value(ID))
				.andExpect(jsonPath("$.data.[0].date").value(TODAY.format(dateFormatter())))
				.andExpect(jsonPath("$.data.[0].description").value(DESCRIPTION))
				.andExpect(jsonPath("$.data.[0].type").value(TYPE.getValue()))
				.andExpect(jsonPath("$.data.[0].value").value(VALUE))
				.andExpect(jsonPath("$.data.[0].wallet").value(ID));

	}

	@Test
	public void findWalletByType() throws Exception {
		List<WalletItem> walletItems = new ArrayList<WalletItem>();
		walletItems.add(getMockWalletItem());

		BDDMockito.given(service.findByWalletAndType(Mockito.anyLong(), Mockito.any(TypeEnum.class)))
				.willReturn(walletItems);

		mvc.perform(MockMvcRequestBuilders.get(URL + "/type/1?type=ENTRADA")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.[0].id").value(ID))
		.andExpect(jsonPath("$.data.[0].date").value(TODAY.format(dateFormatter())))
		.andExpect(jsonPath("$.data.[0].description").value(DESCRIPTION))
		.andExpect(jsonPath("$.data.[0].type").value(TYPE.getValue()))
		.andExpect(jsonPath("$.data.[0].value").value(VALUE))
		.andExpect(jsonPath("$.data.[0].wallet").value(ID));
	}

	@Test
	public void sumByWallet() throws Exception {
		BigDecimal value = BigDecimal.valueOf(536.90);

		BDDMockito.given(service.sumByWalletID(Mockito.anyLong())).willReturn(value);

		mvc.perform(MockMvcRequestBuilders.get(URL + "/total/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value(536.9));
	}

	@Test
	public void update() throws Exception {
		String description = "Nova carteira";
		Wallet wallet = new Wallet();
		wallet.setId(ID);

		BDDMockito.given(service.findByID(Mockito.anyLong())).willReturn(Optional.of(getMockWalletItem()));
		BDDMockito.given(service.save(Mockito.any(WalletItem.class)))
				.willReturn(new WalletItem(wallet, DATE, TypeEnum.SD, description, VALUE, ID));

		mvc.perform(MockMvcRequestBuilders.put(URL)
				.content(getJsonPayLoad(ID, DATE, TYPE, DESCRIPTION, VALUE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.date").value(TODAY.format(dateFormatter())))
		.andExpect(jsonPath("$.data.description").value(description))
		.andExpect(jsonPath("$.data.type").value(TypeEnum.SD.getValue()))
		.andExpect(jsonPath("$.data.value").value(VALUE))
		.andExpect(jsonPath("$.data.wallet").value(ID));
	}

	@Test
	public void updateWalletChange() throws JsonProcessingException, Exception {
		Wallet wallet = new Wallet();
		wallet.setId(99L);

		WalletItem walletItem = new WalletItem(wallet, DATE, TypeEnum.SD, DESCRIPTION, VALUE, ID);
		BDDMockito.given(service.findByID(Mockito.anyLong())).willReturn(Optional.of(walletItem));

		mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayLoad(ID, DATE, TYPE, DESCRIPTION, VALUE))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.errors[0]").value("Você não pode alterar a carteira"));
	}

	@Test
	public void updateInvalid() throws JsonProcessingException, Exception {

		BDDMockito.given(service.findByID(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.put(URL).content(getJsonPayLoad(ID, DATE, TYPE, DESCRIPTION, VALUE))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.data").doesNotExist())
		.andExpect(jsonPath("$.errors[0]").value("Wallet Item não encontrado"));
	}

	@Test
	public void delete() throws Exception {
		BDDMockito.given(service.findByID(Mockito.anyLong())).willReturn(Optional.of(new WalletItem()));

		mvc.perform(MockMvcRequestBuilders.delete(URL + "/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value("A carteira de ID " + ID + " foi excluída com sucesso"));
	}

	@Test
	public void deleteInvalid() throws Exception {
		BDDMockito.given(service.findByID(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.delete(URL + "/99").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.errors[0]").value("A carteira de ID 99 não foi encontrada"));
	}

	private DateTimeFormatter dateFormatter() {
		return DateTimeFormatter.ofPattern("dd-MM-yyyy");
	}

	private String getJsonPayLoad(Long id, Date date, TypeEnum type, String description, BigDecimal value)
			throws JsonProcessingException {
		Wallet wallet = new Wallet();
		wallet.setId(ID);

		ObjectMapper om = new ObjectMapper();
		return om.writeValueAsString(new WalletItemDTO(id, wallet, date, type.getValue(), description, value));
	}

	private WalletItem getMockWalletItem() {
		Wallet wallet = new Wallet();
		wallet.setId(ID);
		return new WalletItem(wallet, DATE, TYPE, DESCRIPTION, VALUE, ID);
	}

}
