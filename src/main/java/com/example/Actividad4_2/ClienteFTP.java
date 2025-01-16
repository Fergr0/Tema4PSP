package com.example.Actividad4_2;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.IOException;
import java.io.PrintWriter;

public class ClienteFTP {
    public static void main(String[] args) throws IOException {
        FTPSClient cliente = new FTPSClient();
        String servFTP = "192.168.1.125";
        System.out.println("Nos conectamos a: " + servFTP);
        String usuario ="fer";
        String clave ="2001";
//        String servFTP = "ftp.rediris.es";
//        System.out.println("Nos conectamos a: " + servFTP);
//        String usuario ="anonymus";
//        String clave ="anonymus";

        try{
            cliente.connect(servFTP);
            cliente.enterLocalPassiveMode();
            boolean login = cliente.login(usuario, clave);
            if(login){
                System.out.println("Login correcto");
            }else {
                System.out.println("Login incorrecto");
                cliente.disconnect();
                System.exit(1);
            }
            System.out.println("Directorio actual: " + cliente.printWorkingDirectory());
            FTPFile[] files = cliente.listFiles();
            System.out.println("Ficheros en el directorio: actual " + files.length);
            String tipos[]={"Fichero", "Directorio", "Enlace simb."};
            for (int i = 0; i < files.length; i++) {
                System.out.println("\t" + files[i].getName() + "=>" + tipos[files[i].getType()]);
            }
            boolean logout=cliente.logout();
            if (logout){
                System.out.println("Logout del servidor FTP...");
            }else{
                System.out.println("Error al hacer logout");
            }
            cliente.disconnect();
            System.out.println("Desconectado...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
