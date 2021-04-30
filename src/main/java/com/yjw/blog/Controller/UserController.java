package com.yjw.blog.Controller;

import com.yjw.blog.Pojo.User;
import com.yjw.blog.Service.UserService;
import com.yjw.blog.Util.IpUtil;
import com.yjw.blog.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("/get_userinfo2")
    public User getUserInfo2(@RequestBody Map<String, Object> map) {
        try {
            Integer userid = (Integer) map.get("userid");
            User user = userService.getById(userid);
            user.setPassword("");
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/get_userinfo")
    public User getUserInfo(HttpServletRequest request) {
        String IP = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userid = JwtUtil.verify(token, IP);
        if (userid != null) {
            User user = userService.getByUserName(userid);
            user.setPassword("");
            return user;
        } else {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/update_userinfo")
    public Map<String, Object> updateUserInfo(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            String IP = IpUtil.getIpAddr(request);
            String token = request.getHeader("token");
            String userid = JwtUtil.verify(token, IP);
            if (userid != null) {
                Map<String, Object> resMap = new HashMap<String, Object>();
                String useridTMP = (String) map.get("userId");
                if (!userid.equals(useridTMP)) {
                    resMap.put("result", "无权修改他人的账号信息");
                    return resMap;
                }
                int uid = (int) map.get("uid");
                String job = (String) map.get("job");
                String email = (String) map.get("email");
                String imagUrl = (String) map.get("imagUrl");


                int result = userService.updateInfo(job, imagUrl, email, uid);
                if (result == 0) {
                    resMap.put("result", "failed");
                } else {
                    resMap.put("result", "success");
                }
                return resMap;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping("/set_user_password")
    public Map<String, Object> updateUserPassword(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        try {
            String IP = IpUtil.getIpAddr(request);
            String token = request.getHeader("token");
            String userid = JwtUtil.verify(token, IP);
            if (userid != null) {
                String old_pwd = (String) map.get("oldPwd");
                String new_pwd = (String) map.get("newPwd");
                User user = userService.getByUserName(userid);
                Map<String, Object> mapRes = new HashMap<String, Object>();
                String old_pwd_md5 = DigestUtils.md5DigestAsHex(old_pwd.getBytes());
                if (!old_pwd_md5.equals(user.getPassword())) {
                    mapRes.put("result", "WrongPassword");
                    return mapRes;
                }
                String new_pwd_md5 = DigestUtils.md5DigestAsHex(new_pwd.getBytes());
                if (userService.updatePassword(new_pwd_md5, user.getId()) == 0) {
                    mapRes.put("result", "failed");
                    return mapRes;
                } else {
                    mapRes.put("result", "success");
                    return mapRes;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/add_user")
    public Map<String, Object> addUser(@RequestBody User user, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String IP = IpUtil.getIpAddr(request);
            String token = request.getHeader("token");
            String userid = JwtUtil.verify(token, IP);
            if (userid != null) {
                try {
                    User exsistUser = userService.getByUserName(user.getUserId());
                    if (exsistUser != null) {
                        map.put("result", "exsistUser");
                        return map;
                    }
                    user.setActive(true);
                    userService.addUser(user);
                    map.put("result", "success");
                } catch (Exception e) {
                    map.put("result", "failed");
                }
            } else {
                map.put("result", "failed");
            }
        } catch (Exception e) {
            map.put("result", "failed");
        }
        return map;
    }

    @CrossOrigin
    @PostMapping("/get_user_list")
    public List<User> getUserList(HttpServletRequest request) {
        try {
            return userService.getUserList();
        } catch (Exception e) {
            return null;
        }
    }
}
