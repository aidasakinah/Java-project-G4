import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PaymentSystem extends JFrame {
    private JTextField customerIdField;
    private JTextField amountField;
    private JTextArea paymentStatusArea;

    private static final String PAYMENT_FILE = "payments.txt";

    public PaymentSystem() {
        setTitle("Electricity Bill Payment System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        paymentStatusArea = new JTextArea(10, 30);
        paymentStatusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(paymentStatusArea);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        loadPaymentsFromFile(); // Load existing payments from file on startup
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdField = new JTextField();
        JLabel amountLabel = new JLabel("Amount (RM):");
        amountField = new JTextField();
        JButton payButton = new JButton("Pay Bill");
        JButton checkButton = new JButton("Check Past Payments");

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = customerIdField.getText();
                String amountStr = amountField.getText();
                if (customerId.isEmpty() || amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(PaymentSystem.this,
                            "Please enter Customer ID and Amount.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                boolean paymentMade = makePayment(customerId, amount);

                if (paymentMade) {
                    JOptionPane.showMessageDialog(PaymentSystem.this,
                            "Payment successful for Customer ID: " + customerId,
                            "Payment Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(PaymentSystem.this,
                            "Payment unsuccessful. Please check details.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                // Clear fields after payment
                customerIdField.setText("");
                amountField.setText("");
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPaymentHistory();
            }
        });

        panel.add(customerIdLabel);
        panel.add(customerIdField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(payButton);
        panel.add(checkButton);

        return panel;
    }

    private boolean makePayment(String customerId, double amount) {
        // Write payment details to file
        String paymentInfo = customerId + "," + amount + "," + System.currentTimeMillis() + "\n";
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
                        .append(", Timestamp: ").append(payment[2]).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        paymentStatusArea.setText(history.toString());
    }

    private void loadPaymentsFromFile() {
        // Load existing payments and display in the text area
        StringBuilder history = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] payment = line.split(",");
                history.append("Customer ID: ").append(payment[0])
                        .append(", Amount: RM").append(payment[1])
                        .append(", Timestamp: ").append(payment[2]).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        paymentStatusArea.setText(history.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaymentSystem paymentSystem = new PaymentSystem();
            paymentSystem.setVisible(true);
        });
    }
}
