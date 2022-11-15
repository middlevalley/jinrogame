CREATE TABLE roles (
  roleid IDENTITY NOT NULL,
  roleName VARCHAR NOT NULL,
  group VARCHAR
);

CREATE TABLE users (
  nameid IDENTITY NOT NULL,
  userName VARCHAR NOT NULL,
  voted INT
);
