package ejemploServelet.ejemploservletweb.Controlado;

import com.fasterxml.jackson.annotation.JsonInclude;
import ejemploServelet.ejemploservletweb.Modelo.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "EjemplarServlet", value = "/Ejemplar-Servlet")

public class EjemplarServlet extends HttpServlet {

    DAOGenerico<Ejemplar, Integer> daoEjemplar;
    DAOGenerico<Libro, Integer> daoLibro;

    public void init(){
        daoEjemplar = new DAOGenerico<>(Ejemplar.class,Integer.class);
        daoLibro = new DAOGenerico<>(Libro.class,Integer.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String operacion = request.getParameter("operacion");
        PrintWriter out = response.getWriter();

        if(operacion.equals("add")){

            String isbn = request.getParameter("isbn");
            String estado = request.getParameter("estado");
            String jsql = "SELECT l FROM Libro l WHERE l.isbn = ?1";
            Libro libro =daoLibro.findByQuery(jsql,isbn);
            if (libro != null){
                Ejemplar ejemplar = new Ejemplar(libro,estado);
                daoEjemplar.add(ejemplar);
                out.println("Se ha agregado el libro");
            } else {
                out.println("No se ha encontrado el ejemplar");
            }

        } else if(operacion.equals("edit")){

            Integer id = Integer.valueOf(request.getParameter("id"));
            String estado = request.getParameter("estado");

                Ejemplar ejemplar = daoEjemplar.getById(id);
                ejemplar.setEstado(estado);
                daoEjemplar.update(ejemplar);
                out.println("Se ha Modifcado el ejemplar");


        } else if(operacion.equals("delete")){
            Integer id = Integer.valueOf(request.getParameter("id"));
            Ejemplar ejemplar = daoEjemplar.getById(id);
            daoEjemplar.delete(ejemplar);
            out.println("Se ha Eliminado el ejemplar");
        }
    }



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        ObjectMapper conversor = new ObjectMapper();
        conversor.registerModule(new JavaTimeModule()); // Manejo de LocalDate

        String operacion = request.getParameter("operacion");

        if (operacion.equals("listar")) {
            List<Ejemplar> lista = daoEjemplar.getAll();
            String json_response = conversor.writeValueAsString(lista);
            out.println(json_response);
        } else if (operacion.equals("listaUno")) {
            String id_Usuario = request.getParameter("id_Usuario");
            String jsql = "SELECT e FROM Ejemplar e WHERE e.libro.id = ?1";
            List<Ejemplar> list = daoEjemplar.findByQueryAll(jsql,id_Usuario);
            String json_response = conversor.writeValueAsString(list);
            out.println(json_response);
        }
    }

    public void destroy(){

    }
}
