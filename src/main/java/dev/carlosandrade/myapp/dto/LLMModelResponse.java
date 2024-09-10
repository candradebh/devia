package dev.carlosandrade.myapp.dto;

import java.util.List;
import dev.carlosandrade.myapp.entity.LLMModelEntity;

public class LLMModelResponse
{

    private List<LLMModelEntity> models;

    // Getters and Setters
    public List<LLMModelEntity> getModels()
    {
        return models;
    }

    public void setModels(List<LLMModelEntity> models)
    {
        this.models = models;
    }
}
