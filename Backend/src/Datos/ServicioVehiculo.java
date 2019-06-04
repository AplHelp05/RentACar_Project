/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Entidades.Clasificacion;
import Entidades.Marca;
import Entidades.Transmision;
import Entidades.Vehiculo;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author david
 */
public class ServicioVehiculo extends Servicio{
     private static final String INSERTARVEHICULO = "{call INSERTARVEHICULO(?,?,?,?,?,?,?,?)}";
    private static final String ELIMINARVEHICULO = "{call ELIMINARVEHICULO(?)}";  
    private static final String MODIFICARVEHICULO = "{call MODIFICARVEHICULO(?,?,?,?,?,?,?,?)}";
    private static final String LISTARVEHICULOS = "{?=call LISTARVEHICULOS()}";
    private static final String LISTARMARCAVEHICULOS = "{?=call LISTARMARCAVEHICULOS()}";
    private static final String LISTARTIPOTRANSMISIONVEHICULOS = "{?=call LISTARTIPOTRANSMISIONVEHICULOS()}";
    private static final String LISTARTIPOVEHICULOS = "{?=call LISTARTIPOVEHICULOS()}";
    
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
    
    public boolean insertarVehiculo(Vehiculo vehiculo) throws GlobalException  	
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
            pstmt = conexion.prepareCall(INSERTARVEHICULO);
            pstmt.setString(1,vehiculo.getCodigo());
            
            String tipo = "";
            tipo+=vehiculo.getClasificacion().getCodigo();
            pstmt.setString(2, tipo);
            
            String transimsion = "";
            transimsion+=vehiculo.getTransmision().getCodigo();
            pstmt.setString(3, transimsion);
            
            String marca = "";
            marca+=vehiculo.getMarca().getCodigo();
            pstmt.setString(4, marca);
            
            pstmt.setInt(5,vehiculo.getCapacidad());
            pstmt.setString(6,vehiculo.getPlaca());
            pstmt.setString(7,vehiculo.getDescripcion());
            pstmt.setInt(8,vehiculo.getPrecio());
            
            
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
    
    public ArrayList listarVehiculos() 	throws GlobalException, NoDataException
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
            
                pstmt = conexion.prepareCall(LISTARVEHICULOS);
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
    
     public boolean eliminarVehiculo(String codigo) throws GlobalException,SQLException  	
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
            pstmt = conexion.prepareCall(ELIMINARVEHICULO);
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
     
    public boolean editarVehiculo(Vehiculo vehiculo) throws GlobalException  	
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
            pstmt = conexion.prepareCall(MODIFICARVEHICULO);
            pstmt.setString(1,vehiculo.getCodigo());
            
            String tipo = "";
            tipo+=vehiculo.getClasificacion().getCodigo();
            pstmt.setString(2, tipo);
            
            String transimsion = "";
            transimsion+=vehiculo.getTransmision().getCodigo();
            pstmt.setString(3, transimsion);
            
            String marca = "";
            marca+=vehiculo.getMarca().getCodigo();
            pstmt.setString(4, marca);
            
            pstmt.setInt(5,vehiculo.getCapacidad());
            pstmt.setString(6,vehiculo.getPlaca());
            pstmt.setString(7,vehiculo.getDescripcion());
            pstmt.setInt(8,vehiculo.getPrecio());
            
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
    
    public ArrayList listarClasificaciones() throws GlobalException  	
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
        ResultSet rs = null;
	ArrayList coleccion = new ArrayList();
	Clasificacion clasificaion = null;
        try
        {
                pstmt = conexion.prepareCall(LISTARTIPOVEHICULOS);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next())
                {
                        clasificaion = new Clasificacion(
                                            rs.getString("COD_TIPO_VEHICULO"),
                                            rs.getString("DESCRIPCION_TIPO_VEHICULO"));
                        coleccion.add(clasificaion);
                }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new GlobalException("Error al recuperar datos.\n");
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
                throw new GlobalException("Estatutos invalidos o nulos.");
            }
        }
        if (coleccion == null || coleccion.isEmpty())
        {
            throw new GlobalException("No hay datos.");
        }
        return coleccion;        
    }
    
    public ArrayList listarMarcas() throws GlobalException  	
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
        ResultSet rs = null;
	ArrayList coleccion = new ArrayList();
	Marca marca = null;
        try
        {
                pstmt = conexion.prepareCall(LISTARMARCAVEHICULOS);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next())
                {
                        marca = new Marca(
                                            rs.getString("COD_MARCA_VEHICULO"),
                                            rs.getString("DESCRIPCION_MARCA_VEHICULO"));
                        coleccion.add(marca);
                }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new GlobalException("Error al recuperar datos.\n");
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
                throw new GlobalException("Estatutos invalidos o nulos.");
            }
        }
        if (coleccion == null || coleccion.isEmpty())
        {
            throw new GlobalException("No hay datos.");
        }
        return coleccion;        
    }
        
    public ArrayList listarTransmisiones() throws GlobalException  	
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
        ResultSet rs = null;
	ArrayList coleccion = new ArrayList();
	Transmision transmision = null;
        try
        {
                pstmt = conexion.prepareCall(LISTARTIPOTRANSMISIONVEHICULOS);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next())
                {
                        transmision = new Transmision(
                                            rs.getString("COD_TIPO_TRANSMISION"),
                                            rs.getString("DESCRIPCION_TIPO_TRANSMISION"));
                        coleccion.add(transmision);
                }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new GlobalException("Error al recuperar datos.\n");
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
                throw new GlobalException("Estatutos invalidos o nulos.");
            }
        }
        if (coleccion == null || coleccion.isEmpty())
        {
            throw new GlobalException("No hay datos.");
        }
        return coleccion;        
    }
}
