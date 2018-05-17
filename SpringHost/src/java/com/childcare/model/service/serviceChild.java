/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.entity.AccountFamily;
import com.childcare.entity.Child;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.support.sim_Child;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.NullException;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("serviceChild")
/**
 *
 * @author New User
 */
public class serviceChild {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    @Autowired
    ServletContext context;
    private final String BASE_PATH = "/mnt/efs/upload"+  File.separator;
    private final String BASE_PATH_TEST = "D:"+  File.separator;
    
    public Object deleteChild(int uid,String token,int cid,String f_password)
    {
        try{
        if(!JsonWebTokenUtil.checkAccess(uid, token)) //验证身份
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
            //查询cid归属fid对应family实例
            //检查uid所有family中包含对应实例中fid,且家庭密码正确
        Family family = this.jdbcDataDAO.getDaoFamily().findFamilyByCID(cid);
        List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
        //1. password
        if (!BCrypt.checkpw(f_password,family.getFamilyPassword())) //家庭密码相同
            return new Response(Response.ERROR_FAMILY_PASSWORD,"Family password is not correct.");
        if (!list.contains(family)) //用户属于Child所属家庭
            return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not a member of this family. Access denied,");
        this.jdbcDataDAO.getDaoChild().delete(cid);
        return new Response(Response.GENERAL_SUCC,"Child removed.");
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|IllegalArgumentException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
            return new Response(e);
        }
    }
    
    public Response getImage(int cid,String token,int uid,HttpServletRequest request,HttpServletResponse response)
    {
        try{
        if (!this.editImageAuth(uid, token, cid))
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"Token verification failed or you have no right to modify this record.");
        } catch (DataAccessException e)
        {
            return new Response(e);
        }
        Child child = this.jdbcDataDAO.getDaoChild().findByCID(cid);
        String path = child.getImage();
        String fullPath = this.BASE_PATH+path;
        File file = new File(fullPath);
        if(!(file.exists() && file.canRead())) 
            file = new File(BASE_PATH+"default.png");
        byte[] data;
            try (FileInputStream inputStream = new FileInputStream(file);OutputStream stream = response.getOutputStream()) {
                data = new byte[(int)file.length()];
                int length = inputStream.read(data);
                response.setContentType("image/png");
                stream.write(data);
                stream.flush();
            } catch (IOException ex) {
                Logger.getLogger(serviceChild.class.getName()).log(Level.SEVERE, null, ex);
                return new Response(Response.EXCEPTION_IO,"Failed to read avatar.");
            }
        return new Response(Response.GENERAL_SUCC,"Avatar fetched.");
    }
    
    @Transactional
    public Response editImage(int cid, String token,int uid,MultipartFile inputFile) throws RuntimeException
    {
        //先检查文件是否为null或为空
        if (inputFile==null | inputFile.isEmpty())
            return new Response(Response.EXCEPTION_NULL,"uploaded file is null or empty.");
        //鉴权
        try{
        if (!this.editImageAuth(uid, token, cid))
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"Token verification failed or you have no right to modify this record.");
        }
        catch (DataAccessException e)
        {
           return new ResponsePayload(e,"Data Access Exception when verifying tokens.");
        }
        String originalFilename = inputFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!(suffix.equalsIgnoreCase("png")|suffix.equalsIgnoreCase("gif")|suffix.equalsIgnoreCase("png")|suffix.equalsIgnoreCase("jpeg")))
            return new Response(Response.ERROR_INVALID_FILE_TYPE,"Only JPG, PNG and GIF are accepted.");
        String finalName = /*context.getRealPath("/WEB-INF/uploaded")*/ this.BASE_PATH+ cid+ "."+ suffix;
        try {
           //鉴权通过，开始业务
           //先生成hash过的文件名
           //部署目标文件
           File destinationFile = new File(finalName);
           inputFile.transferTo(destinationFile);      
           this.jdbcDataDAO.getDaoChild().editImage(cid, cid+"."+suffix);
        } catch (IOException ex) {
            Logger.getLogger(serviceChild.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(Response.EXCEPTION_IO,"IO exception happened. Failed to save uploaded file.");
        }
        return new Response(Response.GENERAL_SUCC,"Image updated.");
    }
    
    
    /**
     * 提供令牌&UID&CID，令牌UID验证无误 且 CID中对应family可以在UID所有家庭列表中找到
     * @param uid
     * @param token
     * @param cid
     * @return 
     */
    public boolean editImageAuth(int uid,String token,int cid)
    {
        try {
            if (JsonWebTokenUtil.checkAccess(uid, token))
            {
                //查询cid归属fid对应family实例
                Family family = this.jdbcDataDAO.getDaoFamily().findFamilyByCID(cid);
                //检查uid所有family中包含对应实例中fid
                List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
                //用户属于Child所属家庭
                return list.contains(family);
            }
            else //not authorised 
            {
                return false;
            }
        } catch (JWTVerificationException | UnsupportedEncodingException ex) {
             return false;
        }
    }
    
    public Object createChild(int uid,String token,Child child)
    {
        try{
        if(JsonWebTokenUtil.checkAccess(uid, token)) 
        {
            List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
            boolean flag= false;  //已验证的用户是否有权对此家庭添加用户（member即可，无需家庭密码）
            boolean deviceIsNull = child.getDeviceID()==null;
            int fid;
            if (!deviceIsNull) //UID对应family列表中有child中device包含的fid ;device不为null，device中fid会覆盖child中fid，检查device中fid
                fid = this.jdbcDataDAO.getDaoDevice().getDeviceInstance(child.getDeviceID().getDeviceID()).getFid().getFid();
            else
                fid = child.getFid().getFid(); //device为null UID对应family列表中有child包含的fid
            Iterator it = list.iterator();
            while (it.hasNext())
            {
                    if (Objects.equals(((Family)it.next()).getFid(),fid )) //
                    {
                    flag = true;break;
                    } 
            }
            if(flag)
            {
                //鉴权通过
                int cid = this.jdbcDataDAO.getDaoChild().create(child);
                return new ResponsePayload(Response.GENERAL_SUCC,"Child created.",cid);
            }
            else //不是家庭成员，无权添加child
                return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member or owner of this family, thus you do not have the permission to add child into this family.");
        }
        else
        {
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
        }
        }catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
                 return new Response(e);
             }
    }
    
    
    //用户需要为操作的child原纪录fid的成员
    public Object editChild(int uid,String token,Child child)
    {
        try{
        if(JsonWebTokenUtil.checkAccess(uid, token)) 
        {
            List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
            Child kidInRecord = this.jdbcDataDAO.getDaoChild().findByCID(child.getCid());
            boolean flag= false;  //已验证的用户是否有权对此家庭添加用户（member即可，无需家庭密码）
            Iterator it = list.iterator();
            while (it.hasNext())
            {
                if (Objects.equals(((AccountFamily)it.next()).getFamily().getFid(), kidInRecord.getFid().getFid())) //UID对应family列表中有 根据child的cid 在数据库中查询到的fid数值
                {
                    flag = true;break;
                } 
            }
            if(flag)
            {
                //鉴权通过
                this.jdbcDataDAO.getDaoChild().update(child);
                return new Response(Response.GENERAL_SUCC,"Child updated.");
            }
            else //不是家庭成员，无权添加child
                return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member or owner of this family, thus you do not have the permission to edit child into this family.");
        }
        else
        {
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
        }
        }catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
                 return new Response(e);
             }
    }
    
    public Object readByUID(long uid,String token)
    {
        try
        {
            if(!JsonWebTokenUtil.checkAccess(uid, token)) 
                return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
            List<Child> list = this.jdbcDataDAO.getDaoChild().findByUID(uid);
            return new ResponsePayload(Response.GENERAL_SUCC,"Child list of UID "+uid+" fetched.",list);
        }
        catch (DataAccessException | JWTVerificationException|UnsupportedEncodingException e )
        {
            return new Response(e);
        }
    }
    
    //获得一个fid下所有child记录
    public Object readChild(long uid,String token,int fid)
    {
          try{
        if(JsonWebTokenUtil.checkAccess(uid, token)) 
        {
            List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
            boolean flag= false;  //已验证的用户是否有权对此家庭添加用户（member即可，无需家庭密码）
            Iterator it = list.iterator();
            while (it.hasNext())
            {
                if (Objects.equals(((Family)it.next()).getFid(),fid)) //UID对应family列表中有child包含的fid
                {
                    flag = true;break;
                } 
            }
            if(flag)
            {
                //鉴权通过
                List<Child> cList = this.jdbcDataDAO.getDaoChild().findByFID(fid);
                List<sim_Child> returnList = new ArrayList<>();
                cList.forEach((c) -> {
                    //(int cid,String fn,String ln,int age,String did,int fid)
                    returnList.add(new sim_Child(c.getCid(),c.getName(),c.getImage(),c.getAge(),c.getDeviceID().getDeviceID(),c.getFid().getFid()));
                });
                return new ResponsePayload(Response.GENERAL_SUCC,"Children in family "+fid+" fetched.",returnList);
            }
            else //不是家庭成员，无权添加child
                return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member or owner of this family, thus you do not have the permission to edit child into this family.");
        }
        else
        {
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"AccessToken's authoration failed. UID in token does not match the request one or this is not an access token.");
        }
        }catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
                 return new Response(e);
             }
    }
}
