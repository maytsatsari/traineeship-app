package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.*;
import myProject.Traineeship_service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private StudentService studentService;
    @Mock
    private ProfessorService professorService;
    @Mock
    private CompanyService companyService;
    @Mock
    private CommitteeService committeeService;
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Mock
    private Model model;

    @InjectMocks
    private RegisterController registerController;

    @Test
    void testRegisterStudentSuccess() {
        Student student = new Student();
        student.setFirstName("Maria");
        student.setPassword("pass");
        student.setUsername("maria");
        student.setEmail("maria@example.com");

        String result = registerController.registerStudent(student, "pass", model);

        verify(userService).createUser(any(User.class));
        verify(studentService).save(student);
        assertEquals("login", result);

    }

    @Test
    void testRegisterProfessorSuccess() {
        Professor professor = new Professor();
        professor.setFirstName("Nikos");
        professor.setUsername("nikos");

        String result = registerController.registerProfessor(professor, "pass", "nikos@example.com", "pass", "AI, Java", model);

        verify(userService).createUser(any(User.class));
        verify(professorService).save(professor);
        assertEquals("login", result);

    }

    @Test
    void testRegisterCompanySuccess() {
        Company company = new Company();
        company.setUsername("tech");
        company.setPassword("secure");
        company.setEmail("info@tech.com");
        company.setFirstName("Anna");
        company.setLastName("CEO");

        when(passwordEncoder.encode("secure")).thenReturn("encodedPassword");

        String result = registerController.registerCompany(company, "secure", model);

        verify(companyService).save(company);
        assertEquals("login", result);

    }

   
}
