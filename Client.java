package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {

    private JFrame clientFrame;
    private JTextArea ta;
    private JScrollPane scrollPane;
    private JTextField tf;
    private JButton sendButton;
    private JButton clearButton;
    private Socket socket;

    private DataInputStream dis;
    private DataOutputStream dos;

    String ipAddress;

    Thread thread = new Thread() {
        public void run() {
            while (true) {
                readMessage();
            }
        }
    };

    public Client(String ipAddress) {
        this.ipAddress = ipAddress;
        connectToServer();
        createGUI();
    }

    void createGUI() {
        clientFrame = new JFrame("Chat Client");
        clientFrame.setSize(500, 500);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Chat Client", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);

        ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Arial", Font.PLAIN, 16));
        ta.setForeground(Color.DARK_GRAY);
        scrollPane = new JScrollPane(ta);

        JPanel inputPanel = new JPanel(new BorderLayout());
        tf = new JTextField();
        tf.setFont(new Font("Arial", Font.PLAIN, 16));
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");

        sendButton.addActionListener(e -> {
            sendMessage(tf.getText());
            tf.setText("");
        });

        clearButton.addActionListener(e -> ta.setText(""));

        inputPanel.add(tf, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(clearButton, BorderLayout.WEST);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        clientFrame.add(mainPanel);
        clientFrame.setVisible(true);

        tf.addActionListener(e -> {
            sendMessage(tf.getText());
            tf.setText("");
        });
    }

    void connectToServer() {
        try {
            socket = new Socket(ipAddress, 1111);
            setIoStream();
            JOptionPane.showMessageDialog(clientFrame, "Connected to server", "Connection Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(clientFrame, "Failed to connect to server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

    void setIoStream() {
        thread.start();
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
            ta.append("You: " + message + "\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readMessage() {
        try {
            String message = dis.readUTF();
            showMessage("Server: " + message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showMessage(String message) {
        ta.append(message + "\n");
    }

    public static void main(String[] args) {
        
    }
}
