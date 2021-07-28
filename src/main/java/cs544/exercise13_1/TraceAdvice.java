package cs544.exercise13_1;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Aspect
@Component
public class TraceAdvice {

    private static final String DATE_FORMATTER = "E MMM dd HH:mm:ss yyyy ";

    @After("execution(* cs544.exercise13_1.EmailSender.sendEmail(..))")
    public void traceAfterMethod(JoinPoint joinpoint) {

        //get argument from send email
        Object[] objects = joinpoint.getArgs();
        String email = (String) objects[0];
        String message = (String) objects[1];

        //get method name
        String methodName = joinpoint.getSignature().getName();

        //get field in the EmailSender Class
        EmailSender test = (EmailSender) joinpoint.getTarget();

        //do time and nice look of it
        LocalDateTime localDateTime = LocalDateTime.now(); //get current date time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        String formatDateTime = localDateTime.format(formatter);

        //print everything
        System.out.println(formatDateTime);
        System.out.println("method = " + methodName);
        System.out.println("address= " + email);
        System.out.println("message= " + message);
        System.out.println("outgoing mail server = " + test.getOutgoingMailServer());
    }

    @Around("execution(* cs544.exercise13_1.CustomerDAO.save(..))")
    public Object invoke(ProceedingJoinPoint call) throws Throwable {
        Object[] args = call.getArgs();

        StopWatch sw = new StopWatch();
        sw.start(call.getSignature().getName());
        Object retVal = call.proceed(args);
        sw.stop();

        long totalTime = sw.getLastTaskTimeMillis();
        // print the time to the console
        System.out.println("Time to execute save = " + totalTime + " ms");

        return retVal;
    }

}
