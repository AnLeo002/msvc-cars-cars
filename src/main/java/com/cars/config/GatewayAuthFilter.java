package com.cars.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayAuthFilter extends OncePerRequestFilter {
    @Value("${gateway.auth.header:X-GATEWAY-SECRET}")
    private String headerName;
    @Value("${gateway.auth.value:key-microservices}")
    private String expectedValue;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String actualHeader = request.getHeader(headerName);

        if (!expectedValue.equals(actualHeader)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Access denied:Missing or invalid gateway header");
        }

        filterChain.doFilter(request,response);
    }
}
