package com.yjw.blog.Service;

import com.yjw.blog.Dao.UserDao;
import com.yjw.blog.Pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User Registe(User user) {
        int userID;
        user = userDao.save(user);
        userID = user.getId();
        if (userID > 0) {
            return user;
        } else {
            return null;
        }
    }

    public boolean CheckLogin(String userid, String password) {
        String md5Str = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userDao.findByUserIdAndPassword(userid, md5Str);
        if (user != null && user.getId() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public User getById(Integer id) throws Exception{
        return userDao.findById(id).get();
    }
    public User getByUserName(String userid) {
        return userDao.findByUserId(userid);
    }

    public int updateInfo(String job, String imagUrl, String email, int uid) {
        try {
            return userDao.updateInfo(job, imagUrl, email, uid);
        } catch (Exception e) {
            return 0;
        }
    }

    public int updatePassword(String pwd, int uid) {
        try {
            return userDao.updatePassword(pwd, uid);
        } catch (Exception e) {
            return 0;
        }
    }

    public User addUser(User user) {
        String md5Str = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Str);
        user.setId(0);
        return userDao.save(user);
    }

    public List<User> getUserList() throws Exception{
        List<User> list= userDao.findAllByAdmin(false)
                .stream()
                .filter(s->s.isActive()).collect(Collectors.toList()); //过滤掉管理员账号
        list.forEach(item->{
            item.setPassword("");  //清空密码
        });
        return list;
    }
}
