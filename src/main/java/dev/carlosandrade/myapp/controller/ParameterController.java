package dev.carlosandrade.myapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.carlosandrade.myapp.entity.ParametersEntity;
import dev.carlosandrade.myapp.repository.ParameterRepository;

@RestController
@RequestMapping("/parameters")
public class ParameterController
{

    @Autowired
    private ParameterRepository repository;

    @GetMapping
    public List<ParametersEntity> getAllConnectors()
    {
        return repository.findAll();
    }

}
