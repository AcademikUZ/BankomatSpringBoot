package fan.company.bankomatspringboot.security.filter;


import fan.company.bankomatspringboot.security.tokenGenerator.JwtProvider;
import fan.company.bankomatspringboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer")) {
                authorization = authorization.substring(7);
                String usernameFromToken = jwtProvider.getUsernameFromToken(authorization);
                if (usernameFromToken != null) {
                    UserDetails userDetails = authService.loadUserByUsername(usernameFromToken);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
