CREATE TABLE roles (
  roleName VARCHAR IDENTITY NOT NULL,
  group VARCHAR
);

CREATE TABLE users (
  nameid IDENTITY,
  userName VARCHAR NOT NULL,
  voted INT
);
