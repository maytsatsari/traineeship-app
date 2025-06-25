package myProject.Traineeship_service;

import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  // Μπορούμε να τον ορίσουμε ως μέλος της κλάσης

    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(" Trying to authenticate: " + username);

        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.out.println(" User not found: " + username);
                return new UsernameNotFoundException("User not found with username: " + username);
            });

        
        System.out.println(" Loaded user: " + user.getUsername());       
        System.out.println(" Check password match for '1234': " + encoder.matches("1234", user.getPassword()));
        System.out.println(" Password (hashed): " + user.getPassword());
        
        
        if (user.getRole() != null) {
        	System.out.println(" Role: " + user.getRole());
  
        } else {
            System.out.println(" User has no role assigned!");
        }

        return new CustomUserDetails(user); 
    }
}
