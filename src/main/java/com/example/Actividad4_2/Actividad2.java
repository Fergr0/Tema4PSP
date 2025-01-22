package com.example.Actividad4_2;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

//Esta clase se conecta a un servidor ftp y abre un dialogo que permite seleccionar archivos de tu disco duro
//para asi seleccionar uno y subirlo al servidor

public class Actividad2 {

    public static void main(String[] args) {
        // Detalles del servidor FTP
        String servidor = "127.0.0.1"; // Dirección del servidor FTP
        int puerto = 21; // Puerto predeterminado del FTP
        String usuario = "fer"; // Usuario del FTP
        String contraseña = "fer"; // Contraseña del FTP

        FTPSClient clienteFTP = new FTPSClient("TLSv1.2"); // Forzando TLS 1.2

        try {
            // Conectar al servidor FTP
            clienteFTP.connect(servidor, puerto);
            boolean inicioSesion = clienteFTP.login(usuario, contraseña);

            if (inicioSesion) {
                System.out.println("Conectado al servidor FTP.");
                clienteFTP.execPBSZ(0); // Proteger el tamaño del buffer
                clienteFTP.execPROT("P"); // Establecer protección para la conexión de datos

                // Modo de transferencia
                clienteFTP.enterLocalPassiveMode();
                clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);

                // Cambiar al directorio raíz
                clienteFTP.changeWorkingDirectory("/");
                System.out.println("Directorio actual: " + clienteFTP.printWorkingDirectory());

                // Abrir diálogo para seleccionar archivo
                JFileChooser selectorArchivos = new JFileChooser();
                int resultado = selectorArchivos.showOpenDialog(null);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivoSeleccionado = selectorArchivos.getSelectedFile();
                    String nombreArchivo = archivoSeleccionado.getName();

                    // Subir archivo
                    try (FileInputStream flujoEntrada = new FileInputStream(archivoSeleccionado)) {
                        boolean subido = clienteFTP.storeFile(nombreArchivo, flujoEntrada);
                        int codigoRespuesta = clienteFTP.getReplyCode();
                        if (subido || (codigoRespuesta >= 200 && codigoRespuesta < 300)) {
                            System.out.println("Archivo subido correctamente: " + nombreArchivo);
                        } else {
                            System.out.println("Error al subir el archivo.");
                            System.out.println("Código de respuesta: " + codigoRespuesta);
                            System.out.println("Mensaje de respuesta: " + clienteFTP.getReplyString());
                        }
                    }
                } else {
                    System.out.println("No se seleccionó ningún archivo.");
                }

                // Listar contenido del directorio raíz
                System.out.println("Contenido del directorio raíz:");
                Arrays.stream(clienteFTP.listFiles()).forEach(archivo -> System.out.println(archivo.getName()));

                // Cerrar sesión
                clienteFTP.logout();
            } else {
                System.out.println("No se pudo iniciar sesión en el servidor FTP.");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (clienteFTP.isConnected()) {
                    clienteFTP.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}