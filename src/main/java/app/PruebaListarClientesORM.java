package app;

import dao.orm.ClienteDAOOrm;
import modelo.orm.ClienteEntidad;

import java.util.List;
import java.util.Scanner;

public class PruebaListarClientesORM {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        ClienteDAOOrm clienteDAOOrm = new ClienteDAOOrm();

        List<ClienteEntidad> clientes = clienteDAOOrm.listar();

        if(clientes.isEmpty()){
            System.out.println("No hau clientes");
        } else{
            for (ClienteEntidad cliente: clientes){
                System.out.println("ID: " + cliente.getId());
                System.out.println("Nombre: " + cliente.getNombre());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("Activo: " + cliente.isActivo());
            }
        }

        System.out.println("Introduce un id a buscar: ");
        int id = sc.nextInt();
        ClienteEntidad cliente2 = clienteDAOOrm.buscar(id);

        if(cliente2 != null) {
            System.out.println("ID: " + cliente2.getId());
            System.out.println("Nombre: " + cliente2.getNombre());
            System.out.println("Email: " + cliente2.getEmail());
            System.out.println("Activo: " + cliente2.isActivo());
        } else {
            System.out.println("no existe el cliente con el id " + id );
        }
    }
}
