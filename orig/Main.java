/**
 * Clase que genera los códigos QR a partir de una cadena. 
 * La entrada se recibe desde los argumentos.
 */
public class Main {

    public static void main(String[] args) {
        QR qr = new QR("Una cadena más");
        System.out.println(qr);
    }
}