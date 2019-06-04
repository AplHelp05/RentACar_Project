/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Entidades.Agencia;
import Entidades.Clasificacion;
import Entidades.Cliente;
import Entidades.Marca;
import Entidades.Reserva;
import Entidades.Transmision;
import Entidades.Vehiculo;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Usuario1
 */
public class ServicioReserva extends Servicio{
    private static final String INSERTARRESERVA = "{call INSERTARRESERVA(?,?,?,?,?,?)}";
    private static final String ELIMINARRESERVA = "{call ELIMINARRESERVA(?)}";  
    private static final String MODIFICARRESERVA = "{call MODIFICARRESERVA(?,?,?,?,?,?)}";
    private static final String LISTARRESERVAS = "{?=call LISTARRESERVAS()}";
    
    private static final String LISTARRESERVASVEHICULOS = "{?=call LISTARVEHICULOS()}"; 
    
    public Marca marca(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }try {
            Marca m= new Marca();
            m.setCodigo(rs.getString("COD_MARCA_VEHICULO"));
            m.setDescripcion(rs.getString("DESCRIPCION_MARCA_VEHICULO"));
            return m;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    public Clasificacion clasificacion(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }try {
            Clasificacion c= new Clasificacion();
            c.setCodigo(rs.getString("COD_TIPO_VEHICULO"));
            c.setDescripcion(rs.getString("DESCRIPCION_TIPO_VEHICULO"));
            return c;
        } catch (SQLException ex) {
            return null;
        }
    }
        
    public Transmision transmision(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }try {
            Transmision t= new Transmision();
            t.setCodigo(rs.getString("COD_TIPO_TRANSMISION"));
            t.setDescripcion(rs.getString("DESCRIPCION_TIPO_TRANSMISION"));
            return t;
        } catch (SQLException ex) {
            return null;
        }
    }
        
    public Vehiculo vehiculo(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        try{
            Vehiculo v = new Vehiculo();
            v.setCodigo(rs.getString("COD_VEHICULO"));
            v.setClasificacion(clasificacion(rs));
            v.setTransmision(transmision(rs));
            v.setMarca(marca(rs));
            v.setCapacidad(rs.getInt("CAPACIDAD_VEHICULO"));
            v.setPlaca(rs.getString("PLACA_VEHICULO"));
            v.setDescripcion(rs.getString("DESCRIPCION_VEHICULO"));
            v.setPrecio(rs.getInt("PRECIO_VEHICULO"));
            
            return v;
        }catch(SQLException e){
            System.out.println("Vehiculo "+e+'\n');
            return null;
        }
    }
    
    public Agencia agencia(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        try{
            Agencia v = new Agencia();
            v.setCodigo(rs.getString("COD_AGENCIA"));
            v.setNombre(rs.getString("NOMBRE_AGENCIA"));
            v.setTelefono(rs.getString("TELEFONO_AGENCIA"));
            v.setEmail(rs.getString("EMAIL_AGENCIA"));
            v.setUbicacion(rs.getString("UBICACION_AGENCIA"));
            v.setHorario(rs.getString("HORARIO_AGENCIA"));
            return v;
        }catch(SQLException e){
            System.out.println("Agencia "+e+'\n');
            return null;
        }
    }
    
    public Cliente cliente(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        try{
            Cliente v = new Cliente();
            v.setCedula(rs.getString("CED_CLIENTE"));
            v.setNombre(rs.getString("NOMBRE_CLIENTE"));
            v.setTelefono(rs.getString("TELEFONO_CLIENTE"));
            v.setEmail(rs.getString("EMAIL_CLIENTE"));
            return v;
        }catch(SQLException e){
            System.out.println("Cliente "+e+'\n');
            return null;
        }
    }
    
    public Reserva reserva(ResultSet rs) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        try{
            Reserva v = new Reserva();
            v.setCodigo(rs.getString("COD_CLIENTE"));
            v.setAgencia(agencia(rs));
            v.setCliente(cliente(rs));
            v.setVehiculo(vehiculo(rs));
            v.setFecha_inicio(rs.getDate("FECHA_INICIO"));
            v.setFecha_final(rs.getDate("FECHA_FINAL"));
            return v;
        }catch(SQLException e){
            System.out.println("Cliente "+e+'\n');
            return null;
        }
    }
    
    public boolean insertarReserva(Reserva reserva) throws GlobalException  	
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            throw new GlobalException("No se ha localizado el driver.");
        } 
        catch (SQLException e) 
        {
            throw new GlobalException("La base de datos no se encuentra disponible.");
        }
        CallableStatement pstmt=null;
        
        try 
        {
            pstmt = conexion.prepareCall(INSERTARRESERVA);
            pstmt.setString(1,reserva.getCodigo());
            
            String agencia = "";
            agencia+=reserva.getAgencia().getCodigo();
            pstmt.setString(2, agencia);
            
            String cliente = "";
            cliente+=reserva.getCliente().getCedula();
            pstmt.setString(3, cliente);
            
            String vehiculo = "";
            vehiculo+=reserva.getVehiculo().getCodigo();
            pstmt.setString(4, vehiculo);
            
            pstmt.setDate(5,reserva.getFecha_inicio());
            pstmt.setDate(6,reserva.getFecha_final());
            
            boolean resultado = pstmt.execute();
            if (resultado == true) 
            {
                throw new GlobalException("No se pudo insertar el cliente.");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new GlobalException("Número de identificación duplicado.");
        } 
        finally 
        {
            try 
            {
                if (pstmt != null) 
                {
                    pstmt.close();
                }
                desconectar();
                
            } 
            catch (SQLException e) 
            {
                throw new GlobalException("Error al desconectar.");
            }
            return true;
        }
    }
    
    public ArrayList listarReservas() 	throws GlobalException, NoDataException
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            try{
                throw new GlobalException("No se ha localizado el driver.");
            } 
            catch (GlobalException ex)
            {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch (SQLException e) 
        {
            try {
                throw new GlobalException("La base de datos no se encuentra disponible.");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        CallableStatement pstmt=null;
        ResultSet rs = null;
	ArrayList coleccion = new ArrayList();
	//Curso curso = null;
        try
        {
            pstmt = conexion.prepareCall(LISTARRESERVAS);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            while (rs.next())
            {
                coleccion.add(reserva(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            try {
                throw new GlobalException("Error al recuperar datos.\n");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (pstmt != null)
                {
                    pstmt.close();
                }
                desconectar();
            }
            catch (SQLException e)
            {
                try {
                    throw new GlobalException("Estatutos invalidos o nulos.");
                } catch (GlobalException ex) {
                    Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (coleccion == null || coleccion.isEmpty())
        {
            try {
                throw new GlobalException("No hay datos.");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coleccion;
    }
    
    
    
    public boolean eliminarReserva(String codigo) throws GlobalException,SQLException  	
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            throw new GlobalException("No se ha localizado el driver.");
        } 
        catch (SQLException e) 
        {
            throw new GlobalException("La base de datos no se encuentra disponible.");
        }
        CallableStatement pstmt=null;
        
        try 
        {
            pstmt = conexion.prepareCall(ELIMINARRESERVA);
            pstmt.setString(1,codigo);
            
            boolean resultado = pstmt.execute();
            if (resultado == true) 
            {
                throw new GlobalException("No se pudo eliminar la carrera.");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new GlobalException("Número de identificación duplicado.");
        } 
        finally 
        {
            try 
            {
                if (pstmt != null) 
                {
                    pstmt.close();
                }
                desconectar();
            } 
            catch (SQLException e) 
            {
                throw new GlobalException("Error al desconectar.");
            }
            return true;
        }     
    }
     
    public boolean editarReserva(Reserva reserva) throws GlobalException  	
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            throw new GlobalException("No se ha localizado el driver.");
        } 
        catch (SQLException e) 
        {
            throw new GlobalException("La base de datos no se encuentra disponible.");
        }
        CallableStatement pstmt=null;
        
        try 
        {
            pstmt = conexion.prepareCall(MODIFICARRESERVA);
            pstmt.setString(1,reserva.getCodigo());
            
            String agencia = "";
            agencia+=reserva.getAgencia().getCodigo();
            pstmt.setString(2, agencia);
            
            String cliente = "";
            cliente+=reserva.getCliente().getCedula();
            pstmt.setString(3, cliente);
            
            String vehiculo = "";
            vehiculo+=reserva.getVehiculo().getCodigo();
            pstmt.setString(4, vehiculo);
            
            pstmt.setDate(5,reserva.getFecha_inicio());
            pstmt.setDate(6,reserva.getFecha_final());
            
            boolean resultado = pstmt.execute();
            if (resultado == true) 
            {
                throw new GlobalException("No se pudo editar el cliente.");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new GlobalException("Número de identificación duplicado.");
        } 
        finally 
        {
            try 
            {
                if (pstmt != null) 
                {
                    pstmt.close();
                }
                desconectar();
                
            } 
            catch (SQLException e) 
            {
                throw new GlobalException("Error al desconectar.");
            }
            return true;
        }
    }
    
    public ArrayList listarReservasVehiculos() 	throws GlobalException, NoDataException
    {
        try 
        {
            conectar();
        } 
        catch (ClassNotFoundException e) 
        {
            try{
                throw new GlobalException("No se ha localizado el driver.");
            } 
            catch (GlobalException ex)
            {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch (SQLException e) 
        {
            try {
                throw new GlobalException("La base de datos no se encuentra disponible.");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        CallableStatement pstmt=null;
        ResultSet rs = null;
	ArrayList coleccion = new ArrayList();
	//Curso curso = null;
        try
        {
            pstmt = conexion.prepareCall(LISTARRESERVASVEHICULOS);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            rs = (ResultSet)pstmt.getObject(1);
            while (rs.next())
            {
                coleccion.add(vehiculo(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            try {
                throw new GlobalException("Error al recuperar datos.\n");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (pstmt != null)
                {
                    pstmt.close();
                }
                desconectar();
            }
            catch (SQLException e)
            {
                try {
                    throw new GlobalException("Estatutos invalidos o nulos.");
                } catch (GlobalException ex) {
                    Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (coleccion == null || coleccion.isEmpty())
        {
            try {
                throw new GlobalException("No hay datos.");
            } catch (GlobalException ex) {
                Logger.getLogger(ServicioVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coleccion;
    }
}
