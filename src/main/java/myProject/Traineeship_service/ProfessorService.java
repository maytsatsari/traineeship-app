package myProject.Traineeship_service;

import java.util.List;
import java.util.Optional;

import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.TraineeshipPosition;

public interface ProfessorService {
    void save(Professor professor);
    Professor findByUsername(String username);
    List<Professor> getSuggestedProfessors(String topic);
    Optional<Professor> findById(Long id);    
    List<Professor> getSuggestedProfessorsForPosition(TraineeshipPosition position);



}
