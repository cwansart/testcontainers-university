CREATE TABLE IF NOT EXISTS PUBLIC.TAB_TODO
(
    col_id    SERIAL      NOT NULL,
    col_name  VARCHAR(80) NOT NULL,
    col_desc  VARCHAR(500),
    col_due   TIMESTAMP   NOT NULL,
    col_state BOOLEAN     NOT NULL,
    PRIMARY KEY (col_id)
);
