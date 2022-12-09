ALTER table user_role_assignment
    ADD COLUMN user_id int4,
    ADD CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id);

insert into users(email,password,username,activated) values
('admin@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','admin',true),
('user@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','user',true);


insert into user_role_assignment(role,user_id) values
('ROLE_ADMIN',1),
('ROLE_USER',2);