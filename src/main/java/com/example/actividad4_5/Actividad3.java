package com.example.actividad4_5;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

/* Esta clase implementa un cliente de correo POP3 básico con una interfaz gráfica.
Permite conectarse a un servidor POP3, autenticar al usuario, recuperar y mostrar mensajes de correo.*/
public class Actividad3 extends JFrame {
    private JTextField serverField, portField, userField;
    private JPasswordField passwordField;
    private JTextArea messageArea;
    private JButton connectButton, retrieveButton, disconnectButton;
    private JRadioButton implicitMode, explicitMode;
    private POP3SClient pop3Client;

    public Actividad3() {
        setTitle("Cliente POP3 Básico");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(5, 2));

        topPanel.add(new JLabel("Servidor POP:"));
        serverField = new JTextField("pop.gmail.com");
        topPanel.add(serverField);

        topPanel.add(new JLabel("Puerto:"));
        portField = new JTextField("995");
        topPanel.add(portField);

        topPanel.add(new JLabel("Usuario:"));
        userField = new JTextField();
        topPanel.add(userField);

        topPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        topPanel.add(passwordField);

        // Modos de conexión
        implicitMode = new JRadioButton("Modo implícito", true);
        explicitMode = new JRadioButton("Modo no implícito");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(implicitMode);
        modeGroup.add(explicitMode);

        JPanel modePanel = new JPanel();
        modePanel.add(implicitMode);
        modePanel.add(explicitMode);

        add(topPanel, BorderLayout.NORTH);
        add(modePanel, BorderLayout.CENTER);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER); //Sección para mostrar los mensajes

        JPanel buttonPanel = new JPanel();
        connectButton = new JButton("Conectar");
        retrieveButton = new JButton("Recuperar mensajes del servidor");
        disconnectButton = new JButton("Desconectar");

        retrieveButton.setEnabled(false);
        disconnectButton.setEnabled(false);

        buttonPanel.add(connectButton);
        buttonPanel.add(retrieveButton);
        buttonPanel.add(disconnectButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Acción para conectar
        connectButton.addActionListener(e -> conectar());

        // Acción para recuperar mensajes
        retrieveButton.addActionListener(e -> recuperarMensajes());

        // Acción para desconectar
        disconnectButton.addActionListener(e -> desconectar());

        setVisible(true);
    }

    private void conectar() {
        String server = serverField.getText();
        int port = Integer.parseInt(portField.getText());
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        try {
            pop3Client = new POP3SClient(implicitMode.isSelected());
            pop3Client.connect(server, port);

            if (pop3Client.login(username, password)) {
                JOptionPane.showMessageDialog(this, "Conexión realizada al servidor POP3: " + server);
                connectButton.setEnabled(false);
                retrieveButton.setEnabled(true);
                disconnectButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al hacer login.", "Error", JOptionPane.ERROR_MESSAGE);
                pop3Client.disconnect();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recuperarMensajes() {
        try {
            POP3MessageInfo[] messages = pop3Client.listMessages();
            if (messages != null && messages.length > 0) {
                messageArea.setText("");
                for (POP3MessageInfo message : messages) {
                    BufferedReader reader = new BufferedReader(pop3Client.retrieveMessage(message.number));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messageArea.append(line + "\n");
                    }
                    messageArea.append("-------------------------\n");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No hay mensajes en el servidor.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al recuperar mensajes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void desconectar() {
        try {
            if (pop3Client != null && pop3Client.isConnected()) {
                pop3Client.logout();
                pop3Client.disconnect();
                JOptionPane.showMessageDialog(this, "Desconectado del servidor POP3.");
                connectButton.setEnabled(true);
                retrieveButton.setEnabled(false);
                disconnectButton.setEnabled(false);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al desconectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Actividad3::new);
    }
}
