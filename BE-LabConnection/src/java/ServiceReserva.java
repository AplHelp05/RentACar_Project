/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Control.Control;
import Datos.GlobalException;
import Entidades.Agencia;
import Entidades.Cliente;
import Entidades.Reserva;
import Entidades.Vehiculo;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
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
 * @author Usuario1
 */
@WebServlet(urlPatterns = {"/ServiceReserva"})
public class ServiceReserva extends HttpServlet {
    private Control control = new Control();
    private String reservasJsonString;
    ArrayList<Reserva> reservas;
    private String agenciasJsonString;
    ArrayList<Agencia> agencias;
    private String clientesJsonString;
    ArrayList<Cliente> clientes;
    private String vehiculosJsonString;
    ArrayList<Vehiculo> vehiculos;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        response.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        // Adding new elements to the ArrayList
        String opcion = (String) request.getParameter("opc");
        switch(Integer.parseInt(opcion)){
            case 1://listar
                try {
                    reservas = getListReservas();
                } catch (Exception ex) {
                    Logger.getLogger(ServiceReserva.class.getName()).log(Level.SEVERE, null, ex);
                }
                reservasJsonString = gson.toJson(reservas);
                try {
                    out.println(reservasJsonString);
                } finally {
                    out.close();
                }
                break;
            case 2://ingresar
                Reserva v = null;
                try{
                    v.setCodigo(request.getParameter("codigo"));
                    v.setAgencia(buscarAgencia(getListAgencias(), request.getParameter("agencia")));
                    v.setCliente(buscarCliente(getListClientes(), request.getParameter("cliente")));
                    v.setVehiculo(buscarVehiculo(getListVehiculos(), request.getParameter("vehiculo")));
                    String finicio=request.getParameter("fechainicio");
                    Date dateini = Date.valueOf(finicio);
                    v.setFecha_inicio(dateini);
                    String ffinal=request.getParameter("fechafinal");
                    Date datefini = Date.valueOf(ffinal);
                    v.setFecha_final(datefini);
                    if(ingresarReserva(v)){
                        try{
                            reservas = getListReservas();
                        }catch(Exception ex){
                            Logger.getLogger(ServiceReserva.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        reservasJsonString = gson.toJson(reservas);
                        try{
                            out.println(reservasJsonString);
                        }finally{
                            out.close();
                        }
                    }else{
                        out.println("Error al agregar");
                    }
                }catch(Exception e){
                    System.out.println("Error "+e);
                }
                break;
            case 3://eliminar
                try {
                    String codEliminar = request.getParameter("codigo");
                    if(eliminarReserva(codEliminar)){
                          out.println("Reserva eliminada");
                    }else{
                          out.println("Error al eliminar");              
                    }
                }catch(Exception e){
                    System.out.println(""+e);
                    Logger.getLogger(ServiceReserva.class.getName()).log(Level.SEVERE, null, e);
                }
                break;
            case 4://modificar
                try{
                    String codigoEditar = request.getParameter("codigo");
                    reservas = getListReservas();
                    Reserva reservaEditar = null; 
                    if(reservaEditar == null){
                        if(buscarReserva(reservas, codigoEditar) != null){
                            reservaEditar = buscarReserva(reservas, codigoEditar);
                        }
                    }else{
                        reservaEditar = new Reserva();
                        reservaEditar.setCodigo(request.getParameter("codigo"));
                        reservaEditar.setAgencia(buscarAgencia(getListAgencias(), request.getParameter("agencia")));
                        reservaEditar.setCliente(buscarCliente(getListClientes(), request.getParameter("cliente")));
                        reservaEditar.setVehiculo(buscarVehiculo(getListVehiculos(), request.getParameter("vehiculo")));
                        String finicio=request.getParameter("fechainicio");
                        Date dateini = Date.valueOf(finicio);
                        reservaEditar.setFecha_inicio(dateini);
                        String ffinal=request.getParameter("fechafinal");
                        Date datefini = Date.valueOf(ffinal);
                        reservaEditar.setFecha_final(datefini);
                        if(modificarReserva(reservaEditar)){
                            try{
                                reservas = getListReservas();
                            }catch(Exception ex){
                                Logger.getLogger(ServiceReserva.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            reservasJsonString = gson.toJson(reservas);
                            try{
                                out.println(reservasJsonString);
                            }finally{
                                out.close();
                            }
                        }else{
                            out.println("Error al editar");
                        }
                    }
                }catch(Exception e){
                    Logger.getLogger(ServiceReserva.class.getName()).log(Level.SEVERE, null, e);
                }
                break;
            case 5://agencias
                try{
                    agencias = getListAgencias();
                }catch(Exception ex){
                    Logger.getLogger(Reserva.class.getName()).log(Level.SEVERE, null, ex);
                }
                agenciasJsonString = gson.toJson(agencias);
                try{
                    out.println(agenciasJsonString);
                }finally{
                    out.close();
                }
                break;
            case 6://clientes
                try{
                    clientes = getListClientes();
                }catch(Exception ex){
                    Logger.getLogger(Reserva.class.getName()).log(Level.SEVERE, null, ex);
                }
                clientesJsonString = gson.toJson(clientes);
                try{
                    out.println(clientesJsonString);
                }finally{
                    out.close();
                }                
                break;
            case 7://vehiculos
                try{
                    vehiculos = getListVehiculos();
                }catch(Exception ex){
                    Logger.getLogger(Reserva.class.getName()).log(Level.SEVERE, null, ex);
                }
                vehiculosJsonString = gson.toJson(vehiculos);
                try{
                    out.println(vehiculosJsonString);
                }finally{
                    out.close();
                }
                break;
        }
        
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

    public boolean ingresarReserva(Reserva r) throws Exception{
        try{          
            control.insertarReserva(r);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    }
    
    public boolean modificarReserva(Reserva r) throws Exception{
        try{          
            control.modificarReserva(r);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    }
    
    public boolean eliminarReserva(String id) throws Exception{
        try{          
            control.eliminarReserva(id);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public Reserva buscarReserva(List<Reserva> reservasList, String codigo){
        for (Reserva reserva : reservasList) {
            if (reserva.getCodigo().equals(codigo)) {
                return reserva;
            }
        }
        return null;
    }
    
    public Agencia buscarAgencia(List<Agencia> agenciasList, String codigo){
        for (Agencia agencia : agenciasList) {
            if (agencia.getCodigo().equals(codigo)) {
                return agencia;
            }
        }
        return null;
    }
    
    public Cliente buscarCliente(List<Cliente> clientesList, String cedula){
        for (Cliente cliente : clientesList) {
            if (cliente.getCedula().equals(cedula)) {
                return cliente;
            }
        }
        return null;
    }
    
    public Vehiculo buscarVehiculo(List<Vehiculo> vehiculosList, String codigo){
        for (Vehiculo vehiculo : vehiculosList) {
            if (vehiculo.getCodigo().equals(codigo)) {
                return vehiculo;
            }
        }
        return null;
    }
    
    public ArrayList<Reserva> getListReservas() throws Exception{
        try{
            return control.listarReservas();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public ArrayList<Agencia> getListAgencias() throws Exception{
        try{
            return control.listarAgencias();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public ArrayList<Cliente> getListClientes() throws Exception{
        try{
            return control.listarClientes();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public ArrayList<Vehiculo> getListVehiculos() throws Exception{
        try{
            return control.listarReservasVehiculos();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
}
