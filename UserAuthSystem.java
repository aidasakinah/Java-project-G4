package com.mycompany.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class UserAuthSystem extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public UserAuthSystem() {
        setTitle("User Authentication System");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainPanel = createMainPanel();
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();

        cardPanel.add(mainPanel, "Main");
        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");

        add(cardPanel);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Register"));

        panel.add(loginButton);
        panel.add(registerButton);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField meterField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Meter:"));
        panel.add(meterField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(backButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String name = nameField.getText();
            String meter = meterField.getText();
            String password = new String(passwordField.getPassword());
            if (registerUser(username, name, meter, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                cardLayout.show(cardPanel, "Main");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));

        return panel;
    }

    private boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[3].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String name, String meter, String password) {
        if (userExists(username)) {
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_records.txt", true))) {
            writer.write(String.format("%s,%s,%s,%s%n", username, name, meter, password));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // If file doesn't exist, it means no users are registered yet
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserAuthSystem().setVisible(true));
    }
}