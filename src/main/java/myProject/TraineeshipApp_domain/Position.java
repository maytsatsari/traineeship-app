package myProject.TraineeshipApp_domain;

import jakarta.persistence.*;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private String duration;

    @ManyToOne
    @JoinColumn(name = "company_id") 
    private Company company;

    @OneToOne
    private Student student;

    private boolean isAssigned;

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

    public String getDuration() { 
    	return duration; 
    }
    public void setDuration(String duration) { 
    	this.duration = duration; }

    public Company getCompany() { 
    	return company; 
    }
    public void setCompany(Company company) { 
    	this.company = company; 
    }

    public Student getStudent() { 
    	return student; 
    }
    public void setStudent(Student student) { 
    	this.student = student; 
    }

    public boolean isAssigned() { 
    	return isAssigned; 
    }
    public void setAssigned(boolean assigned) { 
    	isAssigned = assigned; 
    }
}