package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.Position;

import java.util.List;

public interface PositionService {

    void save(Position position);

    List<Position> findAll();
    List<Position> findByCompany(Company company);
    List<Position> findAssignedByCompany(Company company);
    List<Position> findAssignedByCompanyId(Long companyId);
    Position findById(Long id);




}
