package serverr;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server {

    private JFrame serverFrame;
    private JTextArea ta;
    private JTextField tf;
    private JButton sendButton;
    private JButton clearButton;

    private ServerSocket serverSocket;
    private Socket socket;

    private InetAddress inetAddress;

    private DataInputStream dis;
    private DataOutputStream dos;

    Thread thread = new Thread() {
        public void run() {
            while (true) {
                readMessage();
            }
        }
    };

    public Server() {
        createGUI();
    }

    void createGUI() {
        serverFrame = new JFrame("Server");
        serverFrame.setSize(500, 500);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem startServerMenuItem = new JMenuItem("Start Server");
        JMenuItem stopServerMenuItem = new JMenuItem("Stop Server");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        startServerMenuItem.addActionListener(e -> startServer());
        stopServerMenuItem.addActionListener(e -> stopServer());
        exitMenuItem.addActionListener(e -> System.exit(0));

        menu.add(startServerMenuItem);
        menu.add(stopServerMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);
        serverFrame.setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Chat Server", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);

        ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Arial", Font.PLAIN, 16));
        ta.setForeground(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(ta);

        JPanel inputPanel = new JPanel(new BorderLayout());
        tf = new JTextField();
        tf.setFont(new Font("Arial", Font.PLAIN, 16));
        tf.setEditable(false);
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

        serverFrame.add(mainPanel);
        serverFrame.setVisible(true);
    }

    void startServer() {
        try {
            String ipAddress = getIpAddress();
            serverSocket = new ServerSocket(1111);
            SwingUtilities.invokeLater(() -> {
                ta.setText("To connect with server, please provide IP Address: " + ipAddress + "\nWaiting for client to connect...\n");
            });
            socket = serverSocket.accept();
            SwingUtilities.invokeLater(() -> {
                ta.append("Client connected\n");
                ta.append("-----------------------------------------\n");
            });
            tf.setEditable(true);
            setIoStream();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(serverFrame, "Failed to start server", "Server Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

    void stopServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            ta.append("Server stopped\n");
            tf.setEditable(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getIpAddress() {
        String ipAddress = "";
        try {
            inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.getHostAddress();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ipAddress;
    }

    void setIoStream() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
        thread.start();
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
            showMessage("Client: " + message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showMessage(String message) {
        ta.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Server());
    }
}
