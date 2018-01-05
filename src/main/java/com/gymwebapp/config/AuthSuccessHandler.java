package com.gymwebapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author foldv on 29.11.2017.
 */
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        Response response1  = new Response(Status.STATUS_OK, new ArrayList<String>());
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), response1);
        response.getWriter().flush();
    }
}