package com.localvitrine.controller;

import com.localvitrine.entity.Project;
import com.localvitrine.entity.ProjectStatus;
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
class AdminDashboardControllerTest {

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

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        businessProfileRepository.deleteAll();
        projectRepository.deleteAll();
        templateRepository.deleteAll();
        userRepository.deleteAll();

        Role userRole = roleRepository.findByName(RoleName.USER).orElseGet(() ->
                roleRepository.save(Role.builder().name(RoleName.USER).build()));
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseGet(() ->
                roleRepository.save(Role.builder().name(RoleName.ADMIN).build()));

        User admin = userRepository.save(User.builder()
                .fullName("Admin")
                .email("admin-stats@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(adminRole)
                .build());

        User user = userRepository.save(User.builder()
                .fullName("User")
                .email("user-stats@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(userRole)
                .build());

        Template active = templateRepository.save(Template.builder()
                .name("A")
                .code("a")
                .description("desc")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://example.com/a.png")
                .isActive(true)
                .build());
        templateRepository.save(Template.builder()
                .name("B")
                .code("b")
                .description("desc")
                .activityType(ActivityType.OTHER)
                .previewUrl("https://example.com/b.png")
                .isActive(false)
                .build());

        projectRepository.save(Project.builder()
                .title("P1")
                .status(ProjectStatus.DRAFT)
                .owner(user)
                .template(active)
                .build());

        adminToken = jwtService.generateToken(admin.getEmail(), RoleName.ADMIN);
        userToken = jwtService.generateToken(user.getEmail(), RoleName.USER);
    }

    @Test
    void adminCanGetStats() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard/stats")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(2))
                .andExpect(jsonPath("$.totalProjects").value(1))
                .andExpect(jsonPath("$.activeTemplates").value(1));
    }

    @Test
    void nonAdminCannotGetStats() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard/stats")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}
