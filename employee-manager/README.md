# Employee Manager - Spring Boot and MySQL

Employee Manager is a CRUD web application for Lab Sheet 05. It uses Spring Boot, Thymeleaf, Spring Data JPA, MySQL and Lombok. It supports form validation, duplicate-email protection, employee name search, browser CRUD pages and JSON CRUD endpoints for Postman.

## Requirements

- JDK 17 or later (JDK 21 also works)
- MySQL Server 8.x or XAMPP MySQL running on port `3306`
- Maven, or an IDE with Maven support (IntelliJ IDEA / VS Code)

## Configure MySQL safely

1. Create the database once in MySQL Workbench, phpMyAdmin or the MySQL command line:

   ```sql
   CREATE DATABASE employee_db;
   ```

2. Copy `secrets.properties.example` and rename the copy to `secrets.properties` in this project folder.
3. Edit only `secrets.properties` with the real MySQL password. XAMPP commonly has an empty root password, so use `DB_PASSWORD=` if yours is empty.

`secrets.properties` is intentionally ignored by Git. Do not upload it to GitHub.

## Run the application

Open a terminal in this `employee-manager` folder and run:

```bash
mvn spring-boot:run
```

Then open [http://localhost:8080](http://localhost:8080). Hibernate creates or updates the `employees` table automatically.

If you use an IDE, open this folder as a Maven project and run `EmployeeManagerApplication.java`. Enable annotation processing so Lombok-generated getters and setters are recognised.

## Browser URLs

| Method | URL | Purpose |
|---|---|---|
| GET | `/employees` | Form, search and employee table |
| POST | `/employees` | Save a new employee |
| GET | `/employees/{id}/edit` | Show edit form |
| POST | `/employees/{id}/update` | Save edited employee |
| POST | `/employees/{id}/delete` | Delete employee |

## Postman API

Import `postman/Employee-Manager-API.postman_collection.json` into Postman. It contains ready-to-send requests:

| Method | URL | Expected result |
|---|---|---|
| POST | `/api/employees` | `201 Created` and the saved employee |
| GET | `/api/employees` | `200 OK` and all employees |
| GET | `/api/employees/{id}` | `200 OK` and one employee |
| PUT | `/api/employees/{id}` | `200 OK` and updated employee |
| DELETE | `/api/employees/{id}` | `204 No Content` |

Use a real id returned by the create request. The API returns `400` for invalid data, `404` for a missing id and `409` if an email already exists.

## MySQL verification

After creating employees in the browser or Postman, run:

```sql
USE employee_db;
SHOW TABLES;
DESCRIBE employees;
SELECT * FROM employees;
```

## Project structure

```text
src/main/java/com/example/employeemanager/
  controller/    Browser controller, JSON API controller and API error handler
  exception/     Not-found and duplicate-email exceptions
  model/         Employee JPA entity with Lombok and validation
  repository/    Spring Data JPA repository
  service/       Employee CRUD and search logic
src/main/resources/
  static/css/    Application styling
  templates/     Thymeleaf pages
```

## Git workflow for the lab

```bash
git switch -c feature/employee-crud
git add employee-manager
git commit -m "Add employee CRUD with MySQL and Lombok"
git push -u origin feature/employee-crud
```

Create a Pull Request from `feature/employee-crud` to `main`, inspect the changed files, then merge it. Confirm that `secrets.properties` and `target/` never appear in GitHub.
