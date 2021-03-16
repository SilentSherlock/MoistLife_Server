package com.program.moist.configs.interceptors;

import com.program.moist.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 2021/3/13
 * Author: SilentSherlock
 * Description: use spring AOP record the access to controller, the logs will output to console and save to /logs
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    /**
     * 切入表达式
     * 切入control包及其子包下所有公有方法
     * 第一个星号匹配返回类型
     * 第二三星号匹配类名和方法名
     */
    private final String POINT_CUT = "execution(public * com.program.moist.control..*.*(..))";

    /**
     * 设置切入点
     */
    @Pointcut(POINT_CUT)
    public void setPOINT_CUT() {}

    /**
     * 在切入点前进行日志记录
     * @param joinPoint 切入点，用在control中代表方法响应request之前
     */
    @Before(value = "setPOINT_CUT()")
    public void logBeforePoint(JoinPoint joinPoint) {
        log.info("logBeforePoint开始执行");

        //记录执行方法信息
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        StringBuilder builder = new StringBuilder();
        builder.append(signature.toShortString());
        for (Object arg :
                args) {
            if (null != arg) builder.append(arg.toString()).append("-");
        }
        log.info(builder.toString());

        //记录request信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            log.info("RequestURI-" + request.getRequestURI());
        }else {
            log.info("no request in the context");
        }

    }

    /**
     * 在切入点之后进行日志记录
     * @param joinPoint 切入点
     */
    @After(value = "setPOINT_CUT()")
    public void logAfterPoint(JoinPoint joinPoint) {
        log.info("方法执行完成");
    }

    @AfterReturning(pointcut = "setPOINT_CUT()", returning = "result")
    public void logAfterResponse(JoinPoint joinPoint, Result result) {
        log.info("response执行结束");
        if (null != result) {
            log.info("Result {Code-" + result.getStatus() + "-Msg-" + result.getDescription() + "}");
        }
    }
}
