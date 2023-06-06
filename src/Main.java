import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase que genera los códigos QR a partir de una cadena. 
 * La entrada se recibe desde los argumentos.
 */
public class Main {
        

    public static void main(String[] args) {


        QR qr = new QR("TeoriaDeCodigos", 21);
        //System.out.println(qr);
        /*String s = "";
        for (int i = 0; i < valores.length; i++){
            s += String.valueOf((char)valores[i]);
        }*/

        //
        //s = "TeoriaDeCodigos";
        //s += String.valueOf((char)236) + String.valueOf((char)17);
        /*System.out.println(s);


        ReedSolomonEC r = new ReedSolomonEC(s, 10);
            System.out.println("\nValores: \t" + Arrays.toString(valores));
        System.out.println("Result: \t" + r.getCoeficientes());*/
    } 


}