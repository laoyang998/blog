package com.yjw.blog.Controller;

import com.yjw.blog.Util.IpUtil;
import com.yjw.blog.VO.Response;
import com.yjw.blog.Pojo.User;
import com.yjw.blog.Service.UserService;
import com.yjw.blog.Util.JwtUtil;
import com.yjw.blog.VO.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping("/login")
    public Response Login(@RequestBody User user, HttpServletRequest request) throws Exception {

        String IP = IpUtil.getIpAddr(request);

        if (userService.CheckLogin(user.getUserId(), user.getPassword())) {
            TokenInfo tokenInfo=new TokenInfo();
            tokenInfo.setToken(JwtUtil.sign(user.getUserId(),IP));
            Response res = new Response();
            if(tokenInfo.getToken()!=null) {
                res.setStatus("OK");
                res.setData(tokenInfo);
                return res;
            }else{
                return null;
            }
        } else {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/checklogin")
    public Map<String,Object> checkLogin(HttpServletRequest request){

        String IP = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userid = JwtUtil.verify(token, IP);

        Map<String,Object> map=new HashMap<String,Object>();

        if(userid!=null){
            User user=userService.getByUserName(userid);
            user.setPassword("");
            map.put("user",user);
            map.put("check","OK");
            return map;
        }else{
            map.put("check","NG");
            return map;
        }
    }

}
