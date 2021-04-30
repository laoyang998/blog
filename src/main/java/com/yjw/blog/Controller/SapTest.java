package com.yjw.blog.Controller;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.yjw.blog.Util.SAP.RfcManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SapTest {

    @CrossOrigin
    @PostMapping("/saptest")
    public List<Map<String,Object>> getResult(){
        //获取TABLE参数示例
        JCoFunction function2= RfcManager.getFunction("ZRFC_PP0001");
        JCoParameterList inputParam2 = function2.getImportParameterList();
        inputParam2.setValue("WERKS", "1101");
        inputParam2.setValue("KDMAT", "C123456");
        RfcManager.execute(function2);
        List<Map<String,Object>> lst=new ArrayList<Map<String,Object>>();
        JCoTable itab=function2.getTableParameterList().getTable("OUT_TAB");
        for(int i =0;i<itab.getNumRows();i++){
            itab.setRow(i);
            System.out.println("物料号:"+itab.getString("MATNR"));
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("物料号",itab.getString("MATNR"));
            lst.add(map);
        }
        return lst;
    }
}
