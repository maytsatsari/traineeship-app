package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_repository.CompanyRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void save(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company findByUsername(String username) {
        return companyRepository.findByUserUsername(username);
    }
    
    public List<TraineeshipPosition> findAssignedByCompany(Company company) {
        return companyRepository.findAssignedByCompany(company);
    }
    
    public Company findById(Long id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found"));
    }
    
    public Company findByUserUsername(String username) {
        return companyRepository.findByUserUsername(username);
    }

}
