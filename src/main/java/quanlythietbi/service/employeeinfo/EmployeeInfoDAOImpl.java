package quanlythietbi.service.employeeinfo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import quanlythietbi.connector.IConnectionManager;
import quanlythietbi.entity.EmployeeInfoRecord;

public class EmployeeInfoDAOImpl implements EmployeeInfoDAO {

    private final IConnectionManager connectionManager;

    public EmployeeInfoDAOImpl(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    private static final String COL_EMPLOYEE_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_DEPARTMENT = "department";
    private static final String COL_POSITION = "position";
    private static final String COL_PHONE = "phone";
    private static final String COL_HIRE_DATE = "hire_date";
    private static final String COL_EMPLOYEE_ID_NUMBER = "employee_id_number";
    private static final String COL_MANAGER_ID = "manager_id";
    private static final String COL_STATUS = "status";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private EmployeeInfoRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        return new EmployeeInfoRecord(
            rs.getInt(COL_EMPLOYEE_ID),
            rs.getString(COL_NAME),
            rs.getString(COL_EMAIL),
            rs.getString(COL_DEPARTMENT),
            rs.getString(COL_POSITION),
            rs.getString(COL_PHONE),
            rs.getDate(COL_HIRE_DATE) != null ? rs.getDate(COL_HIRE_DATE).toLocalDate() : null,
            rs.getString(COL_EMPLOYEE_ID_NUMBER),
            rs.getObject(COL_MANAGER_ID) != null ? rs.getInt(COL_MANAGER_ID) : null,
            rs.getString(COL_STATUS),
            rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
            rs.getTimestamp(COL_UPDATED_AT).toLocalDateTime()
        );
    }

    @Override
    public List<EmployeeInfoRecord> findAll() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<EmployeeInfoRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public EmployeeInfoRecord findById(int id) throws SQLException {
        return connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRecord(rs);
            }
            return null;
        });
    }

    @Override
    public void insert(EmployeeInfoRecord employee) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO employees (
                    name, email, department, position, phone,
                    hire_date, employee_id_number, manager_id, status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """);
            int paramIndex = 1;
            stmt.setString(paramIndex++, employee.name());
            stmt.setString(paramIndex++, employee.email());
            stmt.setString(paramIndex++, employee.department());
            stmt.setString(paramIndex++, employee.position());
            stmt.setString(paramIndex++, employee.phone());
            if (employee.hireDate() != null) {
                stmt.setDate(paramIndex++, Date.valueOf(employee.hireDate()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            stmt.setString(paramIndex++, employee.employeeIdNumber());
            if (employee.managerId() != null) {
                stmt.setInt(paramIndex++, employee.managerId());
            } else {
                stmt.setNull(paramIndex++, Types.INTEGER);
            }
            stmt.setString(paramIndex++, employee.status() != null ? employee.status() : "Active");
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void update(EmployeeInfoRecord employee) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                UPDATE employees SET 
                    name = ?, email = ?, department = ?, position = ?, phone = ?,
                    hire_date = ?, employee_id_number = ?, manager_id = ?, status = ?
                WHERE id = ?
            """);
            int paramIndex = 1;
            stmt.setString(paramIndex++, employee.name());
            stmt.setString(paramIndex++, employee.email());
            stmt.setString(paramIndex++, employee.department());
            stmt.setString(paramIndex++, employee.position());
            stmt.setString(paramIndex++, employee.phone());
            if (employee.hireDate() != null) {
                stmt.setDate(paramIndex++, Date.valueOf(employee.hireDate()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            stmt.setString(paramIndex++, employee.employeeIdNumber());
            if (employee.managerId() != null) {
                stmt.setInt(paramIndex++, employee.managerId());
            } else {
                stmt.setNull(paramIndex++, Types.INTEGER);
            }
            stmt.setString(paramIndex++, employee.status() != null ? employee.status() : "Active");
            stmt.setInt(paramIndex++, employee.id());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void deleteById(int id) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM employees WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return null;
        });
    }
}