insert into authority (name) values ('STUDENT_ROLE');
insert into authority (name) values ('PROFESSOR_ROLE');
insert into authority (name) values ('ADMIN_ROLE');


-- DODAVANJE User naslednice sa dtype-om i dodavanje role-ova njemu
insert into user (username, password) values ('stud', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq');
insert into user (username, password) values ('prof', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq');
insert into user (username, password) values ('admin', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq');
insert into user_authority (user_id, authority_id) values (1, 1); -- stud has STUDENT_ROLE
insert into user_authority (user_id, authority_id) values (2, 2); -- prof has PROFESSOR_ROLE
insert into user_authority (user_id, authority_id) values (3, 3); -- prof has PROFESSOR_ROLE

