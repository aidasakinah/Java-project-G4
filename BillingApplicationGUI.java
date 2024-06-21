import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BillingApplicationGUI extends JFrame {

    private Billing billingApp;
    private JTextArea outputArea;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private boolean isAdminLoggedIn = false;

    public BillingApplicationGUI() {
        super("Billing Application");

        billingApp = new Billing();

        // Initialize JFrame
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create menus
        JMenu adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);

        // Create menu items
        JMenuItem viewInfoItem = new JMenuItem("View Customer Information");
        viewInfoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdminLoggedIn) {
                    displayUserRecords("user_records.txt");
                } else {
                    JOptionPane.showMessageDialog(BillingApplicationGUI.this, "Please log in as admin to view customer information.");
                }
            }
        });
        adminMenu.add(viewInfoItem);

        JMenuItem updateStatusItem = new JMenuItem("Update Customer Status");
        updateStatusItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdminLoggedIn) {
                    updateCustomerStatus();
                } else {
                    JOptionPane.showMessageDialog(BillingApplicationGUI.this, "Please log in as admin to update customer status.");
                }
            }
        });
        adminMenu.add(updateStatusItem);

        JMenuItem addAdminItem = new JMenuItem("Add Admin");
        addAdminItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdminLoggedIn) {
                    addAdmin();
                } else {
                    JOptionPane.showMessageDialog(BillingApplicationGUI.this, "Please log in as admin to add admin.");
                }
            }
        });
        adminMenu.add(addAdminItem);

        JMenuItem deleteAdminItem = new JMenuItem("Delete Admin");
        deleteAdminItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdminLoggedIn) {
                    deleteAdmin();
                } else {
                    JOptionPane.showMessageDialog(BillingApplicationGUI.this, "Please log in as admin to delete admin.");
                }
            }
        });
        adminMenu.add(deleteAdminItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(exitItem);

        // Text area for displaying output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Display GUI
        setVisible(true);

        // Prompt admin login
        promptAdminLogin();
    }

    private void promptAdminLogin() {
        String username = JOptionPane.showInputDialog(this, "Enter admin username:");
        if (username == null) {
            // If admin cancels or closes the input dialog, exit the application
            System.exit(0);
        }

        String password = JOptionPane.showInputDialog(this, "Enter admin password:");
        if (password == null) {
            // If admin cancels or closes the input dialog, exit the application
            System.exit(0);
        }

        if (checkAdminCredentials(username, password)) {
            isAdminLoggedIn = true;
            JOptionPane.showMessageDialog(this, "Admin logged in successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials. Please try again.");
            promptAdminLogin(); // Recursively prompt again on failure
        }
    }

    private boolean checkAdminCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("admin_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // Match found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No match found
    }

    private void displayUserRecords(String fileName) {
        outputArea.setText(""); // Clear previous text
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
            displayUserRecords("user_records.txt"); // Update displayed records
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
            // Save admin record
            saveAdminRecord(username, password);

            isAdminLoggedIn = true; // Update login status for new admin
            JOptionPane.showMessageDialog(this, "Admin added successfully. Please log in with the new admin credentials.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input or passwords do not match. Please try again.");
            addAdmin();
        }
    }

    private void saveAdminRecord(String username, String password) {
        // Save admin record to admin_records.txt
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
        // Delete admin record from admin_records.txt
        List<String> lines = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("admin_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    deleted = true;
                    continue; // Skip writing this line
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rewrite the file with updated information
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admin_records.txt"))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deleted;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BillingApplicationGUI();
            }
        });
    }
}

class Billing {
    private List<UserAuthSystem> users;

    public Billing() {
        users = new ArrayList<>();
        loadUserRecords("user_records.txt"); // Load existing user records
    }

    private void loadUserRecords(String fileName) {
        // Load existing user records from file
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

        // Save to file
        updateRecordsFile("user_records.txt");
    }

    public void updateCustomerStatus(String name, boolean active) {
        for (UserAuthSystem user : users) {
            if (user.getName().equals(name)) {
                user.setActive(active);
                break;
            }
        }

        // Update file
        updateRecordsFile("user_records.txt");
    }

    public void deleteCustomer(String name) {
        users.removeIf(user -> user.getName().equals(name));

        // Update file
        updateRecordsFile("user_records.txt");
    }

    private void updateRecordsFile(String fileName) {
    // Append the file with updated information
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
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
