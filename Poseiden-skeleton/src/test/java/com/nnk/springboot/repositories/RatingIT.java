package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingIT {

	@Autowired
	private RatingRepository ratingRepository;

	private Rating ratingGiven;

	@BeforeAll
	void setUpAll() {
		ratingGiven = new Rating("Moodys Rating", "Sand PRating",
				"Fitch Rating", 10);
	}

	@Order(1)
	@Test
	public void ratingSaveTest() {
		// Save
		ratingGiven = ratingRepository.save(ratingGiven);
		assertNotNull(ratingGiven.getId());
		assertEquals(10, (int) ratingGiven.getOrderNumber());
	}

	@Order(2)
	@Test
	public void ratingUpdateTest() {
		// Update
		ratingGiven.setOrderNumber(20);
		ratingGiven = ratingRepository.save(ratingGiven);
		assertEquals(20, (int) ratingGiven.getOrderNumber());
	}

	@Order(3)
	@Test
	public void ratingFindTest() throws InterruptedException {
		// Find
		//Thread.sleep(1000);
		List<Rating> listResult = ratingRepository.findAll();
		assertTrue(listResult.size() > 0);
	}

	@Order(4)
	@Test
	public void ratingDeleteTest() {
		// Delete
		Integer id = ratingGiven.getId();
		ratingRepository.delete(ratingGiven);
		Optional<Rating> ratingList = ratingRepository.findById(id);
		assertFalse(ratingList.isPresent());
	}
}
