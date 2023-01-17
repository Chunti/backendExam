package security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

// We have moved createdToken from LoginEndPoint into its own class
// so we can use the methods in more than just one endpoint
public class Token {
    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min

    public static SignedJWT createToken(String email, List<String> roles, int id) throws JOSEException {
        StringBuilder res = new StringBuilder();
        for (String string : roles) {
            res.append(string);
            res.append(",");
        }
        String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";

        JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
        Date date = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .claim("id", id)
                .claim("email", email)
                .claim("roles", rolesAsString)
                .issueTime(date)
                .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }

    public static SignedJWT getVerifiedToken(String jwtString) throws ParseException, JOSEException, AuthenticationException {
        SignedJWT signedJWT = SignedJWT.parse(jwtString);
        JWSVerifier verifier = new MACVerifier(SharedSecret.getSharedKey());
        if (signedJWT.verify(verifier)) {
            if (new Date().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                throw new AuthenticationException("The provided token is not valid");
            }
            System.out.println("Token is valid");
        }
        return signedJWT;
    }
}
