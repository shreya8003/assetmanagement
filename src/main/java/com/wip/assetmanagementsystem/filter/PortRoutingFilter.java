package com.wip.assetmanagementsystem.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PortRoutingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri  = request.getRequestURI();
        int    port = request.getLocalPort();

        if (port == 3000 && ("/".equals(uri) || "/index.html".equals(uri))) {
            response.sendRedirect("/user-portal.html");
            return;
        }

        chain.doFilter(req, res);
    }
}
