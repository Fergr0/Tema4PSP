package com.example.actividad4_6;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.net.ftp.FTPClient;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

// Este programa permite a los usuarios conectarse a un servidor FTP localhost,
// registrar la fecha y hora de cada conexión en un archivo LOG.TXT,
// y enviar un resumen de las conexiones realizadas mediante correo SMTP.
public class Actividad4_6 {
    private static final String FTP_SERVER = "localhost";
    private static final int FTP_PORT = 21;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Ingreso de credenciales del usuario
            System.out.print("Nombre de usuario: ");
            String username = scanner.nextLine();

            System.out.print("Contraseña: ");
            String password = scanner.nextLine();

            // Conexión al servidor FTP
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(FTP_SERVER, FTP_PORT);

            if (ftpClient.login(username, password)) {
                System.out.println("Conexión FTP exitosa.");

                String logDirectory = "/" + username + "/LOG";
                String logFile = logDirectory + "/LOG.TXT";

                ftpClient.changeWorkingDirectory(logDirectory);

                // Registro de la fecha y hora de la conexión
                String timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").format(new Date());
                String logEntry = "Hora de conexión: " + timestamp + "\n";

                // Verificar si el archivo LOG.TXT existe
                InputStream inputStream = ftpClient.retrieveFileStream(logFile);
                StringBuilder content = new StringBuilder("Conexiones del usuario.\n");

                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    inputStream.close();
                    ftpClient.completePendingCommand();
                }

                // Añadir el nuevo registro de conexión
                content.append(logEntry);

                // Guardar los cambios en LOG.TXT
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.toString().getBytes());
                ftpClient.storeFile(logFile, byteArrayInputStream);

                System.out.println("Registro actualizado correctamente en LOG.TXT.");

                ftpClient.logout();
                ftpClient.disconnect();

                // Envío del resumen por correo
                System.out.print("Introduce el correo del destinatario: ");
                String recipient = scanner.nextLine();

                enviarCorreo(username, recipient, content.toString());
            } else {
                System.out.println("Error de autenticación en el servidor FTP.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enviarCorreo(String username, String recipient, String logContent) {
        final String smtpHost = "smtp.gmail.com";
        final int smtpPort = 587;
        final String smtpUser = "fergr01bbx@gmail.com";
        final String smtpPassword = "dzisrybwfrdmnbsj";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Resumen de conexiones para " + username);
            message.setText(logContent);

            Transport.send(message);

            System.out.println("Correo enviado exitosamente a " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
