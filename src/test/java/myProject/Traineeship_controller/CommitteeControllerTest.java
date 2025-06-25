package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.CommitteeMember;
import myProject.TraineeshipApp_domain.Evaluation;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_service.CommitteeService;
import myProject.Traineeship_service.EvaluationService;
import myProject.Traineeship_service.StudentService;
import myProject.Traineeship_service.TraineeshipPositionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Set;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeControllerTest {

    @Mock
    private CommitteeService committeeService;

    @Mock
    private StudentService studentService;

    @Mock
    private EvaluationService evaluationService;

    @Mock
    private TraineeshipPositionService traineeshipPositionService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    private CommitteeController committeeController;

   

    @BeforeEach
    void setUp() throws Exception {
        committeeController = new CommitteeController(traineeshipPositionService);

        setPrivateField(committeeController, "committeeService", committeeService);
        setPrivateField(committeeController, "studentService", studentService);
        setPrivateField(committeeController, "evaluationService", evaluationService);
    }

    // utility method
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }


    @Test
    void testShowCommitteeDashboard() {
        when(principal.getName()).thenReturn("kommember");

        User mockUser = new User();
        mockUser.setUsername("kommember");

        CommitteeMember committee = new CommitteeMember();
        committee.setUser(mockUser);

        when(committeeService.findByUsername("kommember")).thenReturn(committee);

        String result = committeeController.showDashboard(model, principal);

        verify(model).addAttribute("committee", committee);
        assertEquals("committee_dashboard", result);
    }
    @Test
    void testShowStudents() {
        Student s1 = new Student();
        s1.setFirstName("Maria");

        Student s2 = new Student();
        s2.setFirstName("Nikos");

        // Δύο θέσεις, ο Nikos εμφανίζεται και στις δύο
        TraineeshipPosition p1 = mock(TraineeshipPosition.class);
        when(p1.getApplicants()).thenReturn(List.of(s1, s2));

        TraineeshipPosition p2 = mock(TraineeshipPosition.class);
        when(p2.getApplicants()).thenReturn(List.of(s2));



        List<TraineeshipPosition> positions = List.of(p1, p2);
        when(traineeshipPositionService.findAllWithApplicants()).thenReturn(positions);

        String result = committeeController.showStudents(model);

        // Ελέγχουμε ότι μπήκαν στο model
        verify(model).addAttribute(eq("positions"), eq(positions));
        verify(model).addAttribute(eq("uniqueStudents"), any(Set.class));
        assertEquals("committee_students", result);
    }
    @Test
    void testViewAllApplications() {
        TraineeshipPosition pos1 = new TraineeshipPosition();
        TraineeshipPosition pos2 = new TraineeshipPosition();

        List<TraineeshipPosition> mockPositions = List.of(pos1, pos2);

        when(traineeshipPositionService.findAllWithApplicants()).thenReturn(mockPositions);

        String result = committeeController.viewAllApplications(model);

        verify(model).addAttribute("positions", mockPositions);
        assertEquals("committee_position_applicants", result);
    }
    @Test
    void testShowMatchingPositions() {
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("Maria");

        TraineeshipPosition pos1 = new TraineeshipPosition();
        TraineeshipPosition pos2 = new TraineeshipPosition();
        List<TraineeshipPosition> mockPositions = List.of(pos1, pos2);

        when(studentService.findById(studentId)).thenReturn(Optional.of(student));
        when(traineeshipPositionService.findMatchingPositions(student)).thenReturn(mockPositions);

        String result = committeeController.showMatchingPositions(studentId, model);

        verify(model).addAttribute("student", student);
        verify(model).addAttribute("positions", mockPositions);
        assertEquals("matching-positions", result);
    }

    @Test
    void testAssignPositionToStudent() {
        Long studentId = 1L;
        Long positionId = 42L;

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String result = committeeController.assignPositionToStudent(studentId, positionId, redirectAttributes);

        verify(traineeshipPositionService).assignPositionToStudent(studentId, positionId);
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), anyString());
        assertEquals("redirect:/matching-positions/" + studentId, result);
    }
    @Test
    void testShowEvaluationFormSuccess() {
        Long positionId = 1L;

        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();
        position.setAssignedStudent(student);

        when(traineeshipPositionService.findById(positionId)).thenReturn(position);

        String result = committeeController.showEvaluationForm(positionId, model);

        verify(model).addAttribute(eq("evaluation"), any(Evaluation.class));


        assertEquals("company_evaluation_form", result);
    }

    @Test
    void testFinalizeManually_setsPassResult() {
        Long positionId = 1L;

        MockHttpServletRequest request = new MockHttpServletRequest();
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = committeeController.finalizeManually(positionId, "PASS", redirectAttributes);

        verify(traineeshipPositionService).setFinalResult(positionId, "PASS");
        assertEquals("redirect:/committee/evaluations", result);
    }
    @Test
    void testResetPosition() {
     
        Long positionId = 1L;
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

    
        String result = committeeController.resetPosition(positionId, redirectAttributes);


        verify(traineeshipPositionService).resetPosition(positionId);
        assertEquals("redirect:/committee/evaluations", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("successMessage"));
    }

}
