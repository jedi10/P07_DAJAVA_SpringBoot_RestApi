package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TradeIT {

	@Autowired
	private TradeRepository tradeRepository;

	private Trade tradeGiven;

	@BeforeAll
	void setUpAll() {
		tradeGiven = new Trade("Trade Account", "Type");
	}

	@Order(1)
	@Test
	public void tradeSaveTest() {
		// Save
		tradeGiven = tradeRepository.save(tradeGiven);
		assertNotNull(tradeGiven.getId());
		assertEquals("Trade Account", tradeGiven.getAccount());
	}

	@Order(2)
	@Test
	public void tradeUpdateTest() {
		// Update
		tradeGiven.setAccount("Trade Account Update");
		tradeGiven = tradeRepository.save(tradeGiven);
		assertEquals("Trade Account Update", tradeGiven.getAccount());
	}

	@Order(3)
	@Test
	public void tradeFindTest() {
		// Find
		List<Trade> listResult = tradeRepository.findAll();
		assertTrue(listResult.size() > 0);
	}

	@Order(4)
	@Test
	public void tradeDeleteTest() {
		// Delete
		Integer id = tradeGiven.getId();
		tradeRepository.delete(tradeGiven);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		assertFalse(tradeList.isPresent());
	}
}
