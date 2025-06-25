package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
	List<LogEntry> findByStudentId(Long studentId);
}
