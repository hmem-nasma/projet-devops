package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@SpringBootTest
class StudentManagementApplicationTests {
    
    // Mock la DataSource pour éviter la connexion réelle à MySQL
    @MockBean
    private DataSource dataSource;
    
    @Test
    void contextLoads() {
        // Test vide - vérifie juste que le contexte Spring démarre
    }
}