package dev.carlosandrade.myapp.services;

import java.io.IOException;
import java.util.logging.Logger;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.carlosandrade.myapp.controller.ProjectController;
import dev.carlosandrade.myapp.entity.ProjectEntity;

@Service
public class ProjectService
{
    private static final Logger logger = Logger.getLogger(ProjectController.class.getName());

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;

    public String createRepository(String name, String description, boolean isPrivate) throws IOException
    {
        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        GHCreateRepositoryBuilder builder = github.createRepository(name).description(description).private_(isPrivate).autoInit(true); // Inicializa com README

        // Adicione configurações adicionais conforme necessário

        return builder.create().getHtmlUrl().toString();
    }

    private String createRepositoryInGit(ProjectEntity project, String repoUrl)
    {
        try
        {
            repoUrl = this.createRepository(project.getName(), project.getDescription(), false);
            project.setGitPath(repoUrl);

        }
        catch (IOException e)
        {

            logger.info("Erro ao criar o repositorio no git:\n" + e.getMessage());
            e.printStackTrace();
        }
        return repoUrl;
    }
}
