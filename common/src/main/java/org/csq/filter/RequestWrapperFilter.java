package org.csq.filter;
 
import javax.servlet.*;

import org.csq.entity.RequestWrapper;
import org.springframework.stereotype.Component;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
 
 
@Component
@WebFilter(filterName = "requestWrapperFilter", urlPatterns = "/*")
public class RequestWrapperFilter implements Filter {
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * 将request包装为RequestWrapper替换请求中原来的HttpServletRequest，以便代码中可以重复多次读取请求体内容
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
 
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }
 
    @Override
    public void destroy() {}
}