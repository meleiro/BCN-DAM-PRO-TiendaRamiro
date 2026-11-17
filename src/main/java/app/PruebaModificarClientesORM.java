package app;

import dao.orm.ClienteDAOOrm;
import modelo.orm.ClienteEntidad;

public class PruebaModificarClientesORM {
    public static void main(String[] args) {
        ClienteDAOOrm clienteDAOOrm = new ClienteDAOOrm();
        ClienteEntidad ce = new ClienteEntidad(1, true, "ANAr@YAHOO.ES", "Ana R");
        clienteDAOOrm.actualizar(ce);

    }
}
