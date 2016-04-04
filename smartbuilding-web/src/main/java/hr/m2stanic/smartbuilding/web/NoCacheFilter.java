package hr.m2stanic.smartbuilding.web;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoCacheFilter implements Filter {
    public void destroy() {
    }

    public static String VALID_METHODS = "DELETE, HEAD, GET, OPTIONS, POST, PUT";

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;

        response.addHeader("Cache-control", "no-cache");
        response.addHeader("Cache-control", "no-store");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        chain.doFilter(req, resp);

    }

    public void init(FilterConfig config) throws ServletException {

    }

}