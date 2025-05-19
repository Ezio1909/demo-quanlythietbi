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

### Reporting & Analytics
- Generate various reports:
  - Device inventory status
  - Maintenance history
  - Assignment tracking
  - Usage statistics
- Export reports in multiple formats:
  - PDF
  - Excel
  - CSV
- Customizable report settings
- Auto-generate scheduled reports

### System Settings
- Configurable system preferences:
  - Language settings
  - Theme options
  - Notification preferences
- Database configuration:
  - Multiple database support (H2, SQLite)
  - Automatic backups
  - Backup location configuration
- Report customization:
  - Default format selection
  - Logo inclusion options
  - Auto-generation scheduling

## Technical Stack

- Java 17
- Swing (Modern UI)
- H2 Database
- Custom Connection Pool (using BlockingQueue)
- SLF4J + Logback (Logging)
- Maven

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
- The schema and sample data will be initialized automatically from `mysql-init/schema.sql`.

### 2. Configure the Application

- The application is pre-configured to use MySQL if you set `DBType.MYSQL` in your code (see `DeviceManagementApplication.java`).
- The MySQL JDBC URL is: `jdbc:mysql://localhost:3306/devicedb?useSSL=false&serverTimezone=UTC`
- User: `devuser`, Password: `devpass`

### 3. Build and Run the Application

```bash
mvn clean package
mvn clean compile exec:java
```

Or run the JAR:

```bash
java -jar target/device-management-system-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Implementation Details

### User Interface Design
- Dark theme sidebar (RGB: 51,51,51)
- Card-based layouts
- Responsive design
- Interactive elements with hover effects
- Clean typography
- Proper spacing and padding

### Technical Features
- Custom connection pooling with BlockingQueue
- Comprehensive error logging
- H2 database with schema management
- Clean architecture with DAO pattern
- Service layer abstraction

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
