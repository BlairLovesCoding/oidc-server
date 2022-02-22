package com.netflix.oidcserver.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.netflix.oidcserver.models.JWK;
import com.netflix.oidcserver.models.JWKSDocument;
import com.netflix.oidcserver.models.OpenIdDiscoveryDocument;
import java.io.IOException;
import java.util.Map;
import org.springframework.util.ResourceUtils;

public class Constants {
  public static String ISSUER = "http://localhost:8080";
  public static String SUB = "Blair Wu";
  private static final ObjectMapper objectMapper = new ObjectMapper();
  public static OpenIdDiscoveryDocument DEFAULT_DISCOVERY_DOC = OpenIdDiscoveryDocument.builder()
      .issuer(ISSUER)
      .authorizationEndpoint(ISSUER + "/authorize")
      .tokenEndpoint(ISSUER + "/token")
      .jwksUri(ISSUER + "/jwks.json")
      .scopesSupported(ImmutableList.of("openid"))
      .build();

  public static JWK PUBLIC_KEY;

  static {
    try {
      PUBLIC_KEY = objectMapper.readValue(ResourceUtils.getFile("classpath:public-key.json"), JWK.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static JWKSDocument DEFAULT_JWKS_DOC = JWKSDocument.builder().keys(ImmutableList.of(PUBLIC_KEY)).build();

  public static Map PRIVATE_KEY;

  static {
    try {
      PRIVATE_KEY = objectMapper.readValue(ResourceUtils.getFile("classpath:private-key.json"), Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
