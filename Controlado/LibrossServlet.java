package ejemploServelet.ejemploservletweb.Controlado;

import ejemploServelet.ejemploservletweb.Modelo.DAOGenerico;
import ejemploServelet.ejemploservletweb.Modelo.Libro;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LibrossServlet", value = "/Libross-Servlet")
public class LibrossServlet extends HttpServlet{




    DAOGenerico<Libro, String> daolibro;

    public void init(){
        daolibro = new DAOGenerico<>(Libro.class,String.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String operacion = request.getParameter("operacion");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Libros</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Libros</h1>");
        if(operacion.equals("add")){

            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo");
            String autor = request.getParameter("autor");
            Libro libro = new Libro(isbn,titulo,autor);
            daolibro.add(libro);
            out.println("<p>Añadio perfectamente</p>");
        } else if(operacion.equals("modificar")){
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo");
            String autor = request.getParameter("autor");
            Libro libro = new Libro(isbn,titulo,autor);
            daolibro.update(libro);
            out.println("<p>actualizo perfectamente</p>");

        } else if (operacion.equals("eliminar")) {

            String isbn = request.getParameter("isbn");
            Libro libro =daolibro.getById(isbn);
            daolibro.delete(libro);
            out.println("<p>Elimino perfectamente</p>");

        }else {
            out.println("<p>Datos mal puestos>");
        }


    }



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter impresora = response.getWriter();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Libros</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Libros</h1>");
        String operacion = request.getParameter("operacion");

        if (operacion.equals("todos")) {
            List<Libro> libros = daolibro.getAll();
            for (Libro libro : libros) {
                out.println("<li>");
                out.println("<p>" + libro.getTitulo() + "</p>");
                out.println("<p>" + libro.getAutor() + "</p>");
                out.println("<p>" + libro.getIsbn() + "</p>");
                out.println("</li>");

            }
        } else if (operacion.equals("uno")) {
            String id = request.getParameter("id");
            Libro libro = daolibro.getById(id);
            out.println("<p>" + libro.getTitulo() + "</p>");
            out.println("<p>" + libro.getAutor() + "</p>");
            out.println("<p>" + libro.getIsbn() + "</p>");
        }
        out.println("</body>");
        out.println("</html>");

    }

    public void destroy(){

    }
}
