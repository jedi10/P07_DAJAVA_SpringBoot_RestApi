package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BidIT {

	@Autowired
	private BidListRepository bidListRepository;

	private BidList bidListGiven;
	@BeforeAll
	void setUpAll() {
		bidListGiven = new BidList("Account Test", "Type Test", 10d);
	}

	@Order(1)
	@Test
	public void bidListSaveTest() {
		// Save
		bidListGiven = bidListRepository.save(bidListGiven);
		assertNotNull(bidListGiven.getId());
		assertEquals(bidListGiven.getBidQuantity(), 10d, 10d);
	}

	@Order(2)
	@Test
	public void bidListUpdateTest() {
		// Update
		bidListGiven.setBidQuantity(20d);
		bidListGiven = bidListRepository.save(bidListGiven);
		assertEquals(bidListGiven.getBidQuantity(), 20d, 20d);
	}

	@Order(3)
	@Test
	public void bidListFindAllTest() {
		// Find
		List<BidList> listResult = bidListRepository.findAll();
		assertTrue(listResult.size() > 0);
	}

	@Order(4)
	@Test
	public void bidListDeleteTest() {
		// Delete
		Integer id = bidListGiven.getId();
		bidListRepository.delete(bidListGiven);
		Optional<BidList> bidList = bidListRepository.findById(id);
		assertFalse(bidList.isPresent());
	}
}
