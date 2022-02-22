package com.netflix.oidcserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
@Getter
@Setter
public class JWTDocument {
  @JsonProperty("id_token")
  public String idToken;
}
