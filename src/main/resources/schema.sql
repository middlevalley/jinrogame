CREATE TABLE roles (
  id IDENTITY,
  roleName VARCHAR NOT NULL,
  camp VARCHAR,
  roleImage VARCHAR
);

CREATE TABLE users (
  id IDENTITY,
  userName VARCHAR NOT NULL,
  voted INT,
  userRole VARCHAR,
  killFlag INT,
  useFlag INT
);

CREATE TABLE winner(
  id IDENTITY,
  userName VARCHAR NOT NULL,
  camp VARCHAR
)
