package com.yjw.blog;


import com.sap.conn.jco.*;
import com.yjw.blog.Dao.ArticleTypeDao;
import com.yjw.blog.Dao.UserDao;
import com.yjw.blog.Pojo.User;
import com.yjw.blog.Service.ArticleTypeService;
import com.yjw.blog.Service.UserService;
import com.yjw.blog.Util.SAP.RfcManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.security.MessageDigest;


@SpringBootTest
class BlogApplicationTests {
    @Autowired
    private ArticleTypeDao articleTypeDao;

    @Autowired
    private ArticleTypeService articleTypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
        //获取STRUCTURE参数示例
        JCoFunction function = RfcManager.getFunction("ZRFC_SD0002");
        JCoParameterList inputParam = function.getImportParameterList();
        inputParam.setValue("WERKS", "1101");
        inputParam.setValue("MATNR", "11S1000019A");
        JCoParameterList outputParam = function.getExportParameterList();
        RfcManager.execute(function);
        JCoStructure exportStructure=outputParam.getStructure("EDATA");
        String msg = (String)outputParam.getString("MSG");
        String etype = (String)outputParam.getString("ETYPE");
        for(JCoField field:exportStructure){
            System.out.println(field.getName()+"/t"+field.getString());
        }

        //获取TABLE参数示例
        JCoFunction function2=RfcManager.getFunction("ZRFC_PP0001");
        JCoParameterList inputParam2 = function2.getImportParameterList();
        inputParam2.setValue("WERKS", "1101");
        inputParam2.setValue("KDMAT", "C123456");
        RfcManager.execute(function2);
        JCoParameterList outputParam2 = function2.getExportParameterList();
        JCoTable itab=function2.getTableParameterList().getTable("OUT_TAB");
        for(int i =0;i<itab.getNumRows();i++){
            itab.setRow(i);
            System.out.println("物料号:"+itab.getString("MATNR"));
        }
    }

}
