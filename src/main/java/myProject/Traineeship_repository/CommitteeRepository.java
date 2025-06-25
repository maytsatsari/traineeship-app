package myProject.Traineeship_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import myProject.TraineeshipApp_domain.CommitteeMember;

public interface CommitteeRepository extends JpaRepository<CommitteeMember, Long> {
    CommitteeMember findByUserUsername(String username);
}
