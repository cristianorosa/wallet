package com.wallet.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;

@Repository
public interface WalletItemRepository extends JpaRepository<WalletItem, Long> {

	Page<WalletItem> findAllByWalletIdAndDataGreaterThanEqualAndDataLessThanEqual(Long savedWalletId, Date date,
			Date currentDatePlusFiveDays, PageRequest pr);

	List<WalletItem> findByWalletIdAndType(Long savedWalletId, TypeEnum type);

	@Query(value = "select sum(wi.value) from WalletItem wi where wi.wallet.id = :wallet")
	BigDecimal sumByWalletId(@Param("wallet") Long savedWalletId);
}

