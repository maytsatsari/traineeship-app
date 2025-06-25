package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_repository.ProfessorRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public void save(Professor professor) {
        professorRepository.save(professor);
    }

    @Override
    public Professor findByUsername(String username) {
        Professor p = professorRepository.findByUserUsername(username);
        System.out.println("✅ Professor found: " + (p != null ? p.getId() : "null"));
        return p;
    }
    
    @Override
    public List<Professor> getSuggestedProfessors(String topic) {
        List<Professor> matching = professorRepository.findByInterestContaining(topic);

        if (matching.isEmpty()) {
            return professorRepository.findAllOrderBySupervisionLoad();
        }

        return matching;
    }
    
    @Override
    public Optional<Professor> findById(Long id) {
        return professorRepository.findById(id);
    }
    
    @Override
    public List<Professor> getSuggestedProfessorsForPosition(TraineeshipPosition position) {
        String topicString = position.getTopics_of_interest();
        List<Professor> allProfessors = professorRepository.findAllWithSupervisedPositions();

        if (topicString == null) return List.of();

        
        List<String> topicList = Arrays.stream(topicString.split(","))
                                       .map(String::trim)
                                       .map(String::toLowerCase)
                                       .toList();

        return allProfessors.stream()
            .filter(prof -> {
                if (prof.getInterests() == null) return false;
                String interests = prof.getInterests().toLowerCase();
                return topicList.stream().anyMatch(interests::contains);
            })
            .sorted(Comparator.comparingInt(p -> p.getSupervisedPositions().size()))
            .toList();
    }





    
    





}

