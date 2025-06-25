SHOW DATABASES;
USE DTraineeShip;
CREATE DATABASE DTraineeShip;
SHOW tables ;
SELECT * FROM students;
SELECT * FROM professors;
SELECT * FROM student_logbook;
SELECT * FROM committee_members;
SELECT * FROM company_evaluation;
SELECT * FROM evaluation;

SELECT * FROM log_entries;
SELECT * FROM companies;
SELECT * FROM users WHERE role = 'committee';
SELECT * FROM committee_members;
SELECT id, username FROM users WHERE role = 'committee';
SELECT * FROM users WHERE username = 'committee';

-- 1. Εισαγωγή χρήστη στο users
INSERT INTO users (id, username, password, email, first_name, last_name, full_name, role)
VALUES (
    100, -- άλλαξε το αν χρειάζεται για να μη συγκρούεται
    'committee',
    '$2a$10$wHZYgWk.X1IxHlW8ltqtX.yR6qRBN6bnX0XzCizb39xIHtJmHJuJu', -- password = "password"
    'committee1@example.com',
    'Maria',
    'farma',
    'Maria farma',
    'committee'
);

-- 2. Εισαγωγή στο committee_members με foreign key τον user_id
INSERT INTO committee_members (id, user_id, full_name)
VALUES (
    100, -- ή NULL αν έχεις AUTO_INCREMENT
    100,
    'Maria farma'
);
ALTER TABLE evaluation
DROP COLUMN company_effectiveness,
DROP COLUMN company_efficiency,
DROP COLUMN company_motivation;
