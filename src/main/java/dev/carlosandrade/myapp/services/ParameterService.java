package dev.carlosandrade.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.carlosandrade.myapp.repository.ParameterRepository;

@Service
public class ParameterService
{

    @Autowired
    private ParameterRepository repository;

}
