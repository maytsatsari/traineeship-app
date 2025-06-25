package myProject.Traineeship_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import myProject.TraineeshipApp_domain.CompanyEvaluation;
import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.Student;
import myProject.Traineeship_repository.CompanyEvaluationRepository;
import myProject.Traineeship_repository.EvaluationRepository;
import myProject.Traineeship_repository.TraineeshipPositionRepository;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;
    @Autowired
    private CompanyEvaluationRepository companyEvaluationRepository;

    @Override
    public void save(Evaluation evaluation) {
        evaluationRepository.save(evaluation);
    }

    @Override
    public String determineFinalResult(Long positionId) {
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId).orElseThrow();
        Student student = position.getAssignedStudent();

        List<Evaluation> evaluations = evaluationRepository.findAllByStudentAndPosition(student, position);
        if (evaluations.isEmpty()) return "FAIL";

        double avgRating = evaluations.stream()
                .mapToInt(Evaluation::getRating)
                .average()
                .orElse(0.0);

        return avgRating >= 3.0 ? "PASS" : "FAIL";
    }
    @Override
    public Double getCombinedAverageRatingForPosition(Long positionId) {
        List<Evaluation> professorEvaluations = evaluationRepository.findByPositionId(positionId);
        double professorAvg = professorEvaluations.stream()
                .mapToInt(Evaluation::getRating)
                .average()
                .orElse(0.0);

        CompanyEvaluation companyEval = companyEvaluationRepository.findByPositionId(positionId);
        Double companyAvg = null;

        if (companyEval != null) {
            companyAvg = (companyEval.getCompanyEffectiveness() +
                          companyEval.getCompanyEfficiency() +
                          companyEval.getCompanyMotivation()) / 3.0;
        }


        if (professorEvaluations.isEmpty() && companyAvg == null) {
            return null; 
        } else if (professorEvaluations.isEmpty()) {
            return companyAvg;
        } else if (companyAvg == null) {
            return professorAvg;
        } else {
            return (professorAvg + companyAvg) / 2.0;
        }
    }


}
