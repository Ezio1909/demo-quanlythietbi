package quanlythietbi.service.employeeinfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public List<EmployeeInfoRecord> findAll() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<EmployeeInfoRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new EmployeeInfoRecord(
                    rs.getInt(COL_EMPLOYEE_ID),
                    rs.getString(COL_NAME),
                    rs.getString(COL_EMAIL),
                    rs.getString(COL_DEPARTMENT)
                ));
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
                return new EmployeeInfoRecord(
                    rs.getInt(COL_EMPLOYEE_ID),
                    rs.getString(COL_NAME),
                    rs.getString(COL_EMAIL),
                    rs.getString(COL_DEPARTMENT)
                );
            }
            return null;
        });
    }

    @Override
    public void insert(EmployeeInfoRecord employee) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO employees (name, email, department) VALUES (?, ?, ?)"
            );
            stmt.setString(1, employee.name());
            stmt.setString(2, employee.email());
            stmt.setString(3, employee.department());
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

    @Override
    public void update(EmployeeInfoRecord employee) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement(
            "UPDATE employees SET name = ?, email = ?, department = ? WHERE id = ?"
            );
            stmt.setString(1, employee.name());
            stmt.setString(2, employee.email());
            stmt.setString(3, employee.department());
            stmt.setInt(4, employee.id());
            stmt.executeUpdate();
            return null;
        });
    }
}