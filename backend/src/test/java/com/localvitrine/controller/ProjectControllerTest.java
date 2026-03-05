package com.localvitrine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localvitrine.dto.ProjectContentRequest;
import com.localvitrine.dto.ProjectRequest;
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

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    @Autowired
    private TemplateRepository templateRepository;

    private User userA;
    private User userB;
    private String tokenA;
    private String tokenB;

    @BeforeEach
    void setUp() {
        businessProfileRepository.deleteAll();
        projectRepository.deleteAll();
        templateRepository.deleteAll();
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

    @Test
    void ownerCanAssignActiveTemplate() throws Exception {
        Template template = templateRepository.save(Template.builder()
                .name("T1")
                .code("t1")
                .description("D")
                .activityType(ActivityType.RETAIL)
                .previewUrl("https://example.com/p.png")
                .isActive(true)
                .build());

        String createJson = objectMapper.writeValueAsString(new ProjectRequest(
                "Proj", ProjectStatus.DRAFT, null));

        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long projectId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(put("/api/projects/" + projectId + "/template/" + template.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.templateId").value(template.getId()))
                .andExpect(jsonPath("$.templateName").value("T1"))
                .andExpect(jsonPath("$.templateCode").value("t1"));
    }

    @Test
    void otherUserCannotAssignTemplateToForeignProject() throws Exception {
        Template template = templateRepository.save(Template.builder()
                .name("T2")
                .code("t2")
                .description("D")
                .activityType(ActivityType.SERVICES)
                .previewUrl("https://example.com/p2.png")
                .isActive(true)
                .build());

        String createJson = objectMapper.writeValueAsString(new ProjectRequest(
                "Mine", ProjectStatus.DRAFT, null));

        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long projectId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(put("/api/projects/" + projectId + "/template/" + template.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignTemplateReturns404ForInactiveTemplate() throws Exception {
        Template inactive = templateRepository.save(Template.builder()
                .name("Off")
                .code("off")
                .description("D")
                .activityType(ActivityType.OTHER)
                .previewUrl("https://example.com/h.png")
                .isActive(false)
                .build());

        String createJson = objectMapper.writeValueAsString(new ProjectRequest(
                "P", ProjectStatus.DRAFT, null));

        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long projectId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(put("/api/projects/" + projectId + "/template/" + inactive.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isNotFound());
    }

    @Test
    void ownerCanSaveAndLoadProjectContent() throws Exception {
        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProjectRequest("Editor", ProjectStatus.DRAFT, null))))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long projectId = objectMapper.readTree(createResponse).get("id").asLong();

        ProjectContentRequest content = new ProjectContentRequest(
                "<section><h1>Welcome</h1></section>",
                "h1{color:red;}");

        mockMvc.perform(put("/api/projects/" + projectId + "/content")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.htmlContent").value("<section><h1>Welcome</h1></section>"))
                .andExpect(jsonPath("$.cssContent").value("h1{color:red;}"));

        mockMvc.perform(get("/api/projects/" + projectId + "/content")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.htmlContent").value("<section><h1>Welcome</h1></section>"));
    }

    @Test
    void otherUserCannotAccessProjectContent() throws Exception {
        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProjectRequest("Editor2", ProjectStatus.DRAFT, null))))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long projectId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/projects/" + projectId + "/content")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenB))
                .andExpect(status().isNotFound());
    }

    @Test
    void unauthenticatedCannotSaveProjectContent() throws Exception {
        String createResponse = mockMvc.perform(post("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProjectRequest("Editor3", ProjectStatus.DRAFT, null))))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long projectId = objectMapper.readTree(createResponse).get("id").asLong();

        ProjectContentRequest content = new ProjectContentRequest("<section>x</section>", null);
        mockMvc.perform(put("/api/projects/" + projectId + "/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isUnauthorized());
    }
}
