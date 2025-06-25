package myProject.TraineeshipApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = "myProject") // 👈 Εδώ το κλειδί
@EntityScan(basePackages = "myProject.TraineeshipApp_domain")
@EnableJpaRepositories(basePackages = "myProject.Traineeship_repository")
public class TraineeshipAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TraineeshipAppApplication.class, args);
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("1234");
        System.out.println(" New hash for 1234: " + hash);
   		

    }
}
