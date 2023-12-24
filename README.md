# Work Together Time Tracker

## Overview

The **Work Together Time Tracker** is an application that identifies pairs of employees who have worked together on common projects for the longest period of time. It calculates the total days worked together and provides a breakdown by project.

## Task

The task involves processing input data from a CSV file containing information about employees, projects, and the duration of their collaboration. The application should handle various date formats and support functionalities such as CRUD operations and data persistence.

## Algorithm

1. **CSV Parsing**: The application reads data from a CSV file, supporting multiple date formats. It parses the input, creating Employee objects and storing project dates.

2. **Data Structure**: Employee data is stored in memory, with each employee maintaining a map of project IDs to project date intervals.

3. **Database Interaction: The application interacts with a database to read and write employee data. It supports CRUD operations for employees and ensures data persistence. Database file located in root folder, I used H2 to test.

4. **Identifying Pairs**: The algorithm iterates through employees, identifying pairs who have worked together on common projects. It calculates the intersection of project dates to determine the duration of collaboration.

5. **Thymeleaf Templates**: The web application uses Thymeleaf templates to display information about employee pairs, total days worked together, and project breakdown.

## Components

- **AppController**: Manages web requests, file uploads, and displays information using Thymeleaf templates.

- **Employee**: Represents an employee and stores project date information. It includes methods to add projects and calculate intersections.

- **EmployeeEntity**: Represents the entity mapped to the database, allowing for CRUD operations.

- **EmployeePair**: Represents a pair of employees who have worked together. It calculates the total days worked together and provides a breakdown by project.

- **ProjectDates**: Handles date parsing, calculates intersections, and stores project date information.

- **CsvParser**: Parses the input CSV file and creates Employee objects.

- **DatabaseReader**: Reads employee data from the database.

- **EmployeeEntityRepository**: A repository interface for CRUD operations on the database.

## Usage

1. Clone the repository from GitHub.
2. Configure the database connection in the `application.properties` file.
3. Run the application using `mvn spring-boot:run` or your preferred method.
4. Access the application at `http://localhost:8080`.

## Bonus Features

- **Data Persistence**: The application stores and retrieves data from a database, ensuring information persists between sessions.

## Limitations

- The application may have limitations in handling all possible date formats.
- Additional validation for input data could be implemented.

## Future Enhancements

- Improved error handling and user prompts.
- Support for more date formats.
- Enhanced CRUD operations for employees.
