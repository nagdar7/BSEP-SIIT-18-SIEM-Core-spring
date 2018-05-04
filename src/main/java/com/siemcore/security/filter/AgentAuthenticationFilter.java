package com.siemcore.security.filter;

import com.siemcore.domain.Agent;
import com.siemcore.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import java.io.IOException;

public class AgentAuthenticationFilter extends OncePerRequestFilter {

//    @Autowired
//    private AgentRepository agentRepository;

    public AgentAuthenticationFilter() {
    }

//    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request,
                                    javax.servlet.http.HttpServletResponse response,
                                    javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        String xAuth = request.getHeader("Authorization");

        // validate the value in xAuth
        if(!isValid(xAuth)){
            throw new SecurityException();
        }

        // The token is 'valid' so magically get a user id from it
//        Long id = getUserIdFromToken(xAuth);

        // Create our Authentication and let Spring know about it
//        Authentication auth = new DemoAuthenticationToken(id);
//        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

//    private Long getUserIdFromToken(String xAuth) {
//        return 1L;
//    }

    private boolean isValid(String xAuth) {
        if (xAuth == null) return false;
//        Agent a = agentRepository.findOne(xAuth);
//        return a != null;
        return true;
    }
}
