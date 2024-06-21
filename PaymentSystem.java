import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentSystem extends JFrame {
    private JTextField customerIdField;
    private JTextField amountField;
    private JTextArea paymentStatusArea;

    private static final String PAYMENT_FILE = "payments.txt";

    public PaymentSystem() {
        setTitle("Electricity Bill Payment System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        paymentStatusArea = new JTextArea();
        paymentStatusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(paymentStatusArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Make Payment"),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdField = new JTextField(20);
        JLabel amountLabel = new JLabel("Amount (RM):");
        amountField = new JTextField(20);
        JButton payButton = new JButton("Pay Bill");
        JButton checkButton = new JButton("Check Past Payments");

        // Add action listeners
        payButton.addActionListener(e -> {
            String customerId = customerIdField.getText().trim();
            String amountStr = amountField.getText().trim();
            if (customerId.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(PaymentSystem.this,
                        "Please enter Customer ID and Amount.",
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                boolean paymentMade = makePayment(customerId, amount);

                if (paymentMade) {
                    JOptionPane.showMessageDialog(PaymentSystem.this,
                            "Payment successful for Customer ID: " + customerId,
                            "Payment Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Do not automatically display history after payment
                } else {
                    JOptionPane.showMessageDialog(PaymentSystem.this,
                            "Payment unsuccessful. Please check details.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                // Clear fields after payment
                customerIdField.setText("");
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PaymentSystem.this,
                        "Invalid amount format. Please enter a valid number.",
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        checkButton.addActionListener(e -> displayPaymentHistory());

        // GridBagLayout constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(customerIdLabel, gbc);

        gbc.gridx = 1;
        panel.add(customerIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(amountLabel, gbc);

        gbc.gridx = 1;
        panel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(payButton, gbc);

        gbc.gridy = 3;
        panel.add(checkButton, gbc);

        return panel;
    }

    private boolean makePayment(String customerId, double amount) {
        String paymentInfo = customerId + "," + amount + "," + getCurrentDateTime() + "\n";
        try {
            FileWriter writer = new FileWriter(PAYMENT_FILE, true);
            writer.write(paymentInfo);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void displayPaymentHistory() {
        StringBuilder history = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] payment = line.split(",");
                history.append("Customer ID: ").append(payment[0])
                        .append(", Amount: RM").append(payment[1])
                        .append(", Date: ").append(payment[2]).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        paymentStatusArea.setText(history.toString());
    }

    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set look and feel to Nimbus for nicer appearance (optional)
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            PaymentSystem paymentSystem = new PaymentSystem();
            paymentSystem.setVisible(true);
        });
    }
}
