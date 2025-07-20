package project.personalproject.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServicePerformanceAspect {

    // 서비스 계층의 모든 메소드에 적용
    @Around("execution(* project.personalproject.domain..*Service.*(..))")
    public Object logServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // 메소드 실행 시작 시간
        long startTime = System.currentTimeMillis();

        try {
            // 메소드 실행
            return joinPoint.proceed();
        } finally {
            // 메소드 실행 종료 시간 및 소요 시간 계산
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 성능 로깅
            log.info("성능 측정: {}.{} - 소요 시간: {}ms", className, methodName, duration);

            // DB 조회가 오래 걸리는 경우 경고
            if (duration > 500 && methodName.startsWith("find") || methodName.startsWith("get")) {
                log.warn("DB 조회 성능 경고: {}.{} 메소드가 {}ms 소요됨", className, methodName, duration);
            }
        }
    }

    // 특별히 DB 레포지토리 메소드만 타겟팅
    @Around("execution(* project.personalproject.domain..*Repository.*(..))")
    public Object logDatabasePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        // 쿼리 파라미터 로깅 (필요시)
        Object[] args = joinPoint.getArgs();

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info("DB 연산: {}.{} - 소요 시간: {}ms", className, methodName, duration);

            // 느린 쿼리 감지
            if (duration > 200) {
                log.warn("느린 DB 쿼리 감지: {}.{} - {}ms 소요", className, methodName, duration);
            }
        }
    }

    // 레디스 로그
    @Around("execution(* project.personalproject.global.redis.repository.*Repository.*(..))")
    public Object logRedisPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Redis 연산: {}.{} - 소요 시간: {}ms", className, methodName, duration);

            if (duration > 50) {
                log.warn("Redis 느린 연산 감지: {}.{} - {}ms", className, methodName, duration);
            }
        }
    }
}
