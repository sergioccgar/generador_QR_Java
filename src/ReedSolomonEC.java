import java.util.ArrayList;


/**
 * Clase que nos genera el código de recuperación a partir de una cadena. 
 * 
 * Para calcular los números de redundancia, debemos dividir polinomios.
 * 
 * Dada una cadena s, generaremos una lista tal que en L se encuentre
 * los números de recuperación de la cadena. 
 * 
 * El número máximo de bloques de informacion (bytes) esta delimitada por el nivel de recuperacion,
 * siendo L el menor y H el mayor. y dependiendo del nivel de recuperación, ocuparemos 
 * +/- bloques, por lo que el número máximo de bloques está acotado.
 * 
 * https://www.thonky.com/qr-code-tutorial/character-capacities
 * 
 * por ejemplo, para un QR version 1, si usamos la codificación por bytes (0100)
 * a lo más podemos poner 17 bloques (bytes) de información si usamos un nivel de
 * corrección tipo L (Low).
 * 
 * Si queremos tener un nivel de recuperación H en la misma versión de QR, a lo más
 * podemos poner 7 bloques de información (sin contar la codificación y la longitud del mensaje)
 * 
 */

public class ReedSolomonEC {

    /* El nivel de corrección 7
     * Por default, será L.
     */
    private String levelCorrection;

    /* La cadena de la cual sacaremos la lista de números de redundancia */
    private String mensaje;

    /* La versión del QR
     * Nosotros utilizaremos para esta práctica v1
     */
    private int version;

    /* El número de bloques de corrección a generar
     * a partir del mensaje dado. El número mínimo está 
     * determinado por el tipo de versión y el tipo de nivel de
     * corrección, de acuerdo a :
     * 
     * https://www.thonky.com/qr-code-tutorial/error-correction-table}
     * 
     * Para un 1-L (Qr v1 nivel L) usaremos 7.
     */
    private int errBlocks;

    /* Message Polinomial 
     * Cada caracter del mensaje, pasado a base 10 es el coeficiente
     * del expontente chr[i]. 
     * Ej: "Hi" -> 72x^(1) + 105x^(0) == 72x + 105
     */
    private Polinomio messagePoly;

    /* Generator Polinomial
     * El polinomio generador, es el polinomio de la forma
     * (x - α_0) ... (x - α^n-1)
     * Donde n es el número de bloques de corrección que queremos
     * rellenar. Como estamos en potencias de 2, alpha es 2.
     * y tanto alpha, x y n son acotados por el módulo.
     */
    private Polinomio generatorPoly;


    /* La lista de coeficientes que nos servirán para el nivel de corrección
     * Aquí se guardará el resultado de dividir el polinomio del mensaje
     * entre el generator poly k veces, donde k es el tamaño del mensaje.
     */
    private Polinomio resultado;

    /**
     * Construye un código de recuperación para un N-M QR
     * El número de errBlocks estará dado por la tabla de arriba, por ahora
     * no nos interesa hacer códigos QR que no sean v1 y con un nivel de recuperación diferente a L.
     * @param mensaje Cadena de bits a la cual se le aplicará el algoritmo Reed Solomon.
     * @param version la versión del QR (1 - 40)
     * @param errBlocks el número de bloques de recuperación.
     */
    public ReedSolomonEC(String mensaje, int version, int errBlocks){
        this.mensaje = mensaje;
        this.version = version;
        this.errBlocks = errBlocks;
        
        this.generatorPoly = Polinomio.generator(errBlocks);
        // System.out.println("Polinomio generador en notación alpha:\n" + generatorPoly);
        messagePoly = Polinomio.messagePoly(mensaje);
        // System.out.println("\nPolinomio del mensaje en notacion entera:\n" + messagePoly);
        preludio(); // Prepara los polinomios de mensaje y generador para que el leading term tenga el mismo exponente.
        division();
    }

    public ArrayList<Integer> getCoeficientes(){
        ArrayList<Integer> coeficientes = new ArrayList<>();
        for (Polinomio.Termino t: resultado.getLista()){
            coeficientes.add(t.getCoeficiente());
        }
        return coeficientes;
    }

    /**
     * Getter del resultado de la división.
     */
    public Polinomio getResultado() { return resultado; }

    /**
     * Método que divide n veces el mensaje para obtener el mensaje de recuperación.
     */
    private void division() {
        resultado = messagePoly.copia();

        for (int i = 0; i < mensaje.length()/8; i++){
            //Obtener el primer coeficiente como un polinomio.
            resultado.toAlphaNotation();
            //System.out.println("Polinomio de mensaje en notación Alpha:" + resultado);
            Polinomio.Termino highest = resultado.getPrimerTermino();
            Polinomio.Termino nuevo = new Polinomio.Termino(highest.getAlpha(), 0);
            Polinomio termino = new Polinomio();
            termino.agregaTermino(nuevo);
            termino.toAlphaNotation();
            
            //Multiplicar α^n por el polinomio generador
            termino.multiplicaPolinomio(generatorPoly);
            
            //Convertir a notación entera para poder hacer xor.
            termino.toIntegerNotation();
            resultado.toIntegerNotation();
            termino.dividePolinomio(resultado);
            
            //Quitamos el grado máximo porque la división da 0
            termino.quitaTermino(termino.getLista().get(0));
            //System.out.println("Polinomio sin el leading 0 term: " + termino);
            //WTF?? 
            generatorPoly.bajarGrado();
            resultado = termino;
        }

    }


    /**
     * Prepara a los polinomios para poder ser divididos entre ellos y así 
     * obtener los bloques de error correction.
     * Multiplica a "messagePoly" * x^n donde n es el número de bloques de
     * error correction que queremos crear (mínimo 7)
     * 
     * Después, multiplica el polinomio generador por x^m donde m 
     * era el grado máximo del polinomio Messagepoly. (tamaño del mensaje -1)
     * y el coeficiente de x es una potencia de 2.
     */
    public void preludio(){
        int i = messagePoly.gradoMaximo();
        // System.out.println("Grado máximo del polinomio de mensaje: " + i);
        //Multiplicar por x^n
        // System.out.println("Se multiplicará por x^" + errBlocks);
        Polinomio.Termino n = new Polinomio.Termino(1, errBlocks);
        Polinomio p = new Polinomio();
        p.agregaTermino(n);
        /**//*
        messagePoly.toAlphaNotation();
        System.out.println("Polinomio de mensaje en notación alfa: " + messagePoly);
        *//**/
        messagePoly.multiplicaPolinomio(p);
        // System.out.println("Polinomio de mensaje multiplicado por x^"+errBlocks);
        // System.out.println(messagePoly);
        // System.out.println("Por ahora el polinomio generador es:\n"+generatorPoly);
        //Multiplicar por x^m
        n.setExponente(i);
        p.toAlphaNotation();
        // System.out.println("Ahora el polinomio generador es:\n"+generatorPoly);

        generatorPoly.multiplicaPolinomio(p);
        // System.out.println("Finalmente el polinomio generador es:\n"+generatorPoly);

    }
    
    
}
