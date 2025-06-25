package myProject.Traineeship_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoleSelectionController {

    @GetMapping("/role-select")
    public String selectRoleForm() {
        return "role-select"; 
    }

    @PostMapping("/register/redirect")
    public String handleRoleSelection(@RequestParam("role") String role) {
    	System.out.println("Επιλέχθηκε ρόλος: " + role);

       
        switch (role) {
            case "student":
                return "redirect:/register-student";
            case "professor":
                return "redirect:/register-professor"; 
            case "company":
                return "redirect:/register-company"; 
            case "committee":
                return "redirect:/register-committee"; 
            default:
                return "redirect:/role-select"; 
        }
    }
}

