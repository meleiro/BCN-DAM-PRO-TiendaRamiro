package dao.orm;

import conexion.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Function;

public class TransaccionesORM {
    private TransaccionesORM(){

    }

    static <T> T ejecutar(Function<EntityManager, T> accion) {
        try(EntityManager em = JpaUtil.abrir()) {
            EntityTransaction tx = em.getTransaction();

            try{
                tx.begin();
                T resultado= accion.apply(em);
                tx.commit();
                return resultado;
            } catch(RuntimeException ex) {
                if(tx.isActive()) tx.rollback();
                throw ex;
            }

        }
    }
}
