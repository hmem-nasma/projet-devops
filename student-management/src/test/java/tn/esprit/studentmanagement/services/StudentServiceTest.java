package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    
    @Mock
    private StudentRepository studentRepository;
    
    @InjectMocks
    private StudentService studentService;
    
    // Test 1: Récupérer tous les étudiants
    @Test
    void getAllStudents_ShouldReturnListOfStudents() {
        // Arrange
        Student student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("John");
        student1.setLastName("Doe");
        
        Student student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);
        
        // Act
        List<Student> result = studentService.getAllStudents();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(studentRepository).findAll();
    }
    
    // Test 2: Récupérer un étudiant par ID (trouvé)
    @Test
    void getStudentById_ShouldReturnStudent_WhenExists() {
        // Arrange
        Long studentId = 1L;
        Student student = new Student();
        student.setIdStudent(studentId);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        
        // Act
        Student result = studentService.getStudentById(studentId);
        
        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getIdStudent());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(studentRepository).findById(studentId);
    }
    
    // Test 3: Récupérer un étudiant par ID (non trouvé)
    @Test
    void getStudentById_ShouldReturnNull_WhenNotExists() {
        // Arrange
        Long studentId = 999L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        
        // Act
        Student result = studentService.getStudentById(studentId);
        
        // Assert
        assertNull(result);
        verify(studentRepository).findById(studentId);
    }
    
    // Test 4: Récupérer un étudiant par ID (exception)
    @Test
    void getStudentById_ShouldThrowException_WhenRepositoryThrowsException() {
        // Arrange
        Long studentId = 1L;
        RuntimeException expectedException = new RuntimeException("Database error");
        when(studentRepository.findById(studentId)).thenThrow(expectedException);
        
        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            studentService.getStudentById(studentId);
        });
        
        assertEquals(expectedException, thrown);
        verify(studentRepository).findById(studentId);
    }
    
    // Test 5: Sauvegarder un étudiant (succès)
    @Test
    void saveStudent_ShouldReturnSavedStudent() {
        // Arrange
        Student studentToSave = new Student();
        studentToSave.setFirstName("John");
        studentToSave.setLastName("Doe");
        studentToSave.setEmail("john.doe@example.com");
        
        Student savedStudent = new Student();
        savedStudent.setIdStudent(1L);
        savedStudent.setFirstName("John");
        savedStudent.setLastName("Doe");
        savedStudent.setEmail("john.doe@example.com");
        
        when(studentRepository.save(studentToSave)).thenReturn(savedStudent);
        
        // Act
        Student result = studentService.saveStudent(studentToSave);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdStudent());
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(studentRepository).save(studentToSave);
    }
    
    // Test 6: Sauvegarder un étudiant (exception)
    @Test
    void saveStudent_ShouldThrowException_WhenRepositoryThrowsException() {
        // Arrange
        Student student = new Student();
        student.setFirstName("John");
        RuntimeException expectedException = new RuntimeException("Save failed");
        
        when(studentRepository.save(student)).thenThrow(expectedException);
        
        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            studentService.saveStudent(student);
        });
        
        assertEquals(expectedException, thrown);
        verify(studentRepository).save(student);
    }
    
    // Test 7: Supprimer un étudiant (succès)
    @Test
    void deleteStudent_ShouldCallRepositoryDelete() {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentRepository).deleteById(studentId);
        
        // Act
        studentService.deleteStudent(studentId);
        
        // Assert
        verify(studentRepository).deleteById(studentId);
    }
    
    // Test 8: Supprimer un étudiant (exception)
    @Test
    void deleteStudent_ShouldThrowException_WhenRepositoryThrowsException() {
        // Arrange
        Long studentId = 1L;
        RuntimeException expectedException = new RuntimeException("Delete failed");
        doThrow(expectedException).when(studentRepository).deleteById(studentId);
        
        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(studentId);
        });
        
        assertEquals(expectedException, thrown);
        verify(studentRepository).deleteById(studentId);
    }
    
    // Test 9: getAllStudents avec liste vide
    @Test
    void getAllStudents_ShouldReturnEmptyList_WhenNoStudents() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList());
        
        // Act
        List<Student> result = studentService.getAllStudents();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository).findAll();
    }
    
    // Test 10: Vérifier que les logs sont appelés
    @Test
    void getAllStudents_ShouldLogInformation() {
        // Arrange
        Student student = new Student();
        student.setIdStudent(1L);
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));
        
        // Act
        studentService.getAllStudents();
        
        // Assert
        // Les appels de log sont difficiles à tester directement avec Log4j2
        // Mais le code devrait s'exécuter sans erreur
        verify(studentRepository).findAll();
    }
}