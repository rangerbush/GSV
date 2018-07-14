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
import static com.childcare.entity.structure.Response.GENERAL_SUCC;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.NewDeviceWrapper;
import com.childcare.entity.wrapper.Wrapper;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.exception.Alert;
import com.childcare.model.exception.NullException;
import com.childcare.model.exception.RedAlert;
import com.childcare.model.exception.YellowAlert;
import com.childcare.model.support.ApnsEntity;
import com.childcare.model.support.CoordinateConversion;
import com.childcare.model.support.JsonWebTokenUtil;
import com.childcare.model.support.SpatialRelationUtil;
import com.childcare.model.support.MapUtils;
import com.childcare.model.support.UTM;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import polygonutil.PolygonUtil;
import polygonutil.Vector;

/**
 *
 * @author New User
 */
@Service("serviceDevice")
/**
 *
 * @author New User
 */
public class serviceDevice {
    
         @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
         @Resource
    private pushManager push;
    @Resource
    private AlertHandler alert;
    
    /**
     *
     * @param jdbcDataDAO
     */
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
                     List<Long> list = this.jdbcDataDAO.getDaoFamily().findOwner(wrapper.getFid());
                     System.out.println(list.toString()+"\n"+list.stream().anyMatch(i -> i==wrapper.getUid()));
                    if (list.stream().anyMatch(i -> i.equals(wrapper.getUid())) )//FID的拥有者的UID和申请人匹配
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
               System.out.println("Device.Edit: entering constraint check section.");
               if (this.constraintCheck(device.getDevice()))
               {
               return new Response(Response.GENERAL_SUCC,"Device information updated.");
               }
               else
               return new Response(Response.ALERT_MISSING,"Requested device has gone missing.");
           }
           else
               return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyID or FamilyPassword");         
        }
        catch (DataAccessException|NullPointerException|NullException|UnsupportedEncodingException|Alert e)
        {
            this.alert.handle(e);
             return new Response(e);
        }
    }
    
    /**
     *
     * @param device
     * @return
     */
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
               return new Response(Response.ALERT_MISSING,"WARNING: Current location of device violates security constraints.");
           }
           else
               return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyID or FamilyPassword");         
        }
        catch (DataAccessException|NullException|Alert e)
        {
            this.alert.handle(e);
             return new Response(e);
        }
         catch ( NullPointerException e  )
         {
             return new Response(Response.EXCEPTION_OTHER,"Lati,Long,FID or family password field is missing.");
         }
    }
    
    //---------------

    /**
     *
     * @param device
     * @return
     */
    
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
     
     /**
      * 验证一个thread是否有权执行请求的操作
      * @param did
      * @param uid
      * @return TRUE if the user is related to this device, false if not or a DataAccessException happened.
      */
     public boolean grantGreenLight(String did,long uid)
     {
         try {
             List<Device> list = this.jdbcDataDAO.getDaoDevice().findByUID(uid);
             return list.stream().anyMatch(d-> d.getDeviceID().equals(did));
         }
         catch (DataAccessException e)
         {
             return false;
         }
     }
    /**
     * 不需要任何验证的内部方法
     * @param did
     * @return
     */
    public Response fetch(String did) { 
        try{
        Device device = (Device)jdbcDataDAO.getDaoDevice().getDeviceInstance(did);
        HashMap<String,Object> map = new HashMap();
        map.put("device_id",device.getDeviceID());
        map.put("longitude", device.getLongitude());
        map.put("latitude", device.getLatitude());
        map.put("status", device.getStatus());
        map.put("timestamp", device.getTimeStamp());
        return new ResponsePayload(GENERAL_SUCC,"Device fetched.",map);
        }
         catch (DataAccessException e)
         {
          return new Response(e);
         }

    }
    
    public Response findByFID(Wrapper<Integer> wrapper)
    {
             try {
                 if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))
                     return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
                 List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(wrapper.getUid());
                 if(!list.stream().anyMatch((e) -> (Objects.equals(e.getFid(), wrapper.getPayload()))))
                     return new Response(Response.ERROR_FAMILY_PERMISSION,"You do not have permission to access this family.");
                 List<Device> dList = this.jdbcDataDAO.getDaoDevice().findByFID(wrapper.getPayload());
                 return new ResponsePayload(GENERAL_SUCC,"Devices of family "+wrapper.getPayload()+" fetched.",dList.stream().map(d-> d.getDeviceID()).collect(Collectors.toList()));
             } catch (JWTVerificationException|UnsupportedEncodingException|NullPointerException ex) {
                 return new Response(ex);
             } 
        
    }
       
    /**
     *
     * @param device
     * @return
     */
    public Response update(Device device)
    {
       
       try{
           String hashed = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(device.getFid().getFid()).getFamilyPassword(); //find stored hashed password with given fid
           if (BCrypt.checkpw(device.getFid().getFamilyPassword(), hashed)) //if passes the check, do the update
           {
               jdbcDataDAO.getDaoDevice().updateDevice(device);
               if (this.constraintCheck(device))
               {
               return new Response();
               }
               else
               return new Response(800,"WARNING: Current location of device violates security constraints.");
           }
           else
               return new Response(800,"Invalid FamilyID or FamilyPassword");      
        }
        catch (DataAccessException | NullException | Alert e)
        {
             this.alert.handle(e);
             return new Response(e);
        }
        
    }
    
    @AllArgsConstructor
    @Getter
    @Setter
    private class GroupTrim
    {
        private int gid;
        private int type;
        
        @Override
        public boolean equals(Object obj)
        {
            if(!(obj instanceof GroupTrim))
                return false;
            return ((GroupTrim)obj).getGid()==gid;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + this.gid;
            return hash;
        }

 

    }
    
    
    public Object testConstraint(String did)
    {
        Device device  = this.jdbcDataDAO.getDaoDevice().getDeviceInstance(did);
        try{
        return this.constraintCheck(device);
        }
        catch (Alert e)
        {
            return e.getClass().toString();
        }
    }
    /**
     * Check constraints and call push service if necessary. Run check AFTER finishing main tasks.不涉及自动更新status状态
     * @param device
     * @return true if safe, false if in danger
     * @throws DataAccessException 
     */
    private boolean constraintCheck(Device device) throws DataAccessException,Alert
    {
        System.out.println("----> Starting device constraint check! <----");
        //检查某一个device
        double distance = 10;
        Device d = this.jdbcDataDAO.getDaoDevice().getDeviceInstance(device.getDeviceID());
        switch (d.getStatus())
        {
            case 0: //inactive
                System.out.println("----> Inactivate Device, return true.");
                return true;
            case 2: //missing
                 System.out.println("----> Missing Device, return false.");
                return false;
            default:
        }
        if (device.getCid()==null) //若输入device的cid栏为空，则说明设备未绑定任何child，直接判定安全
            return true;
        //获取设备绑定的cid下属groups列表
        List<Anchorgroup> list = this.jdbcDataDAO.getDaoAnchorGroup().searchGroupsByCID(device.getCid().getCid()); 
        List<Integer> gidList = list.stream().map(temp -> { //装载gid到gidList中
            return temp.getGid();
        }).collect(Collectors.toList());
        List<GroupTrim> gtList = list.stream().map(g -> {
            return new GroupTrim(g.getGid(),g.getType());
        }).collect(Collectors.toList());
        List<Anchor> anchorList = this.jdbcDataDAO.getDaoAnchor().findByMultiGID(gidList); //获取所有相关anchor
        //anchorList.sort((Anchor a1,Anchor a2) -> a1.getAnchorgroup().getGid().compareTo(a2.getAnchorgroup().getGid()));  
        Map<GroupTrim,List<Anchor>> map = new HashMap();
        gtList.forEach((GroupTrim gt) -> {
            List<Anchor> filtered = anchorList.stream()
                    .filter((Anchor anchor) -> Objects.equals(anchor.getAnchorgroup().getGid(), gt.getGid())) //筛选符合指定GID的anchor
                    .sorted((Anchor a1, Anchor a2) -> new Integer(a1.getAnchorPK().getSeqID()).compareTo(a2.getAnchorPK().getSeqID())) //按照seqID排序
                    .collect(Collectors.toList());
            map.put(gt, filtered); //筛选有着指定GID的anchor，装载进一个list里，将这个list用GID作为Key存入map中
        });
        //对每一个group下辖所有anchor进行检查，如果有危险则抛出Alert
                        System.out.println("----> Starting anchor Handler.");
            map.entrySet().forEach((entry) -> {
                this.anchorHandler(entry.getValue(), device,entry.getKey().getType(), distance, entry.getKey().getGid());
            });
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



    static int KEEP_IN_POLYGON = 0;
    static int KEEP_OUT_POLYGON =1;
    static int KEEP_IN_PIPE = 2;
    static int KEEP_OUT_PIPE = 3;
    /**
     * Anchor lever check, find out if one device matches constraint from specified anchors from different group with the same type
     * @param list List of anchors in one group
     * @param device device itself
     * 
     */
    private void anchorHandler(List<Anchor> list,Device device,int type,double distance,long gid) throws Alert
    {
        
        Iterator<Anchor> it = list.iterator();
        Anchor anchor;
        UTM deviceUTM;
        Map<Integer,UTM> utmMap = new HashMap();
        switch (type)
        {
            case 2:case 3:  //pipe型  简单判定点在圆内还是圆外，再根据keep out还是keep in给出安全判断即可  2为keep in，3为keep out
            {
            System.out.println("----> AnchorHandler: type 2 or 3");
            while(it.hasNext())
            {
                anchor = (Anchor)it.next();
                if (distance(device,anchor)/*distance>radius -> true 点在锚点圆外为真*/^type==KEEP_OUT_PIPE/*类型为2 Keep in*/)  //点在圆内 XOR keep in  输入相同则为0 相反为1  若不安全则直接返回否，安全则继续循环
                    //点在圆外 而类型为保持在内 -》不安全 -》终止循环返回不安全
                    //点在圆外 & 类型3 （安全）xor值应为假
                { System.out.println("----> AnchorHandler: type 2 or 3, RedAlert.");
                    throw new RedAlert("gid: "+gid,device,gid); //pipe不采用margin，危险直接丢红色警报
                }
            }
            }
            case 0:case 1:  //circle型 圆心连成封闭图案，调用SpatialRelationUtility.isPolygonContainsPoint判断点是否在图形内部 再根据keep in还是out给出判定
            //SpatialRelationUtil.isPolygonContainsPoint(list,device.getLongitude(),device.getLatitude());  //输入锚点列表 以及 经纬度  如果包含则返回真值
            System.out.println("----> AnchorHandler: type 0 or 1");
            CoordinateConversion cc = new CoordinateConversion();
            deviceUTM = cc.latLon2UTM2(device.getLatitude().doubleValue(), device.getLongitude().doubleValue()); //device的UTM坐标
            it.forEachRemaining(a -> {
                utmMap.put(a.getAnchorPK().getSeqID(),cc.latLon2UTM2(a.getLatitude().doubleValue(), a.getLongitude().doubleValue()));
                // <SeqID , UTM >   生成这一串锚点的SeqID和UTM位置的映射
            });
            Map<Integer,UTM> sortMap = new TreeMap<>((Object o1, Object o2) -> ((Integer)o1).compareTo((Integer)o2));
            sortMap.putAll(utmMap); //迭代器来源于List,因此应该是有序的,但是List本身不保证按照seqID排列，因此还是有必要用TreeMap对SeqID进行排序
            Iterator<UTM> iter = sortMap.values().iterator();
            List<Vector> original = new ArrayList();
            iter.forEachRemaining((UTM u)-> original.add(new Vector(u.east,u.north))); //original 现在包含各个anchor有序排列的UTM坐标信息，可以用来生成新边界
            List[] rendered = PolygonUtil.render(original, distance); //生成扩展的两条边界
            //检查两个边界，两者都不安全则发出红色警报
            boolean redFlag1 = type==KEEP_IN_POLYGON ^ SpatialRelationUtil.isPolygonContainsPoint(rendered[0],deviceUTM.east,deviceUTM.north);
            boolean redFlag2 = type==KEEP_IN_POLYGON ^ SpatialRelationUtil.isPolygonContainsPoint(rendered[1],deviceUTM.east,deviceUTM.north);
            if (redFlag1&redFlag2) { System.out.println("----> AnchorHandler: type 0 or 1. Red Alert"); throw new RedAlert(gid+"",device,gid);}
            //安全返回false,不安全返回true
            boolean yellowFlag = type==KEEP_IN_POLYGON ^ SpatialRelationUtil.isPolygonContainsPoint(original,deviceUTM.east,deviceUTM.north);
            //若yellowFlag为真，发出黄色警报
            if (yellowFlag) {System.out.println("----> AnchorHandler: type 0 or 1. Yellow Alert");throw new YellowAlert(gid+"",device,gid);}
            //检查完成
            default:   
        }
    }

    /**
     *
     * @param id
     * @param fid
     * @param passwd
     * @return
     */
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
    
    /**
     *
     * @param id
     * @return
     */
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
    
    /**
     *
     * @param token
     * @param raw
     * @return
     */
    public Object testRunnable(String token,String raw)
    {
        //String did = "c9484e2343dfe5917e6c5c38371358217f77617f910af70669a0a09d7b19a273";
        Thread thread = new Thread(new ApnsEntity(token,raw));
        thread.start(); 
        return 200;
    }
    
    
}
