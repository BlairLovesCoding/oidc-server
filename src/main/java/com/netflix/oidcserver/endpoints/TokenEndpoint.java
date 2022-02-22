package com.netflix.oidcserver.endpoints;

import com.netflix.oidcserver.database.OauthAuthorizationRowRepository;
import com.netflix.oidcserver.models.OauthAuthorizationRow;
import com.netflix.oidcserver.utils.CredentialUtils;
import java.security.InvalidParameterException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenEndpoint {
  private final OauthAuthorizationRowRepository oauthAuthorizationRowRepository;

  public TokenEndpoint(OauthAuthorizationRowRepository oauthAuthorizationRowRepository) {
    this.oauthAuthorizationRowRepository = oauthAuthorizationRowRepository;
  }

  public String token(String code, String codeVerifier) throws Exception {
    Optional<OauthAuthorizationRow> rowOptional = oauthAuthorizationRowRepository.findByCode(code);
    if (!rowOptional.isPresent()) {
      throw new InvalidParameterException("access code doesn't exist");
    }
    if (rowOptional.get().getIdToken() != null) {
      log.warn("token has been generated for this code before");
    }
    String codeChallenge = rowOptional.get().getCodeChallenge();
    if (!codeChallenge.equals(CredentialUtils.constructCodeChallenge(codeVerifier))) {
      throw new InvalidParameterException("code challenge and code verifier don't match");
    }
    String token = CredentialUtils.generateSignedJWT();
    oauthAuthorizationRowRepository.saveToken(code, token);
    return token;
  }
}
