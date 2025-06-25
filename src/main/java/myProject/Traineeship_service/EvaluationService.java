package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Evaluation;

public interface EvaluationService {
    void save(Evaluation evaluation);
    String determineFinalResult(Long positionId);
    Double getCombinedAverageRatingForPosition(Long positionId);


}
