DROP DATABASE IF EXISTS dlc;

CREATE DATABASE dlc;
USE dlc;

CREATE TABLE VOCABULARY (
    WORD VARCHAR(200) NOT NULL,
    MAX_TERM_FREQUENCY INT NOT NULL,
    DOCUMENT_FREQUENCY INT NOT NULL,
    PRIMARY KEY (WORD)
);

CREATE TABLE DOCUMENTS (
    ID INTEGER AUTO_INCREMENT,
    PATH VARCHAR(200) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE POSTS (
    DOCUMENT_ID INT NOT NULL,
    WORD VARCHAR(200) NOT NULL,
    FREQUENCY INT NOT NULL,
    CONTEXT VARCHAR(200),
    PRIMARY KEY (DOCUMENT_ID, WORD)
);

SHOW TABLES;