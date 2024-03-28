package com.wallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.repository.WalletItemRepository;
import com.wallet.util.enums.TypeEnum;

@SpringBootTest
@ActiveProfiles("test")
public class WalletItemServiceTest {
	
	@MockBean
	WalletItemRepository repo;
	
	@Autowired
	WalletItemService service;
	
	private static final Date DATE = new Date();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "Conta de Luz";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
	
	private Wallet wallet;
	
	@BeforeEach
	public void setUp() {
		wallet = new Wallet(1L, "Carteira test", BigDecimal.valueOf(256.3));
	}
	
	@Test
	public void save() {		
		BDDMockito.given(repo.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());
		
		WalletItem walletItem = service.save(new WalletItem());
		
		assertNotNull(walletItem);
		assertEquals(walletItem.getDescription(), DESCRIPTION);
		assertEquals(walletItem.getType(), TYPE);
		assertEquals(walletItem.getDate(), DATE);
		assertEquals(walletItem.getValue(), VALUE);
		
	}
	
	@Test
	public void findBetweenDates() {
		List<WalletItem> list = new ArrayList<WalletItem>();
		list.add(getMockWalletItem());
		Page<WalletItem> page = new PageImpl<WalletItem>(list);
		
		BDDMockito.given(repo.findAllByWalletIdAndDataGreaterThanEqualAndDataLessThanEqual(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class), Mockito.any(PageRequest.class)))
			.willReturn(page);
		
		Page<WalletItem> response = service.findBetweenDates(1L, new Date(), new Date(), 0); 
		
		assertNotNull(response);
		assertEquals(response.getContent().size(), 1);
		assertEquals(response.getContent().get(0).getDescription(), DESCRIPTION);
	}
	
	@Test
	public void findByType() {
		List<WalletItem> list = new ArrayList<WalletItem>();
		list.add(getMockWalletItem());
		
		BDDMockito.given(repo.findByWalletIdAndType(Mockito.anyLong(), Mockito.any(TypeEnum.class))).willReturn(list);
		
		List<WalletItem> response = service.findByWalletAndType(1L, TypeEnum.EN);
				
		assertNotNull(response);
		assertEquals(response.get(0).getType(), TYPE);
	}
	
	@Test
	public void sumByWallet() {
		BigDecimal value = BigDecimal.valueOf(45);
		BDDMockito.given(repo.sumByWalletId(Mockito.anyLong())).willReturn(value);
		
		BigDecimal response = service.sumByWalletID(1L);
		
		assertEquals(response.compareTo(value), 0);
		
	}
		
	private WalletItem getMockWalletItem() {
		return new WalletItem(wallet, DATE, TYPE, DESCRIPTION, VALUE);
	}	

}
