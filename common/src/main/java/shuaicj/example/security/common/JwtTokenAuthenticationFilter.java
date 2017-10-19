package shuaicj.example.security.common;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authenticate requests with header 'Authorization: Bearer jwt-token'.
 *
 * @author shuaicj 2017/10/18
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationConfig config;

    public JwtTokenAuthenticationFilter(JwtAuthenticationConfig config) {
        this.config = config;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse rsp, FilterChain filterChain)
            throws ServletException, IOException {
        String token = req.getHeader(config.getHeader());
        if (token != null && token.startsWith(config.getPrefix() + " ")) {
            token = token.replace(config.getPrefix() + " ", "");
            try {
                JWEObject jweObject = JWEObject.parse(token);
                jweObject.decrypt(new DirectDecrypter(config.getSecret().getBytes()));
                SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

                if (signedJWT.verify(new MACVerifier((config.getSecret() + config.getSecret()).getBytes()))) {
                    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                    String username = claims.getSubject();
                    Instant expireAt = claims.getExpirationTime().toInstant();
                    @SuppressWarnings("unchecked")
                    List<String> authorities = claims.getStringListClaim("authorities");
                    if (username != null && Instant.now().isBefore(expireAt)) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception ignore) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(req, rsp);
    }
}
