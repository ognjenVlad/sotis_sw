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


insert into subject (title, teacher_id) values ('TestPredmet', 2);

insert into choice (correct, text) values (true, 'pitanje1choice1');
insert into choice (correct, text) values (true, 'pitanje1choice2');
insert into choice (correct, text) values (true, 'pitanje2choice1');
insert into choice (correct, text) values (true, 'pitanje2choice2');
insert into choice (correct, text) values (true, 'pitanje3choice1');
insert into choice (correct, text) values (true, 'pitanje3choice2');
insert into choice (correct, text) values (true, 'pitanje4choice1');
insert into choice (correct, text) values (true, 'pitanje4choice2');
insert into choice (correct, text) values (true, 'pitanje5choice1');
insert into choice (correct, text) values (true, 'pitanje5choice2');

insert into question (text) values ('pitanje1');
insert into question (text) values ('pitanje2');
insert into question (text) values ('pitanje3');
insert into question (text) values ('pitanje4');
insert into question (text) values ('pitanje5');


insert into node (question_id) values (1);
insert into node (question_id) values (2);
insert into node (question_id) values (3);
insert into node (question_id) values (4);
insert into node (question_id) values (5);

insert into graph (root_id) values (1);
insert into graph_nodes (graph_id, nodes_id) values (1,1);
insert into graph_nodes (graph_id, nodes_id) values (1,2);
insert into graph_nodes (graph_id, nodes_id) values (1,3);
insert into graph_nodes (graph_id, nodes_id) values (1,4);
insert into graph_nodes (graph_id, nodes_id) values (1,5);

insert into domain (expected_graph_id, subject_id) values (1,1);
update subject set domain_id = 1 where id = 1;

insert into question_choices (question_id,choices_id) values (1,1);
insert into question_choices (question_id,choices_id) values (1,2);
insert into question_choices (question_id,choices_id) values (2,3);
insert into question_choices (question_id,choices_id) values (2,4);
insert into question_choices (question_id,choices_id) values (3,5);
insert into question_choices (question_id,choices_id) values (3,6);
insert into question_choices (question_id,choices_id) values (4,7);
insert into question_choices (question_id,choices_id) values (4,8);
insert into question_choices (question_id,choices_id) values (5,9);
insert into question_choices (question_id,choices_id) values (5,10);

insert into exam (subject_id) values (1);
insert into subject_exams (subject_id, exams_id) values (1,1);

insert into exam_questions (exam_id, questions_id) values (1,1);
insert into exam_questions (exam_id, questions_id) values (1,2);
insert into exam_questions (exam_id, questions_id) values (1,3);
insert into exam_questions (exam_id, questions_id) values (1,4);
insert into exam_questions (exam_id, questions_id) values (1,5);


-- Stud 1
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 1);

insert into question_answer (correct, question_id) values (true, 1);
insert into question_answer (correct, question_id) values (true, 2);
insert into question_answer (correct, question_id) values (true, 3);
insert into question_answer (correct, question_id) values (true, 4);
insert into question_answer (correct, question_id) values (true, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (1,1);
insert into exam_result_answeres (exam_result_id, answeres_id) values (1,2);
insert into exam_result_answeres (exam_result_id, answeres_id) values (1,3);
insert into exam_result_answeres (exam_result_id, answeres_id) values (1,4);
insert into exam_result_answeres (exam_result_id, answeres_id) values (1,5);

-- Stud 2
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 2);

insert into question_answer (correct, question_id) values (false, 1);
insert into question_answer (correct, question_id) values (false, 2);
insert into question_answer (correct, question_id) values (false, 3);
insert into question_answer (correct, question_id) values (true, 4);
insert into question_answer (correct, question_id) values (false, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (2,6);
insert into exam_result_answeres (exam_result_id, answeres_id) values (2,7);
insert into exam_result_answeres (exam_result_id, answeres_id) values (2,8);
insert into exam_result_answeres (exam_result_id, answeres_id) values (2,9);
insert into exam_result_answeres (exam_result_id, answeres_id) values (2,10);

-- Stud 3
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 3);

insert into question_answer (correct, question_id) values (false, 1);
insert into question_answer (correct, question_id) values (true, 2);
insert into question_answer (correct, question_id) values (false, 3);
insert into question_answer (correct, question_id) values (false, 4);
insert into question_answer (correct, question_id) values (true, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (3,11);
insert into exam_result_answeres (exam_result_id, answeres_id) values (3,12);
insert into exam_result_answeres (exam_result_id, answeres_id) values (3,13);
insert into exam_result_answeres (exam_result_id, answeres_id) values (3,14);
insert into exam_result_answeres (exam_result_id, answeres_id) values (3,15);

-- Stud 4
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 4);

insert into question_answer (correct, question_id) values (true, 1);
insert into question_answer (correct, question_id) values (false, 2);
insert into question_answer (correct, question_id) values (true, 3);
insert into question_answer (correct, question_id) values (true, 4);
insert into question_answer (correct, question_id) values (false, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (4,16);
insert into exam_result_answeres (exam_result_id, answeres_id) values (4,17);
insert into exam_result_answeres (exam_result_id, answeres_id) values (4,18);
insert into exam_result_answeres (exam_result_id, answeres_id) values (4,19);
insert into exam_result_answeres (exam_result_id, answeres_id) values (4,20);

-- Stud 5
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 5);

insert into question_answer (correct, question_id) values (true, 1);
insert into question_answer (correct, question_id) values (false, 2);
insert into question_answer (correct, question_id) values (true, 3);
insert into question_answer (correct, question_id) values (true, 4);
insert into question_answer (correct, question_id) values (true, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (5,21);
insert into exam_result_answeres (exam_result_id, answeres_id) values (5,22);
insert into exam_result_answeres (exam_result_id, answeres_id) values (5,23);
insert into exam_result_answeres (exam_result_id, answeres_id) values (5,24);
insert into exam_result_answeres (exam_result_id, answeres_id) values (5,25);

-- Stud 6
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 6);

insert into question_answer (correct, question_id) values (false, 1);
insert into question_answer (correct, question_id) values (false, 2);
insert into question_answer (correct, question_id) values (true, 3);
insert into question_answer (correct, question_id) values (true, 4);
insert into question_answer (correct, question_id) values (true, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (6,26);
insert into exam_result_answeres (exam_result_id, answeres_id) values (6,27);
insert into exam_result_answeres (exam_result_id, answeres_id) values (6,28);
insert into exam_result_answeres (exam_result_id, answeres_id) values (6,29);
insert into exam_result_answeres (exam_result_id, answeres_id) values (6,30);

-- Stud 7
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 7);

insert into question_answer (correct, question_id) values (false, 1);
insert into question_answer (correct, question_id) values (false, 2);
insert into question_answer (correct, question_id) values (true, 3);
insert into question_answer (correct, question_id) values (false, 4);
insert into question_answer (correct, question_id) values (true, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (7,31);
insert into exam_result_answeres (exam_result_id, answeres_id) values (7,32);
insert into exam_result_answeres (exam_result_id, answeres_id) values (7,33);
insert into exam_result_answeres (exam_result_id, answeres_id) values (7,34);
insert into exam_result_answeres (exam_result_id, answeres_id) values (7,35);

-- Stud 8
insert into exam_result (questions_answered, exam_id, student_id) values (5, 1, 8);

insert into question_answer (correct, question_id) values (false, 1);
insert into question_answer (correct, question_id) values (true, 2);
insert into question_answer (correct, question_id) values (false, 3);
insert into question_answer (correct, question_id) values (true, 4);
insert into question_answer (correct, question_id) values (false, 5);

insert into exam_result_answeres (exam_result_id, answeres_id) values (8,36);
insert into exam_result_answeres (exam_result_id, answeres_id) values (8,37);
insert into exam_result_answeres (exam_result_id, answeres_id) values (8,38);
insert into exam_result_answeres (exam_result_id, answeres_id) values (8,39);
insert into exam_result_answeres (exam_result_id, answeres_id) values (8,40);

