package com.prgrms.devcourse.user;

import com.prgrms.devcourse.configures.JwtConfigure;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestControllerTest {
    private final JwtConfigure jwtConfigure;
    private final TestRestTemplate testTemplate;

    UserRestControllerTest(JwtConfigure jwtConfigure, TestRestTemplate testTemplate) {
        this.jwtConfigure = jwtConfigure;
        this.testTemplate = testTemplate;
    }

    @Test
    public void JWT_토큰_테스트() {
        assertThat(tokenToName(getToken("user"))).isEqualTo("user");
        assertThat(tokenToName(getToken("admin"))).isEqualTo("admin");
    }

    private String getToken(String username) {
        return testTemplate.exchange(
                "/api/v1/user/" + username + "/token",
                HttpMethod.GET,
                null,
                String.class
        ).getBody();
    }

    private String tokenToName(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(jwtConfigure.getHeader(), token);
        return testTemplate.exchange(
                "/api/v1/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        ).getBody();
    }
}