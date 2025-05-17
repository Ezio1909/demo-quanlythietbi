# Device Management System

A Java Swing-based application for managing company devices and their assignments to employees. Built with modern Java features and a clean architecture.

## Features

### Device Management
- Add and register new company devices
- Update device information and status
- Remove devices from inventory
- View all devices in a sortable table
- Track device availability status

### Assignment Management
- Assign devices to employees
- Track who has which device
- Record device returns
- View complete assignment history
- Filter assignments by status (Active/Returned)
- Search assignments by employee or device

### User Interface
- Modern, intuitive interface
- Easy-to-use navigation sidebar
- Quick search and filter capabilities
- Clear status indicators
- Responsive table views
- User-friendly forms and dialogs

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
