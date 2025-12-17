package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    
    @Mock
    private DepartmentRepository departmentRepository;
    
    @InjectMocks
    private DepartmentService departmentService;
    
    @Test
    public void testGetAllDepartments() {
        // Arrange
        Department dept1 = new Department();
        dept1.setIdDepartment(1L);
        dept1.setName("Informatique");
        dept1.setLocation("Bâtiment A");
        
        Department dept2 = new Department();
        dept2.setIdDepartment(2L);
        dept2.setName("Mathématiques");
        dept2.setLocation("Bâtiment B");
        
        List<Department> expectedDepartments = Arrays.asList(dept1, dept2);
        
        when(departmentRepository.findAll()).thenReturn(expectedDepartments);
        
        // Act
        List<Department> actualDepartments = departmentService.getAllDepartments();
        
        // Assert
        assertNotNull(actualDepartments);
        assertEquals(2, actualDepartments.size());
        assertEquals("Informatique", actualDepartments.get(0).getName());
        assertEquals("Mathématiques", actualDepartments.get(1).getName());
        assertEquals(1L, actualDepartments.get(0).getIdDepartment());
        assertEquals(2L, actualDepartments.get(1).getIdDepartment());
        verify(departmentRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetDepartmentById_Success() {
        // Arrange
        Long departmentId = 1L;
        Department expectedDepartment = new Department();
        expectedDepartment.setIdDepartment(departmentId);
        expectedDepartment.setName("Informatique");
        expectedDepartment.setLocation("Bâtiment A");
        
        when(departmentRepository.findById(departmentId))
            .thenReturn(Optional.of(expectedDepartment));
        
        // Act
        Department actualDepartment = departmentService.getDepartmentById(departmentId);
        
        // Assert
        assertNotNull(actualDepartment);
        assertEquals(departmentId, actualDepartment.getIdDepartment());
        assertEquals("Informatique", actualDepartment.getName());
        assertEquals("Bâtiment A", actualDepartment.getLocation());
        verify(departmentRepository, times(1)).findById(departmentId);
    }
    
    @Test
    public void testGetDepartmentById_NotFound() {
        // Arrange
        Long departmentId = 999L;
        
        when(departmentRepository.findById(departmentId))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            departmentService.getDepartmentById(departmentId);
        });
        
        verify(departmentRepository, times(1)).findById(departmentId);
    }
    
    @Test
    public void testSaveDepartment() {
        // Arrange
        Department departmentToSave = new Department();
        departmentToSave.setName("Physique");
        departmentToSave.setLocation("Bâtiment C");
        departmentToSave.setPhone("0123456789");
        departmentToSave.setHead("Dr. Dupont");
        
        Department savedDepartment = new Department();
        savedDepartment.setIdDepartment(1L);
        savedDepartment.setName("Physique");
        savedDepartment.setLocation("Bâtiment C");
        savedDepartment.setPhone("0123456789");
        savedDepartment.setHead("Dr. Dupont");
        
        when(departmentRepository.save(departmentToSave)).thenReturn(savedDepartment);
        
        // Act
        Department result = departmentService.saveDepartment(departmentToSave);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdDepartment());
        assertEquals("Physique", result.getName());
        assertEquals("Bâtiment C", result.getLocation());
        assertEquals("Dr. Dupont", result.getHead());
        verify(departmentRepository, times(1)).save(departmentToSave);
    }
    
    @Test
    public void testDeleteDepartment_Success() {
        // Arrange
        Long departmentId = 1L;
        
        // Simule que la suppression fonctionne sans erreur
        doNothing().when(departmentRepository).deleteById(departmentId);
        
        // Act
        departmentService.deleteDepartment(departmentId);
        
        // Assert
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }
    
    @Test
    public void testDeleteDepartment_WithException() {
        // Arrange
        Long departmentId = 999L;
        
        // Simule une exception lors de la suppression
        doThrow(new RuntimeException("Erreur de suppression"))
            .when(departmentRepository).deleteById(departmentId);
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            departmentService.deleteDepartment(departmentId);
        });
        
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }
}
