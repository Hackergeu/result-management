package com.school.result_management.security;

import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (role.equals("ROLE_TEACHER")) {
            response.sendRedirect("/teacher/dashboard");
        } else {
            response.sendRedirect("/student/dashboard");
        }
    }
}