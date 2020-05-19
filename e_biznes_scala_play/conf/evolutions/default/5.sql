# --- !Ups

CREATE TABLE role(
    id   VARCHAR NOT NULL PRIMARY KEY,
    name VARCHAR
);

INSERT INTO role (id, name) VALUES ('2f67617a-de2d-44b6-aa75-dd75154d88b8', 'user'), ('f7e5b0f2-dbbf-4a49-9deb-3ca8439d9d57', 'admin');

CREATE TABLE user(
    id         UUID    NOT NULL PRIMARY KEY,
    first_name VARCHAR,
    last_name  VARCHAR,
    email      VARCHAR,
    role_id    INT     NOT NULL,
    activated  BOOLEAN NOT NULL,
    avatar_url VARCHAR,
    CONSTRAINT auth_user_role_id_fk FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE login_info(
    id           BIGSERIAL NOT NULL PRIMARY KEY,
    provider_id  VARCHAR,
    provider_key VARCHAR
);

CREATE TABLE user_login_info(
    user_id       UUID   NOT NULL,
    login_info_id BIGINT NOT NULL,
    CONSTRAINT auth_user_login_info_user_id_fk FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT auth_user_login_info_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info(id)
);

CREATE TABLE oauth2_info (
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    access_token  VARCHAR   NOT NULL,
    token_type    VARCHAR,
    expires_in    INT,
    refresh_token VARCHAR,
    login_info_id BIGINT    NOT NULL,
    CONSTRAINT auth_oauth2_info_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info(id)
);

# --- !Downs

DROP TABLE oauth2_info;
DROP TABLE user_login_info;
DROP TABLE login_info;
DROP TABLE user;
DROP TABLE role;