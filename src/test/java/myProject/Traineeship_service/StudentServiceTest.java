package myProject.Traineeship_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import myProject.TraineeshipApp_domain.LogEntry;
import myProject.TraineeshipApp_domain.Position;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_repository.LogEntryRepository;
import myProject.Traineeship_repository.PositionRepository;
import myProject.Traineeship_repository.StudentRepository;
import myProject.Traineeship_repository.TraineeshipPositionRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private LogEntryRepository logEntryRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void testFindByUsername() {
        String username = "maria";

        Student mockStudent = new Student();
        mockStudent.setUsername(username);

        when(studentRepository.findByUserUsername(username)).thenReturn(mockStudent);

        Student result = studentService.findByUsername(username);

        verify(studentRepository).findByUserUsername(username);
        assertEquals(username, result.getUsername());
    }
    @BeforeEach
    void injectDependencies() throws Exception {
        Field field = StudentServiceImpl.class.getDeclaredField("traineeshipPositionRepository");
        field.setAccessible(true);
        field.set(studentService, traineeshipPositionRepository);
    }

    @Test
    void testAssignPositionToStudent() {
        Long studentId = 1L;
        Long positionId = 100L;

        Student student = new Student();
        student.setId(studentId);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(positionId);
        position.setAssignedStudent(null);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(positionId)).thenReturn(Optional.of(position));

        studentService.assignPositionToStudent(studentId, positionId);

        assertEquals(position, student.getAssignedPosition());
        assertEquals(student, position.getAssignedStudent());

        verify(studentRepository).save(student);
        verify(traineeshipPositionRepository).save(position);
    }
    @Test
    void testSaveStudent() {
        Student student = new Student();
        student.setFirstName("Maria");

        studentService.save(student);

        verify(studentRepository).save(student);
    }
    @Test
    void testFindById() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(studentRepository).findById(id);
    }
    @BeforeEach
    void injectPositionRepository() throws Exception {
        Field field = StudentServiceImpl.class.getDeclaredField("positionRepository");
        field.setAccessible(true);
        field.set(studentService, positionRepository);
    }

    @Test
    void testGetAllPositions() {
        List<Position> mockPositions = List.of(new Position(), new Position());

        when(positionRepository.findAll()).thenReturn(mockPositions);

        List<Position> result = studentService.getAllPositions();

        verify(positionRepository).findAll();
        assertEquals(2, result.size());
    }
    @Test
    void testGetLogEntriesForStudent() throws Exception {
        Long studentId = 1L;

        LogEntry log1 = mock(LogEntry.class);
        LogEntry log2 = mock(LogEntry.class);
        List<LogEntry> logs = List.of(log1, log2);

        when(logEntryRepository.findByStudentId(studentId)).thenReturn(logs);

        Field field = StudentServiceImpl.class.getDeclaredField("logEntryRepository");
        field.setAccessible(true);
        field.set(studentService, logEntryRepository);

        List<LogEntry> result = studentService.getLogEntriesForStudent(studentId);

        verify(logEntryRepository).findByStudentId(studentId);
        assertEquals(2, result.size());
    }

}
