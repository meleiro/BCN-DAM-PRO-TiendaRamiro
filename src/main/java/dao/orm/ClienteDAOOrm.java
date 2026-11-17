package dao.orm;

import conexion.JpaUtil;
import jakarta.persistence.EntityManager;
import modelo.Cliente;
import modelo.orm.ClienteEntidad;

import java.util.List;

public class ClienteDAOOrm {
    public ClienteDAOOrm() {
    }

    public List<ClienteEntidad> listar() {
        try (EntityManager em = JpaUtil.abrir()){
            return em.createQuery("SELECT e FROM ClienteEntidad e ORDER BY e.id",
                    ClienteEntidad.class).getResultList();
        }
    }

    public ClienteEntidad buscar(int id){
        try(EntityManager em = JpaUtil.abrir()){
            return em.find(ClienteEntidad.class, id);
        }
    }

    public void insertar(ClienteEntidad nuevo){
        TransaccionesORM.ejecutar( em -> {
            em.persist(nuevo);
            return null;
        });
    }

    public boolean actualizar(ClienteEntidad actualizado){
        return TransaccionesORM.ejecutar( em -> {
            ClienteEntidad gestionado = em.find(ClienteEntidad.class, actualizado.getId());

            if (gestionado == null){
                return false;
            }

            gestionado.setNombre(actualizado.getNombre());
            gestionado.setEmail(actualizado.getEmail());
            gestionado.setActivo(actualizado.isActivo());

            return true;


        });
    }

    public boolean eliminar(int id) {
        return TransaccionesORM.ejecutar( em -> {
            ClienteEntidad gestionado = em.find(ClienteEntidad.class, id);
            if (gestionado == null){
                return false;
            }

            em.remove(gestionado);

            return true;
        });
    }
}
