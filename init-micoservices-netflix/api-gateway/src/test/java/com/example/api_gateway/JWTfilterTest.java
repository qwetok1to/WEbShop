package com.example.api_gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JWTfilterTest {

    private static final String SECRET = "change-me-to-a-32-byte-development-secret";
    private final JWTfilter filter = new JWTfilter(new JWT_UTILS(SECRET));

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesRequestWithValidToken() throws Exception {
        MockHttpServletRequest request = securedRequest();
        request.addHeader("Authorization", "Bearer " + createToken(SECRET));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertNotNull(chain.getRequest());
        assertEquals("test-user", request.getAttribute("username"));
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        assertEquals("test-user", SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void rejectsTokenSignedWithDifferentSecret() throws Exception {
        MockHttpServletRequest request = securedRequest();
        request.addHeader("Authorization", "Bearer " + createToken("another-32-byte-development-secret"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNull(chain.getRequest());
    }

    @Test
    void rejectsProtectedRequestWithoutToken() throws Exception {
        MockHttpServletRequest request = securedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertNull(chain.getRequest());
    }

    @Test
    void allowsPublicLoginWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth-public/login");
        request.setServletPath("/auth-public/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertNotNull(chain.getRequest());
    }

    private MockHttpServletRequest securedRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/shop/products");
        request.setServletPath("/shop/products");
        return request;
    }

    private String createToken(String secret) {
        return Jwts.builder()
                .subject("test-user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .claim("roles", List.of("ROLE_USER"))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
