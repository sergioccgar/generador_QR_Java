import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Clase que genera los códigos QR a partir de una cadena. 
 * La entrada se recibe desde los argumentos.
 */
public class Main {
        

    public static void main(String[] args) {

        String uno = "Hola mundo";
        QR qr_uno = new QR(uno, 3, 4);
        System.out.println(qr_uno);
        qr_uno.crearImagen("PuntoExtraDos");

        String youtube = "https://www.youtube.com/watch?v=KNMbDIKJ6T0";
        QR qr_yt = new QR(youtube, 2, 5);
        System.out.println(qr_yt);
        qr_yt.crearImagen("PuntoExtraTres");

        String ayos = "ADIOSTEORIADECODIGOS";
        QR qr_ayos = new QR(ayos, 1, 6);
        System.out.println(qr_ayos);
        qr_ayos.crearImagen("PuntoExtraCuatro");

        Scanner scn = new Scanner(System.in);
        System.out.println("Ingresa una cadena de texto a codificar: ");
        String texto = scn.nextLine();

        System.out.println("Ingresa un nivel de recuperación. (1 para L, 0 para M, 3 para Q o 2 para H)");
        boolean valid = true;
        int rec = 0;
        try {
            rec = scn.nextInt();
        } catch (Exception e) {
            valid = false;
        }

        System.out.println("Ingresa un valor de máscara (del 0 al 7): ");
        int masc = 0;
        try {
            masc = scn.nextInt();
        } catch (Exception e) {
            valid = false;
        }

        if (valid) {
            QR qr_user = new QR(texto, rec, masc);
            System.out.println(qr_user);
            qr_user.crearImagen("QrUsuario");
        } else {
            System.out.println("Ingresaste mal algún dato.");
        }

        scn.close();

        /*String prueba = "Por supuesto, aquí tienes un texto más largo:\n" +
                "\n" +
                "El sol brilla radiante en el cielo azul, iluminando el paisaje con su cálido resplandor. Las aves revolotean en el aire, entonando su melodía matutina mientras las suaves brisas acarician las hojas de los árboles. En medio de este escenario idílico, me encuentro frente a mi computadora, tratando de plasmar palabras en la pantalla que satisfagan tu solicitud de un texto más largo sin importar el contenido.\n" +
                "\n" +
                "El lenguaje es una herramienta poderosa que nos permite comunicarnos, expresar ideas, compartir conocimiento y conectar con otros seres humanos. A través de la escritura, podemos transportarnos a mundos imaginarios, explorar conceptos abstractos y transmitir emociones profundas. Es un arte que nos desafía a encontrar las palabras precisas, a estructurar oraciones coherentes y a cautivar al lector con nuestra narrativa.\n" +
                "\n" +
                "Cada palabra que escribo se une a las demás, formando frases, párrafos y finalmente un texto completo. Es como un rompecabezas en constante construcción, donde cada pieza encaja de manera precisa y contribuye al conjunto. Las palabras fluyen de mi mente a mis dedos, mientras las ideas se despliegan en la pantalla, dándoles forma y vida.\n" +
                "\n" +
                "En este vasto océano de palabras, se abren infinitas posibilidades. Puedo describirte la majestuosidad de las montañas, la delicadeza de una flor en floración o la emoción de una victoria deportiva. Puedo sumergirme en los misterios de la ciencia, explorar los confines del universo o analizar las complejidades de la mente humana. Las palabras son como pinceles en manos de un artista, capaces de crear imágenes vívidas y despertar emociones profundas.\n" +
                "\n" +
                "Sin embargo, en medio de esta corriente de palabras, me detengo un momento para reflexionar sobre su verdadero significado. ¿Qué tan poderosas son realmente? ¿Cómo pueden transformar nuestras vidas y dar forma a nuestra realidad? Las palabras pueden construir puentes entre culturas, unir corazones separados y transmitir mensajes de amor, esperanza y solidaridad. Pero también pueden ser armas afiladas que hieren, manipulan y dividen.\n" +
                "\n" +
                "En última instancia, el poder de las palabras reside en nosotros, en cómo las utilizamos y en el impacto que generan. Podemos elegir utilizarlas para inspirar, motivar y elevar a otros, o podemos emplearlas de manera negligente, irresponsable o dañina. La elección es nuestra, y es importante recordar que las palabras tienen consecuencias, tanto para nosotros mismos como para aquellos a quienes alcanzan.\n" +
                "\n" +
                "Así que aquí estoy, escribiendo este texto más largo, explorando las profundidades del lenguaje y la escritura. Espero haber cumplido con tu solicitud de extenderme sin restricciones en cuanto a su contenido. Pero más allá de la extensión, mi deseo es que estas palabras te hayan hecho reflexionar, te hayan transportado a un lugar de pensamiento y te hayan recordado el poder que reside en tus propias palabras.\n" +
                "\n" +
                "¡Y así concluye este extenso texto que ha llevado tus ojos a través de un recorrido de letras y significados!";*/
        // Este código crea todos los posibles códigos QR con la cadena de texto prueba, tomando subcadenas de cada longitud, desde
        // longitud 1 hasta longitud 2953, que es el máximo número de caracteres en byte mode que puede tener un código QR, para cada
        // longitud crea un QR por cada nivel de corrección, es decir, 4, y a cada uno le aplica cada máscara posible. Esto es, se crean
        // 94,496 códigos QR. A pesar de que cada imagen pesa poco, se tarda muchísimo en tan sólo crear 15,000 códigos.
        /*int current = 1;
        for (int i = 1; i < 2953; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 8; k++) {
                    QR qr = new QR(prueba.substring(0, i), j, k);
                    qr.crearImagen(String.valueOf(current));
                    System.out.println(current);
                    current += 1;
                }
            }
        }*/
        //System.out.println(256^285);
        //System.out.println(Polinomio.generator(30));
        /*String prueba = "HELLOOOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaHELLOOOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaHELLOOOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaHELLOOOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaHELLOOOaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String prueba2 = "Por supuesto, aquí tienes un texto más largo:\n" +
                "\n" +
                "El sol brilla radiante en el cielo azul, iluminando el paisaje con su cálido resplandor. Las aves revolotean en el aire, entonando su melodía matutina mientras las suaves brisas acarician las hojas de los árboles. En medio de este escenario idílico, me encuentro frente a mi computadora, tratando de plasmar palabras en la pantalla que satisfagan tu solicitud de un texto más largo sin importar el contenido.\n" +
                "\n" +
                "El lenguaje es una herramienta poderosa que nos permite comunicarnos, expresar ideas, compartir conocimiento y conectar con otros seres humanos. A través de la escritura, podemos transportarnos a mundos imaginarios, explorar conceptos abstractos y transmitir emociones profundas. Es un arte que nos desafía a encontrar las palabras precisas, a estructurar oraciones coherentes y a cautivar al lector con nuestra narrativa.\n" +
                "\n" +
                "Cada palabra que escribo se une a las demás, formando frases, párrafos y finalmente un texto completo. Es como un rompecabezas en constante construcción, donde cada pieza encaja de manera precisa y contribuye al conjunto. Las palabras fluyen de mi mente a mis dedos, mientras las ideas se despliegan en la pantalla, dándoles forma y vida.\n" +
                "\n" +
                "En este vasto océano de palabras, se abren infinitas posibilidades. Puedo describirte la majestuosidad de las montañas, la delicadeza de una flor en floración o la emoción de una victoria deportiva. Puedo sumergirme en los misterios de la ciencia, explorar los confines del universo o analizar las complejidades de la mente humana. Las palabras son como pinceles en manos de un artista, capaces de crear imágenes vívidas y despertar emociones profundas.\n" +
                "\n" +
                "Sin embargo, en medio de esta corriente de palabras, me detengo un momento para reflexionar sobre su verdadero significado. ¿Qué tan poderosas son realmente? ¿Cómo pueden transformar nuestras vidas y dar forma a nuestra realidad? Las palabras pueden construir puentes entre culturas, unir corazones separados y transmitir mensajes de amor, esperanza y solidaridad. Pero también pueden ser armas afiladas que hieren, manipulan y dividen.\n" +
                "\n" +
                "En última instancia, el poder de las palabras reside en nosotros, en cómo las utilizamos y en el impacto que generan. Podemos elegir utilizarlas para inspirar, motivar y elevar a otros, o podemos emplearlas de manera negligente, irresponsable o dañina. La elección es nuestra, y es importante recordar que las palabras tienen consecuencias, tanto para nosotros mismos como para aquellos a quienes alcanzan.\n" +
                "\n" +
                "Así que aquí estoy, escribiendo este texto más largo, explorando las profundidades del lenguaje y la escritura. Espero haber cumplido con tu solicitud de extenderme sin restricciones en cuanto a su contenido. Pero más allá de la extensión, mi deseo es que estas palabras te hayan hecho reflexionar, te hayan transportado a un lugar de pensamiento y te hayan recordado el poder que reside en tus propias palabras.\n" +
                "\n" +
                "¡Y así concluye este extenso texto que ha llevado tus ojos a través de un recorrido de letras y significados!";
        System.out.println(prueba2.length());
        QR qr = new QR(prueba2, 3, 5);
        System.out.println(qr);
        qr.crearImagen();*/
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