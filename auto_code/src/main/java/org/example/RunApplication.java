package org.example;

import org.example.bean.TableInfo;
import org.example.bulider.*;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {

        List<TableInfo> tableInfoList =  BuildTable.getTables();
        BuildBase.execute();//基础封装工具调用
       for (TableInfo tableInfo : tableInfoList) {
           //实体类
           BuildPo.execute(tableInfo);
           //查询
           BuildQuery.execute(tableInfo);
           //mapper
           BuildMapper.execute(tableInfo);
           BuildMapperXml.excetus(tableInfo);
       }
    }
}
