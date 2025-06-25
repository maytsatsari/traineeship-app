package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.Student;

import myProject.Traineeship_service.StudentService;
import myProject.Traineeship_service.TraineeshipPositionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.security.Principal;


@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private TraineeshipPositionService traineeshipPositionService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private StudentController studentController;

    private Student testStudent;

    @BeforeEach
    void setup() {
        testStudent = new Student();
        testStudent.setUsername("maria");
    }

   
    @Test
    void testShowDashboard() {
        when(principal.getName()).thenReturn("maria");
        when(studentService.findByUsername("maria")).thenReturn(testStudent);

        String viewName = studentController.showDashboard(model, principal, null); // <== Δώσε 3ο όρισμα

        verify(model).addAttribute("student", testStudent);
        assertEquals("student_dashboard", viewName);
    }

    
    @Test
    void testShowProfileForm() {
        when(principal.getName()).thenReturn("maria");
        when(studentService.findByUsername("maria")).thenReturn(testStudent);

        String viewName = studentController.showProfileForm(model, principal);

        verify(model).addAttribute("student", testStudent);
        assertEquals("student_edit_profile", viewName);
    }
    @Test
    void testSaveProfile() {
        when(principal.getName()).thenReturn("maria");

        Student existingStudent = new Student();
        existingStudent.setUsername("maria");

        when(studentService.findByUsername("maria")).thenReturn(existingStudent);

        Student formStudent = new Student();
        formStudent.setInterests("AI");
        formStudent.setSkills("Java, Spring");
        formStudent.setPreferredLocation("Athens");

        String result = studentController.saveProfile(formStudent, principal);

        assertEquals("AI", existingStudent.getInterests());
        assertEquals("Java, Spring", existingStudent.getSkills());
        assertEquals("Athens", existingStudent.getPreferredLocation());

        verify(studentService).save(existingStudent);
        assertEquals("redirect:/student_dashboard", result);
    }
    @Test
    void testShowLogbook() {
        when(principal.getName()).thenReturn("maria");

        Student maria = new Student();
        maria.setUsername("maria");
        maria.getLogbook().add("Κατέθεσα αναφορά");
        when(studentService.findByUsernameWithLogbook("maria")).thenReturn(maria);

        String viewName = studentController.showLogbook(model, principal);

        verify(model).addAttribute("student", maria);
        assertEquals("student_logbook", viewName);
    }
    @Test
    void testAddLogEntry() {
        when(principal.getName()).thenReturn("maria");

        Student maria = new Student();
        maria.setUsername("maria");
        maria.setLogbook(new java.util.ArrayList<>());
        when(studentService.findByUsernameWithLogbook("maria")).thenReturn(maria);

        String result = studentController.addLogEntry("Έγραψα κώδικα", principal);

        assertEquals(1, maria.getLogbook().size());
        String actualEntry = maria.getLogbook().get(0);
        assertTrue(actualEntry.contains("Έγραψα κώδικα")); // ✅ περιέχει το αναμενόμενο
        assertTrue(actualEntry.matches("\\d{4}-\\d{2}-\\d{2}: .*")); // ✅ ξεκινάει με ημερομηνία
        verify(studentService).save(maria);
        assertEquals("redirect:/student/logbook", result);
    }

    

}
