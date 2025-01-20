package ejemploServelet.ejemploservletweb.Controlado;

import com.fasterxml.jackson.annotation.JsonInclude;
import ejemploServelet.ejemploservletweb.Modelo.DAOGenerico;
import ejemploServelet.ejemploservletweb.Modelo.Libro;
import ejemploServelet.ejemploservletweb.Modelo.Usuario;
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

@WebServlet(name = "UsuarioServlet", value = "/Usuario-Servlet")

public class UsuarioServelet extends HttpServlet {

    DAOGenerico<Usuario, Integer> daoUsuario;
    public void init(){
        daoUsuario = new DAOGenerico<>(Usuario.class,Integer.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String operacion = request.getParameter("operacion");
        PrintWriter out = response.getWriter();

        if(operacion.equals("add")){
            String dni=request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String password = request.getParameter("password");
            String tipo = request.getParameter("tipo");
            String email = request.getParameter("email");


            daoUsuario.add(new Usuario(dni,nombre,email,password,tipo,null));
            out.println("Usuario agregado");
        } else if(operacion.equals("edit")){
            Integer id= Integer.valueOf(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String password = request.getParameter("password");
            String tipo = request.getParameter("tipo");
            LocalDate penalizacionHasta = LocalDate.parse(request.getParameter("penalizacionHasta"));
            String email = request.getParameter("email");
            Usuario usuario = daoUsuario.getById(id);
             usuario.setNombre(nombre);
             usuario.setPassword(password);
             usuario.setTipo(tipo);
             usuario.setPenalizacionHasta(penalizacionHasta);
             usuario.setEmail(email);
             daoUsuario.update(usuario);

            out.println("Usuario Modificado");

        } else if(operacion.equals("delete")){
            Integer id= Integer.valueOf(request.getParameter("id"));
            Usuario usuario = daoUsuario.getById(id);
            daoUsuario.delete(usuario);
            out.println("Usuario Eliminado");

        }
    }



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        ObjectMapper conversor = new ObjectMapper();
        conversor.registerModule(new JavaTimeModule()); // Manejo de LocalDate

        String operacion = request.getParameter("operacion");

        if (operacion.equals("listar")) {
            List<Usuario> listaUsuario = daoUsuario.getAll();

            String json_response = conversor.writeValueAsString(listaUsuario);
            out.println(json_response);
        } else if (operacion.equals("listaUno")) {
            Integer id = Integer.valueOf(request.getParameter("id"));

            Usuario usuario = daoUsuario.getById(id);
            String json_response = conversor.writeValueAsString(usuario);
            out.println(json_response);
        }
    }

    public void destroy(){

    }
}
