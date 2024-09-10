package dev.carlosandrade.myapp.utils;

import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Component;
import dev.carlosandrade.myapp.entity.ProjectEntity;

@Component
public class ReadmeGenerator
{

    public String generateReadme(ProjectEntity project) throws IOException
    {
        String v_filePath = project.getPathProject() + "/README.md";

        String readmeContent = String.format(
            "# Projeto: %s\n\n" + "**Data de Criação**: %s\n" + "**Status**: %s\n" + "**Caminho do Projeto**: %s\n\n" + "## Descrição\n\n" + "%s\n\n"
                + "## Detalhes do Projeto\n\n" + "- **ID do Projeto**: %d\n" + "- **Nome do Projeto**: %s\n" + "- **Diretório de Desenvolvimento Local**: %s\n"
                + "- **Diretório do Git**: %s\n\n" + "## Estrutura do Projeto\n\n" + "- O projeto está localizado no diretório de desenvolvimento local: `%s`\n"
                + "- O repositório Git está vinculado ao diretório: `%s`\n\n" + "### Caminho Completo do Projeto\n\n" + "```bash\n" + "%s\n" + "```\n\n"
                + "Este caminho é composto pelo diretório de desenvolvimento local seguido do nome do projeto.\n",
            project.getName(), project.getDate() != null ? project.getDate().toString() : "N/A", project.getIsActive() ? "Ativo" : "Inativo",
            project.getPathProject(), project.getDescription(), project.getId(), project.getName(), project.getWorkspacePath(), project.getGitPath(),
            project.getWorkspacePath(), project.getGitPath(), project.getPathProject());

        // Grava o conteúdo no arquivo README.md
        try (FileWriter writer = new FileWriter(v_filePath))
        {
            writer.write(readmeContent);
        }

        return v_filePath;
    }
}
