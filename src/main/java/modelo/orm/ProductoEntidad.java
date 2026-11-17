package modelo.orm;

/*
 * ============================================================
 * IMPORTACIÓN DE JAKARTA PERSISTENCE
 * ============================================================
 *
 * Jakarta Persistence (JPA) es una especificación
 * que permite relacionar objetos Java con registros
 * de una base de datos relacional.
 *
 * Mediante:
 *
 * import jakarta.persistence.*;
 *
 * importamos los tipos públicos del paquete
 * jakarta.persistence que necesitamos.
 *
 * En esta clase utilizamos:
 *
 * @Entity
 *     Identifica una clase como entidad JPA.
 *
 * @Table
 *     Permite indicar la tabla de la base de datos.
 *
 * @Id
 *     Identifica el atributo que representa
 *     la clave primaria.
 *
 * @GeneratedValue
 *     Configura la generación del identificador.
 *
 * GenerationType
 *     Define las estrategias disponibles
 *     para generar identificadores.
 *
 * @Column
 *     Permite configurar la correspondencia
 *     entre un atributo y una columna.
 *
 * IMPORTANTE:
 *
 * Estas anotaciones pertenecen a JPA.
 *
 * Hibernate es el proveedor que interpreta
 * esas anotaciones y realiza las operaciones
 * necesarias sobre PostgreSQL.
 */
import jakarta.persistence.*;

/**
 * ============================================================
 * ANOTACIÓN @Entity
 * ============================================================
 *
 * Indica que esta clase es una entidad JPA.
 *
 * Una entidad representa información persistente,
 * normalmente relacionada con una tabla.
 *
 * Gracias a @Entity, Hibernate puede gestionar
 * objetos de esta clase.
 *
 * Por ejemplo:
 *
 * ClienteEntidad cliente = em.find(
 *     ClienteEntidad.class,
 *     1
 * );
 *
 * Hibernate puede buscar un registro con ID 1
 * y representarlo mediante un objeto ClienteEntidad.
 *
 * También podremos utilizar esta entidad
 * en consultas JPQL:
 *
 * SELECT c FROM ClienteEntidad c
 *
 * IMPORTANTE:
 *
 * @Entity no crea automáticamente una tabla.
 *
 * La creación o validación del esquema depende
 * de la configuración de Hibernate.
 *
 * En nuestro proyecto utilizamos:
 *
 * hibernate.hbm2ddl.auto = validate
 *
 * Por tanto, Hibernate comprueba que la estructura
 * existente sea compatible con el mapeo.
 */
@Entity

/*
 * ============================================================
 * ANOTACIÓN @Table
 * ============================================================
 *
 * Permite indicar el nombre de la tabla
 * asociada a esta entidad.
 *
 * En nuestro caso:
 *
 * @Table(name = "cliente")
 *
 * significa que los objetos ClienteEntidad
 * se corresponden con registros de la tabla
 * cliente de PostgreSQL.
 *
 * ------------------------------------------------------------
 * ¿POR QUÉ ES NECESARIO INDICAR EL NOMBRE?
 * ------------------------------------------------------------
 *
 * Porque nuestra clase se llama ClienteEntidad,
 * mientras que la tabla se llama cliente.
 *
 * No tienen exactamente el mismo nombre.
 *
 * La anotación permite establecer explícitamente
 * esa correspondencia.
 *
 * SQL:
 *
 * SELECT * FROM cliente;
 *
 * JPQL:
 *
 * SELECT c FROM ClienteEntidad c
 *
 * En SQL utilizamos el nombre de la tabla.
 *
 * En JPQL utilizamos el nombre de la entidad.
 */
@Table(name = "producto")

/**
 * ============================================================
 * CLASE ClienteEntidad
 * ============================================================
 *
 * Esta clase representa un cliente de nuestra tienda.
 *
 * Cada objeto contiene los datos de un cliente:
 *
 * - Identificador.
 * - Nombre.
 * - Correo electrónico.
 * - Estado activo o inactivo.
 *
 * Por ejemplo:
 *
 * ClienteEntidad cliente = new ClienteEntidad(
 *     null,
 *     true,
 *     "ana@ejemplo.com",
 *     "Ana"
 * );
 *
 * IMPORTANTE:
 *
 * El orden de los parámetros del constructor
 * de esta clase es:
 *
 * Integer id
 * boolean activo
 * String email
 * String nombre
 *
 * ------------------------------------------------------------
 * ¿QUÉ DIFERENCIA HAY CON UNA CLASE JAVA NORMAL?
 * ------------------------------------------------------------
 *
 * Una clase Java normal puede representar
 * información en memoria.
 *
 * Una entidad JPA, además, incorpora metadatos
 * que permiten relacionarla con una tabla.
 *
 * Hibernate puede:
 *
 * - Crear objetos a partir de registros.
 * - Insertar nuevas entidades.
 * - Buscar entidades por identificador.
 * - Detectar modificaciones.
 * - Eliminar entidades.
 *
 * Para ello utilizaremos un EntityManager.
 */
public class ProductoEntidad {

    /*
     * ========================================================
     * ATRIBUTO id
     * ========================================================
     *
     * Representa el identificador del cliente.
     *
     * En PostgreSQL corresponde a la clave primaria
     * de la tabla cliente.
     *
     * Por ejemplo:
     *
     * id = 1
     * id = 2
     * id = 3
     *
     * --------------------------------------------------------
     * ANOTACIÓN @Id
     * --------------------------------------------------------
     *
     * Indica que este atributo representa
     * la clave primaria de la entidad.
     *
     * JPA necesita identificar de forma única
     * cada entidad.
     *
     * Gracias a @Id podemos utilizar:
     *
     * em.find(ClienteEntidad.class, 1);
     *
     * para buscar el cliente con identificador 1.
     *
     * --------------------------------------------------------
     * ANOTACIÓN @GeneratedValue
     * --------------------------------------------------------
     *
     * Configura cómo se genera el identificador.
     *
     * En nuestro caso:
     *
     * GenerationType.IDENTITY
     *
     * indica que utilizamos una estrategia
     * de generación basada en la base de datos.
     *
     * PostgreSQL puede tener una columna:
     *
     * id INTEGER GENERATED ALWAYS AS IDENTITY
     *
     * La base de datos genera el identificador
     * cuando se inserta el registro.
     *
     * Hibernate recupera ese identificador
     * para asociarlo al objeto Java.
     *
     * --------------------------------------------------------
     * ¿POR QUÉ UTILIZAMOS Integer Y NO int?
     * --------------------------------------------------------
     *
     * Integer es la clase envoltorio de int.
     *
     * Permite almacenar:
     *
     * - Valores enteros.
     * - El valor null.
     *
     * Esto resulta útil porque un cliente nuevo
     * todavía no tiene un identificador generado.
     *
     * Por ejemplo:
     *
     * ClienteEntidad nuevo = new ClienteEntidad(
     *     null,
     *     true,
     *     "ana@ejemplo.com",
     *     "Ana"
     * );
     *
     * Antes de insertarlo:
     *
     * nuevo.getId() -> null
     *
     * Después de una inserción correcta:
     *
     * nuevo.getId() -> identificador generado
     *
     * --------------------------------------------------------
     * private
     * --------------------------------------------------------
     *
     * Impide que otras clases accedan directamente
     * al atributo.
     *
     * Para consultarlo utilizaremos getId().
     *
     * Para modificarlo existe setId().
     *
     * Sin embargo, en una entidad con identificador
     * generado no debemos modificar arbitrariamente
     * el ID de una entidad ya persistida.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * ========================================================
     * ATRIBUTO nombre
     * ========================================================
     *
     * Almacena el nombre del cliente.
     *
     * --------------------------------------------------------
     * @Column(name = "nombre")
     * --------------------------------------------------------
     *
     * Indica que el atributo Java nombre
     * se corresponde con la columna nombre
     * de la tabla cliente.
     *
     * --------------------------------------------------------
     * nullable = false
     * --------------------------------------------------------
     *
     * Indica que el mapeo considera que la columna
     * no admite valores NULL.
     *
     * Esto se corresponde con una definición SQL
     * similar a:
     *
     * nombre VARCHAR(100) NOT NULL
     *
     * IMPORTANTE:
     *
     * nullable = false no sustituye una validación
     * del formulario.
     *
     * No comprueba, por ejemplo, que el usuario
     * haya escrito un nombre no vacío.
     *
     * Una cadena vacía "" no es lo mismo que NULL.
     *
     * --------------------------------------------------------
     * length = 100
     * --------------------------------------------------------
     *
     * Indica una longitud de 100 caracteres
     * para el mapeo de una columna de texto.
     *
     * No significa que Java impida automáticamente
     * asignar una cadena de más de 100 caracteres.
     *
     * La restricción real dependerá de la definición
     * de la columna y de las validaciones existentes.
     */
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    /*
     * ========================================================
     * ATRIBUTO email
     * ========================================================
     *
     * Almacena el correo electrónico del cliente.
     *
     * --------------------------------------------------------
     * name = "email"
     * --------------------------------------------------------
     *
     * Relaciona este atributo con la columna email.
     *
     * --------------------------------------------------------
     * nullable = false
     * --------------------------------------------------------
     *
     * Indica que la columna no debería admitir NULL.
     *
     * --------------------------------------------------------
     * length = 150
     * --------------------------------------------------------
     *
     * Establece una longitud de 150 caracteres
     * para el mapeo.
     *
     * --------------------------------------------------------
     * unique = true
     * --------------------------------------------------------
     *
     * Indica que la columna debe tener
     * una restricción de unicidad.
     *
     * Esto significa que no debería permitirse
     * almacenar dos clientes con el mismo email.
     *
     * Por ejemplo:
     *
     * Cliente 1:
     * ana@ejemplo.com
     *
     * Cliente 2:
     * ana@ejemplo.com
     *
     * Si PostgreSQL tiene una restricción UNIQUE
     * sobre email, la segunda inserción fallará.
     *
     * IMPORTANTE:
     *
     * La anotación unique = true describe
     * la restricción esperada en el mapeo.
     *
     * Con hbm2ddl.auto = validate, Hibernate
     * no crea automáticamente esa restricción.
     *
     * Debe existir en el esquema de PostgreSQL
     * si queremos garantizar la unicidad.
     *
     * Además, unique = true no comprueba
     * que el correo tenga un formato válido.
     */
    @Column(
            name = "precio",
            nullable = false,
            length = 10
    )
    private double precio;

    /*
     * ========================================================
     * ATRIBUTO activo
     * ========================================================
     *
     * Representa el estado del cliente.
     *
     * true:
     *     El cliente está activo.
     *
     * false:
     *     El cliente está inactivo.
     *
     * --------------------------------------------------------
     * @Column(name = "activo")
     * --------------------------------------------------------
     *
     * Relaciona este atributo con la columna
     * activo de PostgreSQL.
     *
     * --------------------------------------------------------
     * nullable = false
     * --------------------------------------------------------
     *
     * Indica que la columna no debería admitir NULL.
     *
     * --------------------------------------------------------
     * boolean
     * --------------------------------------------------------
     *
     * Es un tipo primitivo de Java.
     *
     * Solamente puede contener true o false.
     *
     * A diferencia de Boolean, no puede contener null.
     *
     * --------------------------------------------------------
     * = true
     * --------------------------------------------------------
     *
     * Inicializamos el atributo con el valor true.
     *
     * Esto significa que cuando creamos un objeto
     * utilizando el constructor vacío, inicialmente
     * tendrá activo = true.
     *
     * IMPORTANTE:
     *
     * Este valor inicial pertenece a Java.
     *
     * No significa que estemos definiendo
     * un DEFAULT TRUE en PostgreSQL.
     *
     * Además, el constructor con parámetros
     * puede sustituir este valor inicial.
     */
    @Column(name = "stock", nullable = false)
    private int stock;

    /*
     * ========================================================
     * CONSTRUCTOR VACÍO
     * ========================================================
     *
     * JPA necesita que las entidades tengan
     * un constructor sin argumentos.
     *
     * Puede ser public o protected.
     *
     * En nuestro caso utilizamos protected.
     *
     * --------------------------------------------------------
     * ¿POR QUÉ LO NECESITA HIBERNATE?
     * --------------------------------------------------------
     *
     * Cuando Hibernate recupera registros
     * de PostgreSQL, necesita poder construir
     * instancias de ClienteEntidad.
     *
     * Para ello puede utilizar este constructor
     * y después establecer los valores de los atributos.
     *
     * --------------------------------------------------------
     * ¿QUÉ SIGNIFICA protected?
     * --------------------------------------------------------
     *
     * Permite utilizar el constructor desde:
     *
     * - La propia clase.
     * - Clases del mismo paquete.
     * - Subclases, según las reglas de Java.
     *
     * No es accesible directamente desde
     * cualquier clase de otro paquete.
     *
     * Por ejemplo, desde app.PruebaClienteDAOOrm
     * no podríamos escribir:
     *
     * new ClienteEntidad();
     *
     * porque ese constructor es protected.
     *
     * Sin embargo, Hibernate puede utilizarlo
     * para crear instancias de la entidad.
     *
     * --------------------------------------------------------
     * ¿POR QUÉ ESTÁ VACÍO?
     * --------------------------------------------------------
     *
     * Porque no necesitamos recibir datos
     * cuando se utiliza este constructor.
     *
     * Los atributos tendrán inicialmente
     * sus valores predeterminados:
     *
     * id     -> null
     * nombre -> null
     * email  -> null
     * activo -> true
     *
     * Después Hibernate puede establecer
     * los valores recuperados de PostgreSQL.
     */
    protected ClienteEntidad() {

    }

    /*
     * ========================================================
     * CONSTRUCTOR CON PARÁMETROS
     * ========================================================
     *
     * Este constructor permite crear un objeto
     * ClienteEntidad proporcionando sus datos.
     *
     * Su declaración es:
     *
     * public ClienteEntidad(
     *     Integer id,
     *     boolean activo,
     *     String email,
     *     String nombre
     * )
     *
     * --------------------------------------------------------
     * ¿PARA QUÉ SIRVE?
     * --------------------------------------------------------
     *
     * Nos permite crear objetos que posteriormente
     * podremos utilizar en nuestros DAO.
     *
     * Por ejemplo:
     *
     * ClienteEntidad nuevo = new ClienteEntidad(
     *     null,
     *     true,
     *     "ana@ejemplo.com",
     *     "Ana"
     * );
     *
     * --------------------------------------------------------
     * ¿POR QUÉ PASAMOS null COMO ID?
     * --------------------------------------------------------
     *
     * Porque el identificador de un cliente nuevo
     * será generado por PostgreSQL.
     *
     * --------------------------------------------------------
     * ¿QUÉ SIGNIFICA this?
     * --------------------------------------------------------
     *
     * this hace referencia al objeto actual.
     *
     * Por ejemplo:
     *
     * this.nombre = nombre;
     *
     * A la izquierda:
     *
     * this.nombre
     *     Es el atributo del objeto.
     *
     * A la derecha:
     *
     * nombre
     *     Es el parámetro recibido.
     *
     * La instrucción copia el valor del parámetro
     * al atributo correspondiente.
     *
     * IMPORTANTE:
     *
     * Este constructor no guarda automáticamente
     * el cliente en PostgreSQL.
     *
     * Solamente crea un objeto Java en memoria.
     *
     * Para persistirlo necesitaremos utilizar
     * EntityManager.persist() dentro de una transacción.
     */
    public ClienteEntidad(
            Integer id,
            boolean activo,
            String email,
            String nombre
    ) {

        // Asigna el identificador recibido.
        this.id = id;

        // Asigna el estado activo o inactivo.
        this.activo = activo;

        // Asigna el correo electrónico.
        this.email = email;

        // Asigna el nombre del cliente.
        this.nombre = nombre;
    }

    /*
     * ========================================================
     * MÉTODO getId()
     * ========================================================
     *
     * Es un método getter.
     *
     * Permite consultar el identificador del cliente.
     *
     * public:
     *     Puede utilizarse desde otras clases.
     *
     * Integer:
     *     Tipo de valor que devuelve.
     *
     * return id:
     *     Devuelve el identificador almacenado.
     *
     * Por ejemplo:
     *
     * Integer identificador = cliente.getId();
     *
     * Si el cliente todavía no tiene un ID asignado,
     * este método puede devolver null.
     */
    public Integer getId() {
        return id;
    }

    /*
     * ========================================================
     * MÉTODO setId()
     * ========================================================
     *
     * Es un método setter.
     *
     * Permite asignar un valor al atributo id.
     *
     * Recibe:
     *
     * Integer id
     *
     * No devuelve ningún valor porque su tipo
     * de retorno es void.
     *
     * IMPORTANTE:
     *
     * Aunque existe este setter, no debemos cambiar
     * arbitrariamente el identificador de una entidad
     * que ya está gestionada por Hibernate.
     *
     * La identidad de una entidad debe mantenerse
     * estable durante su ciclo de vida.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /*
     * ========================================================
     * MÉTODO isActivo()
     * ========================================================
     *
     * Es un getter para el atributo boolean activo.
     *
     * En Java es habitual utilizar el prefijo is
     * para consultar propiedades booleanas.
     *
     * Por ejemplo:
     *
     * cliente.isActivo()
     *
     * Puede devolver:
     *
     * true
     * false
     *
     * Este método no modifica el estado del cliente.
     *
     * Solamente devuelve el valor actual.
     */
    public boolean isActivo() {
        return activo;
    }

    /*
     * ========================================================
     * MÉTODO setActivo()
     * ========================================================
     *
     * Permite modificar el estado activo del cliente.
     *
     * Por ejemplo:
     *
     * cliente.setActivo(false);
     *
     * El objeto pasará a tener:
     *
     * activo = false
     *
     * IMPORTANTE:
     *
     * Modificar un atributo Java no siempre provoca
     * inmediatamente una consulta SQL.
     *
     * Si la entidad está gestionada por Hibernate
     * dentro de un contexto de persistencia,
     * el proveedor puede detectar el cambio
     * y sincronizarlo con PostgreSQL.
     *
     * Esta técnica se conoce como dirty checking.
     *
     * Si la entidad está separada (detached),
     * modificarla no actualiza automáticamente
     * la base de datos.
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /*
     * ========================================================
     * MÉTODO getEmail()
     * ========================================================
     *
     * Devuelve el correo electrónico del cliente.
     *
     * Por ejemplo:
     *
     * String correo = cliente.getEmail();
     *
     * Si el objeto todavía no tiene un correo
     * asignado, podría devolver null.
     */
    public String getEmail() {
        return email;
    }

    /*
     * ========================================================
     * MÉTODO setEmail()
     * ========================================================
     *
     * Permite modificar el correo electrónico.
     *
     * Por ejemplo:
     *
     * cliente.setEmail("nuevo@ejemplo.com");
     *
     * IMPORTANTE:
     *
     * Este método no comprueba:
     *
     * - Si el correo tiene formato válido.
     * - Si supera los 150 caracteres.
     * - Si existe otro cliente con ese correo.
     *
     * Esas comprobaciones deben gestionarse
     * mediante validaciones y restricciones.
     *
     * Si intentamos guardar un correo duplicado,
     * PostgreSQL puede rechazar la operación
     * por la restricción UNIQUE.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * ========================================================
     * MÉTODO getNombre()
     * ========================================================
     *
     * Devuelve el nombre almacenado en el objeto.
     *
     * Por ejemplo:
     *
     * String nombreCliente = cliente.getNombre();
     *
     * No modifica ningún atributo.
     */
    public String getNombre() {
        return nombre;
    }

    /*
     * ========================================================
     * MÉTODO setNombre()
     * ========================================================
     *
     * Permite asignar o modificar el nombre del cliente.
     *
     * Por ejemplo:
     *
     * cliente.setNombre("María");
     *
     * La instrucción modifica el atributo nombre
     * del objeto Java.
     *
     * Si el objeto está gestionado por Hibernate
     * dentro de una transacción, ese cambio
     * podrá sincronizarse con PostgreSQL.
     *
     * Si el objeto está separado del contexto
     * de persistencia, el setter solamente
     * modifica el objeto en memoria.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
