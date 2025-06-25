package myProject.TraineeshipApp_domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CompanyEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Integer studentMotivation;
    private Integer studentEffectiveness;
    private Integer studentEfficiency;
    private Integer rating; 

    private LocalDate date;

    private String comments;

    
    public Long getId() {
    	return id; 
    }

    public Position getPosition() { 
    	return position; 
    }

    public Student getStudent() { 
    	return student; 
    }

    public Integer getCompanyMotivation() { 
    	return studentMotivation; 
    }

    public Integer getCompanyEffectiveness() { 
    	return studentEffectiveness; 
    }

    public Integer getCompanyEfficiency() { 
    	return studentEfficiency; 
    }

    public LocalDate getDate() { 
    	return date; 
    }

    public String getComments() { 
    	return comments; 
    }
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setId(Long id) { 
    	this.id = id; 
    }

    public void setPosition(Position position) { 
    	this.position = position; 
    }

    public void setStudent(Student student) { 
    	this.student = student; 
    }

    public void setCompanyMotivation(Integer companyMotivation) { 
    	this.studentMotivation = companyMotivation; 
    }

    public void setCompanyEffectiveness(Integer companyEffectiveness) { 
    	this.studentEffectiveness = companyEffectiveness; 
    }

    public void setCompanyEfficiency(Integer companyEfficiency) { 
    	this.studentEfficiency = companyEfficiency; 
    }

    public void setDate(LocalDate date) { 
    	this.date = date; 
    }

    public void setComments(String comments) { 
    	this.comments = comments; 
    }
    
    public Integer getMotivation() { return studentMotivation; }
    public Integer getEffectiveness() { return studentEffectiveness; }
    public Integer getEfficiency() { return studentEfficiency; }
}
