package tmall.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BackServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String Context = request.getServletContext().getContextPath();
        System.out.println(Context);
        String uri = request.getRequestURI();//浏览器发出请求的资源名部分，去掉了协议和主机名"
        uri = StringUtils.remove(uri,Context);//去掉资源名
        if(uri.startsWith("/admin_")){
            //StringUtils.substringBetween(str,header,tail)
            // 在str中取得header和tail之间的字符串。不存在则返回空.
            //得到servlet的名字
            String servletPath = StringUtils.substringBetween(uri,"_", "_") + "Servlet";

            //StringUtils.substringAfterLast(str,seqStr )：
            //取得最后一个指定字符串之后的字符串
            //得到要调用的方法名字
            String method = StringUtils.substringAfterLast(uri,"_" );
            request.setAttribute("method", method);
            request.getRequestDispatcher("/" + servletPath).forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
