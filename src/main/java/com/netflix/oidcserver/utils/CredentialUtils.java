package com.netflix.oidcserver.utils;

import static org.apache.commons.codec.binary.Hex.encodeHexString;

import com.netflix.oidcserver.database.OauthAuthorizationRowRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class CredentialUtils {
  private static final int ACCESS_CODE_LENGTH = 43;
  private static final int BASE_OF_HEXADECIMAL_NUMBERS = 16;
  private static final int RADIX_OF_ALPHANUMERIC_STRING = 36;

  public SecureRandom getSecureRandom() {
    try {
      return SecureRandom.getInstance("NativePRNG");
    } catch (NoSuchAlgorithmException e) {
      log.warn("Cannot generate SecureRandom instance with NativePRNG", e);
      return new SecureRandom();
    }
  }

  private static String getNextRandomString(final SecureRandom random) {
    final byte[] randomBytes = new byte[ACCESS_CODE_LENGTH];
    random.nextBytes(randomBytes);

    // Encode the random bytes as hex, parse it to an integer, and convert the integer to a
    // base 36 string.
    return new BigInteger(encodeHexString(randomBytes), BASE_OF_HEXADECIMAL_NUMBERS)
        .toString(RADIX_OF_ALPHANUMERIC_STRING);
  }

  public static String generateAccessCode(final OauthAuthorizationRowRepository repository) {
    String code;
    do {
      code = getNextRandomString(getSecureRandom());
    } while (repository.findByCode(code).isPresent());
    return code;
  }

  public static String constructCodeChallenge(final String codeVerifier) throws NoSuchAlgorithmException {
    byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(bytes, 0, bytes.length);
    byte[] digest = messageDigest.digest();
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }

  public static String generateSignedJWT() throws ParseException, JOSEException {
    JWTClaimsSet claims = new JWTClaimsSet.Builder()
        .issuer(Constants.ISSUER)
        .subject(Constants.SUB)
        .build();

    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
        .keyID(Constants.PRIVATE_KEY.get("kid").toString())
        .build();

    SignedJWT signedJWT = new SignedJWT(header, claims);
    signedJWT.sign(new ECDSASigner(ECKey.parse(Constants.PRIVATE_KEY)));
    return signedJWT.serialize();
  }
}
