package com.example.actividad4_5;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

/*Esta clase se conecta a un servidor de correo POP3 (como Gmail) de forma segura utilizando TLS.
Permite autenticar al usuario, listar los mensajes disponibles y mostrar el contenido de cada uno.
Además, se desactiva la verificación SSL para pruebas, lo que facilita la conexión a servidores seguros.*/

public class Actividad4_5 {
    public static void main(String[] args) {
        String server = "pop.gmail.com";
        int port = 995;
        String username = "fergr01bbx@gmail.com";
        String password = "dzisrybwfrdmnbsj";

        try {
            // Deshabilitar la verificación SSL (solo para pruebas)
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());

            //Usar el constructor de POP3SClient con el protocolo "TLS"
            POP3SClient pop3 = new POP3SClient("TLS", true);
            pop3.setDefaultTimeout(60000); // Configura un tiempo de espera de 60 segundos

            // Conexión al servidor POP3
            pop3.connect(server, port);
            System.out.println("Conexión realizada al servidor POP3: " + server);

            if (!pop3.login(username, password)) {
                System.err.println("Error al hacer login");
                pop3.disconnect();
                return;
            }

            POP3MessageInfo[] messages = pop3.listMessages();
            if (messages == null) {
                System.out.println("No se pudieron recuperar mensajes.");
            } else {
                System.out.println("Número de mensajes: " + messages.length);

                for (POP3MessageInfo msgInfo : messages) {
                    BufferedReader reader = new BufferedReader(pop3.retrieveMessage(msgInfo.number));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    System.out.println("Respuesta del servidor: " + pop3.getReplyString());
                }
            }

            pop3.logout();
            pop3.disconnect();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

