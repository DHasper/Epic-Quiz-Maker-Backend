package com.quizmaker.backend.utils;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtGenerator {

    static final String SECRET_KEY = "+E+h=h+AGT@4+sN5-wGw#67S*smYgjU4gVr%r8gB*4QyPGk_C=T-LF*aD+UwvGf5";
    static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    public String getToken(String username, int id){
        
        Date issueDate = new Date();

        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(issueDate);
        expirationDate.add(Calendar.DATE, 30);

        return JWT.create()
            .withIssuer("epic quiz maker")
            .withClaim("name", username)
            .withClaim("userid", id)
            .withIssuedAt(issueDate)
            .withExpiresAt(expirationDate.getTime())
            .sign(ALGORITHM);
    }

    public boolean checkToken(String token){
        
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer("epic quiz maker")
                .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }
}