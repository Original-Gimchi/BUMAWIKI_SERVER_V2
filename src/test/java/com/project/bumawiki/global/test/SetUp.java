package com.project.bumawiki.global.test;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SetUp {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SetUpTestData setUpTestData;

	@Transactional
	public void beforeEach() {
		final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
		truncateTables(jdbcTemplate, truncateQueries);
		setUpTestData.setUpTestData();
	}

	private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ' RESTART IDENTITY;') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
	}

	private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
		execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");
		truncateQueries.forEach(v -> execute(jdbcTemplate, v));
		execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
	}

	private void execute(final JdbcTemplate jdbcTemplate, final String query) {
		jdbcTemplate.execute(query);
	}
}
