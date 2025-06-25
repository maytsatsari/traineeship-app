

package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_service.StudentService;
import myProject.Traineeship_service.ProfessorService;
import myProject.Traineeship_service.UserService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import myProject.TraineeshipApp_domain.CommitteeMember;
import myProject.TraineeshipApp_domain.Company;
import myProject.Traineeship_service.CommitteeService;
import myProject.Traineeship_service.CompanyService;
import myProject.Traineeship_service.CustomUserDetails;
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;
    
    @Autowired
    private CompanyService companyService;
   
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CommitteeService committeeService;

    
    //-------------- STUDENT -------------

    @GetMapping("/register-student")
    public String showStudentRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        return "register-student";
    }

    @PostMapping("/register-student")
    public String registerStudent(@ModelAttribute("student") Student student,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {

        if (student.getFirstName() == null || student.getFirstName().isEmpty()) {
            model.addAttribute("error", "Το πεδίο 'First Name' είναι υποχρεωτικό!");
            return "register-student";  
        }

        if (!student.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Οι κωδικοί δεν ταιριάζουν!");
            return "register-student";  
        }

        User user = new User();
        user.setEmail(student.getEmail());
        user.setPassword(student.getPassword());
        user.setRole("student");
        user.setUsername(student.getUsername()); 
        user.setFirstName(student.getFirstName());
        user.setLastName(student.getLastName());
        user.setFullName();

        userService.createUser(user);

        student.setUser(user);         
        student.setFullName(student.getFirstName() + " " + student.getLastName());
        studentService.save(student); 
        UserDetails userDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "login"; 
    }



    // ---------- PROFESSOR ---------
    @GetMapping("/register-professor")
    public String showProfessorForm(Model model) {
        model.addAttribute("professor", new Professor());
        return "register-professor";
    }


    @PostMapping("/register-professor")
    public String registerProfessor(@ModelAttribute("professor") Professor professor,
                                    @RequestParam("confirmPassword") String confirmPassword,
                                    @RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    @RequestParam("interestsInput") String interestsInput,
                                    Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Οι κωδικοί δεν ταιριάζουν!");
            return "register-professor";
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(professor.getUsername());
        user.setFirstName(professor.getFirstName());
        user.setLastName(professor.getLastName());
        user.setFullName();
        user.setPassword(password);
        user.setRole("professor");

        userService.createUser(user);
        professor.setUser(user);                 
        professor.setInterests(interestsInput.trim());
        professorService.save(professor);    
        return "login";
        
    }
    

    
    @GetMapping("/register-company")
    public String showCompanyRegisterForm(Model model) {
        model.addAttribute("company", new Company());
        return "register-company"; 
    }

 
    @PostMapping("/register-company")
    public String registerCompany(
            @ModelAttribute("company") Company company,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!company.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Οι κωδικοί δεν ταιριάζουν!");
            return "register-company";
        }

       
        User user = new User();
        user.setUsername(company.getUsername());
        user.setPassword(passwordEncoder.encode(company.getPassword()));
        user.setEmail(company.getEmail());
        user.setFirstName(company.getFirstName());
        user.setLastName(company.getLastName());
        user.setFullName();
        user.setRole("company");
        company.setUser(user);

        
        companyService.save(company);

        return "login";
    }

	 //-------committee----------
    @GetMapping("/register-committee")
    public String showCommitteeRegisterForm(Model model) {
        model.addAttribute("committeeMember", new CommitteeMember());
        return "register-committee";
    }

    
    @PostMapping("/register-committee")
    public String registerCommittee(
            @ModelAttribute("committeeMember") CommitteeMember committeeMember,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        User user = committeeMember.getUser();

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Οι κωδικοί δεν ταιριάζουν!");
            return "register-committee";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("committee");
        user.setFullName(); 
        userService.createUser(user);

        committeeMember.setUser(user);
        committeeMember.setFullName(user.getFullName()); 
        committeeService.save(committeeMember);

        return "committee_dashboard";
    }

	}