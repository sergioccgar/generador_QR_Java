
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
    private int tamanho;

    /* Tipo máscara [0-7]*/
    private int MASCARA;
    /* Tipo Codificación [0-8]*/
    private int CODIFICACION;
    /* Tamaño mensaje */
    private int TAMAÑO_MENSAJE;
    /* Recuperación de error [0-3]*/
    private int RECUPERACION;
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

    private int[] COORDENADAS_ALINEACION;

    public QR(String texto, int version){
        this.texto = texto;
        tamanho = 21 + (version-1)*4;
        qr = new boolean[tamanho][tamanho];
        binario = "";
        MASCARA = 2; //formulas con módulos de i y j
        RECUPERACION = 0; //nivel de corrección = baja
        CODIFICACION = 3; //ASCII
        TAMAÑO_MENSAJE = texto.length();
        this.VERSION = version;


        String bin = textoABinario(texto);
        construyeQR();
        System.out.println(toString());
    }

    /** Genera una representación en cadena del código QR.
     * @return una representación en cadena del código QR.
     */
    @Override
    public String toString(){
        String s = "";
        for (int i = 0; i < qr.length; i++){
            for (int j = 0; j < qr.length; j++){
                //Se usa la afirmación (1) como negro para una mejor visualizacion en la consola.
                //Pero debería ser al revés. Si es (1) el pixel debe ser blanco.
                if (qr[j][i])
                    s += "██";
                else
                    s += "  ";
            }
            s += "\n";
        }
        return s;
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
    }

    /**
     * Asigna los valores de qr[][] correspondientes a los cuadros de posicionamiento.
     */
    private void cuadrosPosicionamiento(){
        int x = 0;
        int y = 0;
        for (int k = 0; k <= 2; k++) {
            if (k == 1) {
                y = tamanho - 7;
            }
            if (k == 2) {
                x = tamanho - 7;
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
                if ((value == 6 && value == i) || (value == tamanho - 7 && i == 6) || (value == 6 && i == tamanho - 7)) {
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

    /** Método privado para pasar un texto a binario
     * ej: "A" -> "01000001"
     * @return la cadena s en binario como una cadena de booleanos.
     */
    private String textoABinario(String s){
        return null;
    }

    /**
     * Convierte un número en base 10 a un número en base 2 como string.
     * Añade los 0 a la izquierda para completar al bloque.
     * ej: 10 -> "00001010"
     * @param i el número a convertir
     * @param block el tamaño del bloque en binario.
     * @return la cadena en binario de i.
     */
    private String numToBin(int i, int block){
        return null;
    }

    /**
     * Aplica la máscara a este código QR (boolean[][] qr)
     * @param m la máscara a aplicar al QR sin modificar las zonas restringidas.
     */
    private void mascara(int m){
        switch (m){
            //Máscara de tipo 1
            case 1:
                break;
            //Máscara de tipo 2
            case 2:
                break;
            default:
                //No se aplica máscara.
        }

    }

    /**
     * Genera la cadena del cual se puede recuperar la información de s.
     * @param s la cadena original
     * @return bits de redundancia para la cadena s.
     */
    private String recuperacion(String s){
        return null;
    }

    /**
     * Rellena la matriz qr con el byte de información bites codificada a binario.
     * Escribe el byte desde la posición x, y hacia arriba en zigzag izquierda y derecha.
     * 0 <-- 1
     *      X
     *     /
     *    /
     *   /
     * 1 <-- 0
     * @param x la posición en x desde donde escribir el byte
     * @param y la posición en y desde donde escribir el byte
     * @param bites el byte de información a escribir hacia arriba
     */
    private void rellenoArriba(int x, int y, String bites ){

    }

    
    /**
     * Rellena la matriz qr con el byte de información bites codificada a binario.
     * Escribe el byte desde la posición x, y hacia abajo en zigzag izquierda y derecha.
     * 0 <-- 1
     *  \     
     *   \     
     *    \    
     *     X
     * 1 <-- 0
     * @param x la posición en x desde donde escribir el byte
     * @param y la posición en y desde donde escribir el byte
     * @param bites el byte de información a escribir hacia abajo
     */
    private void rellenoAbajo(int x, int y, String bites ){

    }

    /**
     * Rellena la matriz qr con el byte de información bites codificada a binario.
     * Escribe el byte desde la posición x, y hacia Izq en zigzag izquierda y derecha.
     *  0 <-- 1 <-- 0 <-- 1
     *   \               X
     *    \             /
     *     \           /
     *      X         /
     *  1 <-- 1      1 <-- 0
     * @param x la posición en x desde donde escribir el byte
     * @param y la posición en y desde donde escribir el byte
     * @param bites el byte de información a escribir hacia Izq
     */
    private void rellenoIzq(int x, int y, String bites ){

    }

}

