    CREATE TABLE IF NOT EXISTS PUBLIC.tab_todo
    (
        col_id     SERIAL PRIMARY KEY,
        col_name   VARCHAR(80) NOT NULL,
        col_desc   VARCHAR(500),
        col_due    TIMESTAMP NOT NULL,
        col_state  BOOLEAN NOT NULL
    );
