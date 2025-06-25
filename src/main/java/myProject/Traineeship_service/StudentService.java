package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.LogEntry;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student findByUsername(String username);
    void save(Student student);
    List<Position> getAllPositions();
    List<LogEntry> getLogEntriesForStudent(Long studentId);
    List<Student> findAll();
    Optional<Student> findById(Long id);
    List<Student> findAllWithApplication();
    Student findByUsernameWithLogbook(String username);
    void assignPositionToStudent(Long studentId, Long positionId);
    Student findByUsernameWithAssignedPosition(String username);

}
