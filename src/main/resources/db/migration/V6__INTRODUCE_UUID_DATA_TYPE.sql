DROP TABLE RESOURCE;

CREATE TABLE RESOURCE (
    ID UUID PRIMARY KEY,
    ADDED TIMESTAMP NOT NULL,
    TITLE VARCHAR(200),
    URL TEXT
);