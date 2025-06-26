# Traineeship Management Application

A web-based platform developed as part of the university Software Engineering course to manage the complete lifecycle of student traineeships, including application, assignment, evaluation, and supervision.

##  Project Overview

This project implements a **Traineeship Management System** intended for use by:

- **Students** to apply for traineeships and track their progress
- **Companies** to offer traineeship positions and evaluate students
- **Professors** to supervise assigned traineeships and submit evaluations
- **Traineeship Committee** to assign students to positions and professors, and monitor everything centrally

The system follows a modular MVC architecture based on Spring Boot, Thymeleaf, JPA/Hibernate and MySQL.

##  Technologies Used

- **Backend:** Java 17, Spring Boot
- **Frontend:** HTML, Thymeleaf
- **Database:** MySQL
- **Testing:** JUnit, Mockito
- **Build Tool:** Maven
- **IDE:** Eclipse

##  Key Features Implemented

-  User registration and login for all roles
-  Students can apply for positions, track logbook
-  Companies manage traineeship offerings and evaluate students
-  Professors view assigned students and submit evaluations
-  Committee assigns students to companies and professors based on strategy
-  Strategy Pattern used for allocation based on:
  - Interest similarity (Jaccard)
  - Location match
  - Professor load balancing
-  Full MVC architecture using Spring controllers and repositories
-  Database layer with proper entity relationships

##  Project Structure

The files are organized under:

```
src/
├── main/
│   ├── java/           # Java source code (organized by packages)
│   └── resources/      # HTML templates, application config
├── test/               # JUnit test classes
target/                 # Compiled artifacts (excluded via .gitignore)
```

If you are browsing this GitHub repo, **all source files are located under**:
[`/src/main/java`](./src/main/java)

##  How to Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/maytsatsari/traineeship-app.git
   cd traineeship-app
   ```

2. Make sure you have MySQL and Java 17 installed.

3. Update `src/main/resources/application.properties` with your local DB credentials.

4. Run using Maven:
   ```bash
   mvn spring-boot:run
   ```

Then access the application via `http://localhost:8080`.

include screenshots 

## Authors

This project was developed as part of the **Software Engineering Project Course**

Developed by:
- **Maria Tsatsari**
- and one more student teammate

Both team members contributed significantly to all phases of the project, from analysis to implementation.

---
