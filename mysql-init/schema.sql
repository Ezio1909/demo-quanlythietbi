-- DEV/DEMO RESET: Truncate all tables and reset auto-increment counters
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE device_assignments;
TRUNCATE TABLE device_maintenance;
TRUNCATE TABLE devices;
TRUNCATE TABLE employees;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS devices (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL DEFAULT 'Available',
    
    -- Purchase Information
    purchase_date DATE,
    purchase_price DECIMAL(10,2),
    supplier VARCHAR(255),
    warranty_expiry DATE,
    
    -- Technical Information
    model VARCHAR(100),
    manufacturer VARCHAR(100),
    specifications TEXT,
    firmware_version VARCHAR(50),
    
    -- Asset Management
    asset_tag VARCHAR(50) UNIQUE,
    location VARCHAR(100),
    department VARCHAR(100),
    category VARCHAR(50),
    
    -- Lifecycle Management
    last_inspection_date DATE,
    next_inspection_date DATE,
    end_of_life_date DATE,
    device_condition VARCHAR(50) DEFAULT 'New',
    notes TEXT,
    
    -- Timestamps and Audit
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    last_modified_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS employees (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    phone VARCHAR(20),
    hire_date DATE,
    employee_id_number VARCHAR(50) UNIQUE,
    manager_id INTEGER,
    status VARCHAR(50) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (manager_id) REFERENCES employees(id)
);

CREATE TABLE IF NOT EXISTS device_assignments (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    employee_id INTEGER NOT NULL,
    device_id INTEGER NOT NULL,
    assigned_at TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,
    expiration_date TIMESTAMP NOT NULL,
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

-- Insert unique sample employees with explicit IDs
INSERT INTO employees (id, name, email, department, position, phone, hire_date, employee_id_number, status) VALUES
(1, 'John Smith', 'john.smith1@company.com', 'IT', 'Senior Developer', '123-456-7890', '2022-01-15', 'EMP001', 'Active'),
(2, 'Sarah Johnson', 'sarah.j2@company.com', 'HR', 'HR Manager', '123-456-7891', '2021-03-20', 'EMP002', 'Active'),
(3, 'Michael Chen', 'michael.c3@company.com', 'Engineering', 'Lead Engineer', '123-456-7892', '2021-06-10', 'EMP003', 'Active'),
(4, 'Emily Davis', 'emily.d4@company.com', 'Marketing', 'Marketing Director', '123-456-7893', '2022-02-01', 'EMP004', 'Active'),
(5, 'David Wilson', 'david.w5@company.com', 'Sales', 'Sales Manager', '123-456-7894', '2021-09-15', 'EMP005', 'Active'),
(6, 'Lisa Brown', 'lisa.b6@company.com', 'IT', 'System Administrator', '123-456-7895', '2022-04-01', 'EMP006', 'Active'),
(7, 'James Taylor', 'james.t7@company.com', 'Finance', 'Financial Analyst', '123-456-7896', '2021-11-20', 'EMP007', 'Active'),
(8, 'Emma White', 'emma.w8@company.com', 'HR', 'HR Specialist', '123-456-7897', '2022-03-10', 'EMP008', 'Active'),
(9, 'Robert Martinez', 'robert.m9@company.com', 'Engineering', 'Software Engineer', '123-456-7898', '2021-07-15', 'EMP009', 'Active'),
(10, 'Jennifer Lee', 'jennifer.l10@company.com', 'Marketing', 'Content Manager', '123-456-7899', '2022-01-05', 'EMP010', 'Active');

-- Insert unique sample devices with explicit IDs
INSERT INTO devices (id, name, type, serial_number, status, purchase_date, purchase_price, supplier, warranty_expiry, model, manufacturer, specifications, firmware_version, asset_tag, location, department, category, device_condition) VALUES
(1, 'ThinkPad X1 Carbon', 'Laptop', 'LPT001-1', 'Available', '2023-01-15', 1499.99, 'Lenovo', '2026-01-15', 'X1 Carbon Gen 9', 'Lenovo', 'Intel i7, 16GB RAM, 512GB SSD', '1.2.3', 'IT001-1', 'Office 101', 'IT', 'Computers', 'New'),
(2, 'MacBook Pro 16"', 'Laptop', 'LPT002-2', 'Available', '2023-02-20', 2499.99, 'Apple', '2026-02-20', 'MacBook Pro 2023', 'Apple', 'M2 Pro, 32GB RAM, 1TB SSD', '13.4.1', 'IT002-2', 'Office 102', 'Engineering', 'Computers', 'New'),
(3, 'Dell XPS 15', 'Laptop', 'LPT003-3', 'In Use', '2023-03-10', 1799.99, 'Dell', '2026-03-10', 'XPS 15 9520', 'Dell', 'Intel i9, 32GB RAM, 1TB SSD', '1.3.0', 'IT003-3', 'Office 103', 'Marketing', 'Computers', 'New'),
(4, 'Surface Pro 8', 'Tablet', 'TAB001-4', 'Available', '2023-04-05', 1299.99, 'Microsoft', '2026-04-05', 'Surface Pro 8', 'Microsoft', 'Intel i5, 16GB RAM, 256GB SSD', '22H2', 'IT004-4', 'Office 201', 'Sales', 'Tablets', 'New'),
(5, 'iPad Pro 12.9"', 'Tablet', 'TAB002-5', 'In Use', '2023-05-15', 1099.99, 'Apple', '2026-05-15', 'iPad Pro 2023', 'Apple', 'M2, 8GB RAM, 256GB Storage', '16.5', 'IT005-5', 'Office 202', 'Design', 'Tablets', 'New'),
(6, 'iPhone 14 Pro', 'Phone', 'PHN001-6', 'Available', '2023-06-20', 999.99, 'Apple', '2025-06-20', 'iPhone 14 Pro', 'Apple', '256GB Storage, 5G', '16.5', 'IT006-6', 'Office 301', 'Sales', 'Phones', 'New'),
(7, 'Samsung S23 Ultra', 'Phone', 'PHN002-7', 'In Use', '2023-07-10', 1199.99, 'Samsung', '2025-07-10', 'Galaxy S23 Ultra', 'Samsung', '512GB Storage, 5G', 'OneUI 5.1', 'IT007-7', 'Office 302', 'Marketing', 'Phones', 'New'),
(8, 'Dell U2723QE', 'Monitor', 'MON001-8', 'Available', '2023-08-15', 699.99, 'Dell', '2026-08-15', 'UltraSharp U2723QE', 'Dell', '27" 4K USB-C Hub Monitor', '1.0.2', 'IT008-8', 'Office 101', 'IT', 'Monitors', 'New'),
(9, 'LG 34WN80C-B', 'Monitor', 'MON002-9', 'Available', '2023-09-20', 799.99, 'LG', '2026-09-20', '34WN80C-B', 'LG', '34" UltraWide USB-C Monitor', '2.1.0', 'IT009-9', 'Office 102', 'Design', 'Monitors', 'New'),
(10, 'Logitech MX Master 3', 'Mouse', 'MOU001-10', 'In Use', '2023-10-05', 99.99, 'Logitech', '2025-10-05', 'MX Master 3', 'Logitech', 'Wireless Mouse', '12.4', 'IT010-10', 'Office 201', 'Engineering', 'Accessories', 'New');

-- Insert sample device assignments (mix of active and returned devices) using valid IDs
INSERT INTO device_assignments (employee_id, device_id, assigned_at, returned_at, expiration_date, status) VALUES
(1, 3, '2023-03-15 09:00:00', NULL, '2023-06-15 17:00:00', 'Active'),
(2, 7, '2023-04-01 10:30:00', NULL, '2023-07-01 16:30:00', 'Active'),
(3, 10, '2023-04-15 11:45:00', NULL, '2023-09-15 11:30:00', 'Active'),
(4, 2, '2023-05-01 13:15:00', NULL, '2023-10-01 10:45:00', 'Active'),
(5, 4, '2023-05-15 14:30:00', NULL, '2023-10-15 09:30:00', 'Active'),
(6, 5, '2023-06-01 15:45:00', NULL, '2023-11-01 16:15:00', 'Active'),
(7, 6, '2023-06-15 16:00:00', NULL, '2023-12-01 14:30:00', 'Active'),
(8, 8, '2023-07-01 09:30:00', NULL, '2023-12-01 12:00:00', 'Active'),
(9, 9, '2023-07-15 10:45:00', NULL, '2024-01-01 12:30:00', 'Active'),
(10, 1, '2023-08-01 11:15:00', NULL, '2024-02-01 10:00:00', 'Active');

-- Insert sample maintenance records using valid device IDs
INSERT INTO device_maintenance (device_id, maintenance_type, description, reported_at, scheduled_for, completed_at, cost, status, notes) VALUES
(1, 'Hardware Repair', 'Replace faulty keyboard', '2023-12-01 09:00:00', '2023-12-02 10:00:00', '2023-12-02 11:30:00', 150.00, 'Completed', 'Keyboard replaced with new model KB-101'),
(3, 'Software Update', 'OS upgrade and security patches', '2023-12-05 14:00:00', '2023-12-06 09:00:00', '2023-12-06 10:15:00', 0.00, 'Completed', 'Updated to latest OS version'),
(5, 'Cleaning', 'Deep cleaning and sanitization', '2023-12-10 11:00:00', '2023-12-11 14:00:00', '2023-12-11 15:30:00', 50.00, 'Completed', 'Device thoroughly cleaned and sanitized'),
(7, 'Battery Replacement', 'Replace degraded battery', '2023-12-15 10:00:00', '2023-12-16 11:00:00', '2023-12-16 12:45:00', 200.00, 'Completed', 'New battery installed, old battery recycled'),
(9, 'Screen Repair', 'Fix screen flickering issue', '2023-12-20 13:00:00', '2023-12-21 09:00:00', '2023-12-21 11:00:00', 300.00, 'Completed', 'Screen replaced with new LCD panel');

-- TRIGGERS FOR DEVICE STATUS SYNCHRONIZATION

DELIMITER //
CREATE TRIGGER trg_assignment_insert
AFTER INSERT ON device_assignments
FOR EACH ROW
BEGIN
    IF NEW.status = 'Active' THEN
        UPDATE devices SET status = 'In Use' WHERE id = NEW.device_id;
    END IF;
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_assignment_returned
AFTER UPDATE ON device_assignments
FOR EACH ROW
BEGIN
    IF NEW.status = 'Returned' AND OLD.status != 'Returned' THEN
        UPDATE devices SET status = 'Available' WHERE id = NEW.device_id;
    END IF;
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_maintenance_insert
AFTER INSERT ON device_maintenance
FOR EACH ROW
BEGIN
    IF NEW.status IN ('Pending', 'Scheduled', 'In Progress') THEN
        UPDATE devices SET status = 'Maintenance' WHERE id = NEW.device_id;
    END IF;
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_maintenance_update_to_maintenance
AFTER UPDATE ON device_maintenance
FOR EACH ROW
BEGIN
    IF NEW.status IN ('Pending', 'Scheduled', 'In Progress') AND OLD.status != NEW.status THEN
        UPDATE devices SET status = 'Maintenance' WHERE id = NEW.device_id;
    END IF;
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_maintenance_completed
AFTER UPDATE ON device_maintenance
FOR EACH ROW
BEGIN
    IF NEW.status = 'Completed' AND OLD.status != 'Completed' THEN
        UPDATE devices SET status = 'Available' WHERE id = NEW.device_id;
    END IF;
END;//
DELIMITER ; 