package dev.carlosandrade.myapp.controller;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.carlosandrade.myapp.entity.ProjectEntity;
import dev.carlosandrade.myapp.repository.ProjectRepository;
import dev.carlosandrade.myapp.services.GitHubService;

@RestController
@RequestMapping("/projects")
public class ProjectController
{
    private static final Logger logger = Logger.getLogger(ProjectController.class.getName());

    @Value("${github.repository}")
    private String gitHubRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GitHubService gitHubService;

    @GetMapping
    public List<ProjectEntity> getAllProjects()
    {
        return projectRepository.findAll();
    }

    @GetMapping("/isActive")
    public List<ProjectEntity> getActiveProjects()
    {
        return projectRepository.findByIsActiveTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable Long id)
    {
        return projectRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProjectEntity createProject(@RequestBody ProjectEntity project)
    {
        project.setDate(new Date());

        try
        {
            String repoUrl = gitHubService.createRepository(project, false);

            gitHubService.createDirAndCloneRepository(project, repoUrl);

            gitHubService.updateReadmeAndPush(project);
        }
        catch (Exception e)
        {
            logger.info("Falha ao tenta criar e/ou clonar o repositório");
        }

        return projectRepository.save(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectEntity> updateProject(@PathVariable Long id, @RequestBody ProjectEntity projectDetails)
    {

        return projectRepository.findById(id).map(new Function<ProjectEntity, ResponseEntity<ProjectEntity>>()
        {
            @Override
            public ResponseEntity<ProjectEntity> apply(ProjectEntity project)
            {
                String repoUrl = gitHubRepository + project.getName();

                // nao permitir a alteracao desse campo
                if (projectDetails.getGitPath() != null && projectDetails.getGitPath().equals(repoUrl) == false)
                {
                    project.setGitPath(repoUrl);
                }

                // project.setName(projectDetails.getName());// C:/projetos/teste/
                project.setDescription(projectDetails.getDescription());
                project.setWorkspacePath(projectDetails.getWorkspacePath());
                project.setGitPath(projectDetails.getGitPath());
                project.setIsActive(projectDetails.getIsActive());
                // project.setFeatures(projectDetails.getFeatures());
                ProjectEntity updatedProject = projectRepository.save(project);

                gitHubService.updateReadmeAndPush(updatedProject);

                return ResponseEntity.ok(updatedProject);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id)
    {
        return null;

    }

}
