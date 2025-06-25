package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByCompany(Company company);
    List<TraineeshipPosition> findByAssignedFalseAndCompany(Company company);
    List<TraineeshipPosition> findByAssignedFalse();
      
    @Query("SELECT DISTINCT p FROM TraineeshipPosition p LEFT JOIN FETCH p.applicants")
    List<TraineeshipPosition> findAllWithApplicants();

    @Query("SELECT p FROM TraineeshipPosition p LEFT JOIN FETCH p.applicants WHERE p.id = :id")
    Optional<TraineeshipPosition> findWithApplicantsById(@Param("id") Long id);

    @Query("SELECT p FROM TraineeshipPosition p WHERE p.assigned = true AND p.company = :company")
    List<TraineeshipPosition> findAssignedByCompany(@Param("company") Company company);
        
    List<TraineeshipPosition> findByStudentIdIsNotNullAndSupervisingProfessorIsNull();
    List<TraineeshipPosition> findByAssignedTrueAndSupervisingProfessorIsNull();
    List<TraineeshipPosition> findBySupervisingProfessor(Professor professor);    
    List<TraineeshipPosition> findByAssignedTrue();


   
    
    



   

    


    

    

    

}

