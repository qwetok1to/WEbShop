package com.example.shop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:shop-test",
		"spring.datasource.password=test-password",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"security.jwt.secret=test-signing-key-that-is-at-least-32-bytes-long"
})
class ShopApplicationTests {

	@Test
	void contextLoads() {
	}

}
