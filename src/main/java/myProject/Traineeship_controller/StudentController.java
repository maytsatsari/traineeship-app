package myProject.Traineeship_controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_service.StudentService;
import myProject.Traineeship_service.TraineeshipPositionService;


@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TraineeshipPositionService ΤraineeshipPositionService;
    
    @Autowired
    private TraineeshipPositionService traineeshipPositionService;



    @GetMapping("/student/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student());
        return "student_register";
    }

  
    @GetMapping("/student_edit_profile")
    public String showProfileForm(Model model, Principal principal) {
        Student student = studentService.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "student_edit_profile";
    }

    @PostMapping("/student_edit_profile")
    public String saveProfile(@ModelAttribute Student studentForm, Principal principal) {
        Student student = studentService.findByUsername(principal.getName());

        student.setInterests(studentForm.getInterests());
        student.setSkills(studentForm.getSkills());
        student.setPreferredLocation(studentForm.getPreferredLocation());

        studentService.save(student);
        return "redirect:/student_dashboard";
    }

    

 
    @GetMapping("/apply-for-traineeship")
    public String applyForTraineeship(Model model,
                                      @ModelAttribute("successMessage") String successMessage) {
        model.addAttribute("positions", studentService.getAllPositions());

        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }

        return "applyForTraineeship";
    }


 

    @GetMapping("/student/logbook")
    public String showLogbook(Model model, Principal principal) {
        Student student = studentService.findByUsernameWithLogbook(principal.getName());
        model.addAttribute("student", student);
        return "student_logbook";
    }


    @PostMapping("/student/logbook")
    public String addLogEntry(@RequestParam String entry, Principal principal) {
        Student student = studentService.findByUsernameWithLogbook(principal.getName());
        if (entry != null && !entry.trim().isEmpty()) {
        	String date = LocalDate.now().toString();  // π.χ. 2025-05-19
            String logLine = date + ": " + entry;

            student.getLogbook().add(logLine);
            studentService.save(student);
        }
        return "redirect:/student/logbook";
    }

    

    
    @GetMapping("/student_dashboard")
    public String showDashboard(Model model, Principal principal,
                                @RequestParam(required = false) String msg) {

        Student student = studentService.findByUsername(principal.getName());
        if (student == null) return "error";

        model.addAttribute("student", student);
        model.addAttribute("position", student.getAssignedPosition());

        if (msg != null && !msg.isBlank()) {
            model.addAttribute("message", msg); 
        }

        return "student_dashboard";
    }

    
    @GetMapping("/student/available-positions")
    public String viewAvailablePositions(Model model) {
        List<TraineeshipPosition> availablePositions = ΤraineeshipPositionService.findUnassignedPositions();
        model.addAttribute("positions", availablePositions);
        return "student_available_positions";
    }

    
    @GetMapping("/student/apply-form")
    public String showApplyForm(@RequestParam("positionId") Long positionId,
                                Principal principal,
                                Model model,
                                @ModelAttribute("successMessage") String successMessage,
                                @ModelAttribute("errorMessage") String errorMessage) {

        Student student = studentService.findByUsername(principal.getName());
        TraineeshipPosition position = traineeshipPositionService.findById(positionId);

        model.addAttribute("student", student);
        model.addAttribute("position", position);

        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "applyForTraineeship";
    }


    

    @PostMapping("/student/submit-application")
    public String submitApplication(@RequestParam("positionId") Long positionId,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
        Student student = studentService.findByUsername(principal.getName());

        traineeshipPositionService.applyStudentToPosition(student,positionId);

        student.setHasApplied(true);
        studentService.save(student);
        redirectAttributes.addFlashAttribute("successMessage", "Η αίτησή σας υποβλήθηκε με επιτυχία!");
        return "redirect:/student_dashboard";

    }
    @GetMapping("/student/logbook/export-pdf")
    public void exportLogbookPdf(HttpServletResponse response, Principal principal) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=logbook.pdf");

        Student student = studentService.findByUsernameWithLogbook(principal.getName());
        List<String> entries = student.getLogbook();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("📘 Ημερολόγιο Πρακτικής - " + student.getFullName()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.addCell("Ημερομηνία");
        table.addCell("Περιγραφή");

        for (String entry : entries) {
            String[] parts = entry.split(":", 2);
            String date = parts.length > 1 ? parts[0] : "-";
            String description = parts.length > 1 ? parts[1].trim() : entry;
            table.addCell(date);
            table.addCell(description);
        }

        document.add(table);
        document.add(new Paragraph(" "));

        // ➕ Υπογραφές
        Paragraph signatures = new Paragraph();
        signatures.add("\n\n\nΥπογραφή Φοιτητή: _______________________________");
        signatures.add("\n\nΥπογραφή Επόπτη Καθηγητή: __________________________");
        signatures.add("\n\nΥπογραφή Υπευθύνου Εταιρείας: __________________________");

        document.add(signatures);
        
        document.close(); 
    }


}

