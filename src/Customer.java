import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Customer extends JFrame implements ActionListener {
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton prevButton, nextButton;
    private Connection connection;
    private ResultSet resultSet;

    public Customer() {
        try {
            connectToDatabase();
            setupUI();
            fetchData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToDatabase() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "072072";
        connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultSet = statement.executeQuery("SELECT * FROM customers");
        resultSet.next();
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("ID:"));
        idField = new JTextField(20);
        add(idField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField(20);
        add(lastNameField);

        add(new JLabel("First Name:"));
        firstNameField = new JTextField(20);
        add(firstNameField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField(20);
        add(phoneField);

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");

        prevButton.addActionListener(this);
        nextButton.addActionListener(this);

        add(prevButton);
        add(nextButton);

        pack();
        setTitle("Customer Viewer");
        setLocationRelativeTo(null);  // Center the window
        setVisible(true);
    }

    private void fetchData() throws SQLException {
        idField.setText(resultSet.getString("customer_id"));
        lastNameField.setText(resultSet.getString("customer_last_name"));
        firstNameField.setText(resultSet.getString("customer_first_name"));
        phoneField.setText(resultSet.getString("customer_phone"));
        idField.setEditable(false);
        lastNameField.setEditable(false);
        firstNameField.setEditable(false);
        phoneField.setEditable(false);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == nextButton) {
                if (!resultSet.isLast()) {
                    resultSet.next();
                    fetchData();
                }
            } else if (e.getSource() == prevButton) {
                if (!resultSet.isFirst()) {
                    resultSet.previous();
                    fetchData();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error navigating records: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
