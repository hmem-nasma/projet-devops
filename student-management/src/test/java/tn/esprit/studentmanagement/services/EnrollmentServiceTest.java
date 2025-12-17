package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Course;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {
    
    @Mock
    private EnrollmentRepository enrollmentRepository;
    
    @InjectMocks
    private EnrollmentService enrollmentService;
    
    private Student createMockStudent(Long id) {
        Student student = new Student();
        student.setIdStudent(id);
        student.setFirstName("John");
        student.setLastName("Doe");
        return student;
    }
    
    private Course createMockCourse(Long id) {
        Course course = new Course();
        course.setIdCourse(id);
        course.setName("Mathematics");
        return course;
    }
    
    private Enrollment createMockEnrollment(Long id, Student student, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(id);
        enrollment.setEnrollmentDate(LocalDate.of(2024, 1, 15));
        enrollment.setGrade(85.5);
        enrollment.setStatus(Status.ACTIVE); // Utilise ACTIVE qui existe
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollment;
    }
    
    @Test
    public void testGetAllEnrollments() {
        // Arrange
        Student student1 = createMockStudent(1L);
        Course course1 = createMockCourse(1L);
        Enrollment enrollment1 = createMockEnrollment(1L, student1, course1);
        
        Student student2 = createMockStudent(2L);
        Course course2 = createMockCourse(2L);
        Enrollment enrollment2 = createMockEnrollment(2L, student2, course2);
        
        List<Enrollment> expectedEnrollments = Arrays.asList(enrollment1, enrollment2);
        
        when(enrollmentRepository.findAll()).thenReturn(expectedEnrollments);
        
        // Act
        List<Enrollment> actualEnrollments = enrollmentService.getAllEnrollments();
        
        // Assert
        assertNotNull(actualEnrollments);
        assertEquals(2, actualEnrollments.size());
        assertEquals(1L, actualEnrollments.get(0).getIdEnrollment());
        assertEquals(2L, actualEnrollments.get(1).getIdEnrollment());
        assertEquals(Status.ACTIVE, actualEnrollments.get(0).getStatus());
        assertEquals(85.5, actualEnrollments.get(0).getGrade());
        verify(enrollmentRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetEnrollmentById_Success() {
        // Arrange
        Long enrollmentId = 1L;
        Student student = createMockStudent(1L);
        Course course = createMockCourse(1L);
        Enrollment expectedEnrollment = createMockEnrollment(enrollmentId, student, course);
        
        when(enrollmentRepository.findById(enrollmentId))
            .thenReturn(Optional.of(expectedEnrollment));
        
        // Act
        Enrollment actualEnrollment = enrollmentService.getEnrollmentById(enrollmentId);
        
        // Assert
        assertNotNull(actualEnrollment);
        assertEquals(enrollmentId, actualEnrollment.getIdEnrollment());
        assertEquals(LocalDate.of(2024, 1, 15), actualEnrollment.getEnrollmentDate());
        assertEquals(85.5, actualEnrollment.getGrade());
        assertEquals(Status.ACTIVE, actualEnrollment.getStatus());
        assertEquals(1L, actualEnrollment.getStudent().getIdStudent());
        assertEquals(1L, actualEnrollment.getCourse().getIdCourse());
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }
    
    @Test
    public void testGetEnrollmentById_NotFound() {
        // Arrange
        Long enrollmentId = 999L;
        
        when(enrollmentRepository.findById(enrollmentId))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.getEnrollmentById(enrollmentId);
        });
        
        assertTrue(exception.getMessage().contains("Enrollment not found"));
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }
    
    @Test
    public void testSaveEnrollment() {
        // Arrange
        Student student = createMockStudent(1L);
        Course course = createMockCourse(1L);
        
        Enrollment enrollmentToSave = new Enrollment();
        enrollmentToSave.setEnrollmentDate(LocalDate.of(2024, 2, 1));
        enrollmentToSave.setGrade(90.0);
        enrollmentToSave.setStatus(Status.ACTIVE); // Utilise ACTIVE
        enrollmentToSave.setStudent(student);
        enrollmentToSave.setCourse(course);
        
        Enrollment savedEnrollment = new Enrollment();
        savedEnrollment.setIdEnrollment(1L);
        savedEnrollment.setEnrollmentDate(LocalDate.of(2024, 2, 1));
        savedEnrollment.setGrade(90.0);
        savedEnrollment.setStatus(Status.ACTIVE); // Utilise ACTIVE
        savedEnrollment.setStudent(student);
        savedEnrollment.setCourse(course);
        
        when(enrollmentRepository.save(enrollmentToSave)).thenReturn(savedEnrollment);
        
        // Act
        Enrollment result = enrollmentService.saveEnrollment(enrollmentToSave);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdEnrollment());
        assertEquals(LocalDate.of(2024, 2, 1), result.getEnrollmentDate());
        assertEquals(90.0, result.getGrade());
        assertEquals(Status.ACTIVE, result.getStatus()); // Utilise ACTIVE
        assertEquals(1L, result.getStudent().getIdStudent());
        assertEquals(1L, result.getCourse().getIdCourse());
        verify(enrollmentRepository, times(1)).save(enrollmentToSave);
    }
    
    @Test
    public void testDeleteEnrollment_Success() {
        // Arrange
        Long enrollmentId = 1L;
        
        // Simule que la suppression fonctionne sans erreur
        doNothing().when(enrollmentRepository).deleteById(enrollmentId);
        
        // Act
        enrollmentService.deleteEnrollment(enrollmentId);
        
        // Assert
        verify(enrollmentRepository, times(1)).deleteById(enrollmentId);
    }
    
    @Test
    public void testDeleteEnrollment_WithException() {
        // Arrange
        Long enrollmentId = 999L;
        
        // Simule une exception lors de la suppression
        doThrow(new RuntimeException("Database error"))
            .when(enrollmentRepository).deleteById(enrollmentId);
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            enrollmentService.deleteEnrollment(enrollmentId);
        });
        
        verify(enrollmentRepository, times(1)).deleteById(enrollmentId);
    }
}