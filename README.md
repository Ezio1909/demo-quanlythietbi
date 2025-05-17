# Device Management System

A Java Swing-based application for managing company devices and their assignments to employees.

## Features

- Device Management (CRUD operations)
- Employee Management
- Device Assignment System
- Assignment Expiration Tracking
- Device Maintenance Tracking
- Dashboard with Key Metrics
- Report Generation (PDF/Excel)
- Search and Filter Capabilities

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Building the Application

1. Clone the repository
2. Navigate to the project directory
3. Build the project using Maven:
```bash
mvn clean package
```

## Running the Application

After building, you can run the application using:
```bash
java -jar target/device-management-system-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── devicemanagement/
│   │           ├── Main.java
│   │           ├── dao/
│   │           │   └── DataStore.java
│   │           ├── model/
│   │           │   ├── Device.java
│   │           │   ├── Employee.java
│   │           │   └── Assignment.java
│   │           └── ui/
│   │               ├── MainFrame.java
│   │               ├── DashboardPanel.java
│   │               └── DeviceManagementPanel.java
│   └── resources/
└── test/
    └── java/
```

## Features in Detail

### Device Management
- Add new devices
- Edit device information
- Delete devices
- Track device status
- Manage device categories

### Assignment Management
- Assign devices to employees
- Track assignment duration
- Monitor assignment expiration
- Handle device returns

### Dashboard
- Total devices count
- Assigned devices count
- Available devices count
- Devices in maintenance
- Expired assignments
- Assignments expiring soon

### Reports
- Generate PDF reports
- Export data to Excel
- Device history tracking
- Assignment history

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
