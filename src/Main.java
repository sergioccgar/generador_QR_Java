import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase que genera los códigos QR a partir de una cadena. 
 * La entrada se recibe desde los argumentos.
 */
public class Main {
        

    public static void main(String[] args) {

        System.out.println(Polinomio.generator(30));
        //String prueba = "Hello World!";
        //QR qr = new QR(prueba, 2, 4);
        /*String prueba = "a";
        for (int i = 1; i <= 2953; i++) {
            QR qr = new QR(prueba, 1,4);
            if (! qr.prueba()) {
                System.out.println("Error en el caso: " + i);
                break;
            }
            prueba += "a";
        }

        prueba = "a";
        for (int i = 1; i <= 2331; i++) {
            QR qr = new QR(prueba, 0,4);
            if (! qr.prueba()) {
                System.out.println("Error en el caso: " + i);
                break;
            }
            prueba += "a";
            //System.out.println(prueba.length());
        }*/
        //System.out.println(qr);

        /*for (int i = 2; i <= 40; i++) {
            System.out.println((int)Math.ceil((i)/7)+2);
        }*/
        /*String[] binaryStrings = {
                "000111110010010100",
                "001000010110111100",
                "001001101010011001",
                "001010010011010011",
                "001011101111110110",
                "001100011101100010",
                "001101100001000111",
                "001110011000001101",
                "001111100100101000",
                "010000101101111000",
                "010001010001011101",
                "010010101000010111",
                "010011010100110010",
                "010100100110100110",
                "010101011010000011",
                "010110100011001001",
                "010111011111101100",
                "011000111011000100",
                "011001000111100001",
                "011010111110101011",
                "011011000010001110",
                "011100110000011010",
                "011101001100111111",
                "011110110101110101",
                "011111001001010000",
                "100000100111010101",
                "100001011011110000",
                "100010100010111010",
                "100011011110011111",
                "100100101100001011",
                "100101010000101110",
                "100110101001100100",
                "100111010101000001",
                "101000110001101001"
        };*/

        /*for (int i = 7; i <= 40 ; i++) {
            QR qr = new QR("TeoriaDeCodigos", i, 1,4);
            System.out.println(qr.versInf().equals(binaryStrings[i-7]));
        }*/
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