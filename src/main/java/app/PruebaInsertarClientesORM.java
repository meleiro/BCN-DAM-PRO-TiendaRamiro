package app;

import dao.orm.ClienteDAOOrm;
import modelo.orm.ClienteEntidad;

public class PruebaInsertarClientesORM {
    public static void main(String[] args) {
        ClienteDAOOrm clienteDAOOrm = new ClienteDAOOrm();
        ClienteEntidad ce = new ClienteEntidad(null, true, "Pepe2@pepe.com", "Pepe");
        System.out.println("Id de cliente antes de insertar : " + ce.getId());
        clienteDAOOrm.insertar(ce);
        System.out.println("Id de cliente después de insertar: " + ce.getId());

    }
}
