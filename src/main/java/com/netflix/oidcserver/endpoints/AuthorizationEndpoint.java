package com.netflix.oidcserver.endpoints;

import com.netflix.oidcserver.database.OauthAuthorizationRowRepository;
import com.netflix.oidcserver.models.OauthAuthorizationRow;
import com.netflix.oidcserver.utils.CredentialUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationEndpoint {
  private final OauthAuthorizationRowRepository oauthAuthorizationRowRepository;

  public AuthorizationEndpoint(OauthAuthorizationRowRepository oauthAuthorizationRowRepository) {
    this.oauthAuthorizationRowRepository = oauthAuthorizationRowRepository;
  }

  public String authorize(String codeChallenge) {
    String code = CredentialUtils.generateAccessCode(oauthAuthorizationRowRepository);
    oauthAuthorizationRowRepository.saveCode(
        OauthAuthorizationRow.builder()
            .code(code)
            .codeChallenge(codeChallenge)
            .build()
    );
    return code;
  }
}
