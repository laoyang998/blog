package com.yjw.blog.Dao;
import com.yjw.blog.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByUserIdAndPassword(String uid,String pwd);
    User findByUserId(String userid);
    List<User> findAllByAdmin(boolean admin);

    @Transactional
    @Modifying
    @Query(value = "update User u set u.job=?1,u.imagUrl=?2,u.email=?3 where u.id=?4")
    int updateInfo(String job,String imagUrl,String email,int uid);

    @Transactional
    @Modifying
    @Query(value="update User u set u.password=?1 where u.id=?2")
    int updatePassword(String pwd,int uid);
}
