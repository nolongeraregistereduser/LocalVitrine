package com.localvitrine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.entity.ProjectStatus;
import com.localvitrine.entity.Role;
import com.localvitrine.entity.RoleName;
import com.localvitrine.entity.User;
import com.localvitrine.entity.UserStatus;
import com.localvitrine.repository.ProjectRepository;
import com.localvitrine.repository.RoleRepository;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectRepository projectRepository;

    private User userA;
    private User userB;
    private String tokenA;
    private String tokenB;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
        userRepository.deleteAll();

        Role userRole = roleRepository.findByName(RoleName.USER).orElseGet(() ->
                roleRepository.save(Role.builder().name(RoleName.USER).build()));

        userA = userRepository.save(User.builder()
                .fullName("User A")
                .email("user-a@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(userRole)
                .build());

        userB = userRepository.save(User.builder()
                .fullName("User B")
                .email("user-b@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(userRole)
                .build());

        tokenA = jwtService.generateToken(userA.getEmail(), RoleName.USER);
        tokenB = jwtService.generateToken(userB.getEmail(), RoleName.USER);
    }

    @Test
    void crudFlowForOwner() throws Exception {
        String createJson = objectMapper.writeValueAsString(new ProjectRequest(
                "My vitrine", ProjectStatus.DRAFT, "https://example.com"));

        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("My vitrine"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.publicUrl").value("https://example.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/projects").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].title").value("My vitrine"));

        mockMvc.perform(get("/api/projects/" + id).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My vitrine"));

        String updateJson = objectMapper.writeValueAsString(new ProjectRequest(
                "Updated", ProjectStatus.PUBLISHED, ""));

        mockMvc.perform(put("/api/projects/" + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.status").value("PUBLISHED"))
                .andExpect(jsonPath("$.publicUrl").doesNotExist());

        mockMvc.perform(delete("/api/projects/" + id).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.findAll()).isEmpty();
    }

    @Test
    void otherUserCannotAccessProject() throws Exception {
        String createJson = objectMapper.writeValueAsString(new ProjectRequest(
                "Secret", ProjectStatus.DRAFT, null));

        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/projects/" + id).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB))
                .andExpect(status().isNotFound());

        String updateJson = objectMapper.writeValueAsString(new ProjectRequest(
                "Hacked", ProjectStatus.PUBLISHED, null));

        mockMvc.perform(put("/api/projects/" + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/projects/" + id).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB))
                .andExpect(status().isNotFound());
    }

    @Test
    void listOnlyReturnsOwnProjects() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProjectRequest("A1", ProjectStatus.DRAFT, null))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProjectRequest("B1", ProjectStatus.DRAFT, null))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("A1"));
    }
}
