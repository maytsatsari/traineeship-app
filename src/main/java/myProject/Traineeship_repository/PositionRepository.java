package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByCompany(Company company);
    List<Position> findByIsAssignedTrue();
    
    @Query("SELECT p FROM Position p WHERE p.isAssigned = true AND p.company.id = :companyId")
    List<Position> findAssignedByCompanyId(@Param("companyId") Long companyId);


    


}
