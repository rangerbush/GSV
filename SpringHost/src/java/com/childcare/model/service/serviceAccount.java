/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.model.support.Courier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.childcare.entity.Account;
import com.childcare.entity.PasswdChanger;
import com.childcare.entity.RefreshToken;
import com.childcare.entity.MailandPsw;
import com.childcare.entity.structure.Response;
import static com.childcare.entity.structure.Response.GENERAL_SUCC;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Service("serviceAccount")
/**
 *
 * @author New User
 */
public class serviceAccount {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    @Resource
    private ServiceDeviceToken serviceToken;
    private final String BASE_URL = "http://safetyvillage.top:8080/SpringHost/";
    private final String BASE_URL_TEST = "http://localhost:8080/SpringHost/";
    private final String BASE_PATH = "/mnt/efs/upload"+  File.separator;
         
    @Transactional
    public Object createAccount(Account account,String token) throws DataAccessException
    {
        String passwd = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(passwd);
        passwd = BCrypt.hashpw(account.getPin(), BCrypt.gensalt());
        account.setPin(passwd);
        long uid = this.jdbcDataDAO.getDaoAccount().createAccount(account);
        this.serviceToken.register(uid,token);
        String accessToken = JsonWebTokenUtil.accessToken(uid);
        HashMap<String,Object> map = new HashMap();
        map.put("access", accessToken);
        map.put("uid", uid);
        return new ResponsePayload(Response.GENERAL_SUCC,"Account Created.",map);
    }
    
    /**
     * Use email and password to login
     * @param identity
     * @return 
     */
    @Transactional
    public Object signIn(MailandPsw identity)
    {

            Account account = this.jdbcDataDAO.getDaoAccount().getAccountInstanceByEmail(identity.getEmail()); //根据邮箱和密码明文获取account实例
            if (BCrypt.checkpw(identity.getPassword(), account.getPassword())) //验证实例中密码密文和明文加密是否符合
            {
                 String accessToken = JsonWebTokenUtil.accessToken(account.getUid()); //验证投过后获取token
                 String jti = JsonWebTokenUtil.jtiGenerator(50);
                 String refreshToken = JsonWebTokenUtil.refreshToken(account.getUid(),jti,account.getPassword());
                 this.jdbcDataDAO.getDaoToken().create(jti);
                 Map<String,Object> map = new HashMap();
                 map.put("access", accessToken);
                 map.put("refresh", refreshToken);
                 map.put("uid", account.getUid());
                 this.serviceToken.register(account.getUid(),identity.getDevice() );
                 return new ResponsePayload(Response.GENERAL_SUCC,"You are now signed in.",map);  //生成回执信息，状态200，payload中放包含AccessToken和RefreshToken的map
            }
            else
            {
                return new Response(Response.ERROR_ACCOUNT_PASSWORD,"Invalid UID or Password is not correct.");  //密码验证失败，
            }
    }
    
    public Object forgetPasswordViaMail(Map<String,String> map)
    {
        if (!map.containsKey("email") | !map.containsKey("new_password"))  //map中指定两个key必须存在
            return new Response(Response.ERROR_NULL_FIELD ,"Expected field email or new_password is not found.");
        String email = map.get("email");
        try
        {
        Account account = this.jdbcDataDAO.getDaoAccount().getAccountInstanceByEmail(email);
        String newPassword = map.get("new_password");
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String token  = JsonWebTokenUtil.resetRequestToken(email, hashed);
        //String FINAL_URL = this.BASE_URL_TEST+"account/auth/"+token;
        String FINAL_URL = this.BASE_URL+"account/portal";
        String msg = "Dear "+account.getFirstName()+" "+account.getLastName()+":\n\n"
                + "You requested to reset your GSV password recently. Please click the following link to complete the last step."
                + " If you have no idea what this is about, please ignore this email. Thank you.\n\n"
                + FINAL_URL
                + "\n\nPlease paste the following secret key into textbox you find in the page from the link above."
                + "\n\n"+token;
        Courier.resetPasswordViaAliYun(msg, email);
        return new Response(Response.GENERAL_SUCC,"Reset link has been sent to specified email address. Click link to apply new password within 30 minutes.");
        } catch (RuntimeException | MessagingException  e)
        {
            return new Response(e);
        }
    }
    
    public ModelAndView react2Mail(String token)
    {
        ModelAndView modelAndView = new ModelAndView();
        try{
           Map<String,Claim> map = JsonWebTokenUtil.verify(token);
           if (!map.containsKey("email") | !map.containsKey("new_password") | !map.containsKey("Type"))  //map中指定两3个key必须存在
           {
               modelAndView.setViewName("failed");
               modelAndView.addObject("msg","Some fields are missing in your request. Please provide the link to us to get help.");
               return modelAndView;
           }
           String mail = ((Claim)map.get("email")).asString();
           String password = ((Claim)map.get("new_password")).asString();
           String type = ((Claim)map.get("Type")).asString();
           if (!type.equals("Reset_Request"))
           {
               modelAndView.setViewName("failed");
               modelAndView.addObject("msg","Your link does not taste right. Please provide the link to us to get help."); //Type 不是 "Reset_Request"
               return modelAndView;
           }
           //类型正确，包含三个域
           this.jdbcDataDAO.getDaoAccount().updatePassword(password,mail); //更新密码密文到数据库   邮箱不存在会报异常
           modelAndView.setViewName("reply");
           modelAndView.addObject("msg","Password of you account "+mail+" has been reset. Please login with your new password.");
           return modelAndView;
        }
        catch (JWTVerificationException e)
        {
               modelAndView.setViewName("failed");
               modelAndView.addObject("msg","Failed to verify your request. The link may have been damaged or has expired.\n Please provide the link to us for help, or launch another request."); //Type 不是 "Reset_Request"
               return modelAndView;
        }
        catch (DataAccessException e)
        {
               modelAndView.setViewName("failed");
               modelAndView.addObject("msg","Attemp to update your password failed. Please try again later or contact us for help."); //Type 不是 "Reset_Request"
               return modelAndView;
        }
                

    }
    
    
    /**
     * Reset password with PIN
     * @param pc
     * @return 
     */
    public Object forgetPassword(PasswdChanger pc)
    {
        try{
        Account oldAccount = this.jdbcDataDAO.getDaoAccount().getAccountInstance(pc.getUid());
        if (BCrypt.checkpw(pc.getAuth(), oldAccount.getPin())) //验证申请PIN明文是否和储存的PIN密文对应
        {
            oldAccount.setPassword(BCrypt.hashpw(pc.getNewPasswd(), BCrypt.gensalt())); //新密码加密
            this.jdbcDataDAO.getDaoAccount().update(oldAccount); //更新密码
            return new Response();
        }
        else
        {
            return new Response(Response.ERROR_ACCOUNT_PASSWORD,"Provided PIN is not correct.");
        }
        
        }
        catch (DataAccessException | IllegalArgumentException e)
        {
            return new Response(e);
        }
    }
    
    public Object changePassword(PasswdChanger pc)
    {
        String newPassword = BCrypt.hashpw(pc.getNewPasswd(), BCrypt.gensalt());
        //System.out.println(newPassword);
        pc.setNewPasswd(newPassword);
        if (pc.getAuth() == null | pc.getOldPasswd() ==null | pc.getNewPasswd() == null )
            return new Response(Response.ERROR_NULL_FIELD,"one or more fields in input are null, which is not acceptable."); //请求中有null域
        try{
        Map<String,Claim> map = JsonWebTokenUtil.verify(pc.getAuth());
        long uid = ((Claim)map.get("UID")).asLong();
        if (uid != pc.getUid())
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"UID in token does not match UID in request."); //令牌中UID与请求中UID不符
        String oldP = this.jdbcDataDAO.getDaoAccount().getAccountPassword(uid);
        if (!BCrypt.checkpw(pc.getOldPasswd(), oldP))
            return new Response(Response.ERROR_ACCOUNT_PASSWORD,"Provided Old password doest match record."); //旧密码和数据库不符
        this.jdbcDataDAO.getDaoAccount().updatePassword(pc);
        return new Response(Response.GENERAL_SUCC,"OK");
        }
        catch (RuntimeException e)
        {
            return new Response(e);
        }
    }
    
    public Object refreshToken(Map<String,String> inMap)
    {
        if (!inMap.containsKey("refresh"))
            return new Response(Response.ERROR_NULL_FIELD ,"Expected field is not found.");
        try
        {
        String token = inMap.get("refresh");
        Map<String,Claim> map = JsonWebTokenUtil.verify(token);
        long uid = ((Claim)map.get("uid")).asLong();
        DecodedJWT jwt = JWT.decode(token);
        RefreshToken rt = this.jdbcDataDAO.getDaoToken().find(jwt.getId());
        if (((Claim)map.get("Type")).asString().equalsIgnoreCase("refresh") & rt!=null) //老token的类型为refresh且数据库中有对应记录
        {
            String pwd = ((Claim)map.get("Password")).asString();
            //token中密码密文和数据库不符合则重新登录
            if (!this.jdbcDataDAO.getDaoAccount().getAccountPassword(uid).equals(pwd))
                return new Response(Response.ERROR_TOKEN_REFRESH_PSW ,"Password in token does not match records in database. Please login again."); 
            String jti = JsonWebTokenUtil.jtiGenerator(50);  //创造新JTI
            String refreshToken = JsonWebTokenUtil.refreshToken(uid,jti,pwd);  //使用新JTI产生新token
            this.jdbcDataDAO.getDaoToken().replace(jwt.getId(),jti); //删除之前JTI记录并新增新JTI记录
            Map<String,Object> nMap = new HashMap();
            nMap.put("refresh", refreshToken);
            nMap.put("access",JsonWebTokenUtil.accessToken(uid));
            return new ResponsePayload(Response.GENERAL_SUCC,"You token is refreshed.",nMap);
        }
        else
        {
            return new Response(Response.ERROR_TOKEN_VERIFY_FAILURE,"Token type incorrect or ID is not authorised.");
        }
        }
        catch (RuntimeException e)
        {
            return new Response(e);
        }
    }
    
    public Object editAccount(Account account,String accessToken)
    {
        Map<String,Claim> map;
        try{
        map = JsonWebTokenUtil.verify(accessToken);
        }
        catch (RuntimeException e)
        {
            return new Response(e);
        }
        long uid = ((Claim)map.get("UID")).asLong(); //验证accessToken并获取UID
        if (uid != account.getUid())
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This token cannot authorize action on requested UID.");
        try{
            this.jdbcDataDAO.getDaoAccount().updatePartial(account);
            return new Response(Response.GENERAL_SUCC,"Account "+uid+" details updated.");
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
    public Object getAccountDetail(long uid,String accessToken)
    {
        Map<String,Claim> map;
        try{
        map = JsonWebTokenUtil.verify(accessToken);  //验证Access有效
        long tUid = ((Claim)map.get("UID")).asLong();
        String type = ((Claim)map.get("Type")).asString();
        if (!type.equalsIgnoreCase("access"))
            return new Response(Response.ERROR_TOKEN_VERIFY_FAILURE ,"Token Type incorrect. AccessToken expected."); //token不是access类
        if (uid != tUid)
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"UID in token does not match UID in request."); //令牌中UID与请求中UID不符
        Account account = this.jdbcDataDAO.getDaoAccount().getAccountInstance(uid);
        account.setPassword("******");
        return new ResponsePayload(Response.GENERAL_SUCC,"Account detail of UID "+uid+" fetched.",account);
        }
        catch (RuntimeException e)
        {
            return new Response(e);
        }
    }
    
    /**
     *
     * @param uid
     * @param access
     * @param inputFile
     * @return
     * @throws JWTVerificationException
     * @throws UnsupportedEncodingException
     */
    @Transactional
    public Object updateAvatar(long uid, String access, MultipartFile inputFile) throws JWTVerificationException, UnsupportedEncodingException
    {
        if(!JsonWebTokenUtil.checkAccess(uid, access)) 
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
        if (inputFile==null) {
            return new Response(Response.EXCEPTION_NULL,"uploaded file is null or empty.");
        } else {
        }
        String originalFilename = inputFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!(suffix.equalsIgnoreCase("jpg")|suffix.equalsIgnoreCase("gif")|suffix.equalsIgnoreCase("png")|suffix.equalsIgnoreCase("jpeg")))
            return new Response(Response.ERROR_INVALID_FILE_TYPE,"Only JPG, PNG and GIF are accepted.");
        String finalName = /*context.getRealPath("/WEB-INF/uploaded")*/ this.BASE_PATH+"avatar"+File.separator+ uid+ "."+ suffix;
        String url = this.BASE_URL+"upload/avatar"+File.separator+ uid+ "."+ suffix;
        try {
           //鉴权通过，开始业务
           //先生成hash过的文件名
           //部署目标文件
           File destinationFile = new File(finalName);
           inputFile.transferTo(destinationFile);      
           this.jdbcDataDAO.getDaoAccount().updateAvatar(uid,uid+ "."+ suffix);
        } catch (IOException ex) {
            Logger.getLogger(serviceChild.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Response.EXCEPTION_IO,"IO exception happened. Failed to save uploaded file.");
        }
        return new ResponsePayload(Response.GENERAL_SUCC,"Image updated.",url);
    }
    
    /**
     *
     * @param uid
     * @param access
     * @return
     */
    public Object deleteAvatar(long uid,String access)
    {
        try {
            if(!JsonWebTokenUtil.checkAccess(uid, access))
                return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
            this.jdbcDataDAO.getDaoAccount().updateAvatar(uid, this.jdbcDataDAO.getDaoAccount().defaultFileName);
            return new Response(GENERAL_SUCC,"Avatar deleted.");
        } catch (JWTVerificationException|UnsupportedEncodingException|DataAccessException ex) {
            return new Response(ex);
        } 
    }
    
            
            
}
