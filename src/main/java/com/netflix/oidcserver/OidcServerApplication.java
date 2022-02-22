package com.netflix.oidcserver;

import com.netflix.oidcserver.endpoints.AuthorizationEndpoint;
import com.netflix.oidcserver.endpoints.TokenEndpoint;
import com.netflix.oidcserver.models.ErrorResponse;
import com.netflix.oidcserver.models.JWKSDocument;
import com.netflix.oidcserver.models.JWTDocument;
import com.netflix.oidcserver.models.OpenIdDiscoveryDocument;
import com.netflix.oidcserver.utils.Constants;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin
public class OidcServerApplication {
  private final AuthorizationEndpoint authorizationEndpoint;
  private final TokenEndpoint tokenEndpoint;

  @Autowired
  public OidcServerApplication(AuthorizationEndpoint authorizationEndpoint, TokenEndpoint tokenEndpoint) {
    this.authorizationEndpoint = authorizationEndpoint;
    this.tokenEndpoint = tokenEndpoint;
  }

  public static void main(String[] args) {
    SpringApplication.run(OidcServerApplication.class, args);
  }

  @GetMapping("/")
  public String index() {
    return "Hello world!";
  }

  @GetMapping("/.well-known/openid-configuration")
  public ResponseEntity<OpenIdDiscoveryDocument> getOpenIdDiscoveryDocument() {
    return ResponseEntity.ok(Constants.DEFAULT_DISCOVERY_DOC);
  }

  @GetMapping("/jwks.json")
  public ResponseEntity<JWKSDocument> getJWKSDocument() {
    return ResponseEntity.ok(Constants.DEFAULT_JWKS_DOC);
  }

  @GetMapping("/authorize")
  public ResponseEntity<Void> authorize(
      @RequestParam(value = "redirect_uri") String uri,
      @RequestParam(value = "code_challenge") String codeChallenge) {
    String code = authorizationEndpoint.authorize(codeChallenge);
    String redirectUri = uri + "?code=" + code;
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(redirectUri))
        .build();
  }

  @PostMapping("/token")
  public ResponseEntity token(
      @RequestParam(value = "code") String code,
      @RequestParam(value = "code_verifier") String codeVerifier) {
    try {
      String jwt = tokenEndpoint.token(code, codeVerifier);
      return ResponseEntity.ok(JWTDocument.builder().idToken(jwt).build());
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ErrorResponse.builder()
              .error("invalid_request")
              .errorDescription(errorMessage)
              .build());
    }
  }
}
