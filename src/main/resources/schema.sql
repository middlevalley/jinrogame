CREATE TABLE roles (
  roleName VARCHAR NOT NULL,
  camp VARCHAR
);

CREATE TABLE users (
  id IDENTITY,
  userName VARCHAR NOT NULL,
  voted INT
);
