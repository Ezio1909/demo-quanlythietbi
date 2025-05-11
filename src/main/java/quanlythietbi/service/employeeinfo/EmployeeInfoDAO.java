package quanlythietbi.service.employeeinfo;

import quanlythietbi.entity.EmployeeInfoRecord;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeInfoDAO {
    List<EmployeeInfoRecord> findAll() throws SQLException;

    EmployeeInfoRecord findById(int id) throws SQLException;

    void insert(EmployeeInfoRecord employee) throws SQLException;

    void deleteById(int id) throws SQLException;

    void update(EmployeeInfoRecord employee) throws SQLException;
}
