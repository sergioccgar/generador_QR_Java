import java.util.ArrayList;
import java.lang.Comparable;

 
 /* Clase que representa un polinomio.
  * Los polinomos los veremos como listas ordenadas (por potencias)
  * de términos con sus coeficientes

  * Para esta práctica, se utilizarán 2 tipos de coeficientes.
  * 1. Los normales (enteros)
  * 2. Coeficientes de tipo potencias  α^n, donde α puede ser una base
  * 
  * Como estamos trabajando con binarios, α = 2
  */


public class Polinomio {

    /**
     * Clase que representa una potencia de 2. 
     * Esta clase nos será de mucha utilidad al tratar de hacer
     * operaciones dentro del campo F256 para el código Reed Solomon.
     * 
     * En realidad, se puede generalizar para cualquier base α
     * Pero nosotros lo usaremos para α = 2.
     */
    public static class Exponente {
        /* La base de la potencia */
        private int base;
        /* El exponente de la potencia */
        private int exponente;

        /**
         * Construye una potencia de 2^exponente
         * Utilizamos el módulo para que siempre estemos dentro de nuestro campo F256.
         * @param exponente el exponente de la potencia de 2 módulo 255
         */
        public Exponente(int exponente){
            this(2, exponente%255);
        }

        /**
         * Construye una potencia de base b^exponente
         * Es una generalización de potencias base b.
         * No se aplica el módulo
         * @param base  la base de la potencia
         * @param exponente el exponente de la potencia
         */
        public Exponente(int base, int exponente){
            this.base = base;
            this.exponente = exponente;
        }

        /* Suma 2 exponentes de bases iguales
        * Como tal, no hay una regla para sumar 2 potencias, 
        * aunque se pueden calcular manualmente.

        * En el caso cuando trabajemos con 2^i, usaremos
        * la suma definida por el xor para que caiga dentro del campo.
        */
        public int suma(Exponente c){
            //Podemos generalizar esto, pero con cuidado.
            if (base == c.base){

                int m = Polinomio.Termino.getValue(getExponente());
                int n = Polinomio.Termino.getValue(c.getExponente());
                //La suma y resta definida en el campo está decretada por el XOR
                int r = m ^ n;
                exponente = Termino.getExp(r);
            }
            return (int)(Math.pow(base, exponente) + Math.pow(c.base, c.exponente));
            
        }
        /*
        * Multiplica 2 potencias utilizando leyes de los exponentes.
        * Si las bases son diferentes lanza error.
        * En otro caso, sumamos los exponentes con la misma base.
        */
        public Exponente multiplicacion(Exponente c){
            if (base != c.base)
                throw new IllegalArgumentException("No se pueden multiplicar números directamente con diferente base.");
            
            return new Exponente(base, (exponente + c.exponente)%255);
        }

        /*
        * Division de 2 potencias, utilizando leyes de exponentes
        * es la resta de ambos exponentes.
        * No se ha utilizado todavía.
        */
        public Exponente division(Exponente p){
            if (p.base == 0)
                throw new IllegalArgumentException("No se pueden multiplicar números directamente con diferente base.");
            if (base == 2 && p.base == 2)
                return new Exponente(exponente - p.exponente);
            return new Exponente(base, (exponente - p.exponente));
        }

        /**
         * Regresa una representación en cadena de la potencia.
         * ej: 2^1
         * @return una representación en cadena de la potencia.
         */
        public String toString(){
            return String.format("%d^%d", base, exponente);
        }

        /* Getters para acceder desde afuera de la clase. */
        public int getBase() {
            return base;
        }

        public int getExponente() {
            return exponente;
        }
    }


    /**
     * Clase que representa un término de un polinomio.
     * El siguiente polinomio tiene 3 términos:
     * 
     * x^2 + 2x + 1
     * 
     * Los términos tienen 2 valores, su coeficiente y su exponente:
     * Ej: -7x^3 tiene como coeficiente a -7 y de exponente a 3
     * 
     * El coeficiente puede verse de 2 formas, como número entero o como potencia
     * de 2, que es lo que nos interesa para este código.
     * 
     * 2^1x + 2^0x^0 == 2x + 1
     * 
     * Los términos están ordenados, por eso implementamos el método compareTo() para 
     * poder ordenarlos automáticamente por grado del exponente.
     * 
     * Solo existen 2 tipos de Términos. 
     * O tienen coeficientes como enteros o tienen coeficientes como potencias módulo 255
     */
    public static class Termino implements Comparable<Termino> {

        /* El grado del término */
        private int exponente;
        /* El coeficiente como potencia de 2 (por ahora) */
        private Exponente alpha;
        /* El coeficiente como entero */
        private int coeficiente;

        /**
         * Construye un término con un coeficiente entero y un exponente
         * @param coeficiente el coeficiente del término.
         * @param exponente  el exponente del término.
         */
        public Termino(int coeficiente, int exponente){
            this.exponente = exponente;
            this.coeficiente = coeficiente;
        }

        /**
         * Construye un término con un coeficiente como 
         * potencia de 2 módulo 255 y un exponente
         * @param coeficiente el coeficiente del término.
         * @param exponente  el exponente del término.
         */
        public Termino (Exponente coeficiente, int exponente){
            this.alpha = coeficiente;
            this.exponente = exponente;
            this.coeficiente = -1;
        }

        /**
         * Implementación de la interfaz comparable para poder ordenar 
         * Objetos de tipo Término.
         * @param b  el término a comparar
         * @return  quien es mayor entre this y b.
         */
        @Override
        public int compareTo(Termino b){
            return b.exponente - exponente;
        }

        /**
         * Suma 2 términos del mismo grado.
         * Si el exponente del término es diferente, no se puede sumar.
         * Ej: x + x = 2x.
         * No se puede sumar x + 1.
         * En el caso cuando los coeficientes sean potencias de 2, 
         * factorizamos a la x, y sumamos los coeficientes transformando 
         * cada coeficiente a su valor de la tabla usando el módulo bite-wise
         * y luego como estamos en un campo F256, sumamos haciendo xor, y luego
         * el resultado lo reescribimos como potencia de 2.
         * Ej: α^1x^1 + α^0x^1 = (α^1 + α^0)x^1 = sacamos α^1 de la tabla con getValue(1)
         *                                      =  (2 + 1)x^1 = 3x^1 = sacamos i, tal que α^i = 3 con getExp(3)
         *                                      =  α^25x^1        
         * @param t el otro término a sumar con este término.
         */
        public void suma(Termino t){
            //No podemos sumar si tienen diferentes exponentes.
            if (exponente != t.exponente)
                return;
            //Si ambos términos tienen potencia de 2, lo calculamos como lo dice el 
            //algoritmo de arriba.
            if (alpha != null && t.getAlpha() != null){
                alpha.suma(t.getAlpha());
                return;
            }
            // Suma normal de términos sumando coeficientes en los enteros.
            else 
                coeficiente = coeficiente + t.coeficiente;
            
        }

        /**
         * Multiplica este término con el otro término. Los términos no necesariamente deben tener el 
         * mismo exponente, como en la suma.
         * Ej: 3x * -x^2 = -3x^3
         * Para cuando el coeficiente es potencia de 2, sumamos los exponentes de esa potencia
         * por las leyes de los exponentes.
         * Ej: 2^1x * 2^2x^2 = 2^(1 + 2)x^(1 + 2) = 2^3x^3
         * @param c1 el otro término a multiplicar con this.
         * @return el resultado de multiplicar los términos this con c1.
         */
        public Termino multiplicacion(Termino c1){
            if (alpha != null && c1.alpha != null){
                Termino t = new Termino(alpha.multiplicacion(c1.getAlpha()), (exponente + c1.exponente));
                return t;
            }
            return new Termino(coeficiente* c1.coeficiente, exponente + c1.exponente);
        }

        /**
         * Regresa una representación en cadena de un término.
         * Los términos se ven como:
         * Cuando hay potencia de 2: 2^251x^9
         * Cuando no hay potencia de 2: 1x^1
         * @return una representación en cadena del término.
         */
        public String toString(){
            return String.format("%sx^%d", alpha == null? coeficiente:alpha.toString(), exponente);
        }

        /* Polinomios más bonitos pero no salen en consola :( */
        private String toStringExponentes(){
            String s = "";
            String pot = String.valueOf(exponente);
            for (int i = 0; i < pot.length(); i++){
                switch (pot.charAt(i)){
                    case '0':
                        s += "⁰";
                        break;
                    case '1':
                        s += "¹";
                        break;
                    case '2':
                        s += "²";
                        break;
                    case '3':
                        s += "³";
                        break;
                    case '4':
                        s += "⁴";
                        break;
                    case '5':
                        s += "⁵";
                        break;
                    case '6':
                        s += "⁶";
                        break;
                    case '7':
                        s += "⁷";
                        break;
                    case '8':
                        s += "⁸";
                        break;
                    case '9':
                        s += "⁹";
                        break;
                    default:
                        break;
                }
            }
            return s;
        }

        /* Getters */
        public int getExponente() {
            return exponente;
        }
        
        public Exponente getAlpha() {
            return alpha;
        }

        public int getCoeficiente() {
            return coeficiente;
        }

        /* Setters */
        public void setExponente(int exponente){
            this.exponente = exponente;
        }

        
        public void setCoeficiente(int r) {
            this.coeficiente = r;
        }


        /**
         * Si el coeficiente de tipo alpha no es null
         * convertimos ese coeficiente a un entero, usando la
         * tabla modular 285.
         */
        public void toInteger(){
            if (alpha == null)
                return;
            this.coeficiente = getValue(alpha.getExponente());
            alpha = null;
        }

        /**
         * Si el coeficiente no es -1 (invalido) 
         * convertimos ese coeficiente a un coeficiente con
         * la notación de alfa, usando la tabla modular 285
         */
        public void toAlpha(){
            if (coeficiente == -1)
                return;
            this.alpha = new Exponente(getExp(coeficiente));
            coeficiente = -1;
        }

        /* Arreglo con los valores 2^n % 285
        * Si tenemos i, podemos saber el valor de 2^i con esta tabla, solo hay que ir a la posición i
        * dentro del campo F256 usando el bit-wise modulo
        * Ej: 2^14 = exp[14] = 19
        * 
        * De igual manera, si tenemos n, podemos saber el valor de i buscando linealmente.
        * Ej: 3 -> buscar i tal que exp[i] = 3, en este caso i = 29
        * 
        * Tabla de acuerdo a:
        * https://www.thonky.com/qr-code-tutorial/log-antilog-table
        */
        public static final int[] exp = {1, 2, 4, 8, 16, 32, 64, 128, 29, 58, 116, 232, 205, 135, 19, 38, 
            76, 152, 45, 90, 180, 117, 234, 201, 143, 3, 6, 12, 24, 48, 96, 192, 
            157, 39, 78, 156, 37, 74, 148, 53, 106, 212, 181, 119, 238, 193, 159, 
            35, 70, 140, 5, 10, 20, 40, 80, 160, 93, 186, 105, 210, 185, 111, 222, 
            161, 95, 190, 97, 194, 153, 47, 94, 188, 101, 202, 137, 15, 30, 60, 
            120, 240, 253, 231, 211, 187, 107, 214, 177, 127, 254, 225, 223, 163, 
            91, 182, 113, 226, 217, 175, 67, 134, 17, 34, 68, 136, 13, 26, 52, 104, 
            208, 189, 103, 206, 129, 31, 62, 124, 248, 237, 199, 147, 59, 118, 236, 
            197, 151, 51, 102, 204, 133, 23, 46, 92, 184, 109, 218, 169, 79, 158, 
            33, 66, 132, 21, 42, 84, 168, 77, 154, 41, 82, 164, 85, 170, 73, 146, 
            57, 114, 228, 213, 183, 115, 230, 209, 191, 99, 198, 145, 63, 126, 252, 
            229, 215, 179, 123, 246, 241, 255, 227, 219, 171, 75, 150, 49, 98, 196, 
            149, 55, 110, 220, 165, 87, 174, 65, 130, 25, 50, 100, 200, 141, 7, 14, 
            28, 56, 112, 224, 221, 167, 83, 166, 81, 162, 89, 178, 121, 242, 249, 
            239, 195, 155, 43, 86, 172, 69, 138, 9, 18, 36, 72, 144, 61, 122, 244, 
            245, 247, 243, 251, 235, 203, 139, 11, 22, 44, 88, 176, 125, 250, 233, 
            207, 131, 27, 54, 108, 216, 173, 71, 142, 1};

            /**
             * Método que dado i un exponente, nos regresa 2^i % 285 calculado 
             * recursivamente utilizando la tabla para mayor velocidad.
             * @param i el exponente 
             * @return  el valor 2^i dentro del campo
             */
            public static int getValue(int i){
                if (i < 0 || i > 256)
                    return -1;
                return exp[i];
            }

            /**
             * Método que dado un entero i, nos regresa el dígito k tal que
             * 2^k = i, dentro del campo.
             * @param i el entero del que queremos saber su logaritmo base 2 en F256
             * @return  k la potencia la cual 2^k = i
             */
            public static int getExp(int i){
                if (i < 0 || i > 256)
                    return -1;
                for(int k = 0; k < 256; k++ )
                    if (exp[k] == i)
                        return k;
                return -1;
            }

    }


    /* La lista de términos del polinomios ordenada */
    private ArrayList<Termino> polinomio;

    /** Crea un polinomio vacío. 
     *  El polinomio vacío es el que no tiene ningún término.
     */
    public Polinomio(){
        polinomio = new ArrayList<>();
    }

    /**
     * Agrega el término nuevo al polinomio, agrupando por potencia como
     * si se sumara al polinomio que se tiene.
     * Ej: (x + 1) , agregar (x) -> 2x + 1
     * @param nuevo el nuevo término a agregar.
     */
    public void agregaTermino(Termino nuevo){
        boolean add = false;
        //Buscar un término con el mismo grado
        for (Termino t: polinomio){
            if (t.getExponente() == nuevo.getExponente()){
                //Agrupamos los términos
                t.suma(nuevo);
                add = true;
            } 
        }
        /* No se agrupó ningún término */
        if (!add){
            polinomio.add(nuevo);
            //Ordenar por potencias la lista de términos.
            polinomio.sort((a,b) -> a.compareTo(b));
        }
    }

    public void quitaTermino(Termino aQuitar){
        polinomio.remove(aQuitar);
    }

    /**
     * Suma 2 polinomios de acuerdo a las operaciones de suma para 
     * polinomios. El resultado se guarda en this.
     * La suma se hace agregando y agrupando (cuando se pueda) cada término
     * del otro polinomio en this.polinomio.
     * La complejidad es de O(n*m)
     * Agrega el término t para cada término en la lista del otro polinomio.
     * @param p el otro polinomio a sumar.
     */
    public void sumaPolinomio(Polinomio p){ 
        for (Termino t: p.polinomio){
            agregaTermino(t);
        }
    }


    /**
     * Multiplica 2 polinomios de acuerdo a las operaciones de 
     * multiplicación para polinomios. El resultado se guarda en this.
     * La multiplicación se hace multiplicando cada término por todo el polinomio
     * y luego se suman términos iguales. La complejidad es de O(n^2*m), n y m el 
     * número de términos en cada polinomio.
     * 
     * Ambos polinomios deben estar en la misma notación, para evitar confusiones.
     * @param p el otro polinomio a multiplicar.
     */
    public void multiplicaPolinomio(Polinomio p){
        if (polinomio.isEmpty()){
            this.polinomio = p.polinomio;
            return;
        }
        Polinomio r = new Polinomio();
        for (Termino t: polinomio){
            for (Termino q: p.polinomio){
                r.agregaTermino(t.multiplicacion(q));
            }
        }
        this.polinomio = r.polinomio;
    }
    /**
     * Divide 2 polinomios usando las reglas de división de polinomios.
     * Solo lo usaremos para hacer xor a cada término. Los polinomios
     * deben estar en notación entera para poder hacer xor.
     * El algoritmo de la división con números enteros se puede implementar.
     * @param p el otro polinomio a dividir.
     */
    public void dividePolinomio(Polinomio p){
        Polinomio z = new Polinomio();
        /* Método mal optimizado 
         * Hacemos O(n^2) para procesar todos los elementos
         */
        int min = Math.min(getTerminos(), p.getTerminos());
        for (int i = 0; i < min; i++){
            if (i > p.getTerminos()){}
            Termino t = polinomio.get(i);
            Termino s = p.getLista().get(i);
            int c1 = t.coeficiente;
            int c2 = s.getCoeficiente();
            int r = c1 ^ c2;
            t.setCoeficiente(r);
            z.agregaTermino(t);
        }
        if (getTerminos() == p.getTerminos())
        return;
        if (getTerminos() > p.getTerminos()){
            return;
        } else {
            for (int i = min; i < p.getTerminos(); i ++){
                z.agregaTermino(p.polinomio.get(i));
            }
        }
        this.polinomio = z.polinomio;
    }

    /**
     * Regresa una copia de este polinomio.
     * @return una copia de este polinomio.
     */
    public Polinomio copia(){
        Polinomio c = new Polinomio();
        for (Termino t: polinomio){
            if (t.getAlpha() == null)
                c.agregaTermino(new Termino(t.getCoeficiente() , t.getExponente()));
            else 
                c.agregaTermino(new Termino(new Exponente(t.getAlpha().base, t.getAlpha().getExponente()), t.getExponente()));
        }
        return c;
    }

    /**
     * La parte wtf del algoritmo, el polinomio generador se actualiza
     * pero no se espcifíca en ninguna parte del algoritmo.
     */
    public void bajarGrado(){
        for (Termino t: polinomio)
            t.setExponente(t.getExponente()-1);
    }

    /**
     * Convierte este polinomio a un polinomio de notación alpha, según la tabla
     * modular.
     */
    public void toAlphaNotation(){
        for (Termino t: polinomio)
            t.toAlpha();
    }

    /**
     * Convierte este polinomio a un polinomio de notación alpha, según la tabla
     * modular.
     */
    public void toIntegerNotation(){
        for (Termino t: polinomio)
            t.toInteger();
    }

    /*¨
     * Regresa el grado máximo de este polinomio. Si el 
     * polinomio es vacío, se regresa -1.
     */
    public int gradoMaximo(){
        if (polinomio.isEmpty())
            return -1;
        return polinomio.get(0).exponente;
    }

    /**
     * Regresa el grado mínimo de este polinomio.
     * Si el polinomio es vacío, se regresa -1.
     */
    public int gradoMinimo(){
        if (polinomio.isEmpty())
            return -1;
        return polinomio.get(polinomio.size()-1).getExponente();
    }

    /**
     * Nos da el número de términos de este polinomio.
     * Los términos deberían estar agrupados.
     * @return el número de términos de este polinomio.
     */
    public int getTerminos(){
        return polinomio.size();
    }

    public Termino getPrimerTermino(){
        if (getTerminos() == 0)
            return null;
        return polinomio.get(0);
    }

    public ArrayList<Termino> getLista(){
        return this.polinomio;
    }

    /**
     * Regresa una representación en cadena de este polinomio.
     * La representación la haremos de la forma
     * 2x^7 + 87x^6 + 229x^5 + 246x^4 + 49x^3 + 8x^2 + 0x^1 + 1x^0
     * donde si un número está elevado a la potencia 0, es 1, por leyes de los exponentes.
     * Cuando utilicemos el coeficientes del tipo α^n la representación será:
     * 2^0x^2 + 2^25x^1 + 2^1x^0
     * @return una representación en cadena del polinomio.
     */
    @Override
    public String toString(){
        String s = "";
        for (Termino t: polinomio)
            s += t.toString() + " + ";
        return s.substring(0, s.length()-3);
    }

    /* Crea el polinomio generador de grado n-1 (por el 0)
     * Multiplica los polinomios de la forma
     * (x - α^0) (x - α^1) (x - α^2) * ... * (x - α^n-2) (x - α^n-1)
     * donde n es el número de bloques de corrección que se harán.
     * el mínimo para un 1-QR Low es 7. (el mínimo de mínimos)
     */
    public static Polinomio generator(int errBlocks){
        
        ArrayList<Polinomio> generator = new ArrayList<>();
        for (int i = 0; i < errBlocks; i++){
            Polinomio p = new Polinomio();
            //(x +              (α^0x^1)
            p.agregaTermino(new Termino(new Exponente(0), 1));
            // + α^i)           (α^ix^0)
            p.agregaTermino(new Termino(new Exponente(i), 0));
            generator.add(p);
        }
        
        Polinomio r = new Polinomio();
        for (Polinomio t: generator){
            r.multiplicaPolinomio(t);
        }
        return r;
    }

    
    /**
     * Crea un polinomio cuyos coeficientes con los caracteres en binario
     * y las potencias son la longitud del mensaje.
     * "Hi" -> 72x^(1) + 105x^(0) == 72x + 105
     * El grado de "H" será "Hi".length-1 hasta 0
     * @return el polinomio generado a partir de los coeficientes del mensaje.
     */
    public static Polinomio messagePoly(String mensaje) {
        Polinomio m = new Polinomio();
        for (int i = 0; i < mensaje.length(); i++){
            char c = mensaje.charAt(i);
            int ascii = (int)c;
            int grado = mensaje.length()-(i+1);
            Termino t = new Termino(ascii, grado);
            m.agregaTermino(t);
        }
        return m;
    }
}