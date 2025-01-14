package com.example.Actividad4_2;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class ClienteFTP {
    public static void main(String[] args) throws IOException {
        FTPClient cliente = new FTPClient();
        String servFTP = "ftpupload.net";
        System.out.println("Nos conectamos a: " + servFTP);
        cliente.connect(servFTP);
        System.out.println(cliente.getReplyString());

        int respuesta = cliente.getReplyCode();
        System.out.println("Respuesta: " + respuesta);

        if(!FTPReply.isPositiveCompletion(respuesta)) {
            cliente.disconnect();
            System.out.println("Conexión rechazada" + respuesta);
            System.exit(0);
        }

        cliente.disconnect();
        System.out.println("Conexión finalizada");
    }
}
