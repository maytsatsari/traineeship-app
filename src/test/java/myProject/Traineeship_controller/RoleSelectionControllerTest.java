package myProject.Traineeship_controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleSelectionControllerTest {

    private final RoleSelectionController controller = new RoleSelectionController();

    @Test
    void testRedirectStudent() {
        String result = controller.handleRoleSelection("student");
        assertEquals("redirect:/register-student", result);
    }

    @Test
    void testRedirectProfessor() {
        String result = controller.handleRoleSelection("professor");
        assertEquals("redirect:/register-professor", result);
    }

    @Test
    void testRedirectCompany() {
        String result = controller.handleRoleSelection("company");
        assertEquals("redirect:/register-company", result);
    }

    @Test
    void testRedirectCommittee() {
        String result = controller.handleRoleSelection("committee");
        assertEquals("redirect:/register-committee", result);
    }

    @Test
    void testRedirectInvalidRole() {
        String result = controller.handleRoleSelection("unknown");
        assertEquals("redirect:/role-select", result);
    }
}
