package tn.esprit.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;
import tn.esprit.studentmanagement.entities.Enrollment;
import java.util.List;

@Service
public class EnrollmentService implements IEnrollment {
    @Autowired
    EnrollmentRepository enrollmentRepository;

    private static final Logger logger = LogManager.getLogger(EnrollmentService.class);

    @Override
    public List<Enrollment> getAllEnrollments() {
        logger.info("Fetching all enrollments");
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        logger.debug("Found {} enrollments", enrollments == null ? 0 : enrollments.size());
        return enrollments;
    }

    @Override
    public Enrollment getEnrollmentById(Long idEnrollment) {
        logger.info("Fetching enrollment id={}", idEnrollment);
        try {
            Enrollment enrollment = enrollmentRepository.findById(idEnrollment)
                    .orElseThrow(() -> new RuntimeException("Enrollment not found: " + idEnrollment));
            logger.debug("Found enrollment: {}", enrollment);
            return enrollment;
        } catch (Exception e) {
            logger.error("Error fetching enrollment id={}", idEnrollment, e);
            throw e;
        }
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        logger.info("Saving enrollment: {}", enrollment);
        try {
            Enrollment saved = enrollmentRepository.save(enrollment);
            logger.info("Saved enrollment id={}", saved == null ? null : saved.getIdEnrollment());
            return saved;
        } catch (Exception e) {
            logger.error("Error saving enrollment: {}", enrollment, e);
            throw e;
        }
    }

    @Override
    public void deleteEnrollment(Long idEnrollment) {
        logger.info("Deleting enrollment id={}", idEnrollment);
        try {
            enrollmentRepository.deleteById(idEnrollment);
            logger.info("Deleted enrollment id={}", idEnrollment);
        } catch (Exception e) {
            logger.error("Error deleting enrollment id={}", idEnrollment, e);
            throw e;
        }
    }
}
