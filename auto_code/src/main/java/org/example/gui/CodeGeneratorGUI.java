package org.example.gui;

import org.example.RunApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CodeGeneratorGUI extends JFrame {

    public CodeGeneratorGUI() {
        // 设置窗口标题
        setTitle("代码生成器");
        // 设置窗口大小
        setSize(400, 200);
        // 设置窗口关闭时的操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口布局
        setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);



        // 创建生成按钮
        JButton generateButton = new JButton("生成代码");
        // 将按钮添加到窗口中
        add(generateButton);

        JButton propertiesButton = new JButton("配置文件");
        add(propertiesButton);
        propertiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    PropertiesGUI.main(null);

                }catch (Exception exception){
                    JOptionPane.showMessageDialog(CodeGeneratorGUI.this, "获取配置失败");

                }

            }
        });

        // 为按钮添加事件监听器
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 调用代码生成器的主方法
                    RunApplication.main(null);

                } catch (Exception ex) {
                    // 显示代码生成失败的提示信息
                    JOptionPane.showMessageDialog(CodeGeneratorGUI.this, "代码生成失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        // 在事件调度线程中创建和显示 GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CodeGeneratorGUI gui = new CodeGeneratorGUI();
                // 使窗口可见
                gui.setVisible(true);
            }
        });
    }
}