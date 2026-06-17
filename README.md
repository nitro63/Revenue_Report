# Revenue Report System

A desktop application for managing and monitoring revenue — built with Java and JavaFX.

## Features

- Daily revenue entries tracking
- Commission details management
- Cheque details recording
- Collection payment entries
- Value books stock records

## Tech Stack

- **Language:** Java
- **UI:** JavaFX (JFoenix material components)
- **Database:** MySQL
- **Build:** Apache Ant (`build.xml`)

## Getting Started

### Prerequisites

- Java 11+
- MySQL 8+
- [JFoenix](https://github.com/sshahine/JFoenix) (included as `jfoenix-9.0.10.jar`)
- MySQL Connector/J 8.0.22 (included)

### Setup

1. Import the schema from `revenue_monitoring (2).sql` into your MySQL instance.
2. Update `connection.properties` with your database credentials.
3. Build and run via your IDE or:
   ```bash
   ant -f build.xml
   ```

## License

See [LICENSE](LICENSE).
