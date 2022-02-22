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
public class JWKSDocument {
  @JsonProperty("keys")
  public List<JWK> keys;
}
