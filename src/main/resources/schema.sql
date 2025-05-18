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

-- Insert sample employees
INSERT INTO employees (name, email, department, position, phone, hire_date, employee_id_number, status) VALUES
('John Smith', 'john.smith@company.com', 'IT', 'Senior Developer', '123-456-7890', '2022-01-15', 'EMP001', 'Active'),
('Sarah Johnson', 'sarah.j@company.com', 'HR', 'HR Manager', '123-456-7891', '2021-03-20', 'EMP002', 'Active'),
('Michael Chen', 'michael.c@company.com', 'Engineering', 'Lead Engineer', '123-456-7892', '2021-06-10', 'EMP003', 'Active'),
('Emily Davis', 'emily.d@company.com', 'Marketing', 'Marketing Director', '123-456-7893', '2022-02-01', 'EMP004', 'Active'),
('David Wilson', 'david.w@company.com', 'Sales', 'Sales Manager', '123-456-7894', '2021-09-15', 'EMP005', 'Active'),
('Lisa Brown', 'lisa.b@company.com', 'IT', 'System Administrator', '123-456-7895', '2022-04-01', 'EMP006', 'Active'),
('James Taylor', 'james.t@company.com', 'Finance', 'Financial Analyst', '123-456-7896', '2021-11-20', 'EMP007', 'Active'),
('Emma White', 'emma.w@company.com', 'HR', 'HR Specialist', '123-456-7897', '2022-03-10', 'EMP008', 'Active'),
('Robert Martinez', 'robert.m@company.com', 'Engineering', 'Software Engineer', '123-456-7898', '2021-07-15', 'EMP009', 'Active'),
('Jennifer Lee', 'jennifer.l@company.com', 'Marketing', 'Content Manager', '123-456-7899', '2022-01-05', 'EMP010', 'Active'),
('William Anderson', 'william.a@company.com', 'IT', 'Network Engineer', '123-456-7900', '2021-08-20', 'EMP011', 'Active'),
('Maria Garcia', 'maria.g@company.com', 'Sales', 'Sales Representative', '123-456-7901', '2022-02-15', 'EMP012', 'Active'),
('Thomas Moore', 'thomas.m@company.com', 'Engineering', 'QA Engineer', '123-456-7902', '2021-10-01', 'EMP013', 'Active'),
('Jessica Wright', 'jessica.w@company.com', 'Finance', 'Accountant', '123-456-7903', '2022-03-20', 'EMP014', 'Active'),
('Daniel Kim', 'daniel.k@company.com', 'IT', 'Security Specialist', '123-456-7904', '2021-12-10', 'EMP015', 'Active'),
('Rachel Adams', 'rachel.a@company.com', 'HR', 'Recruitment Specialist', '123-456-7905', '2022-01-20', 'EMP016', 'Active'),
('Kevin Patel', 'kevin.p@company.com', 'Engineering', 'DevOps Engineer', '123-456-7906', '2021-09-01', 'EMP017', 'Active'),
('Amanda Clark', 'amanda.c@company.com', 'Marketing', 'Digital Marketing', '123-456-7907', '2022-04-05', 'EMP018', 'Active'),
('Christopher Lee', 'chris.l@company.com', 'Sales', 'Account Manager', '123-456-7908', '2021-11-15', 'EMP019', 'Active'),
('Michelle Wang', 'michelle.w@company.com', 'IT', 'Data Analyst', '123-456-7909', '2022-02-10', 'EMP020', 'Active'),
('Brian Turner', 'brian.t@company.com', 'Finance', 'Financial Controller', '123-456-7910', '2021-07-01', 'EMP021', 'Active'),
('Laura Martinez', 'laura.m@company.com', 'HR', 'Benefits Coordinator', '123-456-7911', '2022-03-15', 'EMP022', 'Active'),
('Steven Chang', 'steven.c@company.com', 'Engineering', 'Systems Engineer', '123-456-7912', '2021-10-20', 'EMP023', 'Active'),
('Nicole Taylor', 'nicole.t@company.com', 'Marketing', 'Brand Manager', '123-456-7913', '2022-01-10', 'EMP024', 'Active'),
('Andrew Wilson', 'andrew.w@company.com', 'IT', 'IT Support', '123-456-7914', '2021-08-15', 'EMP025', 'Active'),
('Catherine Brown', 'catherine.b@company.com', 'Sales', 'Sales Analyst', '123-456-7915', '2022-02-20', 'EMP026', 'Active'),
('Richard Davis', 'richard.d@company.com', 'Engineering', 'Product Engineer', '123-456-7916', '2021-12-01', 'EMP027', 'Active'),
('Sandra Miller', 'sandra.m@company.com', 'Finance', 'Budget Analyst', '123-456-7917', '2022-04-10', 'EMP028', 'Active'),
('Joseph Kim', 'joseph.k@company.com', 'IT', 'Cloud Engineer', '123-456-7918', '2021-09-20', 'EMP029', 'Active'),
('Elizabeth Chen', 'elizabeth.c@company.com', 'HR', 'Training Coordinator', '123-456-7919', '2022-01-25', 'EMP030', 'Active');

-- Insert sample devices
INSERT INTO devices (name, type, serial_number, status, purchase_date, purchase_price, supplier, warranty_expiry, model, manufacturer, specifications, firmware_version, asset_tag, location, department, category, condition) VALUES
('ThinkPad X1 Carbon', 'Laptop', 'LPT001', 'Available', '2023-01-15', 1499.99, 'Lenovo', '2026-01-15', 'X1 Carbon Gen 9', 'Lenovo', 'Intel i7, 16GB RAM, 512GB SSD', '1.2.3', 'IT001', 'Office 101', 'IT', 'Computers', 'New'),
('MacBook Pro 16"', 'Laptop', 'LPT002', 'Available', '2023-02-20', 2499.99, 'Apple', '2026-02-20', 'MacBook Pro 2023', 'Apple', 'M2 Pro, 32GB RAM, 1TB SSD', '13.4.1', 'IT002', 'Office 102', 'Engineering', 'Computers', 'New'),
('Dell XPS 15', 'Laptop', 'LPT003', 'In Use', '2023-03-10', 1799.99, 'Dell', '2026-03-10', 'XPS 15 9520', 'Dell', 'Intel i9, 32GB RAM, 1TB SSD', '1.3.0', 'IT003', 'Office 103', 'Marketing', 'Computers', 'New'),
('Surface Pro 8', 'Tablet', 'TAB001', 'Available', '2023-04-05', 1299.99, 'Microsoft', '2026-04-05', 'Surface Pro 8', 'Microsoft', 'Intel i5, 16GB RAM, 256GB SSD', '22H2', 'IT004', 'Office 201', 'Sales', 'Tablets', 'New'),
('iPad Pro 12.9"', 'Tablet', 'TAB002', 'In Use', '2023-05-15', 1099.99, 'Apple', '2026-05-15', 'iPad Pro 2023', 'Apple', 'M2, 8GB RAM, 256GB Storage', '16.5', 'IT005', 'Office 202', 'Design', 'Tablets', 'New'),
('iPhone 14 Pro', 'Phone', 'PHN001', 'Available', '2023-06-20', 999.99, 'Apple', '2025-06-20', 'iPhone 14 Pro', 'Apple', '256GB Storage, 5G', '16.5', 'IT006', 'Office 301', 'Sales', 'Phones', 'New'),
('Samsung S23 Ultra', 'Phone', 'PHN002', 'In Use', '2023-07-10', 1199.99, 'Samsung', '2025-07-10', 'Galaxy S23 Ultra', 'Samsung', '512GB Storage, 5G', 'OneUI 5.1', 'IT007', 'Office 302', 'Marketing', 'Phones', 'New'),
('Dell U2723QE', 'Monitor', 'MON001', 'Available', '2023-08-15', 699.99, 'Dell', '2026-08-15', 'UltraSharp U2723QE', 'Dell', '27" 4K USB-C Hub Monitor', '1.0.2', 'IT008', 'Office 101', 'IT', 'Monitors', 'New'),
('LG 34WN80C-B', 'Monitor', 'MON002', 'Available', '2023-09-20', 799.99, 'LG', '2026-09-20', '34WN80C-B', 'LG', '34" UltraWide USB-C Monitor', '2.1.0', 'IT009', 'Office 102', 'Design', 'Monitors', 'New'),
('Logitech MX Master 3', 'Mouse', 'MOU001', 'In Use', '2023-10-05', 99.99, 'Logitech', '2025-10-05', 'MX Master 3', 'Logitech', 'Wireless Mouse', '12.4', 'IT010', 'Office 201', 'Engineering', 'Accessories', 'New'),
('HP Color LaserJet Pro', 'Printer', 'PRN001', 'Available', '2023-11-10', 499.99, 'HP', '2026-11-10', 'M479fdw', 'HP', 'Color LaserJet All-in-One', '2.0.8', 'IT011', 'Office 301', 'Office', 'Printers', 'New'),
('Cisco Meraki MR46', 'Network', 'NET001', 'In Use', '2023-12-15', 899.99, 'Cisco', '2026-12-15', 'MR46', 'Cisco', 'WiFi 6 Access Point', '27.7.1', 'IT012', 'Server Room', 'IT', 'Network', 'New'),
('ThinkPad T14', 'Laptop', 'LPT004', 'Available', '2024-01-20', 1299.99, 'Lenovo', '2027-01-20', 'T14 Gen 2', 'Lenovo', 'Intel i5, 16GB RAM, 512GB SSD', '1.1.5', 'IT013', 'Office 104', 'Finance', 'Computers', 'New'),
('MacBook Air', 'Laptop', 'LPT005', 'In Use', '2024-02-01', 1199.99, 'Apple', '2027-02-01', 'MacBook Air 2023', 'Apple', 'M2, 16GB RAM, 512GB SSD', '13.4.1', 'IT014', 'Office 105', 'Marketing', 'Computers', 'New'),
('Dell Precision 5570', 'Laptop', 'LPT006', 'Available', '2024-02-15', 1999.99, 'Dell', '2027-02-15', 'Precision 5570', 'Dell', 'Intel i7, 32GB RAM, 1TB SSD', '1.4.0', 'IT015', 'Office 106', 'Engineering', 'Computers', 'New'),
('Surface Laptop 5', 'Laptop', 'LPT007', 'In Use', '2024-03-01', 1399.99, 'Microsoft', '2027-03-01', 'Surface Laptop 5', 'Microsoft', 'Intel i7, 16GB RAM, 512GB SSD', '22H2', 'IT016', 'Office 107', 'Sales', 'Computers', 'New'),
('iPad Air', 'Tablet', 'TAB003', 'Available', '2024-03-15', 599.99, 'Apple', '2027-03-15', 'iPad Air 2024', 'Apple', 'M1, 8GB RAM, 256GB Storage', '16.5', 'IT017', 'Office 203', 'Marketing', 'Tablets', 'New'),
('Samsung Tab S9', 'Tablet', 'TAB004', 'In Use', '2024-04-01', 899.99, 'Samsung', '2027-04-01', 'Galaxy Tab S9', 'Samsung', '12GB RAM, 256GB Storage', 'OneUI 5.1', 'IT018', 'Office 204', 'Sales', 'Tablets', 'New'),
('iPhone 15', 'Phone', 'PHN003', 'Available', '2024-04-15', 899.99, 'Apple', '2026-04-15', 'iPhone 15', 'Apple', '256GB Storage, 5G', '17.0', 'IT019', 'Office 303', 'IT', 'Phones', 'New'),
('Google Pixel 8', 'Phone', 'PHN004', 'In Use', '2024-05-01', 799.99, 'Google', '2026-05-01', 'Pixel 8', 'Google', '256GB Storage, 5G', 'Android 14', 'IT020', 'Office 304', 'Marketing', 'Phones', 'New'),
('Dell P2723QE', 'Monitor', 'MON003', 'Available', '2024-05-15', 499.99, 'Dell', '2027-05-15', 'P2723QE', 'Dell', '27" 4K USB-C Monitor', '1.1.0', 'IT021', 'Office 103', 'Engineering', 'Monitors', 'New'),
('Samsung G9', 'Monitor', 'MON004', 'In Use', '2024-06-01', 1499.99, 'Samsung', '2027-06-01', 'Odyssey G9', 'Samsung', '49" Super Ultra-Wide', '1.2.1', 'IT022', 'Office 104', 'Design', 'Monitors', 'New'),
('Logitech MX Keys', 'Keyboard', 'KEY001', 'Available', '2024-06-15', 119.99, 'Logitech', '2026-06-15', 'MX Keys', 'Logitech', 'Wireless Keyboard', '11.3', 'IT023', 'Office 202', 'IT', 'Accessories', 'New'),
('Brother MFC-L9570CDW', 'Printer', 'PRN002', 'In Use', '2024-07-01', 799.99, 'Brother', '2027-07-01', 'MFC-L9570CDW', 'Brother', 'Color Laser All-in-One', '1.2.3', 'IT024', 'Office 302', 'Office', 'Printers', 'New'),
('Cisco Catalyst 9200', 'Network', 'NET002', 'Available', '2024-07-15', 1999.99, 'Cisco', '2027-07-15', 'C9200-24P', 'Cisco', '24-Port PoE Switch', '17.6.1', 'IT025', 'Server Room', 'IT', 'Network', 'New'),
('ThinkPad P1', 'Laptop', 'LPT008', 'In Use', '2024-08-01', 2299.99, 'Lenovo', '2027-08-01', 'P1 Gen 5', 'Lenovo', 'Intel i9, 32GB RAM, 1TB SSD', '1.2.0', 'IT026', 'Office 108', 'Engineering', 'Computers', 'New'),
('MacBook Pro 14"', 'Laptop', 'LPT009', 'Available', '2024-08-15', 1999.99, 'Apple', '2027-08-15', 'MacBook Pro 2024', 'Apple', 'M3 Pro, 32GB RAM, 1TB SSD', '14.0', 'IT027', 'Office 109', 'Design', 'Computers', 'New'),
('Dell Latitude 7430', 'Laptop', 'LPT010', 'In Use', '2024-09-01', 1599.99, 'Dell', '2027-09-01', 'Latitude 7430', 'Dell', 'Intel i7, 16GB RAM, 512GB SSD', '1.5.0', 'IT028', 'Office 110', 'Sales', 'Computers', 'New'),
('Surface Studio', 'Desktop', 'DSK001', 'Available', '2024-09-15', 3999.99, 'Microsoft', '2027-09-15', 'Surface Studio 2+', 'Microsoft', 'Intel i7, 32GB RAM, 1TB SSD', '22H2', 'IT029', 'Office 205', 'Design', 'Computers', 'New'),
('Mac Studio', 'Desktop', 'DSK002', 'In Use', '2024-10-01', 3999.99, 'Apple', '2027-10-01', 'Mac Studio 2023', 'Apple', 'M2 Ultra, 64GB RAM, 2TB SSD', '14.0', 'IT030', 'Office 206', 'Engineering', 'Computers', 'New');

-- Insert sample device assignments (mix of active and returned devices)
INSERT INTO device_assignments (employee_id, device_id, assigned_at, returned_at, status) VALUES
(1, 3, '2023-03-15 09:00:00', NULL, 'Active'),
(2, 7, '2023-04-01 10:30:00', NULL, 'Active'),
(3, 10, '2023-04-15 11:45:00', NULL, 'Active'),
(4, 12, '2023-05-01 13:15:00', NULL, 'Active'),
(5, 14, '2023-05-15 14:30:00', NULL, 'Active'),
(6, 17, '2023-06-01 15:45:00', NULL, 'Active'),
(7, 19, '2023-06-15 16:00:00', NULL, 'Active'),
(8, 22, '2023-07-01 09:30:00', NULL, 'Active'),
(9, 24, '2023-07-15 10:45:00', NULL, 'Active'),
(10, 26, '2023-08-01 11:15:00', NULL, 'Active'),
(11, 28, '2023-08-15 13:30:00', NULL, 'Active'),
(12, 30, '2023-09-01 14:45:00', NULL, 'Active'),
(13, 1, '2023-01-15 09:00:00', '2023-06-15 17:00:00', 'Returned'),
(14, 2, '2023-02-01 10:30:00', '2023-07-01 16:30:00', 'Returned'),
(15, 4, '2023-02-15 11:45:00', '2023-07-15 15:45:00', 'Returned'),
(16, 5, '2023-03-01 13:15:00', '2023-08-01 14:30:00', 'Returned'),
(17, 6, '2023-03-15 14:30:00', '2023-08-15 13:15:00', 'Returned'),
(18, 8, '2023-04-01 15:45:00', '2023-09-01 12:00:00', 'Returned'),
(19, 9, '2023-04-15 16:00:00', '2023-09-15 11:30:00', 'Returned'),
(20, 11, '2023-05-01 09:30:00', '2023-10-01 10:45:00', 'Returned'),
(21, 13, '2023-05-15 10:45:00', '2023-10-15 09:30:00', 'Returned'),
(22, 15, '2023-06-01 11:15:00', '2023-11-01 16:15:00', 'Returned'),
(23, 16, '2023-06-15 13:30:00', '2023-11-15 15:00:00', 'Returned'),
(24, 18, '2023-07-01 14:45:00', '2023-12-01 14:30:00', 'Returned'),
(25, 20, '2023-07-15 15:00:00', '2023-12-15 13:45:00', 'Returned'),
(26, 21, '2023-08-01 09:15:00', '2024-01-01 12:30:00', 'Returned'),
(27, 23, '2023-08-15 10:30:00', '2024-01-15 11:15:00', 'Returned'),
(28, 25, '2023-09-01 11:45:00', '2024-02-01 10:00:00', 'Returned'),
(29, 27, '2023-09-15 13:00:00', '2024-02-15 09:45:00', 'Returned'),
(30, 29, '2023-10-01 14:15:00', '2024-03-01 16:30:00', 'Returned'); 