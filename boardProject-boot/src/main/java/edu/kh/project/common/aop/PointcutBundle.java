package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutBundle {

    @Pointcut("execution(* edu.kh.project..*Controller*.*(..))")
    public void controllerPointcut() {}

    @Pointcut("execution(* edu.kh.project..*ServiceImpl*.*(..))")
    public void serviceImplPointcut() {}
    
    @Pointcut("execution(* edu.kh.project..*Mapper*.*(..))")
    public void MapperPointcut() {}
}