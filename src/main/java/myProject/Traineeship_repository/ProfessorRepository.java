package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {  
    
    @Query("SELECT p FROM Professor p WHERE LOWER(p.interests) LIKE LOWER(CONCAT('%', :topic, '%'))")
    List<Professor> findByInterestContaining(@Param("topic") String topic);

    @Query("SELECT p FROM Professor p LEFT JOIN TraineeshipPosition t ON t.supervisingProfessor = p GROUP BY p ORDER BY COUNT(t)")
    List<Professor> findAllOrderBySupervisionLoad();
   
    Professor findByUser(User user);
    
    @Query("SELECT p FROM Professor p WHERE p.user.username = :username")
    Professor findByUsername(@Param("username") String username);
    
    @Query("SELECT p FROM Professor p WHERE p.user.username = :username")
    Professor findByUserUsername(@Param("username") String username);
    
    @Query("SELECT DISTINCT p FROM Professor p LEFT JOIN FETCH p.supervisedPositions")
    List<Professor> findAllWithSupervisedPositions();





    
}
