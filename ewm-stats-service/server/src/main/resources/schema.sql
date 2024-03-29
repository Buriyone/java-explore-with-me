DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS stats (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app_name VARCHAR(255) NOT NULL,
  uri_address VARCHAR(512) NOT NULL,
  ip_address VARCHAR(512) NOT NULL,
  time_request TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_stat_id PRIMARY KEY (id)
);