CREATE TABLE leaf
(
  biz_tag     VARCHAR(128)       NOT NULL
    PRIMARY KEY,
  max_id      BIGINT             NOT NULL,
  step        INT DEFAULT '2000' NULL,
  `desc`      VARCHAR(255)       NULL,
  update_time TIMESTAMP DEFAULT now() ON UPDATE now(),
  min_id      INT DEFAULT '0'    NULL,
  CONSTRAINT leaf_biz_tag_uindex
  UNIQUE (biz_tag)
);

