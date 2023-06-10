import java.lang.annotation.Target;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Clase que dado una cadena, crea su respectivo código QR para ser escaneado con 
 * teléfono.
 */
public class QR {
    
    /* El texto a guardar en el código QR */
    private String texto;
    /* El texto decodificado a binario */
    private String binario;
    /* La matriz de código qr. */
    private boolean[][] qr;
    /* Tamaño de la matriz */
    private int TAMANHO;
    /* Índice de la cadena hasta donde ya se han insertado los datos en el QR */
    private int indice;
    /* Cadena que se insertará en el QR */
    private String bits_insertar;
    /* Valor de la columna que corresponde al lugar del qr que se modificará */
    private int columna;
    /* Valor de la fila que corresponde al lugar del qr que se modificará */
    private int fila;

    /* Tipo máscara [0-7]*/
    private String MASCARA;
    /* Tipo Codificación [0-8]*/
    private String CODIFICACION;
    /* Longitud del mensaje */
    private String LONG_MENSAJE;
    /* Tamaño mensaje */
    private int TAMAÑO_MENSAJE;
    /* Recuperación de error [0-3]*/
    private String RECUPERACION;
    /* información redundante */
    private String REDUNDANCIA;
    /* versión del código QR */
    private int VERSION;
    /* Datos para las coordenadas iniciales de ciertas versiones: */
    private int[] ALINEACION_1 = {6, 18};
    private int[] ALINEACION_2 = {6, 22, 38};
    private int[] ALINEACION_3 = {6, 26, 46, 66};
    private int[] ALINEACION_4 = {6, 28, 50, 72, 94};
    private int[] ALINEACION_5 = {6, 26, 50, 74, 98, 122};
    private int[] ALINEACION_6 = {6, 30, 54, 78,102, 126, 150};
    /* Lista que almacenará, como las listas ALINEACION_X, los datos de las coordenadas que se usarán en los patrones de alineación*/
    private int[] COORDENADAS_ALINEACION;
    /* Cadena que almacenará la información de nivel de corrección y máscara. */
    private String CADENA_FORMATO;
    /* Listas que almacenan el máximo número de caracteres en Byte Mode que una versión n puede almacenar con un nivel de corrección X */
    /* El elemento con índice x indica el máximo número de caracteres que puede almacenar la versión x+1 con el nivel de corrección indicado por la lista */
    private int[] BM_Max_L = {17, 32, 53, 78, 106, 134, 154, 192, 230, 271, 321, 367, 425, 458, 520, 586, 644, 718, 792, 858, 929, 1003, 1091, 1171, 1273, 1367, 1465, 1528, 1628, 1732, 1840, 1952, 2068, 2188, 2303, 2431, 2563, 2699, 2809, 2953};
    private int[] BM_Max_M = {14, 26, 42, 62, 84, 106, 122, 152, 180, 213, 251, 287, 331, 362, 412, 450, 504, 560, 624, 666, 711, 779, 857, 911, 997, 1059, 1125, 1190, 1264, 1370, 1452, 1538, 1628, 1722, 1809, 1911, 1989, 2099, 2213, 2331};
    private int[] BM_Max_Q = {11, 20, 32, 46, 60, 74, 86, 108, 130, 151, 177, 203, 241, 258, 292, 311, 364, 394, 442, 482, 509, 565, 611, 661, 715, 751, 805, 868, 908, 982, 1030, 1112, 1168, 1228, 1283, 1351, 1423, 1499, 1579, 1663};
    private int[] BM_Max_H = {7, 14, 24, 34, 44, 58, 64, 84, 98, 119, 137, 155, 177, 194, 220, 250, 280, 310, 338, 382, 403, 439, 461, 511, 535, 593, 625, 658, 698, 742, 790, 842, 898, 958, 983, 1051, 1093, 1139, 1219, 1273};
    /* Valor del nivel de recuperación en decimal */
    private int RECUPERACION_DEC;
    /* Cadena de bits a guardar en el QR */
    private String bits;
    /* Cadena de bits después de aplicarles el método Reed Solomon */
    private String RS_bits;

    private int[] CODEWORDS = {
            19, 16, 13, 9,
            34, 28, 22, 16,
            55, 44, 34, 26,
            80, 64, 48, 36,
            108, 86, 62, 46,
            136, 108, 76, 60,
            156, 124, 88, 66,
            194, 154, 110, 86,
            232, 182, 132, 100,
            274, 216, 154, 122,
            324, 254, 180, 140,
            370, 290, 206, 158,
            428, 334, 244, 180,
            461, 365, 261, 197,
            523, 415, 295, 223,
            589, 453, 325, 253,
            647, 507, 367, 283,
            721, 563, 397, 313,
            795, 627, 445, 341,
            861, 669, 485, 385,
            932, 714, 512, 406,
            1006, 782, 568, 442,
            1094, 860, 614, 464,
            1174, 914, 664, 514,
            1276, 1000, 718, 538,
            1370, 1062, 754, 596,
            1468, 1128, 808, 628,
            1531, 1193, 871, 661,
            1631, 1267, 911, 701,
            1735, 1373, 985, 745,
            1843, 1455, 1033, 793,
            1955, 1541, 1115, 845,
            2071, 1631, 1171, 901,
            2191, 1725, 1231, 961,
            2306, 1812, 1286, 986,
            2434, 1914, 1354, 1054,
            2566, 1992, 1426, 1096,
            2702, 2102, 1502, 1142,
            2812, 2216, 1582, 1222,
            2956, 2334, 1666, 1276
            };

    /* Bits necesarios para llenar el código QR de la versión que sea con el nivel de corrección dado. */
    private int BITS_NECESARIOS;

    private int[] ERR_BLOCKS_LIST = {
            7, 10, 13, 17,
            10, 16, 22, 28,
            15, 26, 18, 22,
            20, 18, 26, 16,
            26, 24, 18, 22,
            18, 16, 24, 28,
            20, 18, 18, 26,
            24, 22, 22, 26,
            30, 22, 20, 24,
            18, 26, 24, 28,
            20, 30, 28, 24,
            24, 22, 26, 28,
            26, 22, 24, 22,
            30, 24, 20, 24,
            22, 24, 30, 24,
            24, 28, 24, 30,
            28, 28, 28, 28,
            30, 26, 28, 28,
            28, 26, 26, 26,
            28, 26, 30, 28,
            28, 26, 28, 30,
            28, 28, 30, 24,
            30, 28, 30, 30,
            30, 28, 30, 30,
            26, 28, 30, 30,
            28, 28, 28, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30,
            30, 28, 30, 30
    };

    /* Bloques necesarios de corrección */
    private int ERR_BLOCKS;

    /* Bloques de información necesarios según la versión y nivel de corrección */
    private String[] data_blocks;

    /* Bloques de codewords de corrección de errores, es necesario uno por cada bloque de de información */
    private int[][] ec_cw_blocks;

    /* Bloques de codewords de información del grupo 1 */
    private int[][] data_cw_blocks_group1;

    /* Bloques de codewords de información del grupo 2 */
    private int[][] data_cw_blocks_group2;

    /* Codewords de información intercaladas */
    private int[] interleaved_data;

    /* Codewords de corrección de errores intercaladas */
    private int[] interleaved_ec;

    /* Codewords de información intercaladas seguidas de codewords de corrección de errores intercaladas */
    private int[] data_ec_codewords;

    /* Arreglo de bytes a insertar en el QR */
    String[] binary_to_insert;

    /* Cada versión requiere que la cadena de bits se divida en grupos y bloques de codewords para el nivel de recuperación L*/
    /* Esto sigue las especificaciones de la página de Thonky https://www.thonky.com/qr-code-tutorial/error-correction-table */
    private int[][] GRUPOS_Y_BLOQUES_L = {
            {1, 19, 0, 0},
            {1, 34, 0, 0},
            {1, 55, 0, 0},
            {1, 80, 0, 0},
            {1, 108, 0, 0},
            {2, 68, 0, 0},
            {2, 78, 0, 0},
            {2, 97, 0, 0},
            {2, 116, 0, 0},
            {2, 68, 2, 69},
            {4, 81, 0, 0},
            {2, 92, 2, 93},
            {4, 107, 0, 0},
            {3, 115, 1, 116},
            {5, 87, 1, 88},
            {5, 98, 1, 99},
            {1, 107, 5, 108},
            {5, 120, 1, 121},
            {3, 113, 4, 114},
            {3, 107, 5, 108},
            {4, 116, 4, 117},
            {2, 111, 7, 112},
            {4, 121, 5, 122},
            {6, 117, 4, 118},
            {8, 106, 4, 107},
            {10, 114, 2, 115},
            {8, 122, 4, 123},
            {3, 117, 10, 118},
            {7, 116, 7, 117},
            {5, 115, 10, 116},
            {13, 115, 3, 116},
            {17, 115, 0, 0},
            {17, 115, 1, 116},
            {13, 115, 6, 116},
            {12, 121, 7, 122},
            {6, 121, 14, 122},
            {17, 122, 4, 123},
            {4, 122, 18, 123},
            {20, 117, 4, 118},
            {19, 118, 6, 119},
    };

    /* Cada versión requiere que la cadena de bits se divida en grupos y bloques de codewords para el nivel de recuperación M */
    /* Esto sigue las especificaciones de la página de Thonky https://www.thonky.com/qr-code-tutorial/error-correction-table */
    private int[][] GRUPOS_Y_BLOQUES_M = {
            {1, 16, 0, 0},
            {1, 28, 0, 0},
            {1, 44, 0, 0},
            {2, 32, 0, 0},
            {2, 43, 0, 0},
            {4, 27, 0, 0},
            {4, 31, 0, 0},
            {2, 38, 2, 39},
            {3, 36, 2, 37},
            {4, 43, 1, 44},
            {1, 50, 4, 51},
            {6, 36, 2, 37},
            {8, 37, 1, 38},
            {4, 40, 5, 41},
            {5, 41, 5, 42},
            {7, 45, 3, 46},
            {10, 46, 1, 47},
            {9, 43, 4, 44},
            {3, 44, 11, 45},
            {3, 41, 13, 42},
            {17, 42, 0, 0},
            {17, 46, 0, 0},
            {4, 47, 14, 48},
            {6, 45, 14, 46},
            {8, 47, 13, 48},
            {19, 46, 4, 47},
            {22, 45, 3, 46},
            {3, 45, 23, 46},
            {21, 45, 7, 46},
            {19, 47, 10, 48},
            {2, 46, 29, 47},
            {10, 46, 23, 47},
            {14, 46, 21, 47},
            {14, 46, 23, 47},
            {12, 47, 26, 48},
            {6, 47, 34, 48},
            {29, 46, 14, 47},
            {13, 46, 32, 47},
            {40, 47, 7, 48},
            {18, 47, 31, 48},
    } ;

    /* Cada versión requiere que la cadena de bits se divida en grupos y bloques de codewords para el nivel de recuperación Q */
    /* Esto sigue las especificaciones de la página de Thonky https://www.thonky.com/qr-code-tutorial/error-correction-table */
    private int[][] GRUPOS_Y_BLOQUES_Q = {
            {1, 13, 0, 0},
            {1, 22, 0, 0},
            {2, 17, 0, 0},
            {2, 24, 0, 0},
            {2, 15, 2, 16},
            {4, 19, 0, 0},
            {2, 14, 4, 15},
            {4, 18, 2, 19},
            {4, 16, 4, 17},
            {6, 19, 2, 20},
            {4, 22, 4, 23},
            {4, 20, 6, 21},
            {8, 20, 4, 21},
            {11, 16, 5, 17},
            {5, 24, 7, 25},
            {15, 19, 2, 20},
            {1, 22, 15, 23},
            {17, 22, 1, 23},
            {17, 21, 4, 22},
            {15, 24, 5, 25},
            {17, 22, 6, 23},
            {7, 24, 16, 25},
            {11, 24, 14, 25},
            {11, 24, 16, 25},
            {7, 24, 22, 25},
            {28, 22, 6, 23},
            {8, 23, 26, 24},
            {4, 24, 31, 25},
            {1, 23, 37, 24},
            {15, 24, 25, 25},
            {42, 24, 1, 25},
            {10, 24, 35, 25},
            {29, 24, 19, 25},
            {44, 24, 7, 25},
            {39, 24, 14, 25},
            {46, 24, 10, 25},
            {49, 24, 10, 25},
            {48, 24, 14, 25},
            {43, 24, 22, 25},
            {34, 24, 34, 25},
    };

    /* Cada versión requiere que la cadena de bits se divida en grupos y bloques de codewords para el nivel de recuperación H */
    /* Esto sigue las especificaciones de la página de Thonky https://www.thonky.com/qr-code-tutorial/error-correction-table */
    private int[][] GRUPOS_Y_BLOQUES_H = {
            {1, 9, 0, 0},
            {1, 16, 0, 0},
            {2, 13, 0, 0},
            {4, 9, 0, 0},
            {2, 11, 2, 12},
            {4, 15, 0, 0},
            {4, 13, 1, 14},
            {4, 14, 2, 15},
            {4, 12, 4, 13},
            {6, 15, 2, 16},
            {3, 12, 8, 13},
            {7, 14, 4, 15},
            {12, 11, 4, 12},
            {11, 12, 5, 13},
            {11, 12, 7, 13},
            {3, 15, 13, 16},
            {2, 14, 17, 15},
            {2, 14, 19, 15},
            {9, 13, 16, 14},
            {15, 15, 10, 16},
            {19, 16, 6, 17},
            {34, 13, 0, 0},
            {16, 15, 14, 16},
            {30, 16, 2, 17},
            {22, 15, 13, 16},
            {33, 16, 4, 17},
            {12, 15, 28, 16},
            {11, 15, 31, 16},
            {19, 15, 26, 16},
            {23, 15, 25, 16},
            {23, 15, 28, 16},
            {19, 15, 35, 16},
            {11, 15, 46, 16},
            {59, 16, 1, 17},
            {22, 15, 41, 16},
            {2, 15, 64, 16},
            {24, 15, 46, 16},
            {42, 15, 32, 16},
            {10, 15, 67, 16},
            {20, 15, 61, 16},
    };


    public QR(String texto, int recuperacion, int mascara){
        this.texto = texto;
        binario = "";
        MASCARA = String.format("%" + 3 + "s", Integer.toBinaryString(mascara)).replace(' ', '0');
        RECUPERACION = String.format("%" + 2 + "s", Integer.toBinaryString(recuperacion)).replace(' ', '0');
        RECUPERACION_DEC = recuperacion;
        // System.out.println(RECUPERACION);
        CODIFICACION = String.format("%" + 4 + "s", Integer.toBinaryString(4)).replace(' ', '0'); //cambiar a 4, byte mode
        // System.out.println(CODIFICACION);
        TAMAÑO_MENSAJE = texto.length();
        textoABinario();
        // System.out.println("texto en binario: " + binario);
        switch (recuperacion) {
            case 1: //L
                if (texto.length() > 2953) { // Si se eligió este nivel de corrección, pero el texto es demasiado grande, ser recortará a la máxima capacidad para dicho nivel de corrección.
                    System.out.println("Demasiada información, se recortará el texto a: ");
                    texto = texto.substring(0, 2953);
                    System.out.println(texto);
                }
                buscarNumero(texto.length(), BM_Max_L);
                break;
            case 0: //M
                if (texto.length() > 2331) {
                    System.out.println("Demasiada información, se recortará el texto a: ");
                    texto = texto.substring(0, 2331);
                    System.out.println(texto);
                }
                buscarNumero(texto.length(), BM_Max_M);
                break;
            case 3: //Q
                if (texto.length() > 1663) {
                    System.out.println("Demasiada información, se recortará el texto a: ");
                    texto = texto.substring(0, 1663);
                    System.out.println(texto);
                }
                buscarNumero(texto.length(), BM_Max_Q);
                break;
            case 2: //H
                if (texto.length() > 1273) {
                    System.out.println("Demasiada información, se recortará el texto a: ");
                    texto = texto.substring(0, 1273);
                    System.out.println(texto);
                }
                buscarNumero(texto.length(), BM_Max_H);
                break;
        }
        //System.out.println("Version: " + VERSION);
        TAMANHO = 21 + (VERSION-1) * 4;
        qr = new boolean[TAMANHO][TAMANHO];

        if (VERSION > 9) {
            LONG_MENSAJE = String.format("%" + 16 + "s", Integer.toBinaryString(texto.length())).replace(' ', '0');
        } else {
            LONG_MENSAJE = String.format("%" + 8 + "s", Integer.toBinaryString(texto.length())).replace(' ', '0');
        }

        // System.out.println("Longitud del mensaje en binario: " + LONG_MENSAJE);

        BITS_NECESARIOS = CODEWORDS[((VERSION-1)*4)+(RECUPERACION_DEC+(int)Math.pow(-1, RECUPERACION_DEC+2))]*8;
        // System.out.println("Bits necesarios: " + BITS_NECESARIOS);
        // System.out.println("Codificación (byte mode): " + CODIFICACION);
        // System.out.println("La longitud del mensaje en binario es: " + LONG_MENSAJE);
        // System.out.println("El mensaje en binario es: " + binario);
        bits = "" + CODIFICACION + LONG_MENSAJE + binario; // Cadena que contiene el nivel de corrección, la longitud del mensaje y el mensaje en binario
        // System.out.println("Por lo que la cadena es:" + bits);
        // Se agregan los bits 0 de fin de mensaje.
        // System.out.println("La cantidad de bits que tenemos es: " + bits.length());
        if (bits.length() + 4 <= BITS_NECESARIOS) {
            bits += "0000";
            // System.out.println("Se agregaron 4 bits. Ahora la cantidad de bits que tenemos es: " + bits.length());
        } else {
            while (bits.length() < BITS_NECESARIOS) {
                bits += "0";
            }
        }
        while (bits.length() % 8 != 0) {
            // System.out.println("Se agregaron tantos 0s para que la cadena de bits sea de longitud multiplo de 8.");
            bits += "0";
        }
        // System.out.println("La cantidad de bits que tenemos es: " + bits.length());
        // Ahora, mientras no tengamos una cadena de 272 bits, debemos agregar los pad_bytes
        String[] pad_bits = {"11101100", "00010001"};
        int i = 0;
        while (bits.length() < BITS_NECESARIOS) {
            bits += pad_bits[i];
            i = (i+1) % 2;
        }
        /*System.out.println("La cadena de bits ahora tiene: " + bits.length() + " bits.");
        *//*QR v1 H*//*
        System.out.println("Sólo tenemos un bloque de data cw y es:\n" + bits);
        System.out.println("Con longitud:\n" + bits.length());*/
        /* Fin */
        /* Esta parte considera trabajar con un QR v3 Q. Necesitaremos 2 bloques de datawords*/
        /*data_blocks = new String[2];
        data_blocks[0] = bits.substring(0, 136);
        System.out.println("El primer bloque es: " + data_blocks[0] + " y es de longitud: " + data_blocks[0].length());
        System.out.println("Este bloque contiene: " +  data_blocks[0].length()/8 + " codewords.");
        System.out.println("Las codewords son: ");
        for (int j = 0; j < data_blocks[0].length(); j+=8) {
            System.out.println(data_blocks[0].substring(j, j+8) + " = " + Integer.parseInt(data_blocks[0].substring(j, j+8),2));
        }
        data_blocks[1] = bits.substring(136);
        System.out.println("El segundo bloque es: " + data_blocks[1] + " y es de longitud: " + data_blocks[1].length());
        System.out.println("Este bloque contiene: " +  data_blocks[1].length()/8 + " codewords.");
        System.out.println("Las codewords son: ");
        for (int j = 0; j < data_blocks[1].length(); j+=8) {
            System.out.println(data_blocks[1].substring(j, j+8) + " = " + Integer.parseInt(data_blocks[1].substring(j, j+8),2));
        }*/
        /* Fin */
        construyeQR();
    }

    /**
     * Cada versión de QR requiere que la cadena de bits del mensaje se divida en grupos y estos grupos en bloques.
     * Este método calcula los bloques se deben obtener de la cadena de bits según la versión y el nivel de corrección.
     */
    private void obtenerDataBlocks() {
        int[][] data; // Esta variable almacenará cuántos bloques de datos debe tener el QR que se está generando.
        switch (RECUPERACION_DEC) {
            case 1:
                data = GRUPOS_Y_BLOQUES_L;
                break;
            case 0:
                data = GRUPOS_Y_BLOQUES_M;
                break;
            case 3:
                data = GRUPOS_Y_BLOQUES_Q;
                break;
            case 2:
                data = GRUPOS_Y_BLOQUES_H;
                break;
            default:
                data = new int[1][1];
                break;
        }
        /*for (int[] x : data) {
            for (int y : x) {
                System.out.println(y);
            }
        }*/

        // System.out.println("ppppppppppppp" + data[VERSION-1][0]);
        // System.out.println("qqqqqqqqqqqqq" + data[VERSION-1][2]);
        int num_bloques = data[VERSION-1][0] + data[VERSION-1][2];

        data_blocks = new String[num_bloques];
        int current_block = 0;
        int cut = 0;
        for (int i = 0; i < data[VERSION-1][0]; i++) {
            data_blocks[current_block] = bits.substring(cut, cut + (data[VERSION-1][1]*8));
            current_block += 1;
            cut = cut + (data[VERSION-1][1]*8);
        }
        for (int i = 0; i < data[VERSION-1][2]; i++) {
            data_blocks[current_block] = bits.substring(cut, cut + (data[VERSION-1][3]*8));
            current_block += 1;
            cut = cut + (data[VERSION-1][1]*8);
        }
    }

    /**
     * Teniendo los bloques de datos podemos generar las codewords de la corrección de errores
     * con el algoritmo Reed Solomon.
     */
    private void generarCodewordsCorreccionErrores() {
        ec_cw_blocks = new int[data_blocks.length][ERR_BLOCKS];
        for (int i = 0; i < data_blocks.length; i++) {
            ReedSolomonEC rs = new ReedSolomonEC(data_blocks[i], VERSION, ERR_BLOCKS);
            Polinomio r = rs.getResultado();
            int[] ec_cw_block = new int[r.getLista().size()];

            for (int j = 0; j < ec_cw_block.length; j++) {
                String temp = r.getLista().get(j).toString();
                int index = temp.indexOf('x');
                ec_cw_block[j] = Integer.parseInt(temp.substring(0, index));
            }
            for (int k = 0; k < ec_cw_block.length; k++) {
                ec_cw_blocks[i][k] = ec_cw_block[k];
            }
        }
        /*int howmany = 0;
        for (int[] x : ec_cw_blocks) {
            for (int y : x) {
                // System.out.print(":" + y + ":");
                howmany += 1;
            }
            System.out.println();
        }
        System.out.println("WE HAVE: " + howmany);*/
    }

    /**
     * Dados los bloques de datos (que almacenan cadenas de bits), convertimos cada cadena de bits de cada
     * bloque de datos en su valor entero. Cada valor entero corresponde a una codeword de la información.
     */
    private void generarCodewordsData() {
        int[][] data; // Esta variable almacenará cuántos bloques de datos debe tener el QR que se está generando.
        switch (RECUPERACION_DEC) {
            case 1:
                data = GRUPOS_Y_BLOQUES_L;
                break;
            case 0:
                data = GRUPOS_Y_BLOQUES_M;
                break;
            case 3:
                data = GRUPOS_Y_BLOQUES_Q;
                break;
            case 2:
                data = GRUPOS_Y_BLOQUES_H;
                break;
            default:
                data = new int[1][1];
                break;
        }
        /*data_cw_blocks_group1 = new int[data[VERSION-1][1]][data[VERSION-1][0]];
        data_cw_blocks_group2 = new int[data[VERSION-1][3]][data[VERSION-1][2]];*/
        data_cw_blocks_group1 = new int[data[VERSION-1][0]][data[VERSION-1][1]];
        data_cw_blocks_group2 = new int[data[VERSION-1][2]][data[VERSION-1][3]];
        // System.out.println("El grupo 1 tiene " +  data_cw_blocks_group1.length + " codewords por bloque y " + data_cw_blocks_group1[0].length + " bloques");
        // System.out.println("El grupo 2 tiene " +  data_cw_blocks_group2.length + " codewords por bloque y " + data_cw_blocks_group1[0].length + " bloques");
        /*for (String s: data_blocks) {
            System.out.println(s);
        }*/
        int current_block = 0;
        // System.out.println("Este loop itera: " + data[VERSION-1][0] + " veces.");
        for (int i = 0; i < data[VERSION-1][0]; i++) {
            int[] dataCodewords = dataCodewords(data_blocks[current_block]);
            /*for (int x : dataCodewords) {
                System.out.println(x);
            }*/
            for (int j = 0; j < dataCodewords.length; j++) {
                data_cw_blocks_group1[current_block][j] = dataCodewords[j];
            }
            current_block += 1;
        }
        /*for (int[] x : data_cw_blocks_group1) {
            for (int y : x) {
                System.out.print(y + " - ");
            }
            System.out.println();
        }*/
        //current_block = 0; // corregí esto, hmmm
        int done_blocks = current_block;
        for (int i = 0; i < data[VERSION-1][2]; i++) {
            int[] dataCodewords = dataCodewords(data_blocks[current_block]);
            for (int j = 0; j < dataCodewords.length; j++) {
                data_cw_blocks_group2[current_block-done_blocks][j] = dataCodewords[j];
            }
            current_block += 1;
        }
        /*System.out.println("-------------------------");
        for (int[] x : data_cw_blocks_group2) {
            for (int y : x) {
                System.out.print(y + " - ");
            }
            System.out.println();
        }*/
    }

    /**
     * Método que intercala las codewords de información y de corrección de errores.
     */
    private void interleaveCodewords() {
        int[][] data; // Esta variable almacenará cuántos bloques de datos debe tener el QR que se está generando.
        switch (RECUPERACION_DEC) {
            case 1:
                data = GRUPOS_Y_BLOQUES_L;
                break;
            case 0:
                data = GRUPOS_Y_BLOQUES_M;
                break;
            case 3:
                data = GRUPOS_Y_BLOQUES_Q;
                break;
            case 2:
                data = GRUPOS_Y_BLOQUES_H;
                break;
            default:
                data = new int[1][1];
                break;
        }
        interleaved_data = new int[(data[VERSION-1][0] * data[VERSION-1][1]) + (data[VERSION-1][2] * data[VERSION-1][3])];
        // System.out.println("We'll have: " + interleaved_data.length + " codewords.");
        int current = 0;
        // Intercalamos las codewords de información, tomando la primera del primer bloque, luego la primera del segundo
        // bloque, la primera del tercer bloque, y así sucesivamente; después tomamos la segunda del primer bloque, la segunda
        // del segundo, etc. Por ahora cubriremos solo hasta la n-ésima codeword donde n es igual al número de codewords
        // en los bloques del grupo 1, pues los bloques del grupo 2 tienen, cada uno, una codeword más. Estas (n+1)-ésimas
        // codewords, del segundo grupo, se agregarán en un ciclo posterior.

        for (int i = 0; i < data[VERSION-1][1]; i++) {
            for (int j = 0; j < data[VERSION-1][0] + data[VERSION-1][2]; j++) {
                if (j < data[VERSION-1][0]) {
                    // System.out.print(":" + data_cw_blocks_group1[j][i] + ":");
                    interleaved_data[current] = data_cw_blocks_group1[j][i];
                    current += 1;
                } else {
                    //System.out.print(":" + data_cw_blocks_group2[j-2][i] + ":");
                    /*System.out.println("\n" + (j-(data[VERSION-1][0])));
                    System.out.println(data[VERSION-1][0]);*/
                    interleaved_data[current] = data_cw_blocks_group2[j-(data[VERSION-1][0])][i];
                    current += 1;
                    //if (current % 4 == 0) { System.out.println(); }
                }
            }
        }
        // Este ciclo agrega las codewords faltantes de los bloques del grupo 2.
        try {
            // System.out.println(data_cw_blocks_group2[0][data[VERSION-1][3]-1]);
            // System.out.println(data_cw_blocks_group2[1][data[VERSION-1][3]-1]);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        if (data[VERSION-1][2] > 0) {
            for (int i = 0; i < data[VERSION-1][2]; i++) {
                interleaved_data[current] = data_cw_blocks_group2[i][data[VERSION-1][3]-1];
                current += 1;
            }
        }
        // System.out.println("Las codewords de la información intercaladas son: ");
        /*for (int x : interleaved_data) {
            System.out.println(x);
        }*/
        // ¡Ya tenemos las codewords intercaladas!
        // Ahora intercalamos las codewords de los bloques de corrección de errores
        current = 0;
        interleaved_ec = new int[ec_cw_blocks.length * ec_cw_blocks[0].length];
        // System.out.println("Tendremos: " + interleaved_ec.length);
        /*System.out.println("Las codewords de corrección de errores son: ");
        for (int[] x : ec_cw_blocks) {
            for (int y : x) {
                System.out.println(y);
            }
        }*/

        /*:230::165::25::231::114::62::15::114::146::93::98::37::232::83::105::168::3::109:
          :83::125::225::184::140::242::10::137::134::95::245::224::91::158::135::140::194::199:
          :241::95::226::174::117::156::185::232::216::253::200::65::170::19::25::88::99::109:
          :45::236::52::101::7::8::232::29::179::93::5::4::82::215::2::251::102::95:*/
        for (int i = 0; i < ERR_BLOCKS; i++) {
            for (int j = 0; j < (ec_cw_blocks.length * ec_cw_blocks[0].length)/ERR_BLOCKS; j++) {
                interleaved_ec[current] = ec_cw_blocks[j][i];
                current += 1;
            }
        }
        /*System.out.println("Las codewords de los bloques de corrección intercaladas son: ");
        for (int x : interleaved_ec) {
            System.out.print(":" + x + ":");
        }
        System.out.println();*/
    }

    /**
     * Método que concatena las codewords
     */
    private void concatenarCW() {
        data_ec_codewords = new int[interleaved_data.length + interleaved_ec.length];
        for (int i = 0; i < interleaved_data.length; i++) {
            data_ec_codewords[i] = interleaved_data[i];
        }
        for (int i = 0; i < interleaved_ec.length; i++) {
            data_ec_codewords[i + interleaved_data.length] = interleaved_ec[i];
        }
        /*System.out.println("Las codewords son: ");
        for (int x : data_ec_codewords) {
            System.out.print(":" + x + ":");
        }
        System.out.println();*/
    }

    /**
     * Método que convierte las codewords a cadenas de bits, estas serán las que se insertarán en el código QR.
     */
    private void codewordsABinario() {
        binary_to_insert = new String[data_ec_codewords.length];
        for (int i = 0; i < data_ec_codewords.length; i++) {
            binary_to_insert[i] = String.format("%" + 8 + "s", Integer.toBinaryString(data_ec_codewords[i])).replace(' ', '0');
        }
        /*System.out.println("Las codewords a binario son: ");
        for (String s: binary_to_insert) {
            System.out.print(":" + s + ":");
        }*/
    }

    /**
     * Método que dado un arreglo de cadenas binarias, las concatena para generar la cadena de bits que se insertarán en el código QR
     */
    private void cadenaFinal() {
        bits_insertar = "";
        for (String s : binary_to_insert) {
            bits_insertar += s;
        }
        // System.out.println("Longitud cadena obtenida: " + bits_insertar.length());
    }

    /**
     * Genera una matriz correspondiente a la cadena que se quiere guardar en el QR.
     * Este método se encargará de definir todos los bits en su lugar. Al terminar
     * el código QR estará rellenado completamente.
     * Pasos para construir un QR (en teoría):
     * 1. Crear cuadros fijos y timing.
     * 2. Colocar nivel de recuperación de mensaje (2 bits) y reflejar
     * 3. Colocar tipo de máscara (3 bits) y reflejar
     * 3.1 Colocar valores para la recuperación ((amarillo) por ahora random) y reflejar
     * 4. Colocar tipo de códificación (4 bits) en la esquina inf/der.
     * 5. Colocar longitud del mensaje
     * 6. Colocar el mensaje codificado a bits siguiendo la trayectoria para escribir del QR.
     * 7. Colocar el bloque fin de cadena.
     * 8. Colocar los bits de redundancia para la recuperación del mensaje. (por ahora random).
     * 9. Aplicar la máscara a los bits que fueron colocados desde el paso 4 en adelante.
     * 10. QR Terminado.
     * Recordar que si hay blanco, significa bit en 1. Si es negro, el bit es 0.
     *                ██ C         ██
        ██████████    ██ O         ██  ██████████
        ██      ██    ██ R         ██  ██      ██
        ██      ██    ██ R         ██  ██      ██
        ██      ██    ██ E         ██  ██      ██
        ██████████    ██  TIMING   ██  ██████████
                      ██ C         ██
        ████████████████ C         ████████████████
        RECLVL|MASK| ION          CORRECCIÓN X2
                T                R   EOF    E    J
                I                E      .   _    A
                M                D      .        S
                I                U      .   M    N
        ████████████████ :)      N      .   E    E
                      ██ O       D      _   N    M
        ██████████    ██ J       A      E   S   ____
        ██      ██    ██ E       N      J   A   LEN
        ██      ██    ██ L       C      A   J   GTH
        ██      ██    ██ F    .  I      S   E   ____
        ██████████    ██ E    .  A      N   _   CODIF
                      ██ R    .  .      E   M   ICACION
     */
    public void construyeQR(){
        cuadrosPosicionamiento();
        if (VERSION > 1) {
            coordenadasCentralesAlineacion();
            patronesAlineacion();
        }
        timing();
        moduloNegro();
        infFormato();
        if (VERSION >= 7) {
            versInf();
        }
        ERR_BLOCKS = ERR_BLOCKS_LIST[((VERSION-1)*4)+(RECUPERACION_DEC+(int)Math.pow(-1, RECUPERACION_DEC+2))];
        // System.out.println("Necesitamos " + ERR_BLOCKS + " bloques de corrección de errores por cada bloque de data codewords.");
        //System.out.println(bits);
        /*ReedSolomonEC rs = new ReedSolomonEC(bits, nivel, VERSION, ERR_BLOCKS);
        Polinomio resultado = rs.getResultado();
        System.out.println(resultado);*/
        /* v1 - H */
        /*ReedSolomonEC rs = new ReedSolomonEC(bits, VERSION, ERR_BLOCKS);
        Polinomio resultado = rs.getResultado();
        int[] ec_cw_block = new int[resultado.getLista().size()];
        for (int i = 0; i < ec_cw_block.length; i++) {
            int index = resultado.getLista().get(i).toString().indexOf("x");
            ec_cw_block[i] = Integer.parseInt(resultado.getLista().get(i).toString().substring(0, index));
        }
        int[] data_cw_block = dataCodewords(bits);
        int[] final_data = new int[ec_cw_block.length + data_cw_block.length];
        for (int i = 0; i < final_data.length; i++) {
            if (i < data_cw_block.length) {
                final_data[i] = data_cw_block[i];
            } else {
                final_data[i] = ec_cw_block[i-data_cw_block.length];
            }
        }

        String[] final_data_bin = new String[final_data.length];

        for (int i = 0; i < final_data.length; i++) {
            final_data_bin[i] = String.format("%" + 8 + "s", Integer.toBinaryString(final_data[i])).replace(' ', '0');
        }

        String final_bits = "";

        for (String s : final_data_bin) {
            final_bits += s;
        }*/

        /*final_bits += "0000000";*/
        //bits_insertar = final_bits;
        /* Fin */

        /* v3 - Q */
        /*ReedSolomonEC rs1 = new ReedSolomonEC(data_blocks[0], VERSION, ERR_BLOCKS);
        ReedSolomonEC rs2 = new ReedSolomonEC(data_blocks[1], VERSION, ERR_BLOCKS);
        Polinomio resultado1 = rs1.getResultado();
        Polinomio resultado2 = rs2.getResultado();
        System.out.println("El resultado 1 es: " + resultado1);
        System.out.println("El resultado 2 es: " + resultado2);

        int[] ec_cw_block_1 = new int[resultado1.getLista().size()];
        for (int i = 0; i < ec_cw_block_1.length; i++) {
            int index = resultado1.getLista().get(i).toString().indexOf("x");
            ec_cw_block_1[i] = Integer.parseInt(resultado1.getLista().get(i).toString().substring(0, index));
        }
        int[] ec_cw_block_2 = new int[resultado2.getLista().size()];
        for (int i = 0; i < ec_cw_block_2.length; i++) {
            int index = resultado2.getLista().get(i).toString().indexOf("x");
            ec_cw_block_2[i] = Integer.parseInt(resultado2.getLista().get(i).toString().substring(0, index));
        }

        int[] data_cw_block_1 = dataCodewords(data_blocks[0]);
        int[] data_cw_block_2 = dataCodewords(data_blocks[1]);

        System.out.println("El primer bloque de data codewords tiene: " + data_cw_block_1.length + " codewords.");
        System.out.println("El segundo bloque de data codewords tiene: " + data_cw_block_2.length + " codewords.");
        System.out.println("El primer bloque de ec codewords tiene: " + ec_cw_block_1.length + " codewords.");
        System.out.println("El segundo bloque de ec codewords tiene: " + ec_cw_block_2.length + " codewords.");
        //int[] data_cw_block_2 = new int[18];
        *//*for (int i = 0; i < 18; i++) {
            data_cw_block_2[i] = i;
        }*//*
        int[] interleaved_data = new int[data_cw_block_1.length + data_cw_block_2.length];
        for (int i = 0; i < interleaved_data.length; i++) {
            if (i % 2 == 0) {
                interleaved_data[i] = data_cw_block_1[i/2];
            } else {
                interleaved_data[i] = data_cw_block_2[i/2];
            }
        }
        System.out.print("[");
        for (int x : data_cw_block_1) {
            System.out.print(x + ", ");
        }
        System.out.println("]");
        System.out.print("[");
        for (int x : data_cw_block_2) {
            System.out.print(x + ", ");
        }
        System.out.println("]");
        System.out.print("[");
        for (int x : interleaved_data) {
            System.out.print(x + ", ");
        }
        System.out.println("]");

        int[] interleaved_ECw = new int[ec_cw_block_1.length + ec_cw_block_2.length];
        //System.out.println(interleaved_ECw.length);
        for (int i = 0; i < interleaved_ECw.length; i++) {
            if (i % 2 == 0) {
                interleaved_ECw[i] = ec_cw_block_1[i/2];
            } else {
                interleaved_ECw[i] = ec_cw_block_2[i/2];
            }
        }
        System.out.print("[");
        for (int x : ec_cw_block_1) {
            System.out.print(x + ", ");
        }
        System.out.println("]");
        System.out.print("[");
        for (int x : ec_cw_block_2) {
            System.out.print(x + ", ");
        }
        System.out.println("]");
        System.out.print("[");
        for (int x : interleaved_ECw) {
            System.out.print(x + ", ");
        }
        System.out.println("]");

        int interleaved_cw_list_length = interleaved_data.length + interleaved_ECw.length;
        System.out.println("Tenemos " + interleaved_cw_list_length + " codewords en total.");
        int[] interleaved_cw = new int[interleaved_cw_list_length];
        for (int i = 0; i < interleaved_cw.length; i++) {
            if (i < interleaved_data.length) {
                interleaved_cw[i] = interleaved_data[i];
            } else {
                interleaved_cw[i] = interleaved_ECw[i-interleaved_data.length];
            }
        }
        System.out.print("[");
        for (int x : interleaved_cw) {
            System.out.print(x + ", ");
        }
        System.out.println("]");

        String[] interleaved_cw_bin = new String[interleaved_cw.length];

        for (int i = 0; i < interleaved_cw.length; i++) {
            interleaved_cw_bin[i] = String.format("%" + 8 + "s", Integer.toBinaryString(interleaved_cw[i])).replace(' ', '0');
        }

        *//*System.out.print("[");
        for (String x : interleaved_cw_bin) {
            System.out.print(x + ", ");
        }
        System.out.println("]");*//*

        String final_bits = "";

        for (String s : interleaved_cw_bin) {
            final_bits += s;
        }

        //final_bits += "0000000";
        bits_insertar = final_bits;
        System.out.println("Se insertarán " + bits_insertar.length() + "bits.");*/
        obtenerDataBlocks();
        generarCodewordsCorreccionErrores();
        generarCodewordsData();
        interleaveCodewords();
        concatenarCW();
        codewordsABinario();
        cadenaFinal();
        // System.out.println("Se insertará: " + bits_insertar);
        insertar_datos();
    }

    /**
     * Método que inserta los bits en los módulos vacíos del qr.
     */
    private void insertar_datos() {
        indice = 0;
        columna = TAMANHO - 1;
        fila = TAMANHO - 1;
        //System.out.println("Último byte: " + bits_insertar.substring());
        while (indice < bits_insertar.length()) {
            if (columna == -1) {
                return;
            }
            llenarArriba();
            columna -= 2;
            llenarAbajo();
            columna -= 2;
        }
    }

    private void llenarArriba() {
        while (fila > 0) {
            if (indice == bits_insertar.length()-1) {
                return;
            }
            if (moduloLibre(columna, fila)) {
                if (bits_insertar.charAt(indice) == '1') {
                    qr[columna][fila] = true;
                }
                indice += 1;
            }
            columna -= 1;
            if (moduloLibre(columna, fila)) {
                if (bits_insertar.charAt(indice) == '1') {
                    qr[columna][fila] = true;
                }
                indice += 1;
            }
            columna += 1;
            fila -= 1;
        }
        if (columna == 8 && fila == 0) {
            columna = 7;
        }
    }

    private void llenarAbajo() {
        while (fila < TAMANHO) {
            if (indice == bits_insertar.length()-1) {
                return;
            }
            if (moduloLibre(columna, fila)) {
                if (bits_insertar.charAt(indice) == '1') {
                    qr[columna][fila] = true;
                }
                indice += 1;
            }
            columna -= 1;
            if (moduloLibre(columna, fila)) {
                if (bits_insertar.charAt(indice) == '1') {
                    qr[columna][fila] = true;
                }
                indice += 1;
            }
            columna += 1;
            fila += 1;
        }
        fila -= 1;
    }

    /**
     * Método que devuelve si el módulo en el que se pretende escribir información está libre
     * @param col
     * @param fil
     * @return
     */
    private boolean moduloLibre(int col, int fil) {
        boolean libre = true;
        if (VERSION >= 7 && invadeInfVers(col, fil)) {
            libre = false;
        }
        if (invadeTimingHorizontal(col, fil) || invadeCuadroPosicionamiento(col, fil) || invadePatronAlineacion(col, fil)) {
            libre = false;
        }
        return libre;
    }
    /**
     * Método que obtiene una lista con las data codewords de cada bloque de data codewords para entonces llenar el código QR.
     */
    private int[] dataCodewords(String block) {
        int[] data_codewords = new int[block.length()/8];
        for (int i = 0; i < block.length(); i+=8){
            int codeword = Integer.parseInt(block.substring(i, Math.min(i + 8, block.length())),2);
            data_codewords[i/8] = codeword;
        }
        return data_codewords;
    }

    /**
     * Asigna los valores de qr[][] correspondientes a los cuadros de posicionamiento.
     */
    private void cuadrosPosicionamiento(){
        int x = 0;
        int y = 0;
        for (int k = 0; k <= 2; k++) {
            if (k == 1) {
                y = TAMANHO - 7;
            }
            if (k == 2) {
                x = TAMANHO - 7;
                y = 0;
            }
            for (int i = 0; i <= 6; i++){
                qr[x+i][y] = true;
                qr[x][y+i] = true;
                qr[x+i][y+6] = true;
                qr[x+6][y+i] = true;
            }

            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    qr[x + 2 + i][y + 2 + j] = true;
                }
            }
        }
    }

    /**
     * Método que obtiene las coordenadas que se usarán para los patrones de alineación.
     */
    private void coordenadasCentralesAlineacion() {
        COORDENADAS_ALINEACION = new int[(int)Math.ceil((VERSION)/7)+2]; // Tendrá tantos valores como valores de fila/columna tenga la versión.
        if (VERSION >= 2 && VERSION <= 6) {
            COORDENADAS_ALINEACION = ALINEACION_1;
            COORDENADAS_ALINEACION[1] += (VERSION-2)*4;
        } else if (VERSION >= 7 && VERSION <= 13) {
            COORDENADAS_ALINEACION = ALINEACION_2;
            COORDENADAS_ALINEACION[2] += (VERSION-7)*4;
            COORDENADAS_ALINEACION[1] += (VERSION-7)*2;
        } else if (VERSION >= 14 && VERSION <= 20) {
            COORDENADAS_ALINEACION = ALINEACION_3;
            COORDENADAS_ALINEACION[3] += (VERSION-14)*4;
            if (VERSION > 14) {
                for (int i = 1; i <= VERSION - 14; i++) {
                    if (i % 3 == 0) {
                        COORDENADAS_ALINEACION[2] += 4;
                        COORDENADAS_ALINEACION[1] += 4;
                    } else {
                        COORDENADAS_ALINEACION[2] += 2;
                    }
                }
            }
        } else if (VERSION >= 21 && VERSION <= 27) {
            COORDENADAS_ALINEACION = ALINEACION_4;
            COORDENADAS_ALINEACION[4] += (VERSION - 21) * 4;
            if (VERSION > 21) {
                for (int i = 1; i <= VERSION - 21; i++) {
                    if (i % 2 == 0) {
                        COORDENADAS_ALINEACION[3] += 2;
                        COORDENADAS_ALINEACION[1] -= 2;
                    } else {
                        COORDENADAS_ALINEACION[3] += 4;
                        COORDENADAS_ALINEACION[2] += 4;
                        COORDENADAS_ALINEACION[1] += 4;
                    }
                }
            }
        } else if (VERSION >= 28 && VERSION <= 34) {
            COORDENADAS_ALINEACION = ALINEACION_5;
            COORDENADAS_ALINEACION[5] += (VERSION-28)*4;
            if (VERSION > 28) {
                for (int i = 1; i <= VERSION - 28; i++) {
                    if ((i+1) % 3 == 0) {
                        COORDENADAS_ALINEACION[4] += 2;
                        COORDENADAS_ALINEACION[2] -= 2;
                        COORDENADAS_ALINEACION[1] -= 4;
                    } else {
                        COORDENADAS_ALINEACION[4] += 4;
                        COORDENADAS_ALINEACION[3] += 4;
                        COORDENADAS_ALINEACION[2] += 4;
                        COORDENADAS_ALINEACION[1] += 4;
                    }
                }
            }
        }  else if (VERSION >= 35) {
            COORDENADAS_ALINEACION = ALINEACION_6;
            COORDENADAS_ALINEACION[6] += (VERSION-35)*4;
            if (VERSION > 35) {
                for (int i = 0; i < VERSION - 35; i++) {
                    if (i % 3 == 0) {
                        COORDENADAS_ALINEACION[5] += 2;
                        COORDENADAS_ALINEACION[3] -= 2;
                        COORDENADAS_ALINEACION[2] -= 4;
                        COORDENADAS_ALINEACION[1] -= 6;
                    } else {
                        COORDENADAS_ALINEACION[5] += 4;
                        COORDENADAS_ALINEACION[4] += 4;
                        COORDENADAS_ALINEACION[3] += 4;
                        COORDENADAS_ALINEACION[2] += 4;
                        COORDENADAS_ALINEACION[1] += 4;
                    }
                }
            }
        }
    }

    private void patronesAlineacion() {
        for (int value : COORDENADAS_ALINEACION) {
            for (int i : COORDENADAS_ALINEACION) {
                if ((value == 6 && value == i) || (value == TAMANHO - 7 && i == 6) || (value == 6 && i == TAMANHO - 7)) {
                    continue;
                }
                qr[value][i] = true;
                for (int k = 0; k <= 4; k++) {
                    qr[(value - 2) + k][(i - 2)] = true;
                    qr[(value - 2)][(i - 2) + k] = true;
                    qr[(value - 2) + k][(i - 2) + 4] = true;
                    qr[(value - 2) + 4][(i - 2) + k] = true;
                }
            }
        }
    }

    /**
     * Método que nos indica si estamos invadiendo un módulo reservado al querer almacenar información en él.
     * @param col
     * @param fil
     * @return
     */
    private boolean invadeCuadroPosicionamiento(int col, int fil) {
        boolean invade = false;
        if (col >= 0 && col <= 8 && fil >= 0 && fil <= 8) { // Las coordenadas invaden el cuadro de posicionamiento superior izquierdo, y sus separadores
            invade = true;
        } else if (col >= TAMANHO - 8 && col <= TAMANHO - 1 && fil >= 0 && fil <= 8) { // Las coordenadas invaden el cuadro de posicionamiento superior derecho, y sus separadores
            invade = true;
        } else if (col >= 0 && col <= 8 && fil >= TAMANHO - 8 && fil <= TAMANHO - 1) { // Las coordenadas invaden el cuadro de posicionamiento inferior izquierdo, sus separadores, y el módulo negro.
            invade = true;
        }
        return invade;
    }

    /**
     * Método que nos indica si estamos invadiendo un módulo reservado al querer almacenar información en él.
     * @param col
     * @param fil
     * @return
     */
    private boolean invadeTimingHorizontal(int col, int fil) {
        boolean invade = false;
        if (fil == 6) {
            invade = true;
        }
        return invade;
    }

    private boolean invadeInfVers(int col, int fil) {
        boolean invade = false;
        if (col >= TAMANHO - 11 && col <= TAMANHO - 9 && fil >= 0 && fil <= 5) {
            invade = true;
        } else if (col >= 0 && col <= 5 && fil >= TAMANHO - 11 && fil <= TAMANHO - 9) {
            invade = true;
        }
        return invade;
    }

    /**
     * Método que nos indica si estamos invadiendo un módulo reservado al querer almacenar información en él.
     * @param col
     * @param fil
     * @return
     */
    private boolean invadePatronAlineacion(int col, int fil) {
        boolean invade = false;
        if (VERSION == 1) {
            return invade;
        }
        for (int value : COORDENADAS_ALINEACION) {
            for (int i : COORDENADAS_ALINEACION) {
                if ((value == 6 && value == i) || (value == TAMANHO - 7 && i == 6) || (value == 6 && i == TAMANHO - 7)) {
                    continue;
                }
                if (col == value && fil == i) {
                    invade = true;
                } else {
                    if (col >= value - 2 && col <= value + 2 && fil >= i - 2 && fil <= i+2) {
                        invade = true;
                    }
                }
            }
        }
        return invade;
    }

    /**
     * Asigna los valores de qr[][] correspondientes a la zona de timing.
     */
    private void timing() {
        for (int i = 8; i < qr.length-8; i++) {
            if (i % 2 == 0) {
                qr[6][i] = true;
                qr[i][6] = true;
            }
        }
    }

    /**
     * Asigna true al módulo con coordenadas (8, [(4 * VERSION) + 9])
     */
    private void moduloNegro(){
        qr[8][(4 * VERSION) + 9]= true;
    }

    /**
     * Encuentra la cadena correspondiente a la cadena de formato del código QR y asigna los valores de qr de acuerdo
     * a esta cadena encontrada.
     */
    private void infFormato() {
        String correccion_mascara = RECUPERACION + MASCARA;
        //System.out.println("corr+masc: " + correccion_mascara);
        String polinomio_generador = "10100110111";
        correccion_mascara = String.format("%-" + 15 + "s", correccion_mascara).replace(' ', '0');
        //System.out.println(correccion_mascara);
        correccion_mascara = correccion_mascara.replaceFirst("^0+(?!$)", "");
        //System.out.println(correccion_mascara);
        //System.out.println(correccion_mascara.length());
        String xor_final = "101010000010010";
        String temp = "";
        while (correccion_mascara.length() > 10) {
            polinomio_generador = String.format("%-" + correccion_mascara.length() + "s", polinomio_generador).replace(' ', '0');
            for (int i = 0; i < correccion_mascara.length(); i++) {
                if (polinomio_generador.charAt(i) == correccion_mascara.charAt(i)) {
                    temp += "0";
                } else {
                    temp += "1";
                }
            }
            correccion_mascara = temp;
            //System.out.println("hmn: " + correccion_mascara);
            temp = "";
            correccion_mascara = correccion_mascara.replaceFirst("^0+(?!$)", "");
            //System.out.println("no zeroes left: " + correccion_mascara);
        }
        if (correccion_mascara.length() < 10) {
            correccion_mascara = String.format("%" + 10 + "s", correccion_mascara).replace(' ', '0');
        }

        //System.out.println("padded: " + correccion_mascara);

        correccion_mascara = RECUPERACION + MASCARA + correccion_mascara;

        //System.out.println("with rec and masc: " + correccion_mascara);

        for (int i = 0; i < correccion_mascara.length(); i++) {
            if (xor_final.charAt(i) == correccion_mascara.charAt(i)) {
                temp += "0";
            } else {
                temp += "1";
            }
        }

        correccion_mascara = temp;

        CADENA_FORMATO = correccion_mascara;
        //System.out.println(CADENA_FORMATO);
        //System.out.println(COORDENADAS_ALINEACION[0]);
        //System.out.println(COORDENADAS_ALINEACION[1]);
        int k = 8;
        for (int i = 0; i < 7; i++) {
            if (String.valueOf(CADENA_FORMATO.charAt(i)).equals("1")){
                if (i <= 5) {
                    qr[i][8] = true;
                } else {
                    qr[i + 1][8] = true;
                }
            }
        }
        for (int i = 7; i < 15; i++) {
            if (String.valueOf(CADENA_FORMATO.charAt(i)).equals("1")){
                qr[8][k] = true;
            }
            if (k == 7) {
                k -= 2;
            } else {
                k -= 1;
            }
        }
        for (int i = 0; i < 7; i++) {
            if (String.valueOf(CADENA_FORMATO.charAt(i)).equals("1")){
                qr[8][TAMANHO-1-i] = true;
            }
        }
        k = 8;
        for (int i = 7; i < 15; i++) {
            if (String.valueOf(CADENA_FORMATO.charAt(i)).equals("1")){
                qr[TAMANHO-k][8] = true;
            }
            k -= 1;
        }
    }

    /**
     * Método que obtiene la versión de la información del código QR para después colocarlo en los módulos
     * correspondientes.
     */
    private void versInf() {
        String polinomio_generador = "1111100100101";
        //System.out.println("Polinomio generador: " + polinomio_generador);
        String bin_version = String.format("%" + 6 + "s", Integer.toBinaryString(VERSION)).replace(' ', '0');
        String binary_version = String.format("%" + 6 + "s", Integer.toBinaryString(VERSION)).replace(' ', '0');
        //System.out.println("Version binaria: " + binary_version);
        binary_version = String.format("%-" + 18 + "s", binary_version).replace(' ', '0');
        //System.out.println("Version binaria longitud 18: " + binary_version);
        binary_version = binary_version.replaceFirst("^0+(?!$)", "");
        //System.out.println("Version binaria sin ceros izquierdos: " + binary_version);
        //System.out.println("Longitud de la version binaria sin ceros izquierdos: " + binary_version.length());
        polinomio_generador = String.format("%-" + binary_version.length() + "s", polinomio_generador).replace(' ', '0');
        //System.out.println("Polinomio generador de longitud igual que version binaria sin ceros izquierdos: " + polinomio_generador);
        //System.out.println(binary_version.length());
        //System.out.println(polinomio_generador.length());
        String temp = "";
        while (binary_version.length() > 12) {
            polinomio_generador = String.format("%-" + binary_version.length() + "s", polinomio_generador).replace(' ', '0');
            //System.out.println("Polinomio generador: " + polinomio_generador);
            for (int i = 0; i < binary_version.length(); i++) {
                if (polinomio_generador.charAt(i) == binary_version.charAt(i)) {
                    temp += "0";
                } else {
                    temp += "1";
                }
            }
            binary_version = temp;
            //System.out.println("xor: " + binary_version);
            temp = "";
            binary_version = binary_version.replaceFirst("^0+(?!$)", "");
          //  System.out.println("no zeroes left: " + binary_version);
        }
        if (binary_version.length() < 12) {
            binary_version = String.format("%" + 12 + "s", binary_version).replace(' ', '0');
        }

        //System.out.println("final: " + binary_version);

        binary_version = bin_version + binary_version;
        //System.out.println(binary_version.length());

        int k = 0;
        int j = 0;
        for (int i = 0; i < 18; i++) {
            if (i != 0 && i % 3 == 0) {
                j -= 3;
                k += 1;
            }
            if (String.valueOf(binary_version.charAt(i)).equals("1")) {
                qr[5-k][TAMANHO-9-j] = true;
            }
            j += 1;
        }

        j = 0;
        k   = 0;
        for (int i = 0; i < 18; i++) {
            if (i != 0 && i % 3 == 0) {
                j -= 3;
                k += 1;
            }
            if (String.valueOf(binary_version.charAt(i)).equals("1")) {
                qr[TAMANHO-9-j][5-k] = true;
            }
            j += 1;
        }
    }

    /** Método privado para pasar un texto a binario
     * ej: "A" -> "01000001"
     * @return la cadena s en binario como una cadena de booleanos.
     */
    private void textoABinario(){
        char[] chars = texto.toCharArray();
        // System.out.println((int)chars[0]);
        String cadena_binaria = "";
        for (char a : chars) {
            cadena_binaria += String.format("%" + 8 + "s", Integer.toBinaryString((int) a)).replace(' ', '0');
        }
        binario = cadena_binaria;
    }

    public void crearImagen(String s) {
        mascara();
        BufferedImage img = new BufferedImage(TAMANHO+8, TAMANHO+8, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        int nuevoColor = 0xFFFFFFFF;
        g2d.setColor(new java.awt.Color(nuevoColor));
        for (int i = 0; i < TAMANHO+8; i++) {
            for (int j = 0; j < TAMANHO+8; j++) {
                g2d.fillRect(i, j, 1, 1);
            }
        }
        nuevoColor = 0;
        g2d.setColor(new java.awt.Color(nuevoColor));
        for (int i = 4; i < TAMANHO+4; i++) {
            for (int j = 4; j < TAMANHO+4; j++) {
                if (qr[i-4][j-4] == true) {
                    g2d.fillRect(i, j, 1, 1);
                }
            }
        }
        g2d.dispose();

        // Guardar la imagen modificada
        File archivo = new File(s + ".png");
        try {
            ImageIO.write(img, "png", archivo);
        } catch (IOException e) {
            System.out.println("Error al leer o guardar la imagen: " + e.getMessage());
        }


    }

    private void mascara() {
        int count = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (moduloLibre(i,j)) {
                    count += 1;
                }
                if (moduloLibre(i, j) && i != 6) {
                    switch (Integer.parseInt(MASCARA,2)) {
                        case 0:
                            if ((i+j) % 2 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 1:
                            if (j % 2 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 2:
                            if (i % 2 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 3:
                            if ((i+j) % 3 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 4:
                            if ((j/2) + (i/3) % 2 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 5:
                            if (((i*j) % 2) + ((i*j) % 3) == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 6:
                            if ((((i*j) % 2) + ((i*j) % 3)) % 2 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;
                        case 7:
                            if ((((i+j) % 2) + ((i*j) % 3)) % 2 == 0) {
                                qr[i][j] = ! qr[i][j];
                            }
                            break;

                    }
                }
            }
        }
        count -= 5;
        // System.out.println("Hay " + count + " modulos de información.");
    }

    /**
     *
     */
    /*public boolean prueba(){
        switch (recuprueba) {
            case 1: //L
                return (texto.length() <= BM_Max_L[VERSION-1]);
            case 0: //M
                return (texto.length() <= BM_Max_M[VERSION-1]);
            case 3: //Q
                return (texto.length() <= BM_Max_Q[VERSION-1]);
            case 2: //H
                return (texto.length() <= BM_Max_H[VERSION-1]);
            default:
                System.out.println("???");
                return false;
        }
    }*/


    /**
     * Método que dado un número x (longitud del mensaje), busca en la lista de caracteres máximos para un nivel de corrección
     * el número más pequeño de la lista que sea mayor a x, la versión de código QR por utilizar será V = i+1, donde i es el índice
     * del elemento encontrado.
     * @param value
     */
    private void buscarNumero(int value, int[] a) {
        if(value < a[0]) {
            VERSION = 1;
            return;
        }
        if(value > a[a.length-1]) {
            VERSION = 40;
            return;
        }

        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            int mid = (hi + lo) / 2;

            if (value < a[mid]) {
                hi = mid - 1;
            } else if (value > a[mid]) {
                lo = mid + 1;
            } else {
                VERSION = mid + 1;
                return;
            }
        }
        // lo == hi + 1
        VERSION = lo + 1;
        //System.out.println(VERSION);
    }

    /** Genera una representación en cadena del código QR.
     * @return una representación en cadena del código QR.
     */
    @Override
    public String toString(){
        String s = "";
        for (int i = 0; i < qr.length; i++){
            for (int j = 0; j < qr.length; j++){
                if (qr[j][i])
                    s += "██";
                else
                    s += "  ";
            }
            s += "\n";
        }
        return s;
    }


}