package com.netflix.oidcserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
@Getter
@Setter
public class OpenIdDiscoveryDocument {
  @JsonProperty("issuer")
  public String issuer;

  @JsonProperty("authorization_endpoint")
  public String authorizationEndpoint;

  @JsonProperty("token_endpoint")
  public String tokenEndpoint;

  @JsonProperty("jwks_uri")
  public String jwksUri;

  @JsonProperty("scopes_supported")
  public List<String> scopesSupported;
}
