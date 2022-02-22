package com.netflix.oidcserver.database;

import com.netflix.oidcserver.models.OauthAuthorizationRow;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OauthAuthorizationRowRepository {
  private final JdbcTemplate jdbcTemplate;

  private static final RowMapper<OauthAuthorizationRow> rowMapper = (resultSet, unused) -> {
    String code = resultSet.getString("code");
    String codeChallenge = resultSet.getString("code_challenge");
    String idToken = resultSet.getString("id_token");

    return OauthAuthorizationRow.builder()
        .code(code)
        .codeChallenge(codeChallenge)
        .idToken(idToken)
        .build();
  };

  @Autowired
  public OauthAuthorizationRowRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void saveCode(OauthAuthorizationRow oauthAuthorizationRow) {
    jdbcTemplate.update(
        "INSERT INTO oauth_authorization_row (code, code_challenge, id_token) "
        + "VALUES (?, ?, NULL)",
        oauthAuthorizationRow.getCode(),
        oauthAuthorizationRow.getCodeChallenge()
    );
  }

  public void saveToken(String code, String idToken) {
      jdbcTemplate.update(
          "UPDATE oauth_authorization_row SET id_token = ? WHERE code = ?",
          idToken,
          code
      );
  }

  public Optional<OauthAuthorizationRow> findByCode(String code) {
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(
          "SELECT * FROM oauth_authorization_row WHERE code = ?",
          new Object[] { code },
          rowMapper)
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
