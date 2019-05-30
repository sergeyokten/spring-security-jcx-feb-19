package com.oktenweb.springsecurityjcxfeb19.configs;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FilterThatCheckTokentOfEveryRequest extends GenericFilterBean {
    private AuthenticationManager authenticationManager;

    public FilterThatCheckTokentOfEveryRequest(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String ticket = httpServletRequest.getHeader("token");
        if (ticket != null) {
            String decodedTicket = Jwts.parser().setSigningKey("yes".getBytes())
                    .parseClaimsJws(ticket)
                    .getBody().getSubject();
            System.out.println(ticket);
            System.out.println(decodedTicket);

            String[] split = decodedTicket.split("-");
            String username = split[0];
            String password = split[1];
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
            }
            filterChain.doFilter(servletRequest, servletResponse);

        }
    }
}
