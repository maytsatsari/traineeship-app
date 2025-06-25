package myProject.TraineeshipApp_domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "traineeship_positions")
public class TraineeshipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private String duration;

    @Column(length = 1000)
    private String requiredSkills;
    private String topics_of_interest;

  
    @Column(nullable = false)
    private boolean assigned = false;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "final_result")
    private String finalResult;  

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "supervising_professor_id")
    private Professor supervisingProfessor;

    @OneToOne(mappedBy = "assignedPosition")
    private Student assignedStudent;

    @ManyToMany
    @JoinTable(
        name = "position_applications",
        joinColumns = @JoinColumn(name = "position_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> applicants = new ArrayList<>();

  
    public Long getId() { 
    	return id;
    }
    public void setId(Long id) { 
    	this.id = id;
    }

    public String getTitle() { 
    	return title; 
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }

    public String getDescription() { 
    	return description; 
    }
    
    public void setDescription(String description) { 
    	this.description = description;
    }

    public String getLocation() { 
    	return location;
    }
    
    public void setLocation(String location) { 
    	this.location = location;
    }

    public String getDuration(){ 
    	return duration; 
    }
    
    public void setDuration(String duration) {
    	this.duration = duration;
    }

    public String getRequiredSkills() { 
    	return requiredSkills;
    }
    
    public void setRequiredSkills(String requiredSkills){ 
    	this.requiredSkills = requiredSkills;
    }

    public String getTopics_of_interest() { 
    	return topics_of_interest; 
    }
    
    public void setTopics_of_interest(String topics_of_interest) { 
    	this.topics_of_interest = topics_of_interest; 
    }

    public boolean isAssigned() { 
    	return assigned;
    }
    
    public void setAssigned(boolean assigned){ 
    	this.assigned = assigned; 
    }

    public Long getStudentId() { 
    	return studentId;
    }
    
    public void setStudentId(Long studentId) { 
    	this.studentId = studentId; 
    }

    public String getFinalResult() { 
    	return finalResult; 
    }
    
    public void setFinalResult(String finalResult){ 
    	this.finalResult = finalResult;
    }

    public Company getCompany() { 
    	return company; 
    }
    
    public void setCompany(Company company) 
    { 
    	this.company = company;
    }

    public Professor getSupervisingProfessor() { 
    	return supervisingProfessor; 
    }
    
    public void setSupervisingProfessor(Professor supervisingProfessor){ 
    	this.supervisingProfessor = supervisingProfessor;
    }

    public Student getAssignedStudent() { 
    	return assignedStudent; 
    }
    
    public void setAssignedStudent(Student assignedStudent) { 
    	this.assignedStudent = assignedStudent;
    }

    public List<Student> getApplicants() { 
    	return applicants; 
    }
    public void setApplicants(List<Student> applicants) { 
    	this.applicants = applicants;
    }
}
