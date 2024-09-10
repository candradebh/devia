package dev.carlosandrade.myapp.services;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.carlosandrade.myapp.entity.LogEntity;
import dev.carlosandrade.myapp.repository.LogRepository;

@Service
public class LogService
{

    @Autowired
    private LogRepository logRepository;

    public LogEntity createLog(String eventName, String description)
    {
        LogEntity log = new LogEntity(null, new Date(), eventName, description);
        return logRepository.save(log);
    }

    public List<LogEntity> getAllLogs()
    {
        return logRepository.findAll();
    }

    public LogEntity getLogById(Long id)
    {
        return logRepository.findById(id).orElse(null);
    }

    public void deleteLog(Long id)
    {
        logRepository.deleteById(id);
    }
}
