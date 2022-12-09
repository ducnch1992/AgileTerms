insert into users(email,password,username) values
('admin1@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','admin1'),
('admin2@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','linkleebruh'),
('admin3@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','taLUVkats'),
('admin4@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','sexypanda'),
('admin5@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','uarokia'),
('admin6@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','mrUSUK'),
('admin7@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','hotboyD6'),
('admin8@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','pokerface'),
('admin9@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','goatdevops'),
('admin10@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','henparty'),
('admin11@axonactive.com','$2a$10$Wdp9JDwO8AYEfsTutbzus.cDcN1HRncWxl5iRbZjvPUe71em3VSom','user1');


--ALTER table description
--	    ADD COLUMN user_id int4,
--       ADD CONSTRAINT fk_user_description
--       FOREIGN KEY(user_id)
--       REFERENCES users(id),
--       DROP CONSTRAINT fk_author;


--UPDATE description SET user_id = author_id ;

DROP TABLE author CASCADE;