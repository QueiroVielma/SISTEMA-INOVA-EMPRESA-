package inovaEmpresa.controllers;

import inovaEmpresa.dto.user.UpdateUserType;
import inovaEmpresa.dto.user.UserDTO;
import inovaEmpresa.entities.User;
import inovaEmpresa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/collaborators")
    public ResponseEntity<User> registerCollaborator(@RequestBody UserDTO userDTO) {
        User createdUser = userService.registerCollaborator(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/type/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @Validated @RequestBody UpdateUserType userUpdateDTO) {
        try {
            User updatedUser = userService.updateUserStatus(id, userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
