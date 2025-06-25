package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.CompanyEvaluation;
import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_repository.CompanyEvaluationRepository;
import myProject.Traineeship_repository.EvaluationRepository;
import myProject.Traineeship_service.CompanyService;
import myProject.Traineeship_service.EvaluationService;
import myProject.Traineeship_service.PositionService;
import myProject.Traineeship_service.TraineeshipPositionService;
import myProject.Traineeship_service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PositionService positionService;
    
    @Autowired
    private TraineeshipPositionService traineeshipPositionService;
    
    @Autowired
    private EvaluationService evaluationService;
    
    @Autowired
    private CompanyEvaluationRepository companyevaluationRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/company/profile")
    public String showProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company_profile";
    }

    @GetMapping("/company/edit-profile")
    public String editProfile(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        model.addAttribute("company", company);
        return "company_edit_profile";
    }
    @PostMapping("/company/edit-profile")
    public String updateProfile(@ModelAttribute("company") Company updatedCompany, Principal principal) {
        Company existingCompany = companyService.findByUsername(principal.getName());

        if (existingCompany != null) {
            existingCompany.setCompanyName(updatedCompany.getCompanyName());
            existingCompany.setLocation(updatedCompany.getLocation());
            existingCompany.setEmail(updatedCompany.getEmail());
            existingCompany.setFirstName(updatedCompany.getFirstName());
            existingCompany.setLastName(updatedCompany.getLastName());           
            existingCompany.setPassword(updatedCompany.getPassword());
            companyService.save(existingCompany);
        }

        return "redirect:/company/profile";
    }

    @GetMapping("/company_dashboard")
    public String showDashboard(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        if (company == null) {
            return "error";
        }
        model.addAttribute("company", company);
        return "company_dashboard";
    }

    @GetMapping("/company/positions")
    public String showAllPositions(Model model, Principal principal) {
        Company company = companyService.findByUsername(principal.getName());
        List<Position> positions = positionService.findByCompany(company);
        model.addAttribute("positions", positions);
        return "company_positions";  
    }
    @PostMapping("/company/delete-position")
    public String deletePosition(@RequestParam Long positionId) {
        traineeshipPositionService.delete(positionId);
        return "redirect:/company_available_positions";
    }

  
    @GetMapping("/company/assigned-positions")
    public String showAssignedPositions(Model model, Authentication authentication) {
        String username = authentication.getName();        
        Company company = companyService.findByUsername(username);
        if (company == null) {
            return "redirect:/login";  
        }

        System.out.println(" Filtering by company ID: " + company.getId());
     
        List<Position> assigned = positionService.findAssignedByCompanyId(company.getId());
        model.addAttribute("positions", assigned);
        return "company_assigned-positions";
    }


    @GetMapping("/company/evaluation/{positionId}")
    public String showCompanyEvaluationForm(@PathVariable Long positionId,
                                            Model model,
                                            Principal principal) {

        Company company = companyService.findByUserUsername(principal.getName());
        Position position = positionService.findById(positionId);

        if (!position.getCompany().getId().equals(company.getId())) {
            model.addAttribute("error", "Δεν έχετε πρόσβαση σε αυτή τη θέση.");
            return "error";
        }

        if (position.getStudent() == null) {
            model.addAttribute("error", "Δεν έχει ανατεθεί φοιτητής.");
            return "error";
        }

        CompanyEvaluation evaluation = new CompanyEvaluation();
        evaluation.setPosition(position);
        evaluation.setStudent(position.getStudent());

        model.addAttribute("position", position);
        model.addAttribute("evaluation", evaluation);

        return "company_evaluate_form";
    }


    @PostMapping("/company/submit-evaluation")
    public String submitCompanyEvaluation(@ModelAttribute("evaluation") CompanyEvaluation evaluation,
                                          Principal principal,
                                          RedirectAttributes redirectAttributes) {

        Position position = evaluation.getPosition();

        if (position == null || position.getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Δεν επιλέχθηκε θέση.");
            return "redirect:/company/assigned-positions";
        }

        position = positionService.findById(position.getId());
        Student student = position.getStudent();

        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Δεν υπάρχει φοιτητής σε αυτή τη θέση.");
            return "redirect:/company/assigned-positions";
        }

        evaluation.setPosition(position);
        evaluation.setStudent(student);
        evaluation.setDate(LocalDate.now());
        int avgRating = (evaluation.getCompanyMotivation() +
                evaluation.getCompanyEffectiveness() +
                evaluation.getCompanyEfficiency()) / 3;
        evaluation.setRating(avgRating);
        companyevaluationRepository.save(evaluation);

        System.out.println(" Αξιολόγηση αποθηκεύτηκε για θέση: " + position.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Η αξιολόγηση υποβλήθηκε με επιτυχία!");
        return "redirect:/company/assigned-positions";

    }


    
 
    @GetMapping("/company/create-position")
    public String showCreateForm(Model model) {
        model.addAttribute("traineeshipPosition", new TraineeshipPosition());
        return "create_traineeship_position";
    }
  

    
    @PostMapping("/company/create-position")
    public String createPosition(@ModelAttribute("position") TraineeshipPosition position, Principal principal) {
    	User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));


    	Company company = companyService.findByUsername(user.getUsername());

        position.setCompany(company);
        traineeshipPositionService.save(position);

        return "redirect:/company_dashboard";
    }

    
    @GetMapping("/company/available_positions")
    public String showAvailablePositions(Model model, Principal principal) {
    	User user = userService.getUserByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));

    	Company company = companyService.findByUsername(user.getUsername());

        List<TraineeshipPosition> positions = traineeshipPositionService.findAvailableByCompany(company);

        model.addAttribute("positions", positions);
        return "company_available_positions";
    }


}

