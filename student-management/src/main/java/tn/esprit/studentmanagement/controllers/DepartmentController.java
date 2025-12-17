package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.services.DepartmentService;
import tn.esprit.studentmanagement.services.IDepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/Depatment")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class DepartmentController {
    private static final Logger logger = LogManager.getLogger(DepartmentController.class);

    private IDepartmentService departmentService;

    @GetMapping("/getAllDepartment")
    public List<Department> getAllDepartment() {
        logger.info("GET /Depatment/getAllDepartment");
        return departmentService.getAllDepartments();
    }

    @GetMapping("/getDepartment/{id}")
    public Department getDepartment(@PathVariable Long id) {
        logger.info("GET /Depatment/getDepartment/{}", id);
        return departmentService.getDepartmentById(id);
    }

    @PostMapping("/createDepartment")
    public Department createDepartment(@RequestBody Department department) {
        logger.info("POST /Depatment/createDepartment - {}", department);
        return departmentService.saveDepartment(department);
    }

    @PutMapping("/updateDepartment")
    public Department updateDepartment(@RequestBody Department department) {
        logger.info("PUT /Depatment/updateDepartment - {}", department);
        return departmentService.saveDepartment(department);
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        logger.info("DELETE /Depatment/deleteDepartment/{}", id);
        departmentService.deleteDepartment(id);
    }
}
