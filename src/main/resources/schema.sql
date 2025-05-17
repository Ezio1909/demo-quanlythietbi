CREATE TABLE IF NOT EXISTS devices (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL DEFAULT 'Available'
);

CREATE TABLE IF NOT EXISTS employees (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS device_assignments (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    employee_id INTEGER NOT NULL,
    device_id INTEGER NOT NULL,
    assigned_at TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'Active',
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (device_id) REFERENCES devices(id)
);

CREATE TABLE IF NOT EXISTS device_maintenance (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    device_id INTEGER NOT NULL,
    maintenance_type VARCHAR(50) NOT NULL,
    description TEXT,
    reported_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    scheduled_for TIMESTAMP,
    completed_at TIMESTAMP,
    cost DECIMAL(10,2),
    status VARCHAR(50) NOT NULL DEFAULT 'Pending',
    notes TEXT,
    FOREIGN KEY (device_id) REFERENCES devices(id)
); 