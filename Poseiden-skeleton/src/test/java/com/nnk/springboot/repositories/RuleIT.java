package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RuleIT {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	private RuleName ruleGiven;

	@BeforeAll
	void setUpAll() {
		ruleGiven = new RuleName("Rule Name", "Description", "Json",
				"Template", "SQL", "SQL Part");

	}

	@Order(1)
	@Test
	public void ruleSaveTest() {
		// Save
		ruleGiven = ruleNameRepository.save(ruleGiven);
		assertNotNull(ruleGiven.getId());
		assertEquals("Rule Name", ruleGiven.getName());
	}

	@Order(2)
	@Test
	public void ruleUpdateTest() {
		// Update
		ruleGiven.setName("Rule Name Update");
		ruleGiven = ruleNameRepository.save(ruleGiven);
		assertEquals("Rule Name Update", ruleGiven.getName());
	}

	@Order(3)
	@Test
	public void ruleFindTest() {
		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		assertTrue(listResult.size() > 0);
	}

	@Order(4)
	@Test
	public void ruleDeleteTest() {
		// Delete
		Integer id = ruleGiven.getId();
		ruleNameRepository.delete(ruleGiven);
		Optional<RuleName> ruleList = ruleNameRepository.findById(id);
		assertFalse(ruleList.isPresent());
	}

}
