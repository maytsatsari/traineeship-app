package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.CommitteeMember;

public interface CommitteeService {
	
    CommitteeMember findByUsername(String username);
    void save(CommitteeMember member);
}
