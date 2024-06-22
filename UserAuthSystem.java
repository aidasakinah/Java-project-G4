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
        setSize(400, 350);
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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");

        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Register"));

        gbc.gridy = 0;
        panel.add(loginButton, gbc);

        gbc.gridy = 1;
        panel.add(registerButton, gbc);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180)); // Steel blue
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Cornflower blue
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Steel blue
            }
        });

        return button;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        // Add a logo at the top left
        ImageIcon logoIcon = new ImageIcon("image/user.jpg");
        Image img = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(logoLabel, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(backButton, gbc);

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
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField meterField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Meter:"), gbc);
        gbc.gridx = 1;
        panel.add(meterField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(registerButton, gbc);
        gbc.gridx = 1;
        panel.add(backButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String address = addressField.getText();
            String meter = meterField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isStrongPassword(password)) {
                JOptionPane.showMessageDialog(this, "Please use a stronger password.Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character(@$!%*?&).", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(username, address, meter, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                cardLayout.show(cardPanel, "Main");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Registration Error", JOptionPane.ERROR_MESSAGE);
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
                if (parts.length >= 4 && parts[0].equals(username) && parts[3].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String address, String meter, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_records.txt", true))) {
            writer.write(String.format("%s,\"%s\",%s,%s%n", username, address, meter, password));
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

    private boolean isStrongPassword(String password) {
        // Password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }

        // Password must contain at least one uppercase letter, one lowercase letter,
        // one digit, and one special character
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserAuthSystem().setVisible(true));
    }
}