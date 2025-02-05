package com.example.Actividad4_4;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;


/*Clase que permite enviar un mail desde un usuario de un servido local de correo a cualquier correo con TSl do esto con una intefaz swing */

public class Actividad2 extends JFrame {

    private JTextField smtpServerField, portField, usernameField, fromField, toField, subjectField;
    private JPasswordField passwordField;
    private JTextArea messageArea;
    private JRadioButton tlsYes, tlsNo;

    public Actividad2() {
        setTitle("Cliente SMTP Básico");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        // Componentes de la interfaz
        add(new JLabel("Servidor SMTP:"));
        smtpServerField = new JTextField("smtp.gmail.com");
        add(smtpServerField);

        add(new JLabel("Puerto:"));
        portField = new JTextField("587");
        add(portField);

        add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Remitente:"));
        fromField = new JTextField();
        add(fromField);

        add(new JLabel("Destinatario:"));
        toField = new JTextField();
        add(toField);

        add(new JLabel("Asunto:"));
        subjectField = new JTextField();
        add(subjectField);

        add(new JLabel("Redacta el cuerpo del mensaje:"));
        messageArea = new JTextArea(5, 20);
        add(new JScrollPane(messageArea));

        // Opciones TLS
        add(new JLabel("¿Con TLS?:"));
        JPanel tlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tlsYes = new JRadioButton("Con TLS", true);
        tlsNo = new JRadioButton("Sin TLS");
        ButtonGroup tlsGroup = new ButtonGroup();
        tlsGroup.add(tlsYes);
        tlsGroup.add(tlsNo);
        tlsPanel.add(tlsYes);
        tlsPanel.add(tlsNo);
        add(tlsPanel);

        JButton sendButton = new JButton("Enviar Mensaje");
        add(sendButton);

        // Acción del botón
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCorreo();
            }
        });

        setVisible(true);
    }

    private void enviarCorreo() {
        String smtpServer = smtpServerField.getText();
        String port = portField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String from = fromField.getText();
        String to = toField.getText();
        String subject = subjectField.getText();
        String messageBody = messageArea.getText();
        boolean tls = tlsYes.isSelected();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(tls));
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);

            JOptionPane.showMessageDialog(this, "Mensaje enviado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al enviar el correo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Actividad2();
            }
        });
    }
}
