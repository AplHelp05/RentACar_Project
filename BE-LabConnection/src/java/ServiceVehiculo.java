/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Control.Control;
import Datos.GlobalException;
import Entidades.Clasificacion;
import Entidades.Marca;
import Entidades.Transmision;
import Entidades.Vehiculo;
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
@WebServlet(urlPatterns = {"/ServiceVehiculo"})
public class ServiceVehiculo extends HttpServlet {

    private Control control = new Control();
    private String vehiculosJsonString;
    ArrayList<Vehiculo> vehiculos;
    //
    ArrayList<Clasificacion> clasificaciones;
    private String clasificacionesJsonString;
    //
    ArrayList<Transmision> transmisiones;
    private String transmisionesJsonString;
    //
    ArrayList<Marca> marcas;
    private String marcasJsonString;

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
                vehiculos = getListVehiculos();
            } catch (Exception ex) {
                Logger.getLogger(ServiceCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        
                vehiculosJsonString = gson.toJson(vehiculos);
                try {
                    out.println(vehiculosJsonString);
                } finally {
                    out.close();
                }
                break;
            //Agregar Cliente
            case 2:
                Vehiculo v = null;
                try {
                    v = new Vehiculo();
                    v.setCodigo(req.getParameter("codigo"));
                    v.setClasificacion(buscarClasificacion(getListClasificaciones(),req.getParameter("clasificacion")));
                    v.setTransmision(buscarTransmision(getListTransmisiones(),req.getParameter("transmision")));
                    v.setMarca(buscarMarca(getListMarcas(),req.getParameter("marca")));
                    v.setCapacidad(Integer.parseInt(req.getParameter("capacidad")));
                    v.setPlaca(req.getParameter("placa"));
                    v.setDescripcion(req.getParameter("descripcion"));
                    v.setPrecio(Integer.parseInt(req.getParameter("precio")));
                    
                     if(ingresarVehiculo(v)){
                        try {
                            vehiculos = getListVehiculos();
                            } catch (Exception ex) {
                                Logger.getLogger(ServiceVehiculo.class.getName()).log(Level.SEVERE, null, ex);
                            }

                                vehiculosJsonString = gson.toJson(vehiculos);
                                try {
                                    out.println(vehiculosJsonString);
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
                    String codEliminar = req.getParameter("codigo");
                if(eliminarVehiculo(codEliminar)){
                      out.println("vehiculo eliminado");
                }else{
                      out.println("error al eliminar");              
                }
                
            }catch(Exception e){
                System.out.println(""+e);
                Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, e);
            }
                break;
            case 4:
            try{
                String codigoEditar = req.getParameter("codigo");
                String placaEditar = req.getParameter("placa");
                vehiculos = getListVehiculos();
                Vehiculo vehiculoEditar = null;
                if(placaEditar == null){
                    if(buscarVehiculo(vehiculos, codigoEditar) != null){
                        vehiculoEditar = buscarVehiculo(vehiculos, codigoEditar);
                    }
                }else{
                    
                    vehiculoEditar = new Vehiculo();
                    vehiculoEditar.setCodigo(req.getParameter("codigo"));
                    vehiculoEditar.setClasificacion(buscarClasificacion(getListClasificaciones(),req.getParameter("clasificacion")));
                    vehiculoEditar.setTransmision(buscarTransmision(getListTransmisiones(),req.getParameter("transmision")));
                    vehiculoEditar.setMarca(buscarMarca(getListMarcas(),req.getParameter("marca")));
                    vehiculoEditar.setCapacidad(Integer.parseInt(req.getParameter("capacidad")));
                    vehiculoEditar.setPlaca(req.getParameter("placa"));
                    vehiculoEditar.setDescripcion(req.getParameter("descripcion"));
                    vehiculoEditar.setPrecio(Integer.parseInt(req.getParameter("precio")));
                    
                    if(modificarVehiculo(vehiculoEditar)){
                         try {
                                        vehiculos = getListVehiculos();
                                        } catch (Exception ex) {
                                            Logger.getLogger(ServiceVehiculo.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                            vehiculosJsonString = gson.toJson(vehiculos);
                                            try {
                                                out.println(vehiculosJsonString);
                                            } finally {
                                                out.close();
                                            }                           
                        }else{
                             out.println("Error al editar");                      
                        }
                    }
                }catch(Exception e){
                    System.out.println(""+e);
                    Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, e);
                }
                    break;
            case 5:
                try{
                    clasificaciones = getListClasificaciones();
                }catch(Exception e){
                    Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, e);
                }
                clasificacionesJsonString = gson.toJson(clasificaciones);
                try{
                    out.println(clasificacionesJsonString);
                }finally{
                    out.close();
                }
                break;
            case 6:
                try{
                    transmisiones = getListTransmisiones();
                }catch(Exception e){
                    Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, e);
                }
                transmisionesJsonString = gson.toJson(transmisiones);
                try{
                    out.println(transmisionesJsonString);
                }finally{
                    out.close();
                }
                break;
            case 7:
                try{
                    marcas = getListMarcas();
                }catch(Exception e){
                    Logger.getLogger(Vehiculo.class.getName()).log(Level.SEVERE, null, e);
                }
                marcasJsonString = gson.toJson(marcas);
                try{
                    out.println(marcasJsonString);
                }finally{
                    out.close();
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

    public ArrayList<Vehiculo> getListVehiculos() throws Exception{
        try{
            return control.listarVehiculos();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public ArrayList<Clasificacion> getListClasificaciones() throws Exception{
        try{
            return control.listarClasificaciones();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
        
    public ArrayList<Marca> getListMarcas() throws Exception{
        try{
            return control.listarMarcas();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
        
    public ArrayList<Transmision> getListTransmisiones() throws Exception{
        try{
            return control.listarTrasmisiones();      
        }catch(GlobalException ex){
            System.out.println(""+ex);
            return null;
        }
    }
    
    public boolean eliminarVehiculo(String id) throws Exception{
        try{          
            control.eliminarVehiculo(id);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public boolean ingresarVehiculo(Vehiculo v) throws Exception{
        try{          
            control.insertarVehiculo(v);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
    public Vehiculo buscarVehiculo(List<Vehiculo> vehiculosList, String codigo){
        for (Vehiculo vehiculo : vehiculosList) {
            if (vehiculo.getCodigo().equals(codigo)) {
                return vehiculo;
            }
        }
        return null;
    }
    
    public Clasificacion buscarClasificacion(List<Clasificacion> clasificacionList, String codigo){
        for (Clasificacion clasificacion : clasificacionList) {
            if (clasificacion.getCodigo().equals(codigo)) {
                return clasificacion;
            }
        }
        return null;
    }
        
     public Transmision buscarTransmision(List<Transmision> transmisionList, String codigo){
        for (Transmision transmision : transmisionList) {
            if (transmision.getCodigo().equals(codigo)) {
                return transmision;
            }
        }
        return null;
    }

             
    public Marca buscarMarca(List<Marca> marcasList, String codigo){
        for (Marca marca : marcasList) {
            if (marca.getCodigo().equals(codigo)) {
                return marca;
            }
        }
        return null;
    }
    
        
    public boolean modificarVehiculo(Vehiculo v) throws Exception{
        try{          
            control.modificarVehiculo(v);    
            return true;
        }
        catch(GlobalException | SQLException ex){
            return false;
        }
    } 
    
}