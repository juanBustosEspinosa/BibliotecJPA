package ejemploServelet.ejemploservletweb.Controlado;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ejemploServelet.ejemploservletweb.Modelo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet(name = "PrestamoServlet", value = "/Prestamo-Servlet")
public class PrestamoServlet extends HttpServlet{




    DAOGenerico<Usuario, String> daoUsuario;
    DAOGenerico<Ejemplar, String> daoEjemplar;
    DAOGenerico<Prestamo, Integer> daoPrestamo;
    

    public void init(){
        daoUsuario = new DAOGenerico<>(Usuario.class,String.class);
        daoEjemplar = new DAOGenerico<>(Ejemplar.class,String.class);
        daoPrestamo = new DAOGenerico<>(Prestamo.class,Integer.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Libros</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>login</h1>");
        String operacion = request.getParameter("operacion");



        if (operacion.equals("add")) {
            String dni = request.getParameter("dni");
            String isbn = request.getParameter("isbn");
            Integer id_Ejem = Integer.valueOf(request.getParameter("id_ejem"));
            String jpql = "SELECT u FROM Usuario u WHERE u.dni = ?1";
            String jpqlP = "SELECT e FROM Ejemplar e WHERE e.libro.isbn = ?1 and e.id = ?2";
            Usuario usuario = daoUsuario.findByQuery(jpql, dni);
            String jsqll = "SELECT p FROM Prestamo p WHERE p.usuario.id = ?1";
            List<Prestamo> list = daoPrestamo.findByQueryAll(jsqll,usuario.getId());
            if (list.size() < 3 || usuario.getPenalizacionHasta().isAfter(LocalDate.now())) {

            Ejemplar ejemplar = daoEjemplar.findByQuery(jpqlP, isbn,id_Ejem);
            if (ejemplar.getEstado().equals("Disponible")) {
            ejemplar.setEstado("Prestado");
            daoEjemplar.update(ejemplar);
//            Usuario usuario, Ejemplar ejemplar, LocalDate fechaInicio
            Prestamo prestamo = new Prestamo(usuario,ejemplar,LocalDate.now());
            daoPrestamo.add(prestamo);
            out.println("<p>Se ha creado el Prestamo</p>");
            } else {
                out.println("<p>El prestamo no se pudo realizar</p>");

            }
            } else {
                out.println("<p>El prestamo no se pudo realizar El usuario tiene 3 prestamos </p>");

            }

        } else if (operacion.equals("edit")) {
            Integer id = Integer.valueOf(request.getParameter("id"));
            Prestamo prestamo =daoPrestamo.getById(id);
            String dni = request.getParameter("dni");
            String isbn = request.getParameter("isbn");
            Integer id_Ejem = Integer.valueOf(request.getParameter("id_ejem"));

            LocalDate penalizacionDevolucion = LocalDate.parse(request.getParameter("penalizacionDevolucion"));

            String jpql = "SELECT u FROM Usuario u WHERE u.dni = ?1";
            String jpqlP = "SELECT e FROM Ejemplar e WHERE e.libro.isbn = ?1 and e.id = ?2";

            Usuario usuario = daoUsuario.findByQuery(jpql, dni);
            long diasEntre = ChronoUnit.DAYS.between(prestamo.getFechaInicio(), prestamo.getFechaDevolucion());
            if (diasEntre > 15) {
                usuario.setPenalizacionHasta(LocalDate.now().plusDays(40));
                daoUsuario.update(usuario);
            }
            Ejemplar ejemplar = daoEjemplar.findByQuery(jpqlP, isbn,id_Ejem);
            if (ejemplar.getEstado().equals("Prestado")) {
            prestamo.setEjemplar(ejemplar);
            prestamo.setUsuario(usuario);
            prestamo.setFechaDevolucion(penalizacionDevolucion);
            ejemplar.setEstado("Disponible");
            daoEjemplar.update(ejemplar);
            daoPrestamo.update(prestamo);
            out.println("<p>Se ha Modificado el Prestamo</p>");
            } else {
                out.println("<p>La devolucion no se pudo realizar</p>");

            }



        } else if (operacion.equals("delete")) {

            Integer id = Integer.valueOf(request.getParameter("id"));
            Prestamo prestamo =daoPrestamo.getById(id);
            daoPrestamo.delete(prestamo);
            out.println("<p>Se ha eliminado el Prestamo</p>");
        }


        out.println("</body>");
        out.println("</html>");

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        ObjectMapper conversor = new ObjectMapper();
        conversor.registerModule(new JavaTimeModule()); // Manejo de LocalDate

        String operacion = request.getParameter("operacion");

        if (operacion.equals("listar")) {
            List<Prestamo> prestamos = daoPrestamo.getAll();
            String json_response = conversor.writeValueAsString(prestamos);
            out.println(json_response);
        } else if (operacion.equals("buscar")) {
            String id_Usuario = request.getParameter("id_Usuario");
            String jsql = "SELECT p FROM Prestamo p WHERE p.usuario.id = ?1";
            List<Prestamo> list = daoPrestamo.findByQueryAll(jsql,id_Usuario);
            String json_response = conversor.writeValueAsString(list);
            out.println(json_response);
        }
    }

    public void destroy(){

    }
}
