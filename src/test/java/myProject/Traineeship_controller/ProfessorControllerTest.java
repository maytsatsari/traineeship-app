package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.Professor;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_service.ProfessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import myProject.TraineeshipApp_domain.Evaluation;

import myProject.Traineeship_controller.ProfessorController;
import myProject.Traineeship_repository.EvaluationRepository;
import myProject.Traineeship_repository.TraineeshipPositionRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorControllerTest {

	 @Mock
	    private ProfessorService professorService;

	    @Mock
	    private TraineeshipPositionRepository traineeshipPositionRepository;

	    @Mock
	    private EvaluationRepository evaluationRepository;

	    @Mock
	    private Model model;

	    @Mock
	    private Principal principal;

	    @InjectMocks
	    private ProfessorController professorController;
    @Test
    void testProfessorDashboard() {
        when(principal.getName()).thenReturn("kpapadakis");

        Professor professor = new Professor();
        professor.setUsername("kpapadakis");

        List<TraineeshipPosition> mockPositions = List.of(new TraineeshipPosition(), new TraineeshipPosition());

        when(professorService.findByUsername("kpapadakis")).thenReturn(professor);
        when(traineeshipPositionRepository.findBySupervisingProfessor(professor)).thenReturn(mockPositions);

        String result = professorController.professorDashboard(model, principal);

        verify(model).addAttribute("positions", mockPositions);
        assertEquals("professor_dashboard", result);
    }
    @Test
    void testProfessorDashboardProfessorNotFound() {
        when(principal.getName()).thenReturn("unknown");

        when(professorService.findByUsername("unknown")).thenReturn(null);

        String result = professorController.professorDashboard(model, principal);

        assertEquals("error", result);
    }
    @Test
    void testSubmitEvaluationSuccess() {
        Long positionId = 1L;

        Student student = new Student();
        student.setUsername("maria");

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssignedStudent(student);

        when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String result = professorController.submitEvaluation(
                positionId,
                5, 4, 4, 5, 3,
                "Σχόλιο αξιολόγησης",
                principal,
                redirectAttributes
        );


        // Έλεγχος ότι αποθηκεύτηκε κάποια αξιολόγηση
        verify(evaluationRepository).save(any(Evaluation.class));

        // Επιβεβαιώνουμε redirect
        assertEquals("redirect:/professor_dashboard", result);
    }
    @Test
    void testSubmitEvaluationWithoutAssignedStudent() {
        Long positionId = 2L;

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssignedStudent(null); // ❌ no student assigned

        when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String result = professorController.submitEvaluation(
                positionId,
                2, 2, 2, 2, 2,
                null,
                principal,
                redirectAttributes
        );


        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"), anyString());
        assertEquals("redirect:/professor_dashboard", result);
    }

}
