# Happy Dental Hub

Happy Dental Hub is a student web application built with Java Servlets, JDBC,
Oracle Database, HTML, CSS, and JavaScript.

> Update this README with your own project details, screenshots, team members,
> and verified setup instructions before submitting the repository.

## Features

- User registration and login
- Session-based authentication
- Dental service and information pages
- Appointment booking
- Oracle Database connectivity

## Technology Stack

- Java (project configured for Java 21)
- Java Servlets / JDBC
- Oracle Database
- HTML, CSS, JavaScript
- Apache Tomcat 9
- Eclipse IDE project configuration

## Project Structure

```text
HappyDentalHUb/
├── src/main/java/
│   ├── com/happydentalhub/dao/
│   ├── com/happydentalhub/model/
│   ├── com/happydentalhub/servlet/
│   ├── com/happydentalhub/util/
│   └── db.properties.example
├── src/main/webapp/
│   ├── WEB-INF/
│   ├── HTML pages
│   ├── CSS and JavaScript
│   └── image assets
├── .gitignore
└── README.md
```

## Local Setup

1. Install a compatible JDK, Oracle Database, and Apache Tomcat 9.
2. Import the project into Eclipse as an existing Dynamic Web Project.
3. Copy `src/main/java/db.properties.example` to `src/main/java/db.properties`.
4. Edit the local `db.properties` file with your Oracle connection details.
5. Obtain the Oracle JDBC driver from an official Oracle source, review its
   license, and add it to the local project's `WEB-INF/lib` as required.
6. Configure the Tomcat runtime in Eclipse and run the project.

`db.properties`, compiled classes, build output, and the Oracle JDBC JAR are
intentionally excluded from this Git repository.

## Database

The Java code expects Oracle tables named `app_users` and `appointments`.
Add your verified SQL table-creation script here (for example,
`database/schema.sql`) so another developer can reproduce the database.
Do not add real user records, passwords, or personal information.

## Screenshots

Add a `screenshots/` folder with screenshots of the application's main pages
after checking that they contain no real user or personal data.

## Notes Before Submission

- Replace this template with accurate project-specific details.
- Verify that all image assets are original or permitted for reuse.
- Verify or replace any public phone number and email address shown in the pages.
- Add contributor names only with their agreement.
- Do not publish database credentials, API keys, private keys, or real user data.
