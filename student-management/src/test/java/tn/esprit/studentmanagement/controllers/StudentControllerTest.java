package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private IStudentService studentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Test 1: GET /students/getAllStudents - Liste d'étudiants
    @Test
    void getAllStudents_ShouldReturnListOfStudents() throws Exception {
        // Arrange
        Student student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@example.com");
        
        Student student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@example.com");
        
        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(students);
        
        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idStudent").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].idStudent").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
        
        verify(studentService).getAllStudents();
    }
    
    // Test 2: GET /students/getAllStudents - Liste vide
    @Test
    void getAllStudents_ShouldReturnEmptyList_WhenNoStudents() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(Arrays.asList());
        
        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        
        verify(studentService).getAllStudents();
    }
    
    // Test 3: GET /students/getStudent/{id} - Étudiant trouvé
    @Test
    void getStudent_ShouldReturnStudent_WhenExists() throws Exception {
        // Arrange
        Long studentId = 1L;
        Student student = new Student();
        student.setIdStudent(studentId);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        student.setPhone("1234567890");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setAddress("123 Main St");
        
        when(studentService.getStudentById(studentId)).thenReturn(student);
        
        // Act & Assert
        mockMvc.perform(get("/students/getStudent/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(studentId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
        
        verify(studentService).getStudentById(studentId);
    }
    
    // Test 4: GET /students/getStudent/{id} - Étudiant non trouvé (retourne null)
    @Test
    void getStudent_ShouldReturnNull_WhenNotExists() throws Exception {
        // Arrange
        Long studentId = 999L;
        when(studentService.getStudentById(studentId)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(get("/students/getStudent/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        
        verify(studentService).getStudentById(studentId);
    }
    
    // Test 5: POST /students/createStudent - Créer un étudiant
    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        // Arrange
        Student studentToCreate = new Student();
        studentToCreate.setFirstName("Alice");
        studentToCreate.setLastName("Johnson");
        studentToCreate.setEmail("alice.johnson@example.com");
        studentToCreate.setDateOfBirth(LocalDate.of(1999, 5, 15));
        
        Student createdStudent = new Student();
        createdStudent.setIdStudent(1L);
        createdStudent.setFirstName("Alice");
        createdStudent.setLastName("Johnson");
        createdStudent.setEmail("alice.johnson@example.com");
        createdStudent.setDateOfBirth(LocalDate.of(1999, 5, 15));
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(createdStudent);
        
        // Act & Assert
        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Johnson"))
                .andExpect(jsonPath("$.email").value("alice.johnson@example.com"));
        
        verify(studentService).saveStudent(any(Student.class));
    }
    
    // Test 6: PUT /students/updateStudent - Mettre à jour un étudiant
    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        // Arrange
        Student studentToUpdate = new Student();
        studentToUpdate.setIdStudent(1L);
        studentToUpdate.setFirstName("John");
        studentToUpdate.setLastName("Doe Updated");
        studentToUpdate.setEmail("john.updated@example.com");
        
        Student updatedStudent = new Student();
        updatedStudent.setIdStudent(1L);
        updatedStudent.setFirstName("John");
        updatedStudent.setLastName("Doe Updated");
        updatedStudent.setEmail("john.updated@example.com");
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(updatedStudent);
        
        // Act & Assert
        mockMvc.perform(put("/students/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
        
        verify(studentService).saveStudent(any(Student.class));
    }
    
    // Test 7: DELETE /students/deleteStudent/{id} - Supprimer un étudiant
    @Test
    void deleteStudent_ShouldCallServiceDelete() throws Exception {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);
        
        // Act & Assert
        mockMvc.perform(delete("/students/deleteStudent/{id}", studentId))
                .andExpect(status().isOk());
        
        verify(studentService).deleteStudent(studentId);
    }
    
    // Test 8: Vérifier les en-têtes CORS
    @Test
    void shouldAllowCorsFromLocalhost4200() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(Arrays.asList());
        
        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents")
                .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
    
    // Test 9: POST /students/createStudent avec données invalides (JSON mal formaté)
    @Test
    void createStudent_ShouldReturnBadRequest_WhenInvalidJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }
    
    // Test 10: GET /students/getStudent/{id} avec ID non numérique
    @Test
    void getStudent_ShouldReturnBadRequest_WhenIdNotNumber() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/students/getStudent/{id}", "not-a-number"))
                .andExpect(status().isBadRequest());
    }
}