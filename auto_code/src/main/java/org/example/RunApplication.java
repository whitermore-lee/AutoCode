package org.example;

import org.example.bean.TableInfo;
import org.example.bulider.BuildTable;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
       List<TableInfo> tableInfoList =  BuildTable.getTables();
    }
}
