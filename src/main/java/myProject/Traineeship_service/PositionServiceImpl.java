package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.Position;
import myProject.Traineeship_repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Override
    public void save(Position position) {
        positionRepository.save(position);
    }

    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    @Override
    public List<Position> findByCompany(Company company) {
        return positionRepository.findByCompany(company);
    }

    @Override
    public List<Position> findAssignedByCompany(Company company) {
        return positionRepository.findByCompany(company).stream()
                .filter(Position::isAssigned)
                .collect(Collectors.toList());
    }
    
    /*@Override
    public List<Position> findByCompanyAndAssignedStudent(Company company) {
        return positionRepository.findByCompanyAndStudentIsNotNull(company);
    }*/
    

    @Override
    public List<Position> findAssignedByCompanyId(Long companyId) {
        return positionRepository.findAssignedByCompanyId(companyId);
    }
    
    public Position findById(Long id) {
        return positionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Position not found"));
    }






}
