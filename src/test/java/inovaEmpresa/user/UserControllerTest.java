package inovaEmpresa.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import inovaEmpresa.controllers.UserController;
import inovaEmpresa.dto.user.UpdateUserType;
import inovaEmpresa.dto.user.UserDTO;
import inovaEmpresa.entities.User;
import inovaEmpresa.enums.UserType;
import inovaEmpresa.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UpdateUserType updateUserDTO;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterCollaborator() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("password123");

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword("encodedPassword");
        user.setType(UserType.COLLABORATOR.getValue());
        when(userService.registerCollaborator(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/collaborators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(UserType.COLLABORATOR.getValue()));
    }

    @Test
    void testUpdateStatus_UserNotFound() throws Exception {
        String requestBody = "{ \"userId\": 9999, \"userType\": \"ADMIN\" }";
        when(userService.updateUserStatus(anyLong(), any(UpdateUserType.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        MvcResult result = mockMvc.perform(put("/api/users/type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("User not found", responseBody);
    }

    @Test
    void testUpdateStatus_Forbidden() throws Exception {
        String requestBody = "{ \"userId\": 2, \"userType\": \"COLLABORATOR\" }";

        when(userService.updateUserStatus(anyLong(), any(UpdateUserType.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario não é um admin"));

        MvcResult result = mockMvc.perform(put("/api/users/type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("Usuario não é um admin", responseBody);
    }

    @Test
    void testUpdateStatus_Ok() throws Exception {
        String requestBody = "{ \"userId\": 2, \"userType\": \"ADMIN\" }";
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setType(UserType.ADMIN.getValue());

        when(userService.updateUserStatus(eq(1L), any(UpdateUserType.class)))
                .thenReturn(mockUser);

        mockMvc.perform(put("/api/users/type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value(UserType.ADMIN.getValue()));
    }

    @Test
    void testUpdateStatus_ServerError() throws Exception {
        String requestBody = "{ \"userId\": 2, \"userType\": \"ADMIN\" }";

        when(userService.updateUserStatus(anyLong(), any(UpdateUserType.class)))
                .thenThrow(new RuntimeException("Internal server error"));

        MvcResult result = mockMvc.perform(put("/api/users/type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("Internal Server Error", responseBody);
    }
}