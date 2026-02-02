package com.localvitrine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localvitrine.dto.BusinessProfileRequest;
import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.entity.ProjectStatus;
import com.localvitrine.entity.Role;
import com.localvitrine.entity.RoleName;
import com.localvitrine.entity.User;
import com.localvitrine.entity.UserStatus;
import com.localvitrine.repository.BusinessProfileRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BusinessProfileControllerTest {

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

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    private User userA;
    private User userB;
    private String tokenA;
    private String tokenB;

    @BeforeEach
    void setUp() {
        businessProfileRepository.deleteAll();
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

    private long createProjectForUserA() throws Exception {
        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProjectRequest("Shop", ProjectStatus.DRAFT, null))))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(createResponse).get("id").asLong();
    }

    private static BusinessProfileRequest sampleRequest() {
        return new BusinessProfileRequest(
                "Boulangerie Dupont",
                "Lyon",
                "Artisan boulanger depuis 1990.",
                "0478000000",
                "contact@dupont.fr",
                "Augmenter les visites en magasin",
                "Alimentation"
        );
    }

    @Test
    void createGetUpdateAndConflict() throws Exception {
        long projectId = createProjectForUserA();

        mockMvc.perform(get("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        String body = objectMapper.writeValueAsString(sampleRequest());

        mockMvc.perform(post("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.businessName").value("Boulangerie Dupont"))
                .andExpect(jsonPath("$.city").value("Lyon"));

        mockMvc.perform(get("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.businessName").value("Boulangerie Dupont"));

        BusinessProfileRequest updated = new BusinessProfileRequest(
                "Boulangerie Dupont SA",
                "Lyon",
                "Nouvelle description.",
                "0478111111",
                "hello@dupont.fr",
                "Fideliser la clientele",
                "Boulangerie"
        );

        mockMvc.perform(put("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.businessName").value("Boulangerie Dupont SA"))
                .andExpect(jsonPath("$.phone").value("0478111111"));

        mockMvc.perform(post("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void otherUserCannotAccessProfile() throws Exception {
        long projectId = createProjectForUserA();

        mockMvc.perform(post("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/projects/" + projectId + "/business-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isNotFound());
    }
}
