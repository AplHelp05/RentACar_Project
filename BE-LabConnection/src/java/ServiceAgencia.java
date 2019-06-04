/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Control.Control;
import Datos.GlobalException;
import Entidades.Agencia;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author david
 */
@WebServlet(urlPatterns = {"/ServiceAgencia"})
public class ServiceAgencia extends HttpServlet {
    
    private Control control = new Control();
    private String agenciasJsonString;
    ArrayList<Agencia> agencias;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();
        PrintWriter out = resp.getWriter();
        String opcion = (String) req.getParameter("opc");
        switch (Integer.parseInt(opcion)) {
            //Listar Agencias
            case 1:
            try {
                agencias = getListAgencias();
            } catch (Exception ex) {
                Logger.getLogger(ServiceAgencia.class.getName()).log(Level.SEVERE, null, ex);
            }        
                agenciasJsonString = gson.toJson(agencias);
                try {
                    out.println(agenciasJsonString);
                } finally {
                    out.close();
                }
                break;
            //Agregar Agencia
            case 2:
                Agencia a = null;
                try {
                    a = new Agencia();
                    a.setCodigo(req.getParameter("codigo"));
                    a.setNombre(req.getParameter("nombre"));
                    a.setTelefono(req.getParameter("telefono"));
                    a.setEmail(req.getParameter("email"));
                    a.setUbicacion(req.getParameter("ubicacion"));
                    a.setHorario(req.getParameter("horario"));                    
                     if(ingresarAgencia(a)){
                                    try {
                                        agencias = getListAgencias();
                                        } catch (Exception ex) {
                                            Logger.getLogger(ServiceAgencia.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                            agenciasJsonString = gson.toJson(agencias);
                                            try {
                                                out.println(agenciasJsonString);
                                            } finally {
                                                out.close();
                                            }                           
                    }else{
                         out.println("Error al agregar");                      
                    }
                }catch(Exception e){
                    System.out.println("Error "+e);
                }

                break;
            //Elimina Agencia
            case 3:
                try {
                    String codEliminar = req.getParameter("codigo");
                if(eliminarAgencia(codEliminar)){
                      out.println("agencia eliminada");
                }else{
                      out.println("error al eliminar");              
                }
                
            }catch(Exception e){
                System.out.println(""+e);
                Logger.getLogger(Agencia.class.getName()).log(Level.SEVERE, null, e);
            }
                break;
            case 4:
            try{
                String codigoEditar = req.getParameter("codigo");
                String nombreEditar = req.getParameter("nombre");
                agencias = getListAgencias();
                Agencia agenciaEditar = null;
                if(nombreEditar == null){
                    if(buscarAgencia(agencias, codigoEditar) != null){
                        agenciaEditar = buscarAgencia(agencias, codigoEditar);
                    }
                }else{
                    agenciaEditar = new Agencia();
                    agenciaEditar.setCodigo(req.getParameter("codigo"));
                    agenciaEditar.setNombre(req.getParameter("nombre"));
                    agenciaEditar.setTelefono(req.getParameter("telefono"));
                    agenciaEditar.setEmail(req.getParameter("email"));
                    agenciaEditar.setUbicacion(req.getParameter("ubicacion"));
                    agenciaEditar.setHorario(req.getParameter("horario"));      
                    
                    if(modificarAgencia(agenciaEditar)){
                         try {
                                        agencias = getListAgencias();
                                        } catch (Exception ex) {
                                            Logger.getLogger(ServiceAgencia.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                            agenciasJsonString = gson.toJson(agencias);
                                            try {
                                                out.println(agenciasJsonString);
                                            } finally {
                                                out.close();
                                            }                           
                        }else{
                             out.println("Error al editar");                      
                        }
                    }
                }catch(Exception e){
                    System.out.println(""+e);
                    Logger.getLogger(Agencia.class.getName()).log(Level.SEVERE, null, e);
                }
                    break;
        }

    }

    private void response(HttpServletResponse resp, String msg)
            throws IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public ArrayList<Agencia> getListAgencias() throws Exception{
        try{
            return control.listarAgencias();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public boolean eliminarAgencia(String id) throws Exception{
        try{          
            control.eliminarAgencia(id);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public boolean ingresarAgencia(Agencia a) throws Exception{
        try{          
            control.insertarAgencia(a);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public Agencia buscarAgencia(List<Agencia> agenciaList, String agenciaCodigo){
        for (Agencia agencia : agenciaList) {
            if (agencia.getCodigo().equals(agenciaCodigo)) {
                return agencia;
            }
        }
        return null;
    }
    
    public boolean modificarAgencia(Agencia a) throws Exception{
        try{          
            control.modificarAgencia(a);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    }  
}
