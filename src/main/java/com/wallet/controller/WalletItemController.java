package com.wallet.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.WalletItemDTO;
import com.wallet.entity.WalletItem;
import com.wallet.response.Response;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.TypeEnum;

import jakarta.validation.Valid;

@RestController
@RequestMapping("wallet-item")
public class WalletItemController {

	@Autowired
	WalletItemService service;

	@PostMapping
	public ResponseEntity<Response<WalletItemDTO>> create(@Valid @RequestBody WalletItemDTO dto, BindingResult result) {

		Response<WalletItemDTO> response = new Response<WalletItemDTO>();

		if (result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
		}

		WalletItem walletItem = service.save(new WalletItem(dto));
		response.setData(new WalletItemDTO(walletItem));

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping(value = "/{wallet}")
	public ResponseEntity<Response<Page<WalletItemDTO>>> findBetweenDates(@PathVariable("wallet") Long wallet,
			@RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		
		Response<Page<WalletItemDTO>> response = new Response<Page<WalletItemDTO>>();
		Page<WalletItem> items = service.findBetweenDates(wallet, startDate, endDate, page);
		
		Page<WalletItemDTO> dto = items.map(e -> new WalletItemDTO(e));
		response.setData(dto);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/type/{wallet}")
	public ResponseEntity<Response<List<WalletItemDTO>>> findByWalletIdAndType(@PathVariable("wallet") Long wallet,
			@RequestParam("type") String type) {
		
		Response<List<WalletItemDTO>> response = new Response<List<WalletItemDTO>>();
		List<WalletItem> items = service.findByWalletAndType(wallet, TypeEnum.getEnum(type));
		
		List<WalletItemDTO> dto = new ArrayList<WalletItemDTO>();
		items.forEach(e -> dto.add(new WalletItemDTO(e)));
		response.setData(dto);
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/total/{wallet}")
	public ResponseEntity<Response<BigDecimal>> sumByWalletId(@PathVariable("wallet") Long wallet) {
		
		Response<BigDecimal> response = new Response<BigDecimal>();
		BigDecimal total = service.sumByWalletID(wallet);
		 
		response.setData(total == null ? BigDecimal.ZERO : total);
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<Response<WalletItemDTO>> update(@Valid @RequestBody WalletItemDTO dto, BindingResult result) {
		
		Response<WalletItemDTO> response = new Response<WalletItemDTO>();
		Optional<WalletItem> walletItem = service.findByID(dto.getId());
		
		if (!walletItem.isPresent()) {
			result.addError(new ObjectError("WalletItem", "Wallet Item não encontrado"));
		} else if (walletItem.get().getWallet().getId().compareTo(dto.getWallet()) != 0) {
			result.addError(new ObjectError("WalletItemChanged", "Você não pode alterar a carteira"));
		}
		
		if (result.hasErrors()) {			
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		WalletItem saved = service.save(new WalletItem(dto));
		response.setData(new WalletItemDTO(saved));
		
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping(value = "/{wallet}")
	public ResponseEntity<Response<String>> delete(@PathVariable("wallet") Long wallet) {
		Response<String> response = new Response<String>();
		
		Optional<WalletItem> walletItem = service.findByID(wallet);
		
		if (!walletItem.isPresent()) {
			response.getErrors().add("A carteira de ID " + wallet + " não foi encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		service.deleteById(wallet);
		response.setData("A carteira de ID " + wallet + " foi excluída com sucesso");
		return ResponseEntity.ok().body(response);
	}
}
