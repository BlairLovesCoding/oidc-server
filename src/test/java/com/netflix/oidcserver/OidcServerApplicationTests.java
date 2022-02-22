package com.netflix.oidcserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.oidcserver.models.JWK;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OidcServerApplicationTests {
	static ObjectMapper objectMapper = new ObjectMapper();
	@Test
	public void test() {
	}

}
