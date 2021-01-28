insert into authority (name) values ('STUDENT_ROLE');
insert into authority (name) values ('PROFESSOR_ROLE');
insert into authority (name) values ('ADMIN_ROLE');


-- DODAVANJE User naslednice sa dtype-om i dodavanje role-ova njemu
insert into user (username, password, role, student_id) values ('stud', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index1');
insert into user (username, password, role) values ('prof', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 1);
insert into user (username, password, role) values ('admin', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 2);
insert into user (username, password, role, student_id) values ('stud1', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index2');
insert into user (username, password, role, student_id) values ('stud2', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index3');
insert into user (username, password, role, student_id) values ('stud3', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index4');
insert into user (username, password, role, student_id) values ('stud4', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index5');
insert into user (username, password, role, student_id) values ('stud5', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index6');
insert into user (username, password, role, student_id) values ('stud6', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index7');
insert into user (username, password, role, student_id) values ('stud7', '$2a$04$Amda.Gm4Q.ZbXz9wcohDHOhOBaNQAkSS1QO26Eh8Hovu3uzEpQvcq', 0, 'index8');

insert into user_authority (user_id, authority_id) values (1, 1); -- stud has STUDENT_ROLE
insert into user_authority (user_id, authority_id) values (2, 2); -- prof has PROFESSOR_ROLE
insert into user_authority (user_id, authority_id) values (3, 3); -- prof has PROFESSOR_ROLE
insert into user_authority (user_id, authority_id) values (4, 1);
insert into user_authority (user_id, authority_id) values (5, 1);
insert into user_authority (user_id, authority_id) values (6, 1);
insert into user_authority (user_id, authority_id) values (7, 1);
insert into user_authority (user_id, authority_id) values (8, 1);
insert into user_authority (user_id, authority_id) values (9, 1);
insert into user_authority (user_id, authority_id) values (10, 1);

