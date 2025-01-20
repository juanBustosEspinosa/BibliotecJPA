package ejemploServelet.ejemploservletweb.Controlado;


import ejemploServelet.ejemploservletweb.Modelo.DAOGenerico;
import ejemploServelet.ejemploservletweb.Modelo.Libro;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejemploServelet.ejemploservletweb.Modelo.Usuario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "loginServlet", value = "/login-Servlet")
public class loginServlet extends HttpServlet{




    DAOGenerico<Usuario, String> daoUsuario;

    public void init(){
        daoUsuario = new DAOGenerico<>(Usuario.class,String.class);
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

        String nombre = request.getParameter("nombre");
        String password = request.getParameter("password");

        String jpql = "SELECT u FROM Usuario u WHERE u.nombre = ?1 AND u.password = ?2";

        Usuario usuario = daoUsuario.findByQuery(jpql,nombre,password);
        if(usuario != null) {
            out.println("<h2> El usuario esta </h2>");
        } else {
            out.println("<h2>Usuario no encontrado</h2>");
        }



        out.println("</body>");
        out.println("</html>");

    }




    public void destroy(){

    }
}
