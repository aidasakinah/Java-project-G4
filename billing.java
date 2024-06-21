import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class billing extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public billing() {
        setTitle("Billing Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Create card panel for different pages
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createPricingPanel(), "Pricing");
        cardPanel.add(createCalculateBillsPanel(), "Calculate");
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Load icon
        ImageIcon originalIcon = new ImageIcon("path/to/your/tnb_logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        setIconImage(scaledIcon.getImage());
    }

    private JPanel createSidebar() {
    JPanel sidebar = new JPanel();
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
    sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    sidebar.setBackground(new Color(230, 230, 250));

    // Load and scale the icon
    ImageIcon originalIcon = new ImageIcon("C:/Users/OWNER/Desktop/Java/billing/src/main/java/com/mycompany/billing/tnb_logo.png");
    Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    ImageIcon scaledIcon = new ImageIcon(scaledImage);

    // Create a label with the icon
    JLabel iconLabel = new JLabel(scaledIcon);
    iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton pricingButton = new JButton("Pricing & Tariff");
    JButton calculateButton = new JButton("Calculate Bills");
    JButton paybillsButton = new JButton("Pay Bills");

    pricingButton.addActionListener(e -> cardLayout.show(cardPanel, "Pricing"));
    calculateButton.addActionListener(e -> cardLayout.show(cardPanel, "Calculate"));

    pricingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    sidebar.add(iconLabel);
    sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
    sidebar.add(pricingButton);
    sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    sidebar.add(calculateButton);

    return sidebar;
}
    
    private JPanel createPricingPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create a simple table for pricing
    String[] columnNames = {"kWh Range", "Unit", "Current Rate"};
    Object[][] data = {
        {"For the first 200 kWh (1 – 200 kWh) per month", "sen/kWh", "21.80"},
        {"For the next 100 kWh (201 – 300 kWh) per month", "sen/kWh", "33.40"},
        {"For the next 300 kWh (301 – 600 kWh) per month", "sen/kWh", "51.60"},
        {"For the next 300 kWh (601 – 900 kWh) per month", "sen/kWh", "54.60"},
        {"For the next kWh (901 kWh onwards) per month", "sen/kWh", "57.10"}
    };
    JTable table = new JTable(data, columnNames);
    table.setPreferredScrollableViewportSize(new Dimension(500, 100)); // Adjust the height here
    JScrollPane scrollPane = new JScrollPane(table);

    // Create a panel for the title and table
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(new JLabel("Pricing & Tariff"), BorderLayout.NORTH);
    topPanel.add(scrollPane, BorderLayout.CENTER);

    // Create a panel for the notes
    JPanel notesPanel = new JPanel();
    notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
    notesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

    JLabel noteLabel = new JLabel("<html><b>NOTE:</b></html>");
    JLabel note1 = new JLabel("<html>1. This calculator is only a guide and based on normal billing cycle.</html>");
    JLabel note2 = new JLabel("<html>2. The Imbalance Cost Pass Through (ICPT) rebate rate is -RM0.02/kWh</html>");
    JLabel note3 = new JLabel("<html>3. Service Tax (ST) at the rate of 8% will be charged and have no ICPT rebate in the electricity bills for Residential Customer with consumption more than 600 kWh</html>");

    noteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    note1.setAlignmentX(Component.LEFT_ALIGNMENT);
    note2.setAlignmentX(Component.LEFT_ALIGNMENT);
    note3.setAlignmentX(Component.LEFT_ALIGNMENT);

    notesPanel.add(noteLabel);
    notesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    notesPanel.add(note1);
    notesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    notesPanel.add(note2);
    notesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    notesPanel.add(note3);

    // Add components to the main panel
    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(notesPanel, BorderLayout.CENTER);

    return panel;
}

private JPanel createCalculateBillsPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel label = new JLabel("Enter your total consumption (kWh):");
    JTextField textField = new JTextField(10);
    JButton calculateButton = new JButton("Calculate Bill");

    // Add label
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(0, 0, 5, 5);
    panel.add(label, gbc);

    // Add text field
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    panel.add(textField, gbc);

    // Add calculate button
    gbc.gridx = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    panel.add(calculateButton, gbc);

    // Create the table with initial zero values
    String[] columnNames = {"Details", "Total"};
    Object[][] initialData = {
        {"Consumption (kWh)", 0},
        {"Consumption (RM)", "0.00"},
        {"ICPT (-RM0.02 per kWh)", "0.00"},
        {"Service Tax (ST)", "0.00"},
        {"Current Month Consumption (RM)", "0.00"}
    };
    JTable table = new JTable(initialData, columnNames);
    table.setEnabled(false);  // Make table non-editable

    // Set preferred column widths
    table.getColumnModel().getColumn(0).setPreferredWidth(200);
    table.getColumnModel().getColumn(1).setPreferredWidth(100);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(500, 120));

    // Add table
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1.0;
    gbc.insets = new Insets(20, 0, 0, 0);
    panel.add(scrollPane, gbc);

    calculateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int kwh = Integer.parseInt(textField.getText());
                double billBeforeICPT = calculateBill(kwh);
                double icptRebate = 0.0;
                
                // Apply ICPT rebate only if consumption is 600 kWh or less
                if (kwh <= 600) {
                    icptRebate = kwh * 0.02;
                }
                
                double billAfterICPT = billBeforeICPT - icptRebate;
                
                // Calculate Service Tax (ST)
                double serviceTax = 0.0;
                if (kwh > 600) {
                    serviceTax = billAfterICPT * 0.08;
                }
                
                double finalBill = billAfterICPT + serviceTax;

                // Update table data
                table.setValueAt(kwh, 0, 1);
                table.setValueAt(String.format("%.2f", billBeforeICPT), 1, 1);
                table.setValueAt(String.format("%.2f", -icptRebate), 2, 1);
                table.setValueAt(String.format("%.2f", serviceTax), 3, 1);
                table.setValueAt(String.format("%.2f", finalBill), 4, 1);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Invalid input. Please enter a valid number.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    return panel;
}

private void showBillBreakdown(int kwh, double billBeforeICPT) {
    // Calculate ICPT rebate
    double icptRebate = kwh * 0.02;
    double finalBill = billBeforeICPT - icptRebate;
    
    // Create table data
    Object[][] data = {
        {"Consumption (kWh)", kwh, kwh},
        {"Consumption (RM)", String.format("%.2f", billBeforeICPT), String.format("%.2f", billBeforeICPT)},
        {"ICPT (-RM0.02 per kWh)", String.format("%.2f", -icptRebate), String.format("%.2f", -icptRebate)},
        {"Current Month Consumption (RM)", String.format("%.2f", finalBill), String.format("%.2f", finalBill)}
    };

    // Column names
    String[] columnNames = {"Details", "Total"};

    // Create table
    JTable table = new JTable(data, columnNames);
    table.setEnabled(false);  // Make table non-editable

    // Set preferred column widths
    table.getColumnModel().getColumn(0).setPreferredWidth(200);
    table.getColumnModel().getColumn(1).setPreferredWidth(100);

    // Create a scroll pane containing the table
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(500, 100));

    // Show the table in a JOptionPane
    JOptionPane.showMessageDialog(null, scrollPane, "How do we calculate it?", JOptionPane.INFORMATION_MESSAGE);
}
    private double calculateBill(int kwh) {
        // This is a simplified calculation. You should implement the actual tariff structure.
        if (kwh <= 200) return kwh * 0.218;
        if (kwh <= 300) return 200 * 0.218 + (kwh - 200) * 0.334;
        if (kwh <= 600) return 200 * 0.218 + 100 * 0.334 + (kwh - 300) * 0.516;
        if (kwh <= 900) return 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (kwh - 600) * 0.546;
        return 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + 300 * 0.546 + (kwh - 900) * 0.571;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            billing app = new billing();
            app.setVisible(true);
        });
    }
}