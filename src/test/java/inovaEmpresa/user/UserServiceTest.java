package inovaEmpresa.user;

import inovaEmpresa.dto.user.UpdateUserType;
import inovaEmpresa.dto.user.UserDTO;
import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.repositories.UserRepository;
import inovaEmpresa.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterCollaborator() {

        MockitoAnnotations.openMocks(this);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("password123");

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword("encodedPassword");
        user.setType(UserType.COLLABORATOR.getValue());

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerCollaborator(userDTO);

        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(UserType.COLLABORATOR.getValue(), result.getType());
    }

    @Test
    void testUpdateUserStatus() {
        User userLogged = new User();
        userLogged.setId(1L);
        userLogged.setType(UserType.ADMIN.getValue());

        User user = new User();
        user.setId(2L);

        UpdateUserType updateUserDTO = new UpdateUserType();
        updateUserDTO.setUserId(1L);
        updateUserDTO.setUserType(UserType.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userLogged));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        User updatedUser = userService.updateUserStatus(2L, updateUserDTO);
        assertEquals(UserType.ADMIN.getValue(), updatedUser.getType());
    }

    @Test
    void testUpdateUserStatus_NotFound() {
        UpdateUserType updateUserDTO = new UpdateUserType();
        updateUserDTO.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.updateUserStatus(2L, updateUserDTO));
    }
}