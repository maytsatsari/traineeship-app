package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_repository.EvaluationRepository;
import myProject.Traineeship_repository.ProfessorRepository;
import myProject.Traineeship_repository.TraineeshipPositionRepository;
import myProject.Traineeship_repository.UserRepository;
import myProject.Traineeship_service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private  ProfessorRepository professorRepository;
    
    @Autowired
    private  TraineeshipPositionRepository traineeshipPositionRepository;
    
    @Autowired
    private  EvaluationRepository evaluationRepository;


    
    @GetMapping("/professor_dashboard")
    public String professorDashboard(Model model, Principal principal) {
        String username = principal.getName();
        System.out.println(" Logged in as: " + username);

        Professor professor = professorService.findByUsername(username);
        if (professor == null) {
            System.out.println(" Professor not found");
            return "error";
        }

        List<TraineeshipPosition> positions = traineeshipPositionRepository.findBySupervisingProfessor(professor);


        System.out.println(" Positions found: " + positions.size());
        for (TraineeshipPosition pos : positions) {
            System.out.println(pos.getTitle());
        }

        model.addAttribute("positions", positions);
        return "professor_dashboard";
    }
    
    @PostMapping("/professor/evaluate")
    public String submitEvaluation(@RequestParam Long positionId,
                                   @RequestParam int motivation,
                                   @RequestParam int effectiveness,
                                   @RequestParam int efficiency,
                                   @RequestParam int facilities,
                                   @RequestParam int guidance,
                                   @RequestParam(required = false) String comments,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {

        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + positionId));

        Student assignedStudent = position.getAssignedStudent();
        if (assignedStudent == null) {
            redirectAttributes.addFlashAttribute("errorMessage", " Δεν έχει ανατεθεί φοιτητής σε αυτή τη θέση.");
            return "redirect:/professor_dashboard";
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setPosition(position);
        evaluation.setStudent(assignedStudent);  
        evaluation.setDate(LocalDate.now());     
        
        Professor professor = professorService.findByUsername(principal.getName());
        evaluation.setProfessor(professor);
        evaluation.setMotivation(motivation);
        evaluation.setEffectiveness(effectiveness);
        evaluation.setEfficiency(efficiency);
        evaluation.setFacilities(facilities);
        evaluation.setGuidance(guidance);
        evaluation.setComments(comments);
        
        int avgRating = (motivation + effectiveness + efficiency ) / 3;
        evaluation.setRating(avgRating);
        evaluationRepository.save(evaluation);

        redirectAttributes.addFlashAttribute("successMessage", "Η αξιολόγηση καταχωρήθηκε με επιτυχία.");
        return "redirect:/professor_dashboard";
    }



}
