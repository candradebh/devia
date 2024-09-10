package dev.carlosandrade.myapp.entity;

import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LLMModelDetails
{

    private String parentModel;

    private String format;

    private String family;

    @ElementCollection
    @CollectionTable(name = "llm_model_families", joinColumns = @JoinColumn(name = "llm_model_id"))
    @Column(name = "family")
    private List<String> families;

    private String parameterSize;

    private String quantizationLevel;
}
