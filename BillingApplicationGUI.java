import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BillingApplicationGUI extends JFrame {

    private Billing billingApp;
    private JTextArea outputArea;
    private JPanel mainPanel;
    private JPanel loginPanel;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private boolean isAdminLoggedIn = false;

    public BillingApplicationGUI() {
        super("Billing Application");

        billingApp = new Billing();

        // Initialize JFrame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create menus
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Create login panel
        loginPanel = createLoginPanel();
        getContentPane().add(loginPanel);

        // Display GUI
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (checkAdminCredentials(username, password)) {
                isAdminLoggedIn = true;
                showMainInterface();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials. Please try again.");
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        return panel;
    }

    private void showMainInterface() {
        getContentPane().remove(loginPanel);
        mainPanel = createMainPanel();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewInfoButton = new JButton("View Customer Information");
        JButton updateStatusButton = new JButton("Update Customer Status");
        JButton addAdminButton = new JButton("Add New Admin");
        JButton deleteAdminButton = new JButton("Delete Admin");
        JButton viewPaymentButton = new JButton("View Payment");

        viewInfoButton.addActionListener(e -> displayUserRecords("user_records.txt"));
        updateStatusButton.addActionListener(e -> updateCustomerStatus());
        addAdminButton.addActionListener(e -> addAdmin());
        deleteAdminButton.addActionListener(e -> deleteAdmin());
        viewPaymentButton.addActionListener(e -> viewPayment());

        buttonPanel.add(viewInfoButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(addAdminButton);
        buttonPanel.add(deleteAdminButton);
        buttonPanel.add(viewPaymentButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Text area for displaying output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private boolean checkAdminCredentials(String username, String password) {
        // Check hardcoded admin credentials first
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return true;
        }

        // Then check admin_records.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("admin_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void displayUserRecords(String fileName) {
        outputArea.setText("");
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.append(line + "\n");
            }
        } catch (IOException e) {
            outputArea.setText("Error reading records from " + fileName);
            e.printStackTrace();
        }
    }

    private void updateCustomerStatus() {
        String name = JOptionPane.showInputDialog(this, "Enter customer name:");
        if (name != null && !name.isEmpty()) {
            String statusStr = JOptionPane.showInputDialog(this, "Enter new status (true for active, false for inactive):");
            try {
                boolean status = Boolean.parseBoolean(statusStr);
                billingApp.updateCustomerStatus(name, status);
                displayUserRecords("user_records.txt");
                JOptionPane.showMessageDialog(this, "Status updated successfully for customer: " + name);
            } catch (NumberFormatException e) {
                outputArea.setText("Invalid input for status.");
            }
        } else {
            outputArea.setText("Customer name cannot be empty.");
        }
    }

    private void addAdmin() {
        String username = JOptionPane.showInputDialog(this, "Enter admin username:");
        String password = JOptionPane.showInputDialog(this, "Enter admin password:");
        String confirmPassword = JOptionPane.showInputDialog(this, "Confirm admin password:");

        if (username != null && password != null && confirmPassword != null && password.equals(confirmPassword)) {
            saveAdminRecord(username, password);
            JOptionPane.showMessageDialog(this, "Admin added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input or passwords do not match. Please try again.");
        }
    }

    private void saveAdminRecord(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admin_records.txt", true))) {
            writer.write(String.format("%s,%s%n", username, password));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAdmin() {
        String username = JOptionPane.showInputDialog(this, "Enter admin username to delete:");

        if (username != null && !username.isEmpty()) {
            if (deleteAdminRecord(username)) {
                JOptionPane.showMessageDialog(this, "Admin deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Admin not found in records.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Admin username cannot be empty.");
        }
    }

    private boolean deleteAdminRecord(String username) {
        List<String> lines = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("admin_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    deleted = true;
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admin_records.txt"))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deleted;
    }

    private void viewPayment() {
        outputArea.setText("");
        File paymentsFile = new File("payments.txt");
        if (!paymentsFile.exists()) {
            outputArea.setText("Payments file not found.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(paymentsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.append(line + "\n");
            }
        } catch (IOException e) {
            outputArea.setText("Error reading payments from payments.txt");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingApplicationGUI::new);
    }
}

class Billing {
    private List<UserAuthSystem> users;

    public Billing() {
        users = new ArrayList<>();
        loadUserRecords("user_records.txt");
    }

    private void loadUserRecords(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String address = parts[1];
                    boolean active = Boolean.parseBoolean(parts[2]);
                    UserAuthSystem user = new UserAuthSystem(name, address, active);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<UserAuthSystem> getUsers() {
        return users;
    }

    public void addCustomer(String name, String address, boolean active) {
        UserAuthSystem user = new UserAuthSystem(name, address, active);
        users.add(user);
        updateRecordsFile("user_records.txt");
    }

    public void updateCustomerStatus(String name, boolean active) {
        for (UserAuthSystem user : users) {
            if (user.getName().equals(name)) {
                user.setActive(active);
                break;
            }
        }
        updateRecordsFile("user_records.txt");
    }

    public void deleteCustomer(String name) {
        users.removeIf(user -> user.getName().equals(name));
        updateRecordsFile("user_records.txt");
    }

    private void updateRecordsFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (UserAuthSystem user : users) {
                writer.write(String.format("%s,%s,%s%n", user.getName(), user.getAddress(), user.isActive()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class UserAuthSystem {
        private String name;
        private String address;
        private boolean active;

        public UserAuthSystem(String name, String address, boolean active) {
            this.name = name;
            this.address = address;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
