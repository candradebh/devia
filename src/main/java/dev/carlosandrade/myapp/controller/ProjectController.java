package dev.carlosandrade.myapp.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
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
import dev.carlosandrade.myapp.services.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController
{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<ProjectEntity> getAllProjects()
    {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable Long id)
    {
        return projectRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProjectEntity createProject(@RequestBody ProjectEntity project)
    {
        String repoUrl = "https://github.com/candradebh/" + project.getName();

        // CreateRepoResponse response = projectService.createRepository(request);
        // GitUtils.createDirectory(project.getWorkspacePath() + project.getName()); //cria apenas uma pasta
        try
        {
            repoUrl = projectService.createRepository(project.getName(), project.getDescription(), false);
            project.setGitPath(repoUrl);

        }
        catch (IOException e)
        {

            System.out.println("Erro ao criar o repositorio noi git:\n" + e.getMessage());
            e.printStackTrace();
        }

        File localRepoDirectory = new File(project.getWorkspacePath() + "/" + project.getName());
        try
        {
            Git.cloneRepository().setURI(repoUrl).setDirectory(localRepoDirectory).call();
        }
        catch (InvalidRemoteException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (TransportException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (GitAPIException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
                project.setName(projectDetails.getName());// C:/projetos/teste/
                project.setDescription(projectDetails.getDescription());
                project.setWorkspacePath(projectDetails.getWorkspacePath());
                project.setGitPath(projectDetails.getGitPath());
                project.setIsActive(projectDetails.getIsActive());
                // project.setFeatures(projectDetails.getFeatures());
                ProjectEntity updatedProject = projectRepository.save(project);
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
