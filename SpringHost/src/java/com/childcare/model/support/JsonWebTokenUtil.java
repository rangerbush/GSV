/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.childcare.entity.Account;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author New User
 */
public class JsonWebTokenUtil {
    
    private static final String SECRET = "XSyK77YfK22ooPTdXx!AtV9G7nr0BHRi3jgj1opRo&R7$EWz^^";
    private static final int TTL = 24;
    private static final int LONG_TTL = 24*7;
    private static final String seed = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    /** 
     * get jwt String of object 
     * @param uid
     * @return the jwt token 
     */  
    public static String accessToken(long uid) throws RuntimeException{  
        try {
            Date issueDate = new Date();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.HOUR,TTL);
            Date expireDate = nowTime.getTime();
            Map<String,Object> map = new HashMap();
            map.put("alg", "HS256");
            map.put("typ","JWT");
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    .withHeader(map)
                    .withClaim("UID",uid)
                    .withClaim("Type","Access")
                    .withClaim("TTL",TTL)
                    .withExpiresAt(expireDate)
                    .withIssuedAt(issueDate)
                    .sign(algorithm);
            return token;
        } catch (UnsupportedEncodingException ex) {
             throw new RuntimeException("UTF-8 encoding not supported");
        }
    } 
    
    /**
     * 
     * @param uid
     * @param access
     * @return false if type is not correct, or uid does not match input
     * @throws JWTVerificationException if token is not valid
     * @throws UnsupportedEncodingException 
     */
    public static boolean checkAccess(long uid,String access) throws JWTVerificationException,UnsupportedEncodingException
    {
        Map<String,Claim> map = verify(access);
           if (((Claim)map.get("Type")).asString().equals("Access") &
                 ((Claim)map.get("UID")).asLong() == uid  )
               return true;
           else return false;
    }
    
    /**
     * default generator, length as 50
     * @return 
     */
    public static String jtiGenerator()
    {
          Date issueDate = new Date();
            Random random = new Random(issueDate.getTime());
            StringBuilder sb=new StringBuilder();
            for(int i=0; i<50; ++i){  //30位
                //产生0-61的数字
                 int number=random.nextInt(62);
                 //将产生的数字通过length次承载到sb中
                 sb.append(seed.charAt(number));
             }
            return sb.toString();
    }
    
    
    public static String jtiGenerator(int length)
    {
          Date issueDate = new Date();
            Random random = new Random(issueDate.getTime());
            StringBuilder sb=new StringBuilder();
            for(int i=0; i<length; ++i){  //30位
                //产生0-61的数字
                 int number=random.nextInt(62);
                 //将产生的数字通过length次承载到sb中
                 sb.append(seed.charAt(number));
             }
            return sb.toString();
    }
    
    /**
     * 
     * @param uid 用户UID
     * @param jti 使用生成器生成一个令牌ID
     * @param password 刷新令牌中包含密码密文，使用令牌时需密文于数据库中密文一致
     * @return
     * @throws RuntimeException 
     */
        public static String refreshToken(long uid,String jti,String password) throws RuntimeException{  
        try {
            Date issueDate = new Date();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.HOUR,LONG_TTL);
            Date expireDate = nowTime.getTime();
            Map<String,Object> map = new HashMap();
            map.put("alg", "HS256");
            map.put("typ","JWT");
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    .withHeader(map)
                    .withClaim("UID",uid)
                    .withClaim("TTL",LONG_TTL)
                    .withClaim("Type","Refresh")
                    .withClaim("Password",password)
                    .withJWTId(jti)
                    .withExpiresAt(expireDate)
                    .withIssuedAt(issueDate)
                    .sign(algorithm);
            return token;
        } catch (UnsupportedEncodingException ex) {
             throw new RuntimeException("UTF-8 encoding not supported");
        }
    } 
        
    public static String resetRequestToken(String email, String np) throws RuntimeException{  
        try {
            Date issueDate = new Date();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE,30);
            Date expireDate = nowTime.getTime();
            Map<String,Object> map = new HashMap();
            map.put("alg", "HS256");
            map.put("typ","JWT");
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    .withHeader(map)
                    .withClaim("email",email)
                    .withClaim("new_password", np)
                    .withClaim("Type","Reset_Request")
                    .withClaim("Msg","Token valid for 30 minutes.")
                    .withExpiresAt(expireDate)
                    .withIssuedAt(issueDate)
                    .sign(algorithm);
            return token;
        } catch (UnsupportedEncodingException ex) {
             throw new RuntimeException("UTF-8 encoding not supported");
        }
    } 
    
    /**
     * 
     * @param token
     * @return
     * @throws RuntimeException UTF-8 encoding not supported
     * @throws JWTVerificationException Token invalid or has expired
     */
    public static Map<String,Claim> verify(String token) throws RuntimeException
    {
        JWTVerifier verifier;
        try {
            verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("UTF-8 encoding not supported");
        } catch (JWTVerificationException e) {
            throw e;
        }
    }
    
    
    
}
