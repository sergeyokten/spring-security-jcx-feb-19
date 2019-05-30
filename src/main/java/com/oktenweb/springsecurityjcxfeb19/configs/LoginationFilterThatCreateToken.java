package com.oktenweb.springsecurityjcxfeb19.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oktenweb.springsecurityjcxfeb19.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginationFilterThatCreateToken extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;


    protected LoginationFilterThatCreateToken(String defaultFilterProcessesUrl, AuthenticationManager manager) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = manager;
    }

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("i intercept this request");
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        String body = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        System.out.println(body);
//        String[] bodyArray = body.split("-");
//        String username = bodyArray[0];
//        String password = bodyArray[1];
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        System.out.println(authenticate.isAuthenticated());
//
//
//    }

    private User temp;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        User user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);
        System.out.println(user);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        System.out.println(authenticate.isAuthenticated());
        if (authenticate.isAuthenticated()) {
            this.temp = user;
        }
//
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("yes");
        String username = this.temp.getUsername();
        String password = this.temp.getPassword();
        //encode user and password

        String ticket = Jwts.builder()
                .setSubject(username + "-" + password)
                .signWith(SignatureAlgorithm.HS512, "yes".getBytes())
                .compact();
        System.out.println(ticket);
        // send back
        response.addHeader("asdqwezxc",ticket);

    }
}
