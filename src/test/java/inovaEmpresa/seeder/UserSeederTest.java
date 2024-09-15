package inovaEmpresa.seeder;

import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserSeederTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommandLineRunner seederRunner;

    @Test
    void testUserSeeder() throws Exception {
        seederRunner.run();
        User admin = userRepository.findByEmail("admin@example.com");
        assertNotNull(admin, "Administrador não foi criado.");
        assertEquals("Administrator", admin.getName(), "O nome do administrador está incorreto.");
        assertEquals("admin@example.com", admin.getEmail(), "O email do administrador está incorreto.");
        assertEquals(UserType.ADMIN.getValue(), admin.getType(), "O tipo de usuário do administrador está incorreto.");
    }
}
