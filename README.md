# Device Management System

A Java Swing-based application for managing company devices and their assignments to employees. Built with modern Java features and a clean architecture.

## Features

### Device Management
- Add, edit and remove devices from inventory
- Track comprehensive device information:
  - Basic info (name, type, serial number, status)
  - Purchase details (date, price, supplier, warranty)
  - Technical specs (model, manufacturer, firmware)
  - Asset tracking (tag, location, department)
  - Lifecycle management (inspections, end-of-life)
- View devices in a sortable and filterable table
- Multiple status tracking (Available, In Use, Maintenance, Retired)
- Condition monitoring (New, Good, Fair, Poor)
- Search and filter devices by multiple criteria:
  - Name/keyword search
  - Status
  - Condition
  - Department
  - Location
- Real-time data updates with configurable auto-refresh
- Smart status tracking and updates

### Assignment Management
- Assign devices to employees with expiration dates
- Track complete device assignment history
- View all devices assigned to an employee
- Automatic status updates when devices are assigned/returned
- Quick access to employee device history
- Filter assignments by:
  - Active/Returned status
  - Employee
  - Device
  - Department
- Expiration date tracking and notifications
- Real-time assignment status updates
- Automatic device status synchronization

### Maintenance Management
- Schedule and track device maintenance
- Multiple maintenance types (Repair, Inspection, Upgrade, Replacement, Cleaning)
- Track maintenance costs and status
- Detailed maintenance history per device
- Schedule future maintenance tasks
- Filter maintenance records by:
  - Status (Pending, Scheduled, In Progress, Completed)
  - Device
  - Type
- Cost tracking and reporting
- Automatic maintenance status progression:
  - Smart status updates based on scheduling
  - Automatic transition to "In Progress" when scheduled time arrives
  - Real-time status monitoring

### Real-Time Features
- Automatic data refresh across all panels
- Configurable refresh intervals
- Visual refresh status indicators
- Smart status transitions:
  - Maintenance status auto-updates based on schedule
  - Device status sync with assignments
  - Assignment expiration tracking
- Background task processing
- Non-blocking UI updates

### User Interface
- Modern dark theme with professional design
- Intuitive navigation sidebar
- Advanced search and filter capabilities
- Interactive data tables with:
  - Sorting by any column
  - Multi-criteria filtering
  - Quick search
- User-friendly forms with validation
- Responsive layout design
- Clear status indicators and icons
- Real-time update indicators
- Auto-refresh status display

### System Features
- Multi-threaded connection pooling
- Automatic database initialization
- Smart database schema selection:
  - Production schema with triggers (MySQL)
  - Testing schema without triggers (H2)
- Thread-safe singleton connections
- Robust error handling
- Comprehensive logging
- Auto-refresh capabilities
- Background task processing

### Technical Implementation
- Custom connection pooling with BlockingQueue
- Double-checked locking for thread safety
- Volatile variables for memory consistency
- Comprehensive error logging
- Clean architecture with DAO pattern
- Service layer abstraction
- Event-driven UI updates
- Automatic resource management

## Technical Stack

- Java 17
- Swing (Modern UI)
- H2 Database (for fast unit/integration testing)
- MySQL 8 (for dev/demo/production)
- Custom Connection Pool (using BlockingQueue)
- SLF4J + Logback (Logging)
- Maven

## Database Schema & Triggers

### MySQL (dev/demo/production)
- Uses `schema.sql` with full schema and triggers for device status automation
- Triggers automatically update device status on:
  - Assignment creation
  - Device return
  - Maintenance status changes
- Schema is resettable for dev/demo with `TRUNCATE` and sample data

### H2 (unit/integration testing)
- Uses `schema-h2.sql` (no triggers) for compatibility and fast test cycles
- Schema is otherwise identical to MySQL
- Tests run quickly and reliably without MySQL-specific syntax issues

### Automatic Schema Selection
- The application detects the database type at runtime:
  - Loads `schema.sql` for MySQL
  - Loads `schema-h2.sql` for H2
- This ensures the correct schema is always used for the environment

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── quanlythietbi/
│   │       ├── connector/         # Database connection management
│   │       ├── controller/        # Business logic controllers
│   │       ├── entity/           # Data models/records
│   │       ├── enums/            # Enumerations
│   │       ├── service/          # Service layer
│   │       │   ├── adapter/      # Adapter pattern implementations
│   │       │   ├── assignment/   # Device assignment services
│   │       │   └── deviceinfo/   # Device information services
│   │       ├── ui/              # Swing UI components
│   │       └── DeviceManagementApplication.java
│   └── resources/
│       └── schema.sql           # Database schema
└── test/
    └── java/
```

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build
```bash
mvn clean package
```

### Run
```bash
mvn clean compile exec:java
```

## Running with MySQL using Docker Compose

### 1. Start MySQL with Docker Compose

```bash
docker-compose up -d
```

- This will start a MySQL 8.0 instance with:
  - Database: `devicedb`
  - User: `devuser`
  - Password: `devpass`
  - Root password: `secret`
- **Note:** Docker Compose no longer initializes the database with any SQL files. The database will be empty or persistent depending on your Docker volume.

### 2. Application-Driven Database Initialization and Prefill

- On every startup, the application will:
  - Truncate all tables in the target MySQL database.
  - Recreate the schema, triggers, and sample data using the bundled `schema.sql`.
  - This ensures a fresh, demo-ready database every time, regardless of the MySQL instance or previous state.
- If you want to fully reset the database (remove all persistent data), you can remove the Docker volume:

```bash
docker-compose down && docker volume rm demo-quanlythietbi_mysql_data && docker-compose up -d
```

### 3. Configure the Application

- The application is pre-configured to use MySQL if you set `DBType.MYSQL` in your code
- The MySQL JDBC URL is: `jdbc:mysql://localhost:3306/devicedb?useSSL=false&serverTimezone=UTC`
- User: `devuser`, Password: `devpass`

### 4. Build and Run the Application

```bash
mvn clean package
mvn clean compile exec:java
```

Or run the JAR:

```bash
java -jar target/device-management-system-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Database Configuration on Startup

When you start the application, you will be prompted with a **Database Configuration** dialog. This allows you to enter the following MySQL connection details:

- Host (default: `localhost`)
- Port (default: `3306`)
- Database name (default: `devicedb`)
- Username (default: `devuser`)
- Password (default: `devpass`)

**Instruction shown in the dialog:**
> If you are running MySQL using Docker Compose, you can keep the default values below.

If you are using the provided Docker Compose setup, simply keep the prefilled values and click OK. Otherwise, enter your custom database information.

If you cancel the dialog, the application will exit.

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
