CREATE TABLE RESOURCE (
    ID SERIAL PRIMARY KEY,
    ADDED TIMESTAMP NOT NULL,
    TITLE VARCHAR(200),
    URL TEXT
);