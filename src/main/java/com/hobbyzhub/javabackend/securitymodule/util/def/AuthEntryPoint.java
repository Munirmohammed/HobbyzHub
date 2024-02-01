package com.hobbyzhub.javabackend.securitymodule.util.def;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final ObjectMapper mapper = new ObjectMapper();
        GenericResponse<?> unauthorizedResponse =
                GenericResponse.builder()
                    .apiVersion(apiVersion)
                    .organizationName(organizationName)
                    .message("Authentication failed with exception")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .data(null)
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        mapper.writeValue(response.getOutputStream(), unauthorizedResponse);
    }
}
