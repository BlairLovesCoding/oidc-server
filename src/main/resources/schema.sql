DROP TABLE IF EXISTS oauth_authorization_row;

CREATE TABLE oauth_authorization_row (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    code_challenge VARCHAR(43) NOT NULL,
    id_token VARCHAR(255)
);

CREATE UNIQUE INDEX ON oauth_authorization_row (code);
