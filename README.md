# High Priority Task Application

## Overview

This application utilizing Spring Shell to provide a command-line interface (CLI) for user interactions or by loading orders from a JSON file. The application uses MySQL for data storage, with the database schema including four key tables detailed within the provided `es.sql` file.

## Database Configuration

The database is currently hosted remotely, allowing for easy testing and interaction without the need for local setup. The necessary SQL to set up and understand the database structure can be found in the `es.sql` file within the project's directory.

## JSON Input Capabilities

In addition to processing orders via CLI, the application can also handle input from JSON files. This feature allows batch processing of orders, making it suitable for scenarios where orders are collected in bulk or from automated systems.

## Setting Up the Application

### Building the Application

- The application is built with Maven. Navigate to the project's root directory and run `mvn clean install` to build the project and run the tests.

### Running the Application

- Post-build, you can run the application using `java -jar target/highprioritytask.jar`.
- Alternatively, if you're using an IDE like IntelliJ IDEA or Eclipse, you can run the application directly through the IDE by executing the main class or through the configured Maven Spring Boot run configuration.

### Using the Application

- Start the application and use the provided Shell commands to interact with the system. Use `help` in the Spring Shell to see available commands.

- Or you can execute the following commands within the shell to test the functionality:

- To process a single order:
  ```bash
  process order 5,1=10000,4=20000
  ```
- To process orders from a JSON file:
  ```bash
  process orders from file --filePath "classpath:data/orders.json"
  ```