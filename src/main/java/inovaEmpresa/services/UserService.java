package inovaEmpresa.services;

import inovaEmpresa.dto.user.UpdateUserType;
import inovaEmpresa.dto.user.UserDTO;
import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.policies.UserPolicy;
import inovaEmpresa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerCollaborator(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setType(UserType.COLLABORATOR.getValue());

        return userRepository.save(user);
    }

    public User updateUserStatus(Long id, UpdateUserType userUpdateDTO) {
        User userLogged = userRepository.findById(userUpdateDTO.getUserId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged user not found"));
        if (UserPolicy.canUpdateUser(userLogged)) {
            User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            if (userUpdateDTO.getUserType() != null) {
                UserType userType = UserType.fromString(userUpdateDTO.getUserType().name());
                user.setType(userType.getValue());
            }
            return userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario não é um admin");
        }
    }
}
