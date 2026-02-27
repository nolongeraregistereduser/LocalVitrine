package com.localvitrine.controller;

import com.localvitrine.entity.Role;
import com.localvitrine.entity.RoleName;
import com.localvitrine.entity.Template;
import com.localvitrine.entity.User;
import com.localvitrine.entity.UserStatus;
import com.localvitrine.enums.ActivityType;
import com.localvitrine.repository.BusinessProfileRepository;
import com.localvitrine.repository.ProjectRepository;
import com.localvitrine.repository.RoleRepository;
import com.localvitrine.repository.TemplateRepository;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    private String token;
    private Template active;
    private Template inactive;

    @BeforeEach
    void setUp() {
        businessProfileRepository.deleteAll();
        projectRepository.deleteAll();
        templateRepository.deleteAll();
        userRepository.deleteAll();

        Role userRole = roleRepository.findByName(RoleName.USER).orElseGet(() ->
                roleRepository.save(Role.builder().name(RoleName.USER).build()));

        User user = userRepository.save(User.builder()
                .fullName("Template User")
                .email("templates-user@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(userRole)
                .build());

        token = jwtService.generateToken(user.getEmail(), RoleName.USER);

        active = templateRepository.save(Template.builder()
                .name("Active One")
                .code("active-one")
                .description("Desc")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://example.com/preview.png")
                .isActive(true)
                .build());

        inactive = templateRepository.save(Template.builder()
                .name("Inactive")
                .code("inactive-one")
                .description("Hidden")
                .activityType(ActivityType.OTHER)
                .previewUrl("https://example.com/hidden.png")
                .isActive(false)
                .build());
    }

    @Test
    void listReturnsOnlyActiveTemplates() throws Exception {
        mockMvc.perform(get("/api/templates").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(active.getId()))
                .andExpect(jsonPath("$[0].code").value("active-one"));
    }

    @Test
    void getByIdReturnsActiveTemplate() throws Exception {
        mockMvc.perform(get("/api/templates/" + active.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Active One"))
                .andExpect(jsonPath("$.activityType").value("SERVICES"));
    }

    @Test
    void getByIdReturns404ForInactiveTemplate() throws Exception {
        mockMvc.perform(get("/api/templates/" + inactive.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
