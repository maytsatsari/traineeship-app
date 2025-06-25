package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.Position;
import myProject.Traineeship_repository.TraineeshipPositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeshipPositionServiceTest {

    @Mock
    private TraineeshipPositionRepository positionRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private PositionService positionServiceMock;

    @InjectMocks
    private TraineeshipPositionServiceImpl positionServiceImpl;

    private Student student;
    private TraineeshipPosition position;

    @BeforeEach
    void setup() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("Maria");

        position = new TraineeshipPosition();
        position.setId(42L);
        position.setApplicants(new ArrayList<>());
    }

    @Test
    void testApplyStudentToPosition() {
        when(positionRepository.findWithApplicantsById(42L)).thenReturn(Optional.of(position));

        positionServiceImpl.applyStudentToPosition(student, 42L);

        ArgumentCaptor<TraineeshipPosition> captor = ArgumentCaptor.forClass(TraineeshipPosition.class);
        verify(positionRepository).save(captor.capture());

        TraineeshipPosition saved = captor.getValue();
        assertTrue(saved.getApplicants().contains(student));
    }

    @Test
    void testAssignPositionToStudent() {
        Long studentId = 1L;
        Long positionId = 100L;

        Student student = new Student();
        student.setId(studentId);
        student.setHasAssigned(false);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssigned(false);
        position.setApplicants(new ArrayList<>(List.of(student)));

        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        when(studentService.findById(studentId)).thenReturn(Optional.of(student));

        positionServiceImpl.assignPositionToStudent(studentId, positionId);

        assertTrue(position.isAssigned());
        assertEquals(student, position.getAssignedStudent());
        assertEquals(studentId, position.getStudentId());
        assertEquals(position, student.getAssignedPosition());
        assertTrue(student.isHasAssigned());

        verify(positionRepository).save(any(TraineeshipPosition.class));
        verify(studentService).save(student);
        verify(positionServiceMock).save(any(Position.class));
    }

    @Test
    void testFindMatchingPositionsWithEmptyStudentPreferences() {
       
        student.setInterests("");
        student.setPreferredLocation("");

        when(positionRepository.findByAssignedFalse()).thenReturn(Collections.emptyList());

        List<TraineeshipPosition> result = positionServiceImpl.findMatchingPositions(student);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(positionRepository).findByAssignedFalse();
    }
    @Test
    void testFindMatchingPositionsByInterests() {
        student.setInterests("AI, Java");
        student.setPreferredLocation(""); // Άδειο για να μη μετρήσει location

        TraineeshipPosition matchingPosition = new TraineeshipPosition();
        matchingPosition.setTopics_of_interest("Java, Databases");

        TraineeshipPosition nonMatchingPosition = new TraineeshipPosition();
        nonMatchingPosition.setTopics_of_interest("Design, Networks");

        when(positionRepository.findByAssignedFalse())
            .thenReturn(List.of(matchingPosition, nonMatchingPosition));

        List<TraineeshipPosition> result = positionServiceImpl.findMatchingPositions(student);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(matchingPosition));
        assertFalse(result.contains(nonMatchingPosition));
    }
    @Test
    void testFindMatchingPositionsByLocation() {
        student.setInterests(""); // δεν έχει interests
        student.setPreferredLocation("Athens");

        TraineeshipPosition matchingPosition = new TraineeshipPosition();
        matchingPosition.setLocation("Athens");

        TraineeshipPosition nonMatchingPosition = new TraineeshipPosition();
        nonMatchingPosition.setLocation("Thessaloniki");

        when(positionRepository.findByAssignedFalse())
            .thenReturn(List.of(matchingPosition, nonMatchingPosition));

        List<TraineeshipPosition> result = positionServiceImpl.findMatchingPositions(student);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(matchingPosition));
        assertFalse(result.contains(nonMatchingPosition));
    }

} 
