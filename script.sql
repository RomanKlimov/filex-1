CREATE SEQUENCE users_user_id_seq;

CREATE SEQUENCE folders_id_seq;

CREATE SEQUENCE feedback_id_seq;

CREATE SEQUENCE files_id_seq;

CREATE SEQUENCE logs_id_seq;

CREATE SEQUENCE purchases_id_seq;

CREATE TABLE users
(
  id          INTEGER DEFAULT nextval('users_user_id_seq' :: REGCLASS) NOT NULL,
  name        VARCHAR(50)                                              NOT NULL
    CONSTRAINT proper_name
    CHECK ((name) :: TEXT ~* '^[A-Za-zА-Яа-я]{2,20}' :: TEXT),
  email       VARCHAR(50)                                              NOT NULL
    CONSTRAINT email_pk
    PRIMARY KEY
    CONSTRAINT proper_email
    CHECK ((email) :: TEXT ~* '^[-\w.]+@([A-z0-9][-A-z0-9]+\.)+[A-z]{2,4}$' :: TEXT),
  password    VARCHAR(50)                                              NOT NULL,
  phonenumber VARCHAR(50)                                              NOT NULL
    CONSTRAINT unique_pn
    UNIQUE
    CONSTRAINT proper_phone
    CHECK ((phonenumber) :: TEXT ~* '^((8|\+7)[\- ]?)?(\(?\d{3}\)?[\- ]?)?[\d\- ]{7,10}$' :: TEXT)
);

CREATE UNIQUE INDEX users_id_uindex
  ON users (id);

CREATE UNIQUE INDEX users_email_uindex
  ON users (email);

CREATE UNIQUE INDEX "users_phoneNumber_uindex"
  ON users (phonenumber);

CREATE FUNCTION reg_capacity_declaration()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  user_info VARCHAR(100);
BEGIN
  IF TG_OP = 'INSERT'
  THEN
    user_info = NEW.email;
    INSERT INTO users_capacity (email) VALUES (user_info);
    RETURN NEW;
  END IF;
END;
$$;

CREATE TRIGGER t_capacity
AFTER INSERT
  ON users
FOR EACH ROW
EXECUTE PROCEDURE reg_capacity_declaration();

CREATE FUNCTION users_add_to_log()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  message   VARCHAR(30);
  user_info VARCHAR(100);
  log_str   VARCHAR(254);
BEGIN
  IF TG_OP = 'INSERT'
  THEN
    user_info = NEW.email;
    message := 'Add new user ';
    log_str := message || user_info;
    INSERT INTO logs (text, date) VALUES (log_str, NOW());
    RETURN NEW;
  ELSIF TG_OP = 'UPDATE'
    THEN
      user_info = NEW.email;
      message := 'Update user ';
      log_str := message || astr;
      INSERT INTO logs (text, date) VALUES (log_str, NOW());
      RETURN NEW;
  ELSIF TG_OP = 'DELETE'
    THEN
      user_info = OLD.email;
      message := 'Remove user ';
      log_str := message || user_info;
      INSERT INTO logs (text, date) VALUES (log_str, NOW());
      RETURN OLD;
  END IF;
END;
$$;

CREATE TRIGGER t_user
AFTER INSERT OR UPDATE OR DELETE
  ON users
FOR EACH ROW
EXECUTE PROCEDURE users_add_to_log();

CREATE TABLE folders
(
  root_link      INTEGER                                             NOT NULL
    CONSTRAINT root_link_pk
    PRIMARY KEY,
  email          VARCHAR(50)                                         NOT NULL
    CONSTRAINT email_fk
    REFERENCES users
    ON DELETE CASCADE,
  seq_folders_id INTEGER DEFAULT nextval('files_id_gen' :: REGCLASS) NOT NULL
);

CREATE TABLE feedback
(
  firstname VARCHAR(50)                                            NOT NULL,
  lastname  VARCHAR(50)                                            NOT NULL,
  email     VARCHAR(50)                                            NOT NULL,
  message   VARCHAR(500)                                           NOT NULL,
  id        INTEGER DEFAULT nextval('feedback_id_seq' :: REGCLASS) NOT NULL,
  date      TIME DEFAULT now()
);

CREATE TABLE files
(
  root_link     INTEGER                                             NOT NULL
    CONSTRAINT root_link_fk
    REFERENCES folders
    ON DELETE CASCADE,
  id            INTEGER DEFAULT nextval('files_id_seq' :: REGCLASS) NOT NULL,
  file_hash     VARCHAR(100)                                        NOT NULL,
  file_name     VARCHAR(100)                                        NOT NULL,
  publicity     BOOLEAN DEFAULT FALSE                               NOT NULL,
  last_modified TIME DEFAULT now()
);

CREATE INDEX fki_root_link_fk
  ON files (root_link);

CREATE TABLE roles
(
  email      VARCHAR(100)          NOT NULL
    CONSTRAINT email_fk
    REFERENCES users
    ON DELETE CASCADE,
  admin_role BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE INDEX fki_email_fk
  ON roles (email);

CREATE FUNCTION role_add_to_log()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  message   VARCHAR(30);
  message1  VARCHAR(30);
  user_info VARCHAR(100);
  user_role BOOLEAN;
  log_str   VARCHAR(254);
BEGIN
  IF TG_OP = 'INSERT'
  THEN
    user_info = NEW.email;
    user_role = NEW.admin_role;
    message := 'For new user  ';
    message1 := ' setted admin`s role:  ';
    log_str := message || user_info || message1 || user_role;
    INSERT INTO logs (text, date) VALUES (log_str, NOW());
    RETURN NEW;
  ELSIF TG_OP = 'UPDATE'
    THEN
      user_info = NEW.email;
      user_role = NEW.admin_role;
      message := 'For user  ';
      message1 := ' updated admin`s role:  ';
      log_str := message || user_info || message1 || user_role;
      INSERT INTO logs (text, date) VALUES (log_str, NOW());
      RETURN NEW;
  END IF;
END;
$$;

CREATE TRIGGER t_roles
AFTER INSERT OR UPDATE
  ON roles
FOR EACH ROW
EXECUTE PROCEDURE role_add_to_log();

CREATE TABLE logs
(
  text TEXT,
  date TIMESTAMP,
  id   INTEGER DEFAULT nextval('logs_id_seq' :: REGCLASS) NOT NULL
);

CREATE TABLE purchases
(
  id               INTEGER DEFAULT nextval('purchases_id_seq' :: REGCLASS) NOT NULL,
  email            VARCHAR(50)                                             NOT NULL
    CONSTRAINT email
    REFERENCES users,
  "_key"           VARCHAR(500)                                            NOT NULL,
  date_of_purchase TIME DEFAULT now()                                      NOT NULL
);

CREATE INDEX fki_email
  ON purchases (email);

CREATE FUNCTION update_users_capacity()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  user_info VARCHAR(100);
  user_key  VARCHAR(500);
BEGIN
  IF TG_OP = 'INSERT'
  THEN
    user_info = NEW.email;
    user_key = NEW._key;
    UPDATE users_capacity
    SET capacity = capacity + (SELECT us.memory
                               FROM purchases_keys us
                               WHERE "key" = user_key)
    WHERE email = user_info;
    RETURN NEW;
  END IF;
END;
$$;

CREATE TRIGGER purchases_t
AFTER INSERT
  ON purchases
FOR EACH ROW
EXECUTE PROCEDURE update_users_capacity();

CREATE FUNCTION delete_users_key_case()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
  p_key   VARCHAR(500);
  p_email VARCHAR(100);
BEGIN
  IF TG_OP = 'DELETE'
  THEN
    p_key = OLD._key;
    p_email = OLD.email;
    UPDATE users_capacity
    SET capacity = capacity - (SELECT pk.memory
                               FROM purchases_keys pk
                               WHERE "key" = p_key)
    WHERE email = p_email;
    RETURN OLD;
  END IF;
END;
$$;

CREATE TRIGGER delete_key_t
AFTER DELETE
  ON purchases
FOR EACH ROW
EXECUTE PROCEDURE delete_users_key_case();

CREATE TABLE purchases_keys
(
  key         VARCHAR(500) NOT NULL
    CONSTRAINT key_pk
    PRIMARY KEY
    CONSTRAINT uniq_key
    UNIQUE,
  valid_until DATE         NOT NULL,
  memory      INTEGER      NOT NULL
);

CREATE INDEX fki_key_fk
  ON purchases_keys (key);

ALTER TABLE purchases
  ADD CONSTRAINT key_fk
FOREIGN KEY (_key) REFERENCES purchases_keys
ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE users_capacity
(
  email    VARCHAR(50)         NOT NULL
    CONSTRAINT email_fk
    REFERENCES users
    ON DELETE CASCADE,
  capacity INTEGER DEFAULT 500 NOT NULL
);

CREATE VIEW view_allusers AS
  SELECT
    u.name,
    u.email,
    u.phonenumber,
    r.admin_role
  FROM (users u
    JOIN roles r ON (((u.email) :: TEXT = (r.email) :: TEXT)));

CREATE VIEW view_users_capacity AS
  SELECT
    rr.email,
    sum(rr.memory) AS sum
  FROM (SELECT
          pur.email,
          pk.key,
          pk.memory
        FROM (purchases_keys pk
          JOIN purchases pur ON (((pk.key) :: TEXT = (pur._key) :: TEXT)))) rr
  GROUP BY rr.email;

CREATE FUNCTION return_users_count()
  RETURNS INTEGER
LANGUAGE plpgsql
AS $$
DECLARE
  total INTEGER;
BEGIN
  SELECT count(*)
  INTO total
  FROM users;
  RETURN total;
END;
$$;

CREATE FUNCTION return_files_count()
  RETURNS INTEGER
LANGUAGE plpgsql
AS $$
DECLARE
  total INTEGER;
BEGIN
  SELECT count(*)
  INTO total
  FROM files;
  RETURN total;
END;
$$;

