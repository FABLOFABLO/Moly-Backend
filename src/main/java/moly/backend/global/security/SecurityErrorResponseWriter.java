package moly.backend.global.security;

import jakarta.servlet.http.HttpServletResponse;
import moly.backend.global.error.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityErrorResponseWriter {

    public void write(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.status().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"code\":\"%s\",\"message\":\"%s\"}"
                        .formatted(errorCode.name(), errorCode.message())
        );
    }
}
