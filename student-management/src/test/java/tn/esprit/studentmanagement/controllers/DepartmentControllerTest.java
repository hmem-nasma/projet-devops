package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private IDepartmentService departmentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Test 1: GET /Depatment/getAllDepartment - Liste de départements
    @Test
    void getAllDepartment_ShouldReturnListOfDepartments() throws Exception {
        // Arrange
        Department dept1 = new Department();
        dept1.setIdDepartment(1L);
        dept1.setName("Computer Science");
        dept1.setLocation("Building A");
        dept1.setPhone("123-456-7890");
        dept1.setHead("Dr. Smith");
        
        Department dept2 = new Department();
        dept2.setIdDepartment(2L);
        dept2.setName("Mathematics");
        dept2.setLocation("Building B");
        dept2.setPhone("098-765-4321");
        dept2.setHead("Dr. Johnson");
        
        List<Department> departments = Arrays.asList(dept1, dept2);
        when(departmentService.getAllDepartments()).thenReturn(departments);
        
        // Act & Assert
        mockMvc.perform(get("/Depatment/getAllDepartment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idDepartment").value(1))
                .andExpect(jsonPath("$[0].name").value("Computer Science"))
                .andExpect(jsonPath("$[0].location").value("Building A"))
                .andExpect(jsonPath("$[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[0].head").value("Dr. Smith"))
                .andExpect(jsonPath("$[1].idDepartment").value(2))
                .andExpect(jsonPath("$[1].name").value("Mathematics"))
                .andExpect(jsonPath("$[1].location").value("Building B"))
                .andExpect(jsonPath("$[1].phone").value("098-765-4321"))
                .andExpect(jsonPath("$[1].head").value("Dr. Johnson"));
        
        verify(departmentService).getAllDepartments();
    }
    
    // Test 2: GET /Depatment/getAllDepartment - Liste vide
    @Test
    void getAllDepartment_ShouldReturnEmptyList_WhenNoDepartments() throws Exception {
        // Arrange
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList());
        
        // Act & Assert
        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        
        verify(departmentService).getAllDepartments();
    }
    
    // Test 3: GET /Depatment/getDepartment/{id} - Département trouvé
    @Test
    void getDepartment_ShouldReturnDepartment_WhenExists() throws Exception {
        // Arrange
        Long departmentId = 1L;
        Department department = new Department();
        department.setIdDepartment(departmentId);
        department.setName("Computer Science");
        department.setLocation("Building A");
        department.setPhone("123-456-7890");
        department.setHead("Dr. Smith");
        
        when(departmentService.getDepartmentById(departmentId)).thenReturn(department);
        
        // Act & Assert
        mockMvc.perform(get("/Depatment/getDepartment/{id}", departmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(departmentId))
                .andExpect(jsonPath("$.name").value("Computer Science"))
                .andExpect(jsonPath("$.location").value("Building A"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.head").value("Dr. Smith"));
        
        verify(departmentService).getDepartmentById(departmentId);
    }
    
    // Test 4: GET /Depatment/getDepartment/{id} - Département non trouvé
    @Test
    void getDepartment_ShouldReturnNull_WhenNotExists() throws Exception {
        // Arrange
        Long departmentId = 999L;
        when(departmentService.getDepartmentById(departmentId)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/Depatment/getDepartment/{id}", departmentId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        
        verify(departmentService).getDepartmentById(departmentId);
    }
    
    // Test 5: POST /Depatment/createDepartment - Créer un département
    @Test
    void createDepartment_ShouldReturnCreatedDepartment() throws Exception {
        // Arrange
        Department departmentToCreate = new Department();
        departmentToCreate.setName("Physics");
        departmentToCreate.setLocation("Building C");
        departmentToCreate.setPhone("555-123-4567");
        departmentToCreate.setHead("Dr. Einstein");
        
        Department createdDepartment = new Department();
        createdDepartment.setIdDepartment(1L);
        createdDepartment.setName("Physics");
        createdDepartment.setLocation("Building C");
        createdDepartment.setPhone("555-123-4567");
        createdDepartment.setHead("Dr. Einstein");
        
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(createdDepartment);
        
        // Act & Assert
        mockMvc.perform(post("/Depatment/createDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(1))
                .andExpect(jsonPath("$.name").value("Physics"))
                .andExpect(jsonPath("$.location").value("Building C"))
                .andExpect(jsonPath("$.phone").value("555-123-4567"))
                .andExpect(jsonPath("$.head").value("Dr. Einstein"));
        
        verify(departmentService).saveDepartment(any(Department.class));
    }
    
    // Test 6: PUT /Depatment/updateDepartment - Mettre à jour un département
    @Test
    void updateDepartment_ShouldReturnUpdatedDepartment() throws Exception {
        // Arrange
        Department departmentToUpdate = new Department();
        departmentToUpdate.setIdDepartment(1L);
        departmentToUpdate.setName("Computer Science Updated");
        departmentToUpdate.setLocation("Building A - Floor 2");
        departmentToUpdate.setPhone("111-222-3333");
        departmentToUpdate.setHead("Dr. Smith Updated");
        
        Department updatedDepartment = new Department();
        updatedDepartment.setIdDepartment(1L);
        updatedDepartment.setName("Computer Science Updated");
        updatedDepartment.setLocation("Building A - Floor 2");
        updatedDepartment.setPhone("111-222-3333");
        updatedDepartment.setHead("Dr. Smith Updated");
        
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(updatedDepartment);
        
        // Act & Assert
        mockMvc.perform(put("/Depatment/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(1))
                .andExpect(jsonPath("$.name").value("Computer Science Updated"))
                .andExpect(jsonPath("$.location").value("Building A - Floor 2"))
                .andExpect(jsonPath("$.phone").value("111-222-3333"))
                .andExpect(jsonPath("$.head").value("Dr. Smith Updated"));
        
        verify(departmentService).saveDepartment(any(Department.class));
    }
    
    // Test 7: DELETE /Depatment/deleteDepartment/{id} - Supprimer un département
    @Test
    void deleteDepartment_ShouldCallServiceDelete() throws Exception {
        // Arrange
        Long departmentId = 1L;
        doNothing().when(departmentService).deleteDepartment(departmentId);
        
        // Act & Assert
        mockMvc.perform(delete("/Depatment/deleteDepartment/{id}", departmentId))
                .andExpect(status().isOk());
        
        verify(departmentService).deleteDepartment(departmentId);
    }
    
    // Test 8: Vérifier les en-têtes CORS
    @Test
    void shouldAllowCorsFromLocalhost4200() throws Exception {
        // Arrange
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList());
        
        // Act & Assert
        mockMvc.perform(get("/Depatment/getAllDepartment")
                .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
    
    // Test 9: Test avec JSON minimal
    @Test
    void createDepartment_ShouldWorkWithMinimalData() throws Exception {
        // Arrange
        String minimalJson = "{\"name\":\"Test Dept\",\"location\":\"Test Building\"}";
        
        Department savedDept = new Department();
        savedDept.setIdDepartment(1L);
        savedDept.setName("Test Dept");
        savedDept.setLocation("Test Building");
        
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(savedDept);
        
        // Act & Assert
        mockMvc.perform(post("/Depatment/createDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(minimalJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(1))
                .andExpect(jsonPath("$.name").value("Test Dept"))
                .andExpect(jsonPath("$.location").value("Test Building"));
    }
    
    // Test 10: Vérifier que le logger est utilisé (test indirect)
    @Test
    void getAllDepartment_ShouldCallServiceAndReturn() throws Exception {
        // Arrange
        Department dept = new Department();
        dept.setIdDepartment(1L);
        dept.setName("Test");
        
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(dept));
        
        // Act & Assert
        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk());
        
        // Le logger dans le controller sera appelé mais difficile à tester directement
        verify(departmentService).getAllDepartments();
    }
}