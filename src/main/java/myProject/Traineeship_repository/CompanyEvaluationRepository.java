package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.CompanyEvaluation;
import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyEvaluationRepository extends JpaRepository<CompanyEvaluation, Long> {
    Optional<CompanyEvaluation> findByStudentAndPosition(Student student, Position position);
   
    CompanyEvaluation findByPositionId(Long positionId);


}
