package project.personalproject.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().toLowerCase().contains("/api")) {
            long startTime = System.currentTimeMillis();

            // 응답 상태 코드를 읽기 위해 래퍼 사용
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
                private int httpStatus = SC_OK;

                @Override
                public void setStatus(int sc) {
                    super.setStatus(sc);
                    this.httpStatus = sc;
                }

                @Override
                public void sendError(int sc, String msg) throws IOException {
                    super.sendError(sc, msg);
                    this.httpStatus = sc;
                }
            };

            try {
                filterChain.doFilter(request, responseWrapper);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                int status = responseWrapper.getStatus();
                log.info("[API] {} {}, status: {}, content-type: {}, {}ms",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        request.getContentType(),
                        duration);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
