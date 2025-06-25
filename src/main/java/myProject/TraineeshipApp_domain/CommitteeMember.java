package myProject.TraineeshipApp_domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "committee_members")
@Getter
@Setter
@NoArgsConstructor
public class CommitteeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    @Column(nullable = false)
    private String fullName;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
 
	public void setFullName(String fullName) {
		this.fullName = fullName;
		
	}
}
