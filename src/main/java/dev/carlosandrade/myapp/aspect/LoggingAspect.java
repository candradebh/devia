package dev.carlosandrade.myapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.carlosandrade.myapp.services.LogService;


@Aspect
@Component
public class LoggingAspect
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods()
    {
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(Exception ex)
    {
        logService.createLog("Service Exception", ex.getMessage() + "\n" + this.getStackTrace(ex));
    }

    @Before("execution(* dev.carlosandrade.myapp..*(..))")  // Intercepta todos os métodos dentro do pacote com.example
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Executing method: " + joinPoint.getSignature().getName());
        logger.info("With arguments: ");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.info("-> " + arg);
        }
    }

    @AfterReturning(pointcut = "execution(* dev.carlosandrade.myapp..*(..))", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        logger.info("Method executed: " + joinPoint.getSignature().getName());
        logger.info("Returned value: " + result);
    }

    @AfterThrowing(pointcut = "execution(* dev.carlosandrade.myapp..*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: " + joinPoint.getSignature().getName());
        logger.error("Exception message: " + exception.getMessage());
    }

    private String getStackTrace(Exception ex)
    {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace())
        {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
