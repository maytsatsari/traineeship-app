package myProject.Traineeship_repository;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.TraineeshipPosition;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByUserUsername(String username);
    
    @Query("SELECT p FROM TraineeshipPosition p WHERE p.assigned = true AND p.company = :company")
    List<TraineeshipPosition> findAssignedByCompany(@Param("company") Company company);
}
