package am.bdg.boot_shopping_place.filter;


import am.bdg.boot_shopping_place.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = {"/users/**"})
public class AuthTokenFilter extends HttpFilter {

    private final AuthService authService;

    public AuthTokenFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, X-Auth-Token, Content-Type");

        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        final String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            try {
                UserRecord userRecord = authService.validateToken(token.substring(7));
                request.setAttribute("user", userRecord);
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }

        } else return;
        super.doFilter(request, response, chain);
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void destroy() {

    }
}
