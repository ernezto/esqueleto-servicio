package ec.gob.senescyt.commons.filters;

import ec.gob.senescyt.commons.Constantes;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

public class RedirectFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }


    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            String redirectUrl = getRedirect(request);
            if (redirectUrl != null) {
                HttpServletResponse response = (HttpServletResponse) res;

                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader(HttpHeaders.LOCATION, redirectUrl);
                return;
            }
        }
        chain.doFilter(req, res);
    }

    private String getRedirect(HttpServletRequest request) {

        String scheme = request.getScheme();
        if ("http".equals(scheme)) {
            return getRedirectUrl(request, "https");
        }

        return null;
    }

    private String getRedirectUrl(HttpServletRequest request, String newScheme) {
        String serverName = request.getServerName();
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        StringBuilder redirect = new StringBuilder(100);
        redirect.append(newScheme);
        redirect.append("://");
        redirect.append(serverName);
        redirect.append(":");

        redirect.append(Constantes.HTTPS_PORT);
        redirect.append(uri);

        if (query != null) {
            redirect.append('?');
            redirect.append(query);
        }

        return redirect.toString();
    }

}
