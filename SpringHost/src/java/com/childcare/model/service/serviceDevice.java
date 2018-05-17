/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.model.support.Courier;
import com.childcare.entity.Anchor;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.NewDeviceWrapper;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.NullException;
import com.childcare.model.support.JsonWebTokenUtil;
import com.childcare.model.support.SpatialRelationUtil;
import com.childcare.model.support.MapUtils;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;

@Service("serviceDevice")
/**
 *
 * @author New User
 */
public class serviceDevice {
    
         @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
         @Resource
    private IpushManager push;
    
    public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
    //------------Iteration April 2018 -----------
    
    /**
     * 注册一个device，其归属家庭为申请人提供的FID，申请人必须为FID所有者
     * @param wrapper
     * @return 
     */
    public Response registerAndAdd(NewDeviceWrapper wrapper)
    {
             try {
                 if(JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//通过令牌检测
                 {
                    if (this.jdbcDataDAO.getDaoFamily().findOwner(wrapper.getFid()) == wrapper.getUid() )//FID的拥有者的UID和申请人匹配
                    { //鉴权通过
                        Device d = wrapper.getDevice();
                        d.setFid(new Family(wrapper.getFid())); //将device的FID记录更换为申请人提出的目标family
                        this.jdbcDataDAO.getDaoDevice().createDevice(d); //成功创建device记录,记录中device的归属家庭为目标申请的家庭
                        return new Response(Response.GENERAL_SUCC,"Device "+d.getDeviceID()+" created and added to Family "+wrapper.getFid());
                    }
                    else //申请人UID和查询的FID拥有者UID不匹配
                    {
                        return new Response(Response.ERROR_FAMILY_PERMISSION,"You don't have permission to do requested operation on this family.");
                    }
                    
                 }
                 else //令牌Type不为Access，或UID和输入值不匹配
                 {
                     return new Response(Response.ERROR_TOKEN_ACCESS_UID,"You do not own this token, or this is not an access token.");
                 }
                 
             } catch (DataAccessException|JWTVerificationException|NullException|UnsupportedEncodingException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
                 return new Response(e);
             }
    }
    
    //更新脉搏，经纬度，时间戳
    /**
     * 接受一个device信息，先检查uid和token，根据FID和对应的Fpassword鉴权，通过后更新device脉搏，经纬度，时间戳，然后根据其位置检查安全性
     * @param device
     * @return 
     */
    public Response edit(NewDeviceWrapper device)
    {
         try{
             if (device.getDevice()==null | device.getAccess()==null)
                 return new Response(new NullException("device and access fields must not be null."));
             if (!JsonWebTokenUtil.checkAccess(device.getUid(), device.getAccess()))
                 return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
           String hashed = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(device.getDevice().getFid().getFid()).getFamilyPassword(); //find stored hashed password with given fid
           if (BCrypt.checkpw(device.getDevice().getFid().getFamilyPassword(), hashed)) //if passes the check, do the update
           {
               jdbcDataDAO.getDaoDevice().updateDevice(device.getDevice());
               if (this.constraintCheck(device.getDevice()))
               {
               return new Response(Response.GENERAL_SUCC,"Device information updated.");
               }
               else
               return new Response(Response.WARNING_DANGER_ZONE,"WARNING: Current location of device violates security constraints.");
           }
           else
               return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyID or FamilyPassword");         
        }
        catch (DataAccessException|NullPointerException|NullException|UnsupportedEncodingException e)
        {
             return new Response(e);
        }
    }
    
        public Response touch(Device device)
    {
         try{
           String hashed = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(device.getFid().getFid()).getFamilyPassword(); //find stored hashed password with given fid
           if (BCrypt.checkpw(device.getFid().getFamilyPassword(), hashed)) //if passes the check, do the update
           {
               jdbcDataDAO.getDaoDevice().updateDevice(device);
               if (this.constraintCheck(device))
               {
               return new Response(Response.GENERAL_SUCC,"Device information updated.");
               }
               else
               return new Response(Response.WARNING_DANGER_ZONE,"WARNING: Current location of device violates security constraints.");
           }
           else
               return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyID or FamilyPassword");         
        }
        catch (DataAccessException|NullException e)
        {
             return new Response(e);
        }
         catch ( NullPointerException e  )
         {
             return new Response(Response.EXCEPTION_OTHER,"Lati,Long,FID or family password field is missing.");
         }
    }
    
    //---------------
    
     public Response register(Device device) {
        try{
         String hashed = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(device.getFid().getFid()).getFamilyPassword(); //find stored hashed password with given fid
          if (BCrypt.checkpw(device.getFid().getFamilyPassword(), hashed)) //if passes the check, do the update
           {
               jdbcDataDAO.getDaoDevice().createDevice(device);
               return new Response();
           }
           else
               return new Response(700,"Invalid FamilyID or FamilyPassword");
        }
        catch (DataAccessException| NullException e)
        {
          return new Response(e);
        }
     }
     
       public Object fetch(String id,int fid,String passwd) {
        
        try{
            System.out.println(this.jdbcDataDAO.getUtility().selfCheck());
        if (!this.jdbcDataDAO.getUtility().Validator(passwd, fid))
            return new Response(700,"Invalid FamilyID or FamilyPassword");
        Device device = (Device)jdbcDataDAO.getDaoDevice().getDeviceInstance(id);
        if (!device.getFid().getFid().equals(fid))
           return new Response(700,"Invalid Family ID");
                //return device;
                return new ResponsePayload(device);
        }
         catch (DataAccessException e)
         {
          return new Response(e);
         }

    }
       

       
    public Response update(Device device)
    {
       
       try{
           String hashed = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(device.getFid().getFid()).getFamilyPassword(); //find stored hashed password with given fid
           if (BCrypt.checkpw(device.getFid().getFamilyPassword(), hashed)) //if passes the check, do the update
           {
               if (this.constraintCheck(device))
               {
               jdbcDataDAO.getDaoDevice().updateDevice(device);
               return new Response();
               }
               else
               return new Response(800,"WARNING: Current location of device violates security constraints.");
           }
           else
               return new Response(800,"Invalid FamilyID or FamilyPassword");      
        }
        catch (DataAccessException | NullException e)
        {
             return new Response(e);
        }
        
    }
    
    /**
     * Check constraints and call push service if necessary
     * @param device
     * @return true if safe, false if in danger
     * @throws DataAccessException 
     */
    private boolean constraintCheck(Device device) throws DataAccessException
    {
        
        List<Anchorgroup> list = this.jdbcDataDAO.getDaoAnchorGroup().searchGroupsByFIDandCluster(device.getFid().getFid(),device.getCluster()); //groups match the FID and clusterNum
        System.out.println("======> Start Constraint Check| "+list.size()+" Anchorgroups with Cluster ID "+device.getCluster()+" Found. <======");
        Iterator it = list.iterator();
        Anchorgroup group;
        ArrayList<Anchorgroup> failed = new ArrayList(); //group级别快速检查失败名单
        ArrayList<Anchorgroup> pipeList = new ArrayList();
        while(it.hasNext())
        {
            group = (Anchorgroup)it.next();
            if (group.getType()==0 | group.getType() ==1)  //0或1型为封闭图样，支持快速检查
            {
                if (!new CircleHandler().handle(device, group)) //quick check on group level 如果圆圈快速检查失败，将此锚点组添加到失败列表
                {
                    System.out.println("======>Constraint Check Stage 1|Quick Check Failed: GID: "+group.getGid()+" DID: "+device.getDeviceID()+" | Type is "+group.getType()+" in&Ex Radius is "+group.getInRadius()+"&"+group.getExRadius()+"  <======");
                    failed.add(group);  //log those groups failed in quick check
                }
            }
            else
            {
                pipeList.add(group);  //若图样不是封闭类型，则添加到管道类型表
                System.out.println("======>Constraint Check Stage 1|Pipe Group Found: "+group.getGid()+" DID: "+device.getDeviceID()+"  <======");
            }
        } //failed list should be empty to pass quick check
        //下一步，将失败名单提取including zone
                    System.out.println("======>Constraint Check Stage 2: Failed list size is "+failed.size()+"<======");
        if (failed.size()>0) //检查圆环类型快速检查失败的锚点组 以及  检查管道类型列表内容
        {
            //0,2为including，
         System.out.println("======>Constraint Check Stage 2| Checking failed List<======");
         Map<Integer,List<Anchor>> map = this.jdbcDataDAO.getDaoAnchor().findByMultiGroup(failed);  //根据失败名单提取锚点列表，根据type拆分为多个表格，使用type作为key存放在map中
         it = map.keySet().iterator();
         Integer type;
         while (it.hasNext())  //遍历map的keyset
         {
             type = (Integer)it.next();
             if (!anchorHandler(map.get(type),device,type))  //对于每个type值，如果锚点检查结果为假，则返回假（不安全）结束程序
             {
                 this.push.push(device);  
                 return false; //若有失败则终止检查
             }
         }
        }
        if (pipeList.size()>0 )
        {
         System.out.println("======>Constraint Check Stage 2| Checking pipe List<======");
         Map<Integer,List<Anchor>> map = this.jdbcDataDAO.getDaoAnchor().findByMultiGroup(pipeList);  //根据失败名单提取锚点列表，根据type拆分为多个表格，使用type作为key存放在map中
         it = map.keySet().iterator();
         Integer type;
         while (it.hasNext())  //遍历map的keyset
         {
             type = (Integer)it.next();
             if (!anchorHandler(map.get(type),device,type))  //对于每个type值，如果锚点检查结果为假，则返回假（不安全）结束程序
             {
                 this.push.push(device);  
                 return false; //若有失败则终止检查
             }
         }
        }
        System.out.println("======>Constraint Check | All checkes finished. Device is SAFE.<======");
        return true;
        

    }
    

    
    /**
     * distance between device and an anchor
     * @param device
     * @param anchor
     * @return true if distance > radius
     */
    private boolean distance(Device device, Anchor anchor)
    {
        boolean flag = MapUtils.GetDistance(device.getLatitude().doubleValue(),device.getLongitude().doubleValue(),
                anchor.getLatitude().doubleValue(),anchor.getLongitude().doubleValue())>anchor.getRadius().doubleValue();
        System.out.println("Distance>anchor raidus :"+flag);
        return flag;
    }
    
    /**
     * distance between device and including or excluding zone
     * @param device
     * @param group
     * @param isInner true if referring to inner zone (Keep out mode)
     * @return powered distance between device and center of group (meters)
     */
    private double distance(Device device, Anchorgroup group, boolean isInner)
    {
        if (isInner)
        {
            return MapUtils.GetDistance(group.getInLat().doubleValue(), group.getInLong().doubleValue(), device.getLatitude().doubleValue(), device.getLongitude().doubleValue());    
        }
        else
        {
            return MapUtils.GetDistance(group.getExLat().doubleValue(), group.getExLong().doubleValue(), device.getLatitude().doubleValue(), device.getLongitude().doubleValue());
        }
            
    }
    
    private class PipeHandler implements IGroupHandler
    {

        @Override
        public boolean handle(Device device, Anchorgroup group) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class CircleHandler implements IGroupHandler
    {
        @Override
            
        /**
        * handle circle situation
        * @param device
        * @param group
        * @return true if passed quick check
        */
        public boolean handle(Device device, Anchorgroup group) {
            switch (group.getType())
            {
                case 0:  //keep in
                  return  distance(device,group,false)<=(group.getInRadius().doubleValue()); //Inner(In)内部包含圆 点到圆心距离小于等于圆半径则返回真，
                  //圆在多边形内部，点在圆内部则安全
                case 1:  //keep out
                  return distance(device,group,true)>=(group.getExRadius().doubleValue()); //outer(EX)外部圆 点到圆圆心距离大于等于圆半径则返回真
                  //圆在多边形外部，而点在圆外部则说明点在多边形外，安全
                default:
                    return false; //默认快速检查失败
                        
            }               
        }      
    }


    
    /**
     * Anchor lever check, find out if one device matches constraint from specified anchors from different group with the same type
     * @param list List of anchors in one group
     * @param device device itself
     * @param isIncluding is this group an including zone(true) or excluding zone(false)
     * @return If the device is safe
     */
    private boolean anchorHandler(List<Anchor> list,Device device,int type)
    {
        
        Iterator it = list.iterator();
        Anchor anchor;
        int dis;
        switch (type)
        {
            case 2:case 3:  //pipe型  简单判定点在圆内还是圆外，再根据keep out还是keep in给出安全判断即可  2为keep in，3为keep out
            {
            while(it.hasNext())
            {
                anchor = (Anchor)it.next();
                if (distance(device,anchor)/*distance>radius -> true 点在锚点圆外为真*/^type==3/*类型为2 Keep in*/)  //点在圆内 XOR keep in  输入相同则为0 相反为1  若不安全则直接返回否，安全则继续循环
                    //点在圆外 而类型为保持在内 -》不安全 -》终止循环返回不安全
                    //点在圆外 & 类型3 （安全）xor值应为假
                    return false;
            }
            return true;
            }
            case 0:case 1:  //circle型 圆心连成封闭图案，调用SpatialRelationUtility.isPolygonContainsPoint判断点是否在图形内部 再根据keep in还是out给出判定
            //SpatialRelationUtil.isPolygonContainsPoint(list,device.getLongitude(),device.getLatitude());  //输入锚点列表 以及 经纬度  如果包含则返回真值
            return type==1 ^ SpatialRelationUtil.isPolygonContainsPoint(list,device.getLongitude(),device.getLatitude());  //包含点 且 类型为keep out 则返回false
            default:
                return false;         
        }
    }

    public Response delete(String id,int fid,String passwd)
    {
        try
        {
         if (!this.jdbcDataDAO.getDaoDevice().checkDIDandFID(id, fid))   
            return new Response(999,"Given FamilyID does not match record of given DeviceID.");
        if (this.jdbcDataDAO.getUtility().Validator(passwd, fid))  
            {
                this.jdbcDataDAO.getDaoDevice().deleteDevice(id);
                return new Response();
            }
        else
             return new Response(999,"Invalid FamilyID or FamilyPassword");
        }
        catch (DataAccessException e)
        {
             return new Response(e);
        }
    }
    
    
    

    public Response mail(String id) { 
        List<DeviceAudit> list;
        list = jdbcDataDAO.getDaoDevice().getAudit(id);
        Iterator it = list.iterator();
        String text="";
        DeviceAudit instance;
        while (it.hasNext())
        {
            instance = (DeviceAudit)it.next();
            text = text+instance.getDeviceID().getDeviceID()+","+instance.getDate()+","+instance.getLongitude().setScale(5,BigDecimal.ROUND_HALF_EVEN)+","+instance.getLatitude().setScale(5,BigDecimal.ROUND_HALF_EVEN)+"\n";
        }
         try {
             Courier.sendMail(text);
         } catch (RuntimeException e) {
            return new Response(e);
         } 
          return new Response();
    }
    
    
}
