package myProject.Traineeship_service;

import java.util.List;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.TraineeshipPosition;

public interface CompanyService {
    void save(Company company);
    Company findByUsername(String username);
    List<TraineeshipPosition> findAssignedByCompany(Company company);
    Company findById(Long id);
    Company findByUserUsername(String username);
}
