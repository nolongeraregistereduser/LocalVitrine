package com.localvitrine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localvitrine.dto.AdminTemplateRequest;
import com.localvitrine.entity.Role;
import com.localvitrine.entity.RoleName;
import com.localvitrine.entity.Project;
import com.localvitrine.entity.ProjectStatus;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminTemplateControllerTest {

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
                .email("admin@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(adminRole)
                .build());

        User user = userRepository.save(User.builder()
                .fullName("User")
                .email("user@test.com")
                .password(passwordEncoder.encode("password"))
                .status(UserStatus.ACTIVE)
                .role(userRole)
                .build());

        adminToken = jwtService.generateToken(admin.getEmail(), RoleName.ADMIN);
        userToken = jwtService.generateToken(user.getEmail(), RoleName.USER);
    }

    @Test
    void adminCrudFlowAndStatusToggle() throws Exception {
        AdminTemplateRequest create = new AdminTemplateRequest(
                "Nova",
                "nova",
                "Template moderne",
                ActivityType.SERVICES,
                "https://example.com/nova.png",
                "<section><h1>{{businessName}}</h1></section>",
                "h1{color:#222;}");

        String createRes = mockMvc.perform(post("/api/admin/templates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").value("nova"))
                .andExpect(jsonPath("$.starterHtml").value("<section><h1>{{businessName}}</h1></section>"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(createRes).get("id").asLong();

        mockMvc.perform(get("/api/admin/templates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id));

        AdminTemplateRequest update = new AdminTemplateRequest(
                "Nova Prime",
                "nova-prime",
                "Template modernise",
                ActivityType.RETAIL,
                "https://example.com/nova-prime.png",
                "<section><h1>Updated {{businessName}}</h1></section>",
                "h1{color:#333;}");

        mockMvc.perform(put("/api/admin/templates/" + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nova Prime"))
                .andExpect(jsonPath("$.code").value("nova-prime"))
                .andExpect(jsonPath("$.starterCss").value("h1{color:#333;}"))
                .andExpect(jsonPath("$.activityType").value("RETAIL"));

        mockMvc.perform(patch("/api/admin/templates/" + id + "/deactivate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false));

        mockMvc.perform(patch("/api/admin/templates/" + id + "/activate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void userCannotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/admin/templates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void duplicateTemplateCodeReturnsConflict() throws Exception {
        templateRepository.save(Template.builder()
                .name("Base")
                .code("base")
                .description("desc")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://example.com/base.png")
                .isActive(true)
                .build());

        AdminTemplateRequest duplicate = new AdminTemplateRequest(
                "Another",
                "base",
                "desc",
                ActivityType.RESTAURANT,
                "https://example.com/a.png",
                "<section>dup</section>",
                "");

        mockMvc.perform(post("/api/admin/templates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteTemplateWorksWhenNotInUse() throws Exception {
        Template template = templateRepository.save(Template.builder()
                .name("Del")
                .code("del")
                .description("desc")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://example.com/del.png")
                .isActive(true)
                .build());

        mockMvc.perform(delete("/api/admin/templates/" + template.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTemplateReturnsConflictWhenInUse() throws Exception {
        User owner = userRepository.findByEmail("user@test.com").orElseThrow();
        Template template = templateRepository.save(Template.builder()
                .name("Used")
                .code("used")
                .description("desc")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://example.com/used.png")
                .isActive(true)
                .build());

        projectRepository.save(Project.builder()
                .title("Project")
                .status(ProjectStatus.DRAFT)
                .owner(owner)
                .template(template)
                .build());

        mockMvc.perform(delete("/api/admin/templates/" + template.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andExpect(status().isConflict());
    }
}
