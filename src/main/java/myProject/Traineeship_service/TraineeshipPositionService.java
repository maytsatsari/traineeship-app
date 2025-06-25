package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public interface TraineeshipPositionService {
	
	

	TraineeshipPosition findById(Long id);
    
    void save(TraineeshipPosition position);
    List<TraineeshipPosition> findByCompany(Company company);
    List<TraineeshipPosition> findAvailableByCompany(Company company);
    
    List<TraineeshipPosition> findUnassignedPositions();
    List<TraineeshipPosition> findAll();
    void applyStudentToPosition( Student student,Long positionId);
    List<TraineeshipPosition> findAllAvailablePositions();
    List<TraineeshipPosition> findAllWithApplicants();
    TraineeshipPosition findWithApplicantsById(Long id); 
    List<TraineeshipPosition> findMatchingPositions(Student student);
    void delete(Long id);
    void assignPositionToStudent(Long studentId, Long positionId);
    List<TraineeshipPosition> findAssignedByCompany(Company company);
  
    List<TraineeshipPosition> findAssignedWithoutProfessor();
    
    List<TraineeshipPosition> findAssignedPositions();
    void setFinalResult(Long positionId, String result);

    Map<String, Object> getCommitteeStats();

    void resetPosition(Long positionId);




 
}

