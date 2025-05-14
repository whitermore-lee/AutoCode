package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesGUI extends JFrame {
    private Properties properties;
    private String propertiesFilePath;
    private JPanel panel;
    private JButton saveButton;


    public PropertiesGUI(String filePath) {
        this.propertiesFilePath = filePath;
        this.properties = loadProperties(filePath);

        setTitle("配置文件");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.setLocationRelativeTo(null);
        // 添加每个属性的输入框
        for (String key : properties.stringPropertyNames()) {
            addPropertyField(key, properties.getProperty(key));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);

        saveButton = new JButton("保存配置文件");
        saveButton.addActionListener(new SaveButtonListener());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addPropertyField(String key, String value) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(key);
        JTextField textField = new JTextField(value, 30);
        fieldPanel.add(label);
        fieldPanel.add(textField);
        panel.add(fieldPanel);
    }


    private Properties loadProperties(String filePath) {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                JOptionPane.showMessageDialog(this, "未找到 " + filePath + " 文件", "错误", JOptionPane.ERROR_MESSAGE);
                return props;
            }
            props.load(input);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "读取 " + filePath + " 文件时出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        return props;
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(getClass().getClassLoader().getResource(propertiesFilePath).getFile())) {
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {

                    JPanel fieldPanel = (JPanel) component;
                    JLabel label = (JLabel) fieldPanel.getComponent(0);
                    JTextField textField = (JTextField) fieldPanel.getComponent(1);
                    String key = label.getText();
                    String value = textField.getText();
                    properties.setProperty(key, value);
                }
            }
            properties.store(output, null);
            JOptionPane.showMessageDialog(this, "保存配置文件成功");
            dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "保存 " + propertiesFilePath + " 文件时出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveProperties();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String filePath = "application.properties";
            PropertiesGUI gui = new PropertiesGUI(filePath);
            gui.setVisible(true);
        });
    }
}