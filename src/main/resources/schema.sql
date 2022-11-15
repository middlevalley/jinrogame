CREATE TABLE roles (
  roleName VARCHAR NOT NULL,
  groups VARCHAR
);

CREATE TABLE users (
  nameid IDENTITY,
  userName VARCHAR NOT NULL,
  voted INT
);
