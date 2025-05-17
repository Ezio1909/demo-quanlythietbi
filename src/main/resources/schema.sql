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
    condition VARCHAR(50) DEFAULT 'New',
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