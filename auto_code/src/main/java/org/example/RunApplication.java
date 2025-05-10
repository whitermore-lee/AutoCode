package org.example;

import org.example.bean.TableInfo;
import org.example.bulider.BuildBase;
import org.example.bulider.BuildPo;
import org.example.bulider.BuildQuery;
import org.example.bulider.BuildTable;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {

        List<TableInfo> tableInfoList =  BuildTable.getTables();
        BuildBase.execute();//基础封装工具调用
       for (TableInfo tableInfo : tableInfoList) {
           BuildPo.execute(tableInfo);
           BuildQuery.execute(tableInfo);
       }
    }
}
