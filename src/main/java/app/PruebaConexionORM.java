
package app;

/*
 * ============================================================
 * IMPORTACIÓN DE JpaUtil
 * ============================================================
 *
 * JpaUtil es nuestra clase auxiliar de conexión ORM.
 *
 * Se encuentra dentro del paquete conexion.
 *
 * Su responsabilidad es gestionar la infraestructura
 * de Jakarta Persistence (JPA).
 *
 * En concreto, contiene una EntityManagerFactory
 * compartida por nuestra aplicación.
 *
 * Esta factoría se configura utilizando el archivo:
 *
 * src/main/resources/META-INF/persistence.xml
 *
 * En ese archivo tenemos definida la unidad:
 *
 *     tiendaPU
 *
 * JpaUtil proporciona dos métodos principales:
 *
 * ------------------------------------------------------------
 * JpaUtil.abrir()
 * ------------------------------------------------------------
 *
 * Obtiene un nuevo EntityManager.
 *
 * Si todavía no existe una EntityManagerFactory,
 * primero la inicializa utilizando Hibernate.
 *
 * ------------------------------------------------------------
 * JpaUtil.cerrar()
 * ------------------------------------------------------------
 *
 * Cierra la EntityManagerFactory compartida.
 *
 * Normalmente se utiliza al finalizar la aplicación.
 *
 * En esta clase de prueba podemos cerrarla porque
 * no necesitaremos realizar más operaciones ORM
 * después de terminar el método main().
 */
import conexion.JpaUtil;

/*
 * ============================================================
 * IMPORTACIÓN DE EntityManager
 * ============================================================
 *
 * EntityManager es una interfaz de Jakarta Persistence.
 *
 * Representa un contexto de persistencia.
 *
 * Permite trabajar con entidades Java y ejecutar consultas.
 *
 * Algunos de sus métodos más importantes son:
 *
 * find():
 *     Busca una entidad por su clave primaria.
 *
 * persist():
 *     Registra una entidad nueva en el contexto
 *     de persistencia para su inserción.
 *
 * remove():
 *     Marca una entidad gestionada para eliminarla.
 *
 * createQuery():
 *     Permite crear consultas JPQL.
 *
 * createNativeQuery():
 *     Permite crear consultas SQL nativas.
 *
 * close():
 *     Cierra el EntityManager.
 *
 * IMPORTANTE:
 *
 * EntityManager no es una conexión JDBC directa.
 *
 * Es un objeto de nivel superior que administra
 * el contexto de persistencia.
 *
 * Hibernate utiliza JDBC internamente cuando
 * necesita comunicarse con PostgreSQL.
 */
import jakarta.persistence.EntityManager;

/**
 * ============================================================
 * CLASE PruebaConexionORM
 * ============================================================
 *
 * OBJETIVO:
 *
 * Comprobar que nuestra configuración JPA/Hibernate
 * permite ejecutar una consulta contra PostgreSQL.
 *
 * La prueba seguirá estos pasos:
 *
 * 1. Obtener un EntityManager desde JpaUtil.
 *
 * 2. Ejecutar una consulta SQL:
 *
 *        SELECT 1
 *
 * 3. Recuperar el resultado de la consulta.
 *
 * 4. Comprobar que el resultado es el número 1.
 *
 * 5. Mostrar un mensaje de éxito o de error.
 *
 * 6. Cerrar el EntityManager.
 *
 * 7. Cerrar la EntityManagerFactory.
 *
 * Esta prueba no utiliza todavía:
 *
 * - ClienteEntidad.
 * - ProductoEntidad.
 * - ClienteDAOOrm.
 * - ProductoDAOOrm.
 *
 * Su finalidad es comprobar el funcionamiento
 * básico de la infraestructura ORM.
 */
public class PruebaConexionORM {

    /*
     * ========================================================
     * MÉTODO main()
     * ========================================================
     *
     * Es el punto de entrada de nuestra aplicación.
     *
     * public:
     *     Permite que el método sea accesible.
     *
     * static:
     *     Permite ejecutarlo sin crear un objeto
     *     de la clase PruebaConexionORM.
     *
     * void:
     *     Indica que el método no devuelve ningún valor.
     *
     * String[] args:
     *     Permite recibir argumentos desde la consola.
     *
     * En esta prueba no utilizamos esos argumentos.
     */
    public static void main(String[] args) {

        /*
         * ====================================================
         * PASO 1: DECLARAR EL ENTITYMANAGER
         * ====================================================
         *
         * Declaramos una variable llamada em.
         *
         * Su tipo es EntityManager.
         *
         * Inicialmente le asignamos null:
         *
         *     EntityManager em = null;
         *
         * Esto significa que todavía no tenemos
         * ningún EntityManager creado.
         *
         * ----------------------------------------------------
         * ¿POR QUÉ DECLARAMOS em FUERA DEL try?
         * ----------------------------------------------------
         *
         * Porque necesitaremos utilizar esta variable
         * también dentro del bloque finally.
         *
         * En el finally intentaremos cerrar el
         * EntityManager si ha llegado a crearse.
         *
         * Si declaramos la variable dentro del try,
         * no estaría disponible desde el finally.
         *
         * ----------------------------------------------------
         * ¿POR QUÉ LA INICIALIZAMOS A null?
         * ----------------------------------------------------
         *
         * Porque la llamada a JpaUtil.abrir()
         * podría fallar.
         *
         * Por ejemplo, si Hibernate no encuentra
         * persistence.xml o no puede conectar
         * con PostgreSQL.
         *
         * Si eso sucede, em continuará siendo null.
         *
         * Posteriormente podremos comprobarlo
         * antes de intentar cerrar el recurso.
         */
        EntityManager em = null;

        /*
         * ====================================================
         * PASO 2: BLOQUE try PRINCIPAL
         * ====================================================
         *
         * El bloque try contiene las instrucciones
         * que pueden producir una excepción.
         *
         * Por ejemplo:
         *
         * - Error en persistence.xml.
         * - Unidad tiendaPU no encontrada.
         * - Contraseña de PostgreSQL incorrecta.
         * - Servidor PostgreSQL detenido.
         * - Problema al ejecutar SQL.
         * - Resultado inesperado de la consulta.
         *
         * Si se produce una excepción compatible
         * con Exception, Java ejecutará el catch.
         *
         * Después ejecutará el bloque finally.
         */
        try {

            /*
             * =================================================
             * PASO 3: OBTENER UN ENTITYMANAGER
             * =================================================
             *
             * Llamamos al método estático:
             *
             *     JpaUtil.abrir()
             *
             * Este método pertenece a nuestra clase
             * auxiliar JpaUtil.
             *
             * Internamente comprueba si existe una
             * EntityManagerFactory abierta.
             *
             * Si no existe, la inicializa mediante:
             *
             * Persistence.createEntityManagerFactory(
             *     "tiendaPU"
             * );
             *
             * Después utiliza:
             *
             * factoria.createEntityManager();
             *
             * para obtener un EntityManager.
             *
             * -------------------------------------------------
             * ¿QUÉ SUCEDE SI TODO FUNCIONA?
             * -------------------------------------------------
             *
             * La referencia al EntityManager devuelto
             * se almacena en nuestra variable em.
             *
             * -------------------------------------------------
             * ¿QUÉ SUCEDE SI HAY UN ERROR?
             * -------------------------------------------------
             *
             * Si JpaUtil.abrir() lanza una excepción,
             * la ejecución pasa al catch.
             *
             * En ese caso, em puede continuar siendo null.
             *
             * IMPORTANTE:
             *
             * Crear un EntityManager no demuestra por sí
             * solo que se haya ejecutado correctamente
             * una consulta contra PostgreSQL.
             *
             * Esa comprobación la realizaremos después.
             */
            em = JpaUtil.abrir();

            /*
             * =================================================
             * PASO 4: MOSTRAR MENSAJE INFORMATIVO
             * =================================================
             *
             * System.out.println() escribe texto
             * en la salida estándar de la consola.
             *
             * System:
             *     Clase estándar de Java.
             *
             * out:
             *     Salida estándar.
             *
             * println():
             *     Imprime el mensaje y añade
             *     un salto de línea.
             *
             * Este mensaje indica que hemos obtenido
             * correctamente un EntityManager.
             *
             * No significa todavía que SELECT 1
             * se haya ejecutado correctamente.
             */
            System.out.println("EntityManager creado");

            /*
             * =================================================
             * PASO 5: EJECUTAR UNA CONSULTA SQL NATIVA
             * =================================================
             *
             * Esta es una de las instrucciones principales:
             *
             * Object resultado = em.createNativeQuery(
             *     "SELECT 1"
             * ).getSingleResult();
             *
             * Vamos a analizar cada elemento.
             *
             * -------------------------------------------------
             * Object resultado
             * -------------------------------------------------
             *
             * Declaramos una variable llamada resultado.
             *
             * Su tipo es Object.
             *
             * Object es la clase base de los objetos Java.
             *
             * Una consulta SQL nativa puede devolver
             * valores de diferentes tipos.
             *
             * Por eso utilizamos Object para recibir
             * el resultado de la consulta.
             *
             * -------------------------------------------------
             * em.createNativeQuery("SELECT 1")
             * -------------------------------------------------
             *
             * createNativeQuery() es un método
             * de EntityManager.
             *
             * Permite crear una consulta utilizando
             * directamente el lenguaje SQL.
             *
             * Esto es diferente de createQuery(),
             * que utiliza JPQL.
             *
             * Por ejemplo:
             *
             * SQL nativo:
             *
             *     SELECT * FROM cliente;
             *
             * JPQL:
             *
             *     SELECT c FROM ClienteEntidad c
             *
             * -------------------------------------------------
             * ¿QUÉ HACE SELECT 1?
             * -------------------------------------------------
             *
             * Es una consulta SQL muy sencilla.
             *
             * Solicita devolver el valor numérico 1.
             *
             * PostgreSQL permite ejecutar:
             *
             *     SELECT 1;
             *
             * sin consultar ninguna tabla.
             *
             * Por tanto, no necesitamos que la tabla
             * cliente contenga registros.
             *
             * Esta consulta es útil para comprobar
             * que podemos comunicarnos con el servidor.
             *
             * -------------------------------------------------
             * .getSingleResult()
             * -------------------------------------------------
             *
             * Ejecuta la consulta y recupera
             * un único resultado.
             *
             * En este caso esperamos recibir
             * el número 1.
             *
             * Si la consulta no pudiera ejecutarse,
             * se produciría una excepción.
             *
             * -------------------------------------------------
             * ENCADENAMIENTO DE MÉTODOS
             * -------------------------------------------------
             *
             * Estamos encadenando dos llamadas:
             *
             * em.createNativeQuery("SELECT 1")
             *   .getSingleResult();
             *
             * La primera crea el objeto Query.
             *
             * La segunda ejecuta esa consulta
             * y obtiene su resultado.
             *
             * También podríamos escribir:
             *
             * Query consulta = em.createNativeQuery(
             *     "SELECT 1"
             * );
             *
             * Object resultado = consulta.getSingleResult();
             *
             * Ambas formas son equivalentes.
             */
            Object resultado = em.createNativeQuery(
                    "SELECT 1"
            ).getSingleResult();

            /*
             * =================================================
             * PASO 6: COMPROBAR EL RESULTADO
             * =================================================
             *
             * Utilizamos esta condición:
             *
             * !(resultado instanceof Number)
             * ||
             * ((Number) resultado).intValue() != 1
             *
             * El objetivo es comprobar que:
             *
             * 1. El resultado es numérico.
             *
             * 2. Su valor entero es exactamente 1.
             *
             * Si alguna de estas condiciones falla,
             * lanzaremos una excepción.
             *
             * -------------------------------------------------
             * instanceof Number
             * -------------------------------------------------
             *
             * instanceof es un operador de Java.
             *
             * Permite comprobar si un objeto es
             * compatible con un determinado tipo.
             *
             * Number es una clase abstracta
             * que representa valores numéricos.
             *
             * Algunos ejemplos de subclases son:
             *
             * Integer
             * Long
             * Double
             * BigDecimal
             *
             * Por ejemplo:
             *
             * Object valor = Integer.valueOf(1);
             *
             * valor instanceof Number -> true
             *
             * Object valor2 = "Hola";
             *
             * valor2 instanceof Number -> false
             *
             * -------------------------------------------------
             * OPERADOR !
             * -------------------------------------------------
             *
             * El símbolo ! representa la negación lógica.
             *
             * Invierte el resultado de una expresión booleana.
             *
             * Por ejemplo:
             *
             * true  -> false
             *
             * false -> true
             *
             * Por tanto:
             *
             * !(resultado instanceof Number)
             *
             * significa:
             *
             * "El resultado NO es un número".
             *
             * -------------------------------------------------
             * OPERADOR ||
             * -------------------------------------------------
             *
             * Es el operador OR lógico.
             *
             * La condición completa será verdadera
             * si al menos una de sus partes es verdadera.
             *
             * Además, utiliza evaluación de cortocircuito.
             *
             * Esto significa que, si la primera condición
             * es verdadera, Java no evalúa la segunda.
             *
             * ¿POR QUÉ ES IMPORTANTE?
             *
             * Porque si resultado no es un Number,
             * no queremos intentar convertirlo a Number.
             *
             * Así evitamos una ClassCastException
             * provocada por ese intento de conversión.
             *
             * -------------------------------------------------
             * (Number) resultado
             * -------------------------------------------------
             *
             * Realizamos una conversión explícita de tipos.
             *
             * Esta operación se conoce como casting.
             *
             * La variable resultado está declarada
             * como Object.
             *
             * Para utilizar los métodos de Number,
             * necesitamos tratarla como un Number.
             *
             * Por eso escribimos:
             *
             *     (Number) resultado
             *
             * -------------------------------------------------
             * .intValue()
             * -------------------------------------------------
             *
             * Es un método de Number.
             *
             * Devuelve una representación del valor
             * numérico como un int primitivo.
             *
             * Por ejemplo:
             *
             * Number numero = Integer.valueOf(1);
             *
             * numero.intValue() -> 1
             *
             * -------------------------------------------------
             * OPERADOR !=
             * -------------------------------------------------
             *
             * Significa "distinto de".
             *
             * Por tanto:
             *
             * ((Number) resultado).intValue() != 1
             *
             * comprueba si el valor entero
             * obtenido es distinto de 1.
             *
             * -------------------------------------------------
             * ¿CUÁNDO ENTRAMOS EN EL IF?
             * -------------------------------------------------
             *
             * Entramos cuando:
             *
             * - El resultado no es numérico.
             *
             * O bien:
             *
             * - El resultado es numérico,
             *   pero su valor entero no es 1.
             *
             * Si el resultado es el número 1,
             * no entraremos en el bloque if.
             */
            if (
                    !(resultado instanceof Number)
                            ||
                            ((Number) resultado).intValue() != 1
            ) {

                /*
                 * =============================================
                 * LANZAR UNA EXCEPCIÓN MANUALMENTE
                 * =============================================
                 *
                 * Utilizamos:
                 *
                 * throw new IllegalStateException(...)
                 *
                 * ------------------------------------------------
                 * throw
                 * ------------------------------------------------
                 *
                 * Es una palabra reservada de Java.
                 *
                 * Permite lanzar una excepción
                 * de forma explícita.
                 *
                 * ------------------------------------------------
                 * new
                 * ------------------------------------------------
                 *
                 * Crea un nuevo objeto.
                 *
                 * En este caso, un objeto excepción.
                 *
                 * ------------------------------------------------
                 * IllegalStateException
                 * ------------------------------------------------
                 *
                 * Es una excepción de ejecución
                 * que hereda de RuntimeException.
                 *
                 * Se utiliza para indicar que
                 * una operación ha encontrado
                 * un estado inesperado.
                 *
                 * En nuestro caso, esperamos
                 * que PostgreSQL devuelva 1.
                 *
                 * Si devuelve otro valor o tipo,
                 * consideramos que la prueba ha fallado.
                 *
                 * ------------------------------------------------
                 * "Resultado inesperado: " + resultado
                 * ------------------------------------------------
                 *
                 * Construimos un mensaje que incluye
                 * el valor recibido.
                 *
                 * Por ejemplo:
                 *
                 * Resultado inesperado: 2
                 *
                 * Al lanzar la excepción, Java abandona
                 * el flujo normal del try y busca
                 * un bloque catch compatible.
                 */
                throw new IllegalStateException(
                        "Resultado inesperado: " + resultado
                );
            }

            /*
             * =================================================
             * PASO 7: MOSTRAR MENSAJE DE ÉXITO
             * =================================================
             *
             * Esta instrucción solamente se ejecutará
             * si hemos llegado hasta aquí sin excepciones.
             *
             * Eso significa que:
             *
             * 1. JpaUtil ha creado un EntityManager.
             *
             * 2. Hemos ejecutado SELECT 1.
             *
             * 3. Hemos recibido un resultado numérico.
             *
             * 4. Su valor entero es 1.
             *
             * Por tanto, podemos confirmar que
             * la consulta de prueba ha funcionado.
             *
             * IMPORTANTE:
             *
             * Esto no demuestra todavía que el CRUD
             * de clientes o productos funcione.
             *
             * Es una prueba de conectividad básica.
             */
            System.out.println("CONEXIÓN CORRECTA");

        } catch (Exception e) {

            /*
             * =================================================
             * PASO 8: CAPTURAR LAS EXCEPCIONES
             * =================================================
             *
             * Este bloque se ejecuta si una instrucción
             * del try lanza una excepción compatible
             * con Exception.
             *
             * La variable e contiene el objeto excepción.
             *
             * Algunos errores posibles son:
             *
             * - PersistenceException.
             * - IllegalStateException.
             * - Errores de conexión encapsulados por JPA.
             * - Errores al ejecutar la consulta SQL.
             *
             * -------------------------------------------------
             * System.err.println()
             * -------------------------------------------------
             *
             * Muestra texto en la salida de errores.
             *
             * Es diferente de System.out.println(),
             * que utiliza la salida estándar.
             *
             * -------------------------------------------------
             * e.printStackTrace()
             * -------------------------------------------------
             *
             * Muestra información detallada:
             *
             * - Tipo de excepción.
             * - Mensaje de error.
             * - Clase donde ocurrió.
             * - Línea de código.
             * - Cadena de llamadas.
             * - Causas internas, si existen.
             *
             * Es especialmente útil para depurar
             * errores de Hibernate y PostgreSQL.
             */
            System.err.println("Error en la prueba ORM");

            e.printStackTrace();

        } finally {

            /*
             * =================================================
             * PASO 9: BLOQUE finally EXTERIOR
             * =================================================
             *
             * finally permite ejecutar instrucciones
             * de limpieza al abandonar el bloque try.
             *
             * Normalmente se ejecuta tanto si:
             *
             * - La consulta termina correctamente.
             * - Se produce una excepción.
             * - Lanzamos IllegalStateException.
             *
             * En nuestro caso queremos liberar
             * dos recursos diferentes:
             *
             * 1. El EntityManager.
             *
             * 2. La EntityManagerFactory.
             *
             * Primero intentaremos cerrar
             * el EntityManager.
             *
             * Después cerraremos la factoría.
             */

            /*
             * =================================================
             * PASO 10: TRY INTERIOR
             * =================================================
             *
             * Introducimos otro try dentro del finally.
             *
             * ¿POR QUÉ?
             *
             * Porque cerrar un EntityManager también
             * puede producir una excepción.
             *
             * Queremos que, aunque falle em.close(),
             * se intente ejecutar JpaUtil.cerrar().
             *
             * Para conseguirlo utilizamos:
             *
             * try {
             *     cerrar EntityManager;
             * } finally {
             *     cerrar EntityManagerFactory;
             * }
             *
             * Es una estructura de limpieza encadenada.
             */
            try {

                /*
                 * =============================================
                 * COMPROBAR SI EL ENTITYMANAGER ESTÁ ABIERTO
                 * =============================================
                 *
                 * Evaluamos:
                 *
                 * em != null && em.isOpen()
                 *
                 * ------------------------------------------------
                 * em != null
                 * ------------------------------------------------
                 *
                 * Comprueba que hemos obtenido
                 * un EntityManager.
                 *
                 * Si JpaUtil.abrir() falló antes
                 * de devolverlo, em puede ser null.
                 *
                 * ------------------------------------------------
                 * &&
                 * ------------------------------------------------
                 *
                 * Es el operador AND lógico.
                 *
                 * Las dos condiciones deben ser verdaderas.
                 *
                 * También utiliza cortocircuito.
                 *
                 * Si em es null, Java no evalúa em.isOpen().
                 *
                 * Esto evita una NullPointerException.
                 *
                 * ------------------------------------------------
                 * em.isOpen()
                 * ------------------------------------------------
                 *
                 * Comprueba si el EntityManager
                 * continúa abierto.
                 *
                 * Si está cerrado, no necesitamos
                 * volver a cerrarlo.
                 */
                if (em != null && em.isOpen()) {

                    /*
                     * =========================================
                     * CERRAR EL ENTITYMANAGER
                     * =========================================
                     *
                     * em.close() cierra el contexto
                     * de persistencia.
                     *
                     * Libera los recursos asociados
                     * a ese EntityManager.
                     *
                     * Las entidades que estuviesen
                     * gestionadas por ese contexto
                     * dejan de estar gestionadas.
                     *
                     * IMPORTANTE:
                     *
                     * Cerrar el EntityManager no significa
                     * necesariamente cerrar físicamente
                     * una conexión JDBC con PostgreSQL.
                     *
                     * Hibernate puede utilizar un pool
                     * de conexiones.
                     *
                     * El cierre del EntityManager permite
                     * liberar los recursos que utiliza.
                     */
                    em.close();
                }

            } finally {

                /*
                 * =============================================
                 * PASO 11: CERRAR LA FACTORÍA JPA
                 * =============================================
                 *
                 * Este finally interior se ejecuta
                 * aunque em.close() lance una excepción.
                 *
                 * Llamamos a:
                 *
                 * JpaUtil.cerrar();
                 *
                 * Este método comprueba si existe
                 * una EntityManagerFactory abierta.
                 *
                 * Si existe, la cierra.
                 *
                 * Después restablece la referencia
                 * estática factoria a null.
                 *
                 * ------------------------------------------------
                 * ¿POR QUÉ CERRAMOS LA FACTORÍA AQUÍ?
                 * ------------------------------------------------
                 *
                 * Porque esta clase es una prueba
                 * independiente de consola.
                 *
                 * Una vez terminada, ya no necesitamos
                 * utilizar Hibernate.
                 *
                 * En nuestra aplicación Swing, en cambio,
                 * no deberíamos cerrar la factoría
                 * después de cada consulta.
                 *
                 * La mantendríamos abierta durante
                 * toda la ejecución de la aplicación.
                 *
                 * ------------------------------------------------
                 * PRECAUCIÓN
                 * ------------------------------------------------
                 *
                 * Si JpaUtil.cerrar() lanza una excepción,
                 * esta puede propagarse fuera de main().
                 *
                 * El catch exterior no captura excepciones
                 * producidas por su propio bloque finally.
                 *
                 * Para esta prueba mantenemos exactamente
                 * la estructura de tu código original.
                 */
                JpaUtil.cerrar();
            }
        }
    }
}
