/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Entidades.Agencia;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author david
 */
public class ServicioAgencia extends Servicio{
    private static final String INSERTARAGENCIA = "{call INSERTARAGENCIA(?,?,?,?,?,?)}";
    private static final String ELIMINARAGENCIA = "{call ELIMINARAGENCIA(?)}";  
    private static final String MODIFICARAGENCIA = "{call MODIFICARAGENCIA(?,?,?,?,?,?)}";
    private static final String LISTARAGENCIAS = "{?=call LISTARAGENCIAS()}";
    
    public boolean insertarAgencia(Agencia agencia) throws GlobalException  	
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
            pstmt = conexion.prepareCall(INSERTARAGENCIA);
            pstmt.setString(1,agencia.getCodigo());
            pstmt.setString(2,agencia.getNombre());
            pstmt.setString(3,agencia.getTelefono());
            pstmt.setString(4,agencia.getEmail());
            pstmt.setString(5,agencia.getUbicacion());
            pstmt.setString(6,agencia.getHorario());
            
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
    
    public ArrayList listarAgencias() throws GlobalException  	
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
	Agencia agencia = null;
        try
        {
                pstmt = conexion.prepareCall(LISTARAGENCIAS);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next())
                {
                        agencia = new Agencia(rs.getString("COD_AGENCIA"),
                                            rs.getString("NOMBRE_AGENCIA"),
                                            rs.getString("TELEFONO_AGENCIA"),
                                            rs.getString("EMAIL_AGENCIA"),
                                            rs.getString("UBICACION_AGENCIA"),
                                            rs.getString("HORARIO_AGENCIA"));
                        coleccion.add(agencia);
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
    
     public boolean eliminarAgencia(String codigo) throws GlobalException,SQLException  	
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
            pstmt = conexion.prepareCall(ELIMINARAGENCIA);
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
     
    public boolean editarAgencia(Agencia agencia) throws GlobalException  	
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
            pstmt = conexion.prepareCall(MODIFICARAGENCIA);
            pstmt.setString(1,agencia.getCodigo());
            pstmt.setString(2,agencia.getNombre());
            pstmt.setString(3,agencia.getTelefono());
            pstmt.setString(4,agencia.getEmail());
            pstmt.setString(5,agencia.getUbicacion());
            pstmt.setString(6,agencia.getHorario());

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
}
