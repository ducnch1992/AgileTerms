create table verification_token (
       id  serial not null,
        creation_date date,
        verification_code varchar(255),
        primary key (id)
    );

ALTER table users
    ADD COLUMN verification_token_id int4,
    ADD COLUMN activated boolean,
    ADD CONSTRAINT fk_verification_token FOREIGN KEY(verification_token_id) REFERENCES verification_token(id),
    DROP COLUMN status;



