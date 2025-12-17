package tn.esprit.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Service
public class StudentService implements IStudentService {
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        logger.info("Fetching all students");
        List<Student> list = studentRepository.findAll();
        logger.debug("Found {} students", list == null ? 0 : list.size());
        return list;
    }

    public Student getStudentById(Long id) {
        logger.info("Fetching student id={}", id);
        try {
            Student s = studentRepository.findById(id).orElse(null);
            logger.debug("Found student: {}", s);
            return s;
        } catch (Exception e) {
            logger.error("Error fetching student id={}", id, e);
            throw e;
        }
    }

    public Student saveStudent(Student student) {
        logger.info("Saving student: {}", student);
        try {
            Student saved = studentRepository.save(student);
            logger.debug("Saved student id={}", saved == null ? "null" : saved.getIdStudent());
            return saved;
        } catch (Exception e) {
            logger.error("Error saving student: {}", student, e);
            throw e;
        }
    }

    public void deleteStudent(Long id) {
        logger.info("Deleting student id={}", id);
        try {
            studentRepository.deleteById(id);
            logger.debug("Deleted student id={}", id);
        } catch (Exception e) {
            logger.error("Error deleting student id={}", id, e);
            throw e;
        }
    }

}
