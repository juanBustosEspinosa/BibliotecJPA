package ejemploServelet.ejemploservletweb.Modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class DAOGenerico<T, ID> {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidad-biblioteca");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    Class<T> clase;
    Class<ID> claseID;

    public DAOGenerico(Class<T> clase, Class<ID> claseID){
        this.clase=clase;
        this.claseID=claseID;
    }

    //INSERT
    public boolean add(T objeto){
        tx.begin();
        em.persist(objeto);
        tx.commit();
        return false;
    }

    //SELECT WHERE ID
    public T getById(ID id){
        return em.find(clase, id);
    }

    //SELECT *
    public List<T> getAll(){
        return em.createQuery("SELECT c from "+ clase.getName()+" c").getResultList();
    }

    //UPDATE
    public T update(T objeto){
        tx.begin();
        objeto = em.merge(objeto);
        tx.commit();
        return objeto;
    }
    //DELETE WHERE objeto.id
    public boolean delete(T objeto){
        tx.begin();
        em.remove(objeto);
        tx.commit();
        return true;
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
    @Override
    public String toString() {
        return "DAOGenerico{" +
                "clase=" + clase.getSimpleName() +
                "clase=" + clase.getName() +
                ", claseID=" + claseID +
                '}';
    }
}
