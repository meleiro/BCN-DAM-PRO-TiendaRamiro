
package conexion;

/*
 * ============================================================
 * IMPORTACIÓN DE EntityManager
 * ============================================================
 *
 * EntityManager es una interfaz de Jakarta Persistence (JPA).
 *
 * Representa un contexto de persistencia.
 *
 * Un contexto de persistencia permite gestionar objetos
 * Java que están relacionados con registros de una base
 * de datos.
 *
 * Por ejemplo, con un EntityManager podemos:
 *
 * - Buscar un cliente por su identificador.
 * - Consultar todos los clientes mediante JPQL.
 * - Registrar un nuevo cliente con persist().
 * - Eliminar un cliente con remove().
 * - Detectar cambios en entidades gestionadas.
 *
 * Ejemplos:
 *
 * em.find(ClienteEntidad.class, 1);
 *
 * em.persist(cliente);
 *
 * em.remove(cliente);
 *
 * IMPORTANTE:
 *
 * EntityManager NO es exactamente una conexión JDBC.
 *
 * Es un objeto de más alto nivel que permite trabajar
 * con entidades Java.
 *
 * Hibernate utiliza JDBC internamente para comunicarse
 * con PostgreSQL cuando necesita ejecutar operaciones.
 *
 * Un EntityManager debe cerrarse cuando termina
 * la operación o unidad de trabajo correspondiente.
 */
import jakarta.persistence.EntityManager;

/*
 * ============================================================
 * IMPORTACIÓN DE EntityManagerFactory
 * ============================================================
 *
 * EntityManagerFactory es una interfaz de JPA.
 *
 * Su función principal es crear objetos EntityManager.
 *
 * Podemos imaginarla como una fábrica:
 *
 *           EntityManagerFactory
 *                    |
 *          ---------------------
 *          |         |         |
 *          v         v         v
 *         EM1       EM2       EM3
 *
 * Cada EntityManager puede utilizarse para realizar
 * una operación o unidad de trabajo independiente.
 *
 * La creación de EntityManagerFactory puede requerir:
 *
 * - Leer el archivo persistence.xml.
 * - Inicializar Hibernate.
 * - Interpretar las entidades anotadas con @Entity.
 * - Preparar los metadatos de persistencia.
 * - Configurar el acceso a PostgreSQL.
 * - Preparar otros recursos internos.
 *
 * Por eso es un objeto relativamente costoso de crear.
 *
 * Lo habitual es crear una factoría compartida
 * para toda la aplicación y reutilizarla.
 *
 * EntityManagerFactory está diseñada para compartirse
 * entre diferentes hilos.
 *
 * EntityManager, en cambio, no debe compartirse
 * simultáneamente entre distintos hilos.
 */
import jakarta.persistence.EntityManagerFactory;

/*
 * ============================================================
 * IMPORTACIÓN DE Persistence
 * ============================================================
 *
 * Persistence es una clase de Jakarta Persistence.
 *
 * Proporciona métodos para inicializar una
 * EntityManagerFactory.
 *
 * En nuestro proyecto utilizaremos:
 *
 * Persistence.createEntityManagerFactory("tiendaPU");
 *
 * El texto "tiendaPU" es el nombre de la unidad
 * de persistencia definida en:
 *
 * src/main/resources/META-INF/persistence.xml
 *
 * Por ejemplo:
 *
 * <persistence-unit name="tiendaPU"
 *                   transaction-type="RESOURCE_LOCAL">
 *
 * La unidad de persistencia contiene la configuración
 * necesaria para inicializar Hibernate.
 *
 * Entre otras cosas:
 *
 * - Proveedor de persistencia.
 * - Entidades registradas.
 * - URL de PostgreSQL.
 * - Usuario y contraseña.
 * - Propiedades de Hibernate.
 *
 * El nombre utilizado en Java debe coincidir
 * exactamente con el definido en el XML.
 */
import jakarta.persistence.Persistence;

/**
 * ============================================================
 * CLASE JpaUtil
 * ============================================================
 *
 * OBJETIVO:
 *
 * Centralizar la creación y el cierre de los objetos
 * relacionados con la infraestructura JPA.
 *
 * Esta clase permite que los DAO no tengan que
 * inicializar Hibernate directamente.
 *
 * Por ejemplo, desde ClienteDAOOrm:
 *
 * EntityManager em = JpaUtil.abrir();
 *
 * try {
 *     // Realizar operaciones con clientes.
 * } finally {
 *     em.close();
 * }
 *
 * VENTAJAS:
 *
 * 1. Evitamos repetir la configuración de Hibernate.
 *
 * 2. Reutilizamos una única EntityManagerFactory.
 *
 * 3. Centralizamos la gestión de la factoría.
 *
 * 4. Facilitamos el mantenimiento del proyecto.
 *
 * 5. Separamos la infraestructura de persistencia
 *    de las operaciones concretas de cada DAO.
 *
 * Esta clase utiliza una estructura de clase de utilidad.
 *
 * Sus métodos son estáticos y no necesitamos
 * crear objetos JpaUtil.
 */
public class JpaUtil {

    /*
     * ========================================================
     * ATRIBUTO ESTÁTICO factoria
     * ========================================================
     *
     * Declaramos una variable:
     *
     * private static EntityManagerFactory factoria;
     *
     * Vamos a analizar cada elemento.
     *
     * --------------------------------------------------------
     * private
     * --------------------------------------------------------
     *
     * Indica que la variable solamente puede utilizarse
     * directamente desde dentro de JpaUtil.
     *
     * Otras clases no pueden acceder a ella mediante:
     *
     * JpaUtil.factoria
     *
     * Esto permite controlar cómo se crea y se cierra.
     *
     * --------------------------------------------------------
     * static
     * --------------------------------------------------------
     *
     * Significa que la variable pertenece a la clase,
     * no a un objeto concreto.
     *
     * Todas las llamadas a JpaUtil.abrir() utilizan
     * el mismo atributo estático factoria.
     *
     * No se crea una factoría nueva cada vez que
     * invocamos el método abrir().
     *
     * --------------------------------------------------------
     * EntityManagerFactory
     * --------------------------------------------------------
     *
     * Es el tipo de la variable.
     *
     * Almacenará una referencia a la factoría
     * que proporciona Hibernate.
     *
     * --------------------------------------------------------
     * factoria
     * --------------------------------------------------------
     *
     * Es el nombre de la variable.
     *
     * Como no asignamos un valor inicial,
     * Java la inicializa automáticamente a null.
     *
     * Por tanto, al comenzar:
     *
     * factoria == null
     *
     * Todavía no hemos inicializado Hibernate.
     */
    private static EntityManagerFactory factoria;

    /*
     * ========================================================
     * CONSTRUCTOR PRIVADO
     * ========================================================
     *
     * Un constructor sirve para crear objetos de una clase.
     *
     * Normalmente podríamos escribir:
     *
     * JpaUtil utilidad = new JpaUtil();
     *
     * Sin embargo, aquí no queremos permitirlo.
     *
     * ¿POR QUÉ?
     *
     * Porque JpaUtil solamente contiene métodos estáticos.
     *
     * No necesitamos crear objetos de esta clase
     * para utilizar sus funcionalidades.
     *
     * Declarar el constructor como private impide
     * que otras clases creen instancias mediante new.
     *
     * Desde fuera de JpaUtil, esta instrucción
     * produciría un error de compilación:
     *
     * new JpaUtil();
     *
     * Esta es una técnica habitual en clases de utilidad.
     *
     * El constructor está vacío porque no necesitamos
     * realizar ninguna operación de inicialización
     * de objetos JpaUtil.
     */
    private JpaUtil() {

    }

    /*
     * ========================================================
     * MÉTODO abrir()
     * ========================================================
     *
     * Este método permite obtener un EntityManager.
     *
     * Su declaración es:
     *
     * public static synchronized EntityManager abrir()
     *
     * --------------------------------------------------------
     * public
     * --------------------------------------------------------
     *
     * Permite utilizar el método desde otras clases.
     *
     * Por ejemplo:
     *
     * ClienteDAOOrm
     * ProductoDAOOrm
     * PruebaConexionORM
     *
     * --------------------------------------------------------
     * static
     * --------------------------------------------------------
     *
     * Permite llamar al método sin crear un objeto JpaUtil.
     *
     * Ejemplo:
     *
     * JpaUtil.abrir();
     *
     * --------------------------------------------------------
     * synchronized
     * --------------------------------------------------------
     *
     * Indica que el método está sincronizado.
     *
     * Al tratarse de un método static synchronized,
     * Java utiliza el monitor asociado a JpaUtil.class.
     *
     * Esto permite que solamente un hilo a la vez
     * ejecute un método estático sincronizado
     * de esta clase.
     *
     * ¿POR QUÉ ES ÚTIL?
     *
     * Imaginemos que dos hilos llaman a abrir()
     * cuando factoria todavía es null.
     *
     * Sin sincronización, ambos podrían intentar
     * inicializar una factoría al mismo tiempo.
     *
     * Con synchronized, uno de los hilos ejecuta
     * el método y el otro espera.
     *
     * Así evitamos inicializaciones simultáneas
     * de la factoría desde este método.
     *
     * IMPORTANTE:
     *
     * synchronized protege la ejecución del método.
     *
     * No convierte los EntityManager devueltos
     * en objetos seguros para compartir entre hilos.
     *
     * --------------------------------------------------------
     * EntityManager
     * --------------------------------------------------------
     *
     * Es el tipo de dato que devuelve el método.
     *
     * El método entregará un nuevo EntityManager.
     *
     * --------------------------------------------------------
     * abrir()
     * --------------------------------------------------------
     *
     * Es el nombre del método.
     *
     * No recibe parámetros.
     */
    public static synchronized EntityManager abrir() {

        /*
         * ====================================================
         * COMPROBAR EL ESTADO DE LA FACTORÍA
         * ====================================================
         *
         * Evaluamos esta condición:
         *
         * factoria == null || !factoria.isOpen()
         *
         * El objetivo es determinar si necesitamos
         * inicializar una EntityManagerFactory.
         *
         * ----------------------------------------------------
         * factoria == null
         * ----------------------------------------------------
         *
         * Comprueba si todavía no existe una factoría
         * almacenada en nuestra variable.
         *
         * Esto sucede normalmente en la primera llamada
         * al método abrir().
         *
         * ----------------------------------------------------
         * ||
         * ----------------------------------------------------
         *
         * Es el operador OR lógico.
         *
         * La condición será verdadera si al menos
         * una de las dos expresiones es verdadera.
         *
         * Además, utiliza evaluación de cortocircuito.
         *
         * Si factoria == null es verdadero,
         * Java no evalúa la segunda condición.
         *
         * Esto evita llamar a isOpen() sobre null,
         * lo que provocaría una NullPointerException.
         *
         * ----------------------------------------------------
         * !factoria.isOpen()
         * ----------------------------------------------------
         *
         * isOpen() devuelve true si la factoría
         * continúa abierta.
         *
         * El operador ! invierte el resultado.
         *
         * Por tanto:
         *
         * factoria.isOpen() == true
         *     La factoría está abierta.
         *
         * !factoria.isOpen() == true
         *     La factoría está cerrada.
         *
         * ----------------------------------------------------
         * RESULTADO DE LA CONDICIÓN
         * ----------------------------------------------------
         *
         * Si no existe factoría o está cerrada:
         *
         *     Entramos en el if.
         *
         * Si existe y está abierta:
         *
         *     No entramos en el if.
         *
         *     Reutilizamos la factoría existente.
         */
        if (factoria == null || !factoria.isOpen()) {

            /*
             * ================================================
             * MOSTRAR MENSAJE DE INICIALIZACIÓN
             * ================================================
             *
             * Este mensaje es informativo.
             *
             * Permite comprobar cuándo se ejecuta
             * realmente la inicialización de Hibernate.
             *
             * Normalmente aparecerá durante
             * la primera llamada a JpaUtil.abrir().
             *
             * En llamadas posteriores no debería
             * volver a aparecer mientras la factoría
             * continúe abierta.
             */
            System.out.println("inicializando hibernate..");

            /*
             * ================================================
             * CREAR LA ENTITYMANAGERFACTORY
             * ================================================
             *
             * Esta es la instrucción principal
             * de inicialización de JPA.
             *
             * Persistence.createEntityManagerFactory(
             *     "tiendaPU"
             * );
             *
             * ------------------------------------------------
             * ¿QUÉ SUCEDE INTERNAMENTE?
             * ------------------------------------------------
             *
             * 1. JPA localiza la configuración de
             *    la unidad de persistencia tiendaPU.
             *
             * 2. Lee persistence.xml.
             *
             * 3. Identifica Hibernate como proveedor.
             *
             * 4. Hibernate procesa la configuración.
             *
             * 5. Analiza las entidades registradas.
             *
             * 6. Prepara los metadatos necesarios
             *    para trabajar con PostgreSQL.
             *
             * 7. Inicializa los servicios y recursos
             *    necesarios para la persistencia.
             *
             * 8. Devuelve una EntityManagerFactory.
             *
             * ------------------------------------------------
             * INICIALIZACIÓN DIFERIDA
             * ------------------------------------------------
             *
             * Esta técnica se denomina:
             *
             * Lazy initialization.
             *
             * Significa que no inicializamos Hibernate
             * al cargar la clase JpaUtil.
             *
             * Lo inicializamos cuando realmente
             * necesitamos abrir un EntityManager.
             *
             * ------------------------------------------------
             * ¿QUÉ PASA SI HAY UN ERROR?
             * ------------------------------------------------
             *
             * Por ejemplo:
             *
             * - No existe persistence.xml.
             * - La unidad tiendaPU no está definida.
             * - El XML contiene un error.
             * - Hibernate no puede validar el esquema.
             * - Hay problemas de conexión con PostgreSQL.
             *
             * En esos casos puede producirse una excepción.
             *
             * Como no la capturamos aquí, se propagará
             * al código que llamó a JpaUtil.abrir().
             *
             * Esto permite gestionar el error desde
             * una clase de prueba o desde la aplicación.
             */
            factoria = Persistence.createEntityManagerFactory(
                    "tiendaPU"
            );
        }

        /*
         * ====================================================
         * CREAR Y DEVOLVER UN ENTITYMANAGER
         * ====================================================
         *
         * En este punto tenemos una factoría disponible.
         *
         * Ejecutamos:
         *
         * factoria.createEntityManager();
         *
         * La factoría crea un nuevo EntityManager.
         *
         * IMPORTANTE:
         *
         * Cada llamada a abrir() devuelve un nuevo
         * EntityManager.
         *
         * Pero normalmente todos se crean utilizando
         * la misma EntityManagerFactory.
         *
         * Por ejemplo:
         *
         * EntityManager em1 = JpaUtil.abrir();
         *
         * EntityManager em2 = JpaUtil.abrir();
         *
         * em1 y em2 representan contextos
         * de persistencia diferentes.
         *
         * ----------------------------------------------------
         * ¿QUÉ HACE return?
         * ----------------------------------------------------
         *
         * Devuelve el EntityManager al método
         * que realizó la llamada.
         *
         * Ejemplo:
         *
         * EntityManager em = JpaUtil.abrir();
         *
         * La variable em recibe el objeto devuelto.
         *
         * ----------------------------------------------------
         * ¿QUIÉN DEBE CERRARLO?
         * ----------------------------------------------------
         *
         * El código que utiliza el EntityManager.
         *
         * Por ejemplo:
         *
         * EntityManager em = JpaUtil.abrir();
         *
         * try {
         *     // Consultar clientes.
         * } finally {
         *     em.close();
         * }
         *
         * JpaUtil.abrir() crea y devuelve el gestor,
         * pero no lo cierra inmediatamente.
         *
         * Si lo cerrásemos aquí, el DAO recibiría
         * un EntityManager que ya no podría utilizar.
         */
        return factoria.createEntityManager();
    }

    /*
     * ========================================================
     * MÉTODO cerrar()
     * ========================================================
     *
     * Su objetivo es cerrar la EntityManagerFactory
     * compartida por nuestra aplicación.
     *
     * --------------------------------------------------------
     * public
     * --------------------------------------------------------
     *
     * Permite llamar al método desde otras clases.
     *
     * --------------------------------------------------------
     * static
     * --------------------------------------------------------
     *
     * Permite ejecutarlo sin crear un objeto JpaUtil.
     *
     * Ejemplo:
     *
     * JpaUtil.cerrar();
     *
     * --------------------------------------------------------
     * synchronized
     * --------------------------------------------------------
     *
     * Evita que este método se ejecute simultáneamente
     * con otros métodos estáticos sincronizados
     * de JpaUtil.
     *
     * Por ejemplo, evita que un hilo ejecute abrir()
     * mientras otro está ejecutando cerrar().
     *
     * IMPORTANTE:
     *
     * Esto coordina el acceso a la factoría
     * a través de estos métodos.
     *
     * No espera automáticamente a que terminen
     * todas las operaciones que otros EntityManager
     * puedan estar realizando.
     *
     * Por eso debemos cerrar la factoría únicamente
     * cuando la aplicación ya no necesite utilizarla.
     *
     * --------------------------------------------------------
     * void
     * --------------------------------------------------------
     *
     * Indica que el método no devuelve ningún valor.
     *
     * --------------------------------------------------------
     * cerrar()
     * --------------------------------------------------------
     *
     * Es el nombre del método.
     *
     * No recibe parámetros.
     */
    public static synchronized void cerrar() {

        /*
         * ====================================================
         * COMPROBAR SI EXISTE UNA FACTORÍA ABIERTA
         * ====================================================
         *
         * Evaluamos:
         *
         * factoria != null && factoria.isOpen()
         *
         * ----------------------------------------------------
         * factoria != null
         * ----------------------------------------------------
         *
         * Comprueba que la variable contiene
         * una referencia a una factoría.
         *
         * ----------------------------------------------------
         * &&
         * ----------------------------------------------------
         *
         * Es el operador AND lógico.
         *
         * Ambas condiciones deben ser verdaderas
         * para ejecutar el bloque if.
         *
         * También utiliza evaluación de cortocircuito.
         *
         * Si factoria es null, Java no evalúa isOpen().
         *
         * ----------------------------------------------------
         * factoria.isOpen()
         * ----------------------------------------------------
         *
         * Comprueba que la factoría está abierta.
         *
         * ----------------------------------------------------
         * ¿CUÁNDO ENTRAMOS EN EL IF?
         * ----------------------------------------------------
         *
         * Cuando existe una EntityManagerFactory
         * y todavía está abierta.
         */
        if (factoria != null && factoria.isOpen()) {

            /*
             * ================================================
             * CERRAR LA FACTORÍA
             * ================================================
             *
             * close() cierra la EntityManagerFactory.
             *
             * Hibernate libera los recursos
             * asociados a esa factoría.
             *
             * Esto puede incluir:
             *
             * - Servicios internos.
             * - Recursos de conexión.
             * - Pools de conexiones gestionados.
             * - Otros recursos de Hibernate.
             *
             * IMPORTANTE:
             *
             * Cerrar la factoría no sustituye
             * el cierre individual de cada EntityManager.
             *
             * Los EntityManager deben cerrarse
             * cuando terminamos de utilizarlos.
             *
             * La factoría debe cerrarse normalmente
             * al finalizar la aplicación.
             */
            factoria.close();
        }

        /*
         * ====================================================
         * RESTABLECER LA REFERENCIA A NULL
         * ====================================================
         *
         * Después de cerrar la factoría ejecutamos:
         *
         * factoria = null;
         *
         * Esto elimina la referencia almacenada
         * en el atributo estático.
         *
         * IMPORTANTE:
         *
         * Asignar null no es lo mismo que cerrar
         * la factoría.
         *
         * El cierre se realiza con:
         *
         * factoria.close();
         *
         * Asignar null solamente cambia
         * el valor de nuestra variable.
         *
         * ----------------------------------------------------
         * ¿QUÉ OCURRE SI VOLVEMOS A LLAMAR A abrir()?
         * ----------------------------------------------------
         *
         * Como factoria será null, se cumplirá:
         *
         * factoria == null
         *
         * Por tanto, abrir() intentará crear
         * una nueva EntityManagerFactory.
         *
         * Esto permite reinicializar Hibernate
         * si volvemos a necesitarlo.
         *
         * Sin embargo, en una aplicación normal,
         * lo habitual es mantener la factoría abierta
         * durante toda su ejecución y cerrarla al final.
         */
        factoria = null;
    }
}
