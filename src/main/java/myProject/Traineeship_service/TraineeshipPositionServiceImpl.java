package myProject.Traineeship_service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

import jakarta.transaction.Transactional;
import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_repository.EvaluationRepository;
import myProject.Traineeship_repository.TraineeshipPositionRepository;

@Service
public class TraineeshipPositionServiceImpl implements TraineeshipPositionService {

    @Autowired
    private TraineeshipPositionRepository repository;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private PositionService positionService;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public void save(TraineeshipPosition position) {
        repository.save(position);
    }

    @Override
    public List<TraineeshipPosition> findByCompany(Company company) {
        return repository.findByCompany(company);
    }

    @Override
    public List<TraineeshipPosition> findAvailableByCompany(Company company) {
        return repository.findByAssignedFalseAndCompany(company);
    }

    @Override
    public List<TraineeshipPosition> findUnassignedPositions() {
        return repository.findByAssignedFalse();
    }
    
    public List<TraineeshipPosition> findAssignedByCompany(Company company) {
        return repository.findAssignedByCompany(company);
    }

   

    @Override
    public List<TraineeshipPosition> findAll() {
        return repository.findAll();
    }
    
    @Override
    @Transactional
    public void applyStudentToPosition (Student student,Long positionId) {
        TraineeshipPosition position = repository.findWithApplicantsById(positionId)
            .orElseThrow(() -> new RuntimeException("Η θέση δεν βρέθηκε!"));

        if (!position.getApplicants().contains(student)) {
            position.getApplicants().add(student);
            repository.save(position);
        }
    }

    @Override
    public List<TraineeshipPosition> findMatchingPositions(Student student) {
        
        List<TraineeshipPosition> allAvailable = repository.findByAssignedFalse();

        
        String interestsRaw = student.getInterests();
        List<String> interestsList = (interestsRaw != null && !interestsRaw.isBlank())
            ? Arrays.stream(interestsRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toList())
            : List.of(); 

        String preferredLocation = student.getPreferredLocation();

        
        if ((preferredLocation == null || preferredLocation.isBlank()) && interestsList.isEmpty()) {
            return List.of();
        }

        return allAvailable.stream().filter(pos -> {
           
            boolean matchesLocation = (preferredLocation != null && !preferredLocation.isBlank()) &&
                                      (pos.getLocation() != null && !pos.getLocation().isBlank()) &&
                                      pos.getLocation().equalsIgnoreCase(preferredLocation);

            
            boolean matchesInterest = false;
            if (pos.getTopics_of_interest() != null && !pos.getTopics_of_interest().isBlank()) {
                List<String> posInterests = Arrays.stream(pos.getTopics_of_interest().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.toList());

                matchesInterest = interestsList.stream().anyMatch(interest ->
                        posInterests.stream().anyMatch(posInt ->
                                posInt.equalsIgnoreCase(interest))
                );
            }

            
            return matchesLocation || matchesInterest;
        }).collect(Collectors.toList());
    }




    @Override
    public List<TraineeshipPosition> findAllAvailablePositions() {
        return repository.findByAssignedFalse();
    }

    
    @Override
    public List<TraineeshipPosition> findAllWithApplicants() {
        return repository.findAllWithApplicants();
    }

    @Override
    public TraineeshipPosition findWithApplicantsById(Long id) {
        return repository.findWithApplicantsById(id).orElse(null);
    }

    @Override
    public TraineeshipPosition findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;
    public void delete(Long id) {
        traineeshipPositionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void assignPositionToStudent(Long studentId, Long positionId) {
       
        TraineeshipPosition position = repository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

       
        if (position.isAssigned()) {
            throw new RuntimeException("Position is already assigned to another student");
        }

        
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.isHasAssigned()) {
            throw new RuntimeException("Student is already assigned to a position");
        }


        student.setAssignedPosition(position);   
        student.setHasAssigned(true);             

        
        position.setAssigned(true);
        position.setAssignedStudent(student);
        position.setStudentId(studentId);
        position.getApplicants().remove(student);

        repository.save(position);

        Position newPosition = new Position();
        newPosition.setTitle(position.getTitle());
        newPosition.setDescription(position.getDescription());
        newPosition.setLocation(position.getLocation());
        newPosition.setDuration(position.getDuration());
        newPosition.setCompany(position.getCompany());
        newPosition.setStudent(student);
        newPosition.setAssigned(true);

        positionService.save(newPosition);


        
        studentService.save(student);
    }


    /*@Override
    public List<TraineeshipPosition> findByStudentAssignedAndProfessorUnassigned() {
        return repository.findByStudentIsNotNullAndSupervisingProfessorIsNull();
    }*/
    
 
    
    @Override
    public List<TraineeshipPosition> findAssignedWithoutProfessor() {
        return repository.findByAssignedTrueAndSupervisingProfessorIsNull();
    }

    @Override
    public List<TraineeshipPosition> findAssignedPositions() {
        return traineeshipPositionRepository.findByAssignedTrue();
    }


    @Override
    public void setFinalResult(Long positionId, String result) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
            .orElseThrow(() -> new RuntimeException("Δεν βρέθηκε η θέση με ID " + positionId));

        position.setFinalResult(result);
        traineeshipPositionRepository.save(position);
    }

    @Override
    public Map<String, Object> getCommitteeStats() {
        Map<String, Object> stats = new HashMap<>();

        List<TraineeshipPosition> allPositions = traineeshipPositionRepository.findAll();

        long total = allPositions.size();
        long pass = allPositions.stream().filter(p -> "PASS".equals(p.getFinalResult())).count();
        long fail = allPositions.stream().filter(p -> "FAIL".equals(p.getFinalResult())).count();
        long assigned = allPositions.stream().filter(TraineeshipPosition::isAssigned).count();
        long companies = allPositions.stream().map(TraineeshipPosition::getCompany).distinct().count();

        stats.put("total", total);
        stats.put("pass", pass);
        stats.put("fail", fail);
        stats.put("assigned", assigned);
        stats.put("companies", companies);

        return stats;
    }

    @Override
    public void resetPosition(Long positionId) {
        TraineeshipPosition pos = traineeshipPositionRepository.findById(positionId)
            .orElseThrow(() -> new RuntimeException("Position not found"));
       
        pos.setFinalResult(null);

        traineeshipPositionRepository.save(pos);
    }



}

