package myProject.Traineeship_repository;
import myProject.TraineeshipApp_domain.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Student findByUserUsername(String username);
	List<Student> findByHasAssignedTrue();
	
	@Query("SELECT s FROM Student s LEFT JOIN FETCH s.logbook WHERE s.username = :username")
	Student findByUsernameWithLogbook(@Param("username") String username);

	@Query("SELECT s FROM Student s LEFT JOIN FETCH s.assignedPosition WHERE s.username = :username")
	Student findByUsernameWithAssignedPosition(@Param("username") String username);


}


