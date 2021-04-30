package com.yjw.blog.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final long EXPIRE_TIME = 24*60 * 60 * 1000;
    private static final String TOKEN_SECRET = "askdflas83ff34";

    public static String sign(String userid, String IP) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userid)
                    .withClaim("IP", IP)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    //解码,取得用户名
    public static String verify(String token, String IP) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String IP_verify = jwt.getClaim("IP").asString();
            if (IP.equals(IP_verify)) {
                return jwt.getClaim("userId").asString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
