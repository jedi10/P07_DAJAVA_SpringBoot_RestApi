package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CurvePointIT {

	@Autowired
	private CurvePointRepository curvePointRepository;

	private CurvePoint curvePointGiven;


	@BeforeAll
	void setUpAll() {
		curvePointGiven = new CurvePoint(10, 10d, 30d);
	}

	@Order(1)
	@Test
	public void curvePointSaveTest() {
		// Save
		curvePointGiven = curvePointRepository.save(curvePointGiven);
		assertNotNull(curvePointGiven.getId());
		assertEquals(10, (int) curvePointGiven.getCurveId());
	}

	@Order(2)
	@Test
	public void curvePointUpdateTest() {
		// Update
		curvePointGiven.setCurveId(20);
		curvePointGiven = curvePointRepository.save(curvePointGiven);
		assertEquals(20, (int) curvePointGiven.getCurveId());
	}

	@Order(3)
	@Test
	public void curvePointFindTest() {
		// Find
		List<CurvePoint> listResult = curvePointRepository.findAll();
		assertTrue(listResult.size() > 0);
	}

	@Order(4)
	@Test
	public void curvePointTest() {
		// Delete
		Integer id = curvePointGiven.getId();
		curvePointRepository.delete(curvePointGiven);
		Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
		assertFalse(curvePointList.isPresent());
	}

}
