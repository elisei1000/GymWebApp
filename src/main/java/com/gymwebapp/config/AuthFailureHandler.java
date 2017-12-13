package com.gymwebapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author foldv on 29.11.2017.
 */
@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        ArrayList<String> errors = new ArrayList<String>();
        errors.add("Invalid username or password!");
        Response response1  = new Response(Status.STATUS_FAILED, errors);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), response1);
        response.getWriter().flush();
    }
}
