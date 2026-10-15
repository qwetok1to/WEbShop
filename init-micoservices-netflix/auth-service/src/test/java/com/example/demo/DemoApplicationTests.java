package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.DTO.AuthDTOS;
import com.example.demo.Servise.AuthTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:auth-test",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"security.jwt.secret=test-signing-key-that-is-at-least-32-bytes-long"
})
class DemoApplicationTests {

	@Autowired
	private AuthTokenService authTokenService;

	@Test
	void contextLoads() {
	}

	@Test
	void createsTokenAcceptedByShopJwtParser() {
		String secret = "test-signing-key-that-is-at-least-32-bytes-long";
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		AuthDTOS.UserResponse user = new AuthDTOS.UserResponse("user-123", "user@example.com", "User");

		String token = authTokenService.createToken(user);
		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

		assertEquals(user.id(), claims.getSubject());
		assertEquals(3600, claims.getExpiration().toInstant().getEpochSecond()
				- claims.getIssuedAt().toInstant().getEpochSecond());
	}
}
