package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IpAddressFrame extends JFrame {

    private JPanel panel;
    private JLabel titleLabel, ipAddressLabel;
    private JTextField ipAddressField;
    private JButton connectButton;

    public IpAddressFrame() {
        setTitle("Enter IP Address");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 248, 220)); 

        titleLabel = new JLabel("Enter IP Address", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 144, 255)); 
        titleLabel.setBounds(10, 10, 264, 30);

        ipAddressLabel = new JLabel("IP Address:");
        ipAddressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ipAddressLabel.setBounds(30, 50, 80, 20);

        ipAddressField = new JTextField();
        ipAddressField.setFont(new Font("Arial", Font.PLAIN, 14));
        ipAddressField.setBounds(120, 50, 150, 25);

        connectButton = new JButton("Connect");
        connectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        connectButton.setBackground(new Color(30, 144, 255)); 
        connectButton.setForeground(Color.WHITE); 
        connectButton.setBounds(100, 90, 100, 30);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        panel.add(titleLabel);
        panel.add(ipAddressLabel);
        panel.add(ipAddressField);
        panel.add(connectButton);

        add(panel);
    }

    private void connect() {
        String ipAddress = ipAddressField.getText();
        if (!ipAddress.isEmpty()) {
            Client client = new Client(ipAddress);
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(IpAddressFrame.this, "Please enter IP Address", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
