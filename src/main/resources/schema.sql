CREATE TABLE roles (
  id IDENTITY,
  roleName VARCHAR NOT NULL,
  camp VARCHAR
);

CREATE TABLE users (
  id IDENTITY,
  userName VARCHAR NOT NULL,
  voted INT,
  userole VARCHAR
);
