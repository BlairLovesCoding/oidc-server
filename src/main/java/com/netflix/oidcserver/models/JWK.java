package com.netflix.oidcserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWK {
  @JsonProperty("crv")
  public String crv;

  @JsonProperty("ext")
  public boolean ext;

  @JsonProperty("key_ops")
  public List<String> keyOps;

  @JsonProperty("kid")
  public String kid;

  @JsonProperty("kty")
  public String kty;

  @JsonProperty("x")
  public String x;

  @JsonProperty("y")
  public String y;
}
