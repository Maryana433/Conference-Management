DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS conference_user;

CREATE TABLE conference_user (
    id IDENTITY PRIMARY KEY,
    login varchar(128),
    email varchar (128)
);


CREATE TABLE reservation (
    id IDENTITY PRIMARY KEY,
    lecture_id bigint,
    user_id bigint
);


ALTER TABLE reservation
    ADD FOREIGN KEY (user_id) REFERENCES conference_user(id);