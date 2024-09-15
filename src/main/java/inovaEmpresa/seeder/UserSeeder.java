package inovaEmpresa.seeder;

import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserSeeder {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            if (userRepository.findByEmail("admin@example.com") == null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                User admin = new User();
                admin.setName("Administrator");
                admin.setEmail("admin@example.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setType(UserType.ADMIN.getValue());
                userRepository.save(admin);

                System.out.println("Administrador criado com sucesso.");
            } else {
                System.out.println("Usuário administrador já criado no sistema.");
            }
        };
    }
}
