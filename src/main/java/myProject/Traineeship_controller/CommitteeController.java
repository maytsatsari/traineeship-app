package myProject.Traineeship_controller;

import java.awt.Color;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import myProject.TraineeshipApp_domain.CommitteeMember;
import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_service.CommitteeService;
import myProject.Traineeship_service.EvaluationService;
import myProject.Traineeship_service.PositionService;
import myProject.Traineeship_service.ProfessorService;
import myProject.Traineeship_service.StudentService;
import myProject.Traineeship_service.TraineeshipPositionService;

@Controller
public class CommitteeController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TraineeshipPositionService traineeshipPositionService;
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private CommitteeService committeeService;
    
    @Autowired
    private PositionService positionService;
    
    @Autowired
    private ProfessorService professorService;
    
    
    @GetMapping("/company_available_positions")
    public String showAvailablePositionsForCommittee(Model model) {
        List<TraineeshipPosition> positions = traineeshipPositionService.findAllAvailablePositions();
        model.addAttribute("positions", positions);
        return "company_available_positions";
    }


    @GetMapping("/committee_dashboard")
    public String showDashboard(Model model, Principal principal) {
        System.out.println(">>> Committee Dashboard ενεργοποιήθηκε");
        CommitteeMember committee = committeeService.findByUsername(principal.getName());
        model.addAttribute("committee", committee);
        return "committee_dashboard";
    }
    
    @GetMapping("/committee_students")
    public String showStudents(Model model) {
        List<TraineeshipPosition> positions = traineeshipPositionService.findAllWithApplicants();

        Set<Student> uniqueStudents = new HashSet<>();

        for (TraineeshipPosition pos : positions) {
            for (Student applicant : pos.getApplicants()) {              
                if (applicant.getAssignedPosition() == null && applicant.isHasApplied()) {
                    uniqueStudents.add(applicant);
                }
            }
        }

        model.addAttribute("uniqueStudents", uniqueStudents);
        model.addAttribute("positions", positions);

        return "committee_students";
    }




    

    public CommitteeController(TraineeshipPositionService traineeshipPositionService) {
        this.traineeshipPositionService = traineeshipPositionService;
    }

    @GetMapping("/committee_position_applicants")
    public String viewAllApplications(Model model) {
        List<TraineeshipPosition> allPositions = traineeshipPositionService.findAllWithApplicants();

        // Φιλτράρουμε τις θέσεις και κρατάμε μόνο φοιτητές που έχουν αιτηθεί
        for (TraineeshipPosition pos : allPositions) {
            List<Student> filtered = pos.getApplicants().stream()
                .filter(Student::isHasApplied)
                .toList();
            pos.setApplicants(filtered); 
        }

        model.addAttribute("positions", allPositions);
        return "committee_position_applicants";
    }




    @GetMapping("/matching-positions/{id}")
    public String showMatchingPositions(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        List<TraineeshipPosition> positions = traineeshipPositionService.findMatchingPositions(student);
        model.addAttribute("student", student);
        model.addAttribute("positions", positions);
        return "matching-positions";
    }

    @PostMapping("/assign/{studentId}/{positionId}")
    public String assignPositionToStudent(@PathVariable Long studentId,
                                          @PathVariable Long positionId,
                                          RedirectAttributes redirectAttributes) {
        try {
            traineeshipPositionService.assignPositionToStudent(studentId, positionId);
            redirectAttributes.addFlashAttribute("successMessage", "Η θέση ανατέθηκε επιτυχώς!");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Student is already assigned")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Ο φοιτητής έχει ήδη ανατεθεί σε θέση.");
            } else if (e.getMessage().contains("Position is already assigned")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Η θέση έχει ήδη ανατεθεί.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Σφάλμα ανάθεσης: " + e.getMessage());
            }
        }

        return "redirect:/matching-positions/" + studentId;
    }

    @GetMapping("/committee/assign-supervisors")
    public String showAssignablePositions(Model model) {
        List<TraineeshipPosition> positions = traineeshipPositionService.findAssignedWithoutProfessor();        
        Map<Long, List<Professor>> suggestions = new HashMap<>();

        for (TraineeshipPosition pos : positions) {
            List<Professor> profs = professorService.getSuggestedProfessorsForPosition(pos);
            suggestions.put(pos.getId(), profs);
        }

        model.addAttribute("positions", positions);
        model.addAttribute("professorSuggestions", suggestions);

        return "assign_professors_bulk";
    }
    
    @PostMapping("/committee/assign-supervising-professor")

    public String assignProfessorToPosition(
            @RequestParam Long positionId,
            @RequestParam Long professorId,
            RedirectAttributes redirectAttributes) {

        TraineeshipPosition position = traineeshipPositionService.findById(positionId);
        if (position == null) {
            redirectAttributes.addFlashAttribute("errorMessage", " Η θέση δεν βρέθηκε.");
            return "redirect:/committee/assign-supervisors";
        }

        if (position.getSupervisingProfessor() != null) {
            redirectAttributes.addFlashAttribute("errorMessage", " Η θέση \"" + position.getTitle() + "\" έχει ήδη ανατεθεί σε καθηγητή.");
            return "redirect:/committee/assign-supervisors";
        }

        Professor professor = professorService.findById(professorId)
            .orElseThrow(() -> new RuntimeException("Professor not found"));

        position.setSupervisingProfessor(professor);
        traineeshipPositionService.save(position);

        redirectAttributes.addFlashAttribute("successMessage", " Καθηγητής \"" + professor.getUsername() +
                "\" ανατέθηκε επιτυχώς στη θέση \"" + position.getTitle() + "\".");

        return "redirect:/committee/assign-supervisors";
    }


    
    @GetMapping("/company/evaluate/{positionId}")
    public String showEvaluationForm(@PathVariable Long positionId, Model model) {
        TraineeshipPosition position = traineeshipPositionService.findById(positionId);
        if (position == null || position.getAssignedStudent() == null) {
            return "error";
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setPosition(position); 
        evaluation.setStudent(position.getAssignedStudent());

        model.addAttribute("evaluation", evaluation);
        return "company_evaluation_form";
    }

    @PostMapping("/company/evaluate")
    public String submitEvaluation(@ModelAttribute Evaluation evaluation) {
        evaluation.setDate(java.time.LocalDate.now());
        evaluationService.save(evaluation);
        return "redirect:/company/assigned-positions";
    }
    @GetMapping("/committee/evaluations")
    public String showEvaluatedPositions(Model model) {
        List<TraineeshipPosition> positions = traineeshipPositionService.findAssignedPositions();

        Map<Long, Double> averageRatings = new HashMap<>();
        for (TraineeshipPosition pos : positions) {
        	Double combinedAvg = evaluationService.getCombinedAverageRatingForPosition(pos.getId());
        	averageRatings.put(pos.getId(), combinedAvg);

        }

        model.addAttribute("positions", positions);
        model.addAttribute("averageRatings", averageRatings);

        return "committee_evaluations";
    }

    @PostMapping("/committee/finalize")
    public String finalizeManually(@RequestParam Long positionId,
                                   @RequestParam String result,
                                   RedirectAttributes redirectAttributes) {
        traineeshipPositionService.setFinalResult(positionId, result);
        redirectAttributes.addFlashAttribute("successMessage", "Η θέση ολοκληρώθηκε ως: " + result);
        return "redirect:/committee/evaluations";
    }

  

    
    @GetMapping("/company_assigned_positions")
    public String viewAssignedPositions(Model model) {
        List<Position> assignedPositions = positionService.findAll();  
        model.addAttribute("positions", assignedPositions);
        return "company_assigned_positions";  
    } 
    
    @GetMapping("/committee/evaluations/pdf")
    public void exportEvaluationsPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=evaluations.pdf");

        List<TraineeshipPosition> positions = traineeshipPositionService.findAssignedPositions();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Αξιολογήσεις Θέσεων Πρακτικής"));
        document.add(new Paragraph(" ")); 

        PdfPTable table = new PdfPTable(5); 
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        Stream.of("Θέση", "Φοιτητής", "Καθηγητής", "Εταιρεία", "Αποτέλεσμα")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(cell);
                });

        for (TraineeshipPosition pos : positions) {
            String student = pos.getAssignedStudent() != null
                    ? pos.getAssignedStudent().getFullName()
                    : "—";
            String professor = pos.getSupervisingProfessor() != null
                    ? pos.getSupervisingProfessor().getFullName()
                    : "—";
            String company = pos.getCompany() != null
                    ? pos.getCompany().getCompanyName()
                    : "—";
            String result = pos.getFinalResult() != null ? pos.getFinalResult() : "Σε εξέλιξη";

            table.addCell(pos.getTitle());
            table.addCell(student);
            table.addCell(professor);
            table.addCell(company);
            table.addCell(result);
        }

        document.add(table);
        document.close();
    }
    @GetMapping("/committee/summary")
    public String showSummary(Model model) {
        Map<String, Object> stats = traineeshipPositionService.getCommitteeStats();
        model.addAllAttributes(stats);
        return "committee_summary";
    }
    @PostMapping("/committee/reset")
    public String resetPosition(@RequestParam Long positionId, RedirectAttributes redirectAttributes) {
        traineeshipPositionService.resetPosition(positionId);
        redirectAttributes.addFlashAttribute("successMessage", "Το αποτέλεσμα διαγράφηκε. Μπορείτε να επανεκτιμήσετε τη θέση.");
        return "redirect:/committee/evaluations";
    }

    

}


