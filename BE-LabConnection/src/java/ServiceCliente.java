/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Control.Control;
import Datos.GlobalException;
import Entidades.Cliente;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author david
 */
@WebServlet(urlPatterns = {"/ServiceCliente"})
public class ServiceCliente extends HttpServlet {
    
    private Control control = new Control();
    private String clientesJsonString;
    ArrayList<Cliente> clientes;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     *
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
        // Adding new elements to the ArrayList
        String opcion = (String) req.getParameter("opc");
        switch (Integer.parseInt(opcion)) {
            //Listar estudiantes
            case 1:
            try {
                clientes = getListClientes();
            } catch (Exception ex) {
                Logger.getLogger(ServiceCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        
                clientesJsonString = gson.toJson(clientes);
                try {
                    out.println(clientesJsonString);
                } finally {
                    out.close();
                }
                break;
            //Agregar Cliente
            case 2:
                Cliente c = null;
                try {
                    c = new Cliente();
                    c.setCedula(req.getParameter("cedula"));
                    c.setNombre(req.getParameter("nombre"));
                    c.setTelefono(req.getParameter("telefono"));
                    c.setEmail(req.getParameter("email"));
                    
                     if(ingresarCliente(c)){
                                    try {
                                        clientes = getListClientes();
                                        } catch (Exception ex) {
                                            Logger.getLogger(ServiceCliente.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                            clientesJsonString = gson.toJson(clientes);
                                            try {
                                                out.println(clientesJsonString);
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
            //Elimina cliente
            case 3:
                try {
                    String cedEliminar = req.getParameter("cedula");
                if(eliminarCliente(cedEliminar)){
                      out.println("cliente eliminado");
                }else{
                      out.println("error al eliminar");              
                }
                
            }catch(Exception e){
                System.out.println(""+e);
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
            }
                break;
            case 4:
            try{
                String cedulaEditar = req.getParameter("cedula");
                String nombreEditar = req.getParameter("nombre");
                clientes = getListClientes();
                Cliente clienteEditar = null;
                if(nombreEditar == null){
                    if(buscarCliente(clientes, cedulaEditar) != null){
                        clienteEditar = buscarCliente(clientes, cedulaEditar);
                    }
                }else{
                    clienteEditar = new Cliente();
                    clienteEditar.setCedula(req.getParameter("cedula"));
                    clienteEditar.setNombre(req.getParameter("nombre"));
                    clienteEditar.setTelefono(req.getParameter("telefono"));
                    clienteEditar.setEmail(req.getParameter("email"));
                    
                    if(modificarCliente(clienteEditar)){
                         try {
                                        clientes = getListClientes();
                                        } catch (Exception ex) {
                                            Logger.getLogger(ServiceCliente.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                            clientesJsonString = gson.toJson(clientes);
                                            try {
                                                out.println(clientesJsonString);
                                            } finally {
                                                out.close();
                                            }                           
                        }else{
                             out.println("Error al editar");                      
                        }
                    }
                }catch(Exception e){
                    System.out.println(""+e);
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
                }
                    break;
            case 5:
                
//                estudiantesJsonString = gson.toJson(estudiantes.get(estudiantes.size() - 1));
//                try {
//                    out.println(estudiantesJsonString);
//                } finally {
//                    out.close();
//                }
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

    public ArrayList<Cliente> getListClientes() throws Exception{
        try{
            return control.listarClientes();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public boolean eliminarCliente(String id) throws Exception{
        try{          
            control.eliminarCliente(id);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public boolean ingresarCliente(Cliente c) throws Exception{
        try{          
            control.insertarCliente(c);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public Cliente buscarCliente(List<Cliente> clienteList, String clienteCedula){
        for (Cliente cliente : clienteList) {
            if (cliente.getCedula().equals(clienteCedula)) {
                return cliente;
            }
        }
        return null;
    }
    
    public boolean modificarCliente(Cliente c) throws Exception{
        try{          
            control.modificarCliente(c);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
}
