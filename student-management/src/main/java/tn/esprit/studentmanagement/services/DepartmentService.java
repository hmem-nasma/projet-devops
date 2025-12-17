package tn.esprit.studentmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Service

public class DepartmentService implements IDepartmentService {
    private static final Logger logger = LogManager.getLogger(DepartmentService.class);

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        logger.info("Fetching all departments");
        List<Department> list = departmentRepository.findAll();
        logger.debug("Found {} departments", list == null ? 0 : list.size());
        return list;
    }

    @Override
    public Department getDepartmentById(Long idDepartment) {
        logger.info("Fetching department by id {}", idDepartment);
        try {
            Department dept = departmentRepository.findById(idDepartment).get();
            logger.debug("Found department: {}", dept);
            return dept;
        } catch (Exception e) {
            logger.error("Error fetching department id {}", idDepartment, e);
            throw e;
        }
    }

    @Override
    public Department saveDepartment(Department department) {
        logger.info("Saving department: {}", department);
        try {
            Department saved = departmentRepository.save(department);
            logger.debug("Saved department id {}", saved == null ? "null" : saved.getIdDepartment());
            return saved;
        } catch (Exception e) {
            logger.error("Error saving department {}", department, e);
            throw e;
        }
    }

    @Override
    public void deleteDepartment(Long idDepartment) {
        logger.info("Deleting department id {}", idDepartment);
        try {
            departmentRepository.deleteById(idDepartment);
            logger.debug("Deleted department id {}", idDepartment);
        } catch (Exception e) {
            logger.error("Error deleting department id {}", idDepartment, e);
            throw e;
        }
    }
}
