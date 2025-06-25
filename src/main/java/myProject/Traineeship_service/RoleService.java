package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.Role;
import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_repository.RoleRepository;
import myProject.Traineeship_repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
	
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
    	
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public User assignRoleToUser(String username, String roleName) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        Optional<Role> roleOpt = roleRepository.findByName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();
            
            
            if (user.getRole() != null) {
                throw new RuntimeException("User already has a role and it cannot be changed.");
            }
            
            
        }
        throw new RuntimeException("User or Role not found");
    }
}
