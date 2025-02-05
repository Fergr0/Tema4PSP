package com.example.Actividad4_4;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Scanner;

/*Clase que permite enviar un mail desde un usuario de un servido local de correo a cualquier correo con TSl*/

public class EmailSender {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Datos del servidor SMTP
        System.out.print("Servidor SMTP: ");
        String smtpServer = scanner.nextLine();

        System.out.print("Puerto: ");
        String port = scanner.nextLine();

        System.out.print("¿Requiere TLS? (si/no): ");
        boolean tls = scanner.nextLine().equalsIgnoreCase("si");

        // Credenciales de autenticación
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        // Datos del correo
        System.out.print("Correo del remitente: ");
        String from = scanner.nextLine();

        System.out.print("Correo del destinatario: ");
        String to = scanner.nextLine();

        System.out.print("Asunto: ");
        String subject = scanner.nextLine();

        System.out.println("Mensaje (introduce * para finalizar):");
        StringBuilder messageBody = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("*")) {
            messageBody.append(line).append("\n");
        }

        // Configuración de propiedades
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(tls));
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
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
            message.setText(messageBody.toString());

            Transport.send(message);

            System.out.println("Correo enviado exitosamente.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo.");
        }

        scanner.close();
    }
}
