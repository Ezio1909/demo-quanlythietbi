package quanlythietbi.service.employeeinfo;

import java.sql.SQLException;
import java.util.List;

import quanlythietbi.entity.EmployeeInfoRecord;

public interface EmployeeInfoDAO {
    List<EmployeeInfoRecord> findAll() throws SQLException;

    EmployeeInfoRecord findById(int id) throws SQLException;

    void insert(EmployeeInfoRecord employee) throws SQLException;

    void deleteById(int id) throws SQLException;

    void update(EmployeeInfoRecord employee) throws SQLException;
}
