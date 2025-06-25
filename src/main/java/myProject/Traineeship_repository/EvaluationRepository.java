package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
	
	Optional<Evaluation> findByStudentAndPosition(Student student, Position position);
	List<Evaluation> findAllByStudentAndPosition(Student student, TraineeshipPosition position);
	List<Evaluation> findByPositionId(Long positionId);



}
