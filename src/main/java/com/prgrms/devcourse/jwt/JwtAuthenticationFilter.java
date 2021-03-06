package com.prgrms.devcourse.jwt;

import com.prgrms.devcourse.jwt.Jwt.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class JwtAuthenticationFilter extends GenericFilterBean {
    private final String headerKey;
    private final Jwt jwt;

    public JwtAuthenticationFilter(String headerKey, Jwt jwt) {
        this.headerKey = headerKey;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(request);
            if(token != null) {
                Claims claims = verify(token);
                String username = claims.username;
                List<GrantedAuthority> authorities = getAuthorities(claims);

                if(isNotEmpty(username) && authorities.size() > 0) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.getContext().getAuthentication();
                }

                chain.doFilter(request, response);
            }
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(headerKey);
        if (isNotEmpty(token)) {
            return URLDecoder.decode(token, StandardCharsets.UTF_8);
        }
        return null;
    }

    private Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0 ?
                emptyList() :
                Arrays.stream(roles)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }
}
