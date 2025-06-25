package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.LogEntry;
import myProject.TraineeshipApp_domain.Student;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.Traineeship_repository.LogEntryRepository;
import myProject.Traineeship_repository.PositionRepository;
import myProject.Traineeship_repository.StudentRepository;
import myProject.Traineeship_repository.TraineeshipPositionRepository;

import java.util.List;
import java.util.Optional;

import myProject.TraineeshipApp_domain.Position;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
   
    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    @Autowired
    private TraineeshipPositionRepository traineeshipPositionRepository;


    @Transactional
    @Override
    public Student findByUsername(String username) {
        return studentRepository.findByUserUsername(username);
    }

    
    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }
    
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private LogEntryRepository logEntryRepository;

    
    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public List<LogEntry> getLogEntriesForStudent(Long studentId) {
        return logEntryRepository.findByStudentId(studentId);
    }
    public Student findByUsernameWithLogbook(String username) {
        return studentRepository.findByUsernameWithLogbook(username);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    public List<Student> findAllWithApplication() {
        return studentRepository.findByHasAssignedTrue();
    }
    
    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    @Override
    public void assignPositionToStudent(Long studentId, Long positionId) {
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Φοιτητής με ID " + studentId + " δεν βρέθηκε"));

        System.out.println("Ψάχνω για θέση με ID: " + positionId);  // Εμφάνιση της τιμής του positionId

        
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Θέση πρακτικής άσκησης με ID " + positionId + " δεν βρέθηκε"));

        
        if (position.getAssignedStudent() != null) {
            throw new RuntimeException("Η θέση είναι ήδη ανατεθειμένη σε άλλον φοιτητή.");
        }

        
        student.setAssignedPosition(position); 
        position.setAssignedStudent(student);

        
        studentRepository.save(student);
        traineeshipPositionRepository.save(position);
    }
    @Override
    public Student findByUsernameWithAssignedPosition(String username) {
        return studentRepository.findByUsernameWithAssignedPosition(username);
    }





}
