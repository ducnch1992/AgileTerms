
    create table users (
       id  serial not null,
        email varchar(255) not null,
        password varchar(255),
        status boolean,
        username varchar(255) not null,
        primary key (id)
    );


   create table user_role_assignment (
       id  serial not null,
        role varchar(255),
        users_id int4,
        primary key (id),
              CONSTRAINT fk_user
              FOREIGN KEY(users_id)
        	  REFERENCES users
    );
