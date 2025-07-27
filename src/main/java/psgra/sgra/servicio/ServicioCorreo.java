package psgra.sgra.servicio;

public class ServicioCorreo {
    public static void enviarCorreo(String destino, String asunto, String mensaje) {
        // Aquí usarías JavaMail
        System.out.println("Correo enviado a " + destino + ": " + asunto + " - " + mensaje);
    }
}
