package dev.carlosandrade.myapp.services;

import java.io.IOException;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProjectService
{
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
}
