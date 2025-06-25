package myProject.TraineeshipApp_domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Integer motivation;
    private Integer effectiveness;
    private Integer efficiency;
    private Integer facilities;
    private Integer guidance;

    @Column(length = 1000)
    private String comments;

    @Column(nullable = false)
    private Integer rating; 

   
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private TraineeshipPosition position;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = true)
    private Professor professor;


    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public Integer getMotivation() {
        return motivation;
    }

    public void setMotivation(Integer motivation) {
        this.motivation = motivation;
    }

    public Integer getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(Integer effectiveness) {
        this.effectiveness = effectiveness;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Integer efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getFacilities() {
        return facilities;
    }

    public void setFacilities(Integer facilities) {
        this.facilities = facilities;
    }

    public Integer getGuidance() {
        return guidance;
    }

    public void setGuidance(Integer guidance) {
        this.guidance = guidance;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public TraineeshipPosition getPosition() {
        return position;
    }

    public void setPosition(TraineeshipPosition position) {
        this.position = position;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
