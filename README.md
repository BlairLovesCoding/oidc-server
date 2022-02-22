# OpenID Connect Server

This is a small web server that covers a subset of OpenId Connect standard. It's written in Java 8 using Spring Boot Framework.

## Configuration

**REQUIRED**

In order to test and verify with `oidc-tester` client app, you need to create the following `/src/oidc-config.json` file under the `/src` directory of the tester client app:
```js
{
    "redirect_uri": "http://localhost:3000/oauth/callback",
    "oidc_server": "http://localhost:8080/"
}
```

## Running and Building

### Local JDK8+
To run the application, run the following command in a terminal window under the project directory:
```bash
./mvnw spring-boot:run
```

By default, the application listens to `localhost:8080`.
