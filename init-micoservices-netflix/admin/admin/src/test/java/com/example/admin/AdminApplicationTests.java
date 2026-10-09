package com.example.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:admin-test",
		"spring.datasource.password=test-password",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"security.jwt.secret=test-signing-key-that-is-at-least-32-bytes-long",
		"security.admin.username=test-admin",
		"security.admin.password=test-password"
})
class AdminApplicationTests {

	@Test
	void contextLoads() {
	}

}
