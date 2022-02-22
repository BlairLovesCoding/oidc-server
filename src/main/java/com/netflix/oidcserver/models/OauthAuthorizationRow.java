package com.netflix.oidcserver.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
@Getter
@Setter
public class OauthAuthorizationRow {
  public String code;
  public String codeChallenge;
  public String idToken;
}
