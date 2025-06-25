package myProject.TraineeshipApp_domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_entries")
@Getter
@Setter
@NoArgsConstructor
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

   
    public LogEntry(String message, LocalDateTime timestamp, Student student) {
        this.message = message;
        this.timestamp = timestamp;
        this.student = student;
    }
}
