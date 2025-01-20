package ejemploServelet.ejemploservletweb.Modelo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class DAOGen<T> {


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion_equipos");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    Class<T> clase;

    public DAOGen(Class<T> clase) {

        this.clase = clase;
    }


    public void addClase(T t) {
        tx.begin();
        em.persist(t);
        tx.commit();
    }

    public void updateClase(T t) {
        tx.begin();
        em.merge(t);
        tx.commit();
    }

    public void removeClase(T t) {
        tx.begin();
        em.remove(t);
        tx.commit();
    }
    public void removeClaseId(int id) {
        T objeto = em.find(clase,id);
        tx.begin();
        em.remove(objeto);
        tx.commit();
    }

    public T getClaseId(int id) {
        return em.find(clase,id);
    }
    public T getClase(String nombre) {
        return em.find(clase,nombre);
    }

    public List<T> getAllClase() {
        String name = clase.getName();
        return em.createQuery("select e From "+ name+ " e",clase).getResultList();
    }


    public T findByQuery(String jpql, Object... params) {
        TypedQuery<T> query = em.createQuery(jpql, clase);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]); // Usar índices para parámetros posicionales
        }
        try {
            return (T) query.getSingleResult(); // Retorna solo un resultado
        } catch (NoResultException e) {
            return null; // Retorna null si no hay resultados
        }
    }

    public List<T> findByQueryAll(String jpql, Object... params) {
        TypedQuery<T> query = em.createQuery(jpql, clase);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]); // Usar índices para parámetros posicionales
        }
        try {
            return query.getResultList(); // Retorna una lista de resultados
        } catch (NoResultException e) {
            return new ArrayList<>(); // Retorna una lista vacía si no hay resultados
        }
    }

}



