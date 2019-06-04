/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Entidades.Cliente;
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
public class ServicioCliente extends Servicio{
    private static final String INSERTARCLIENTE = "{call INSERTARCLIENTE(?,?,?,?)}";
    private static final String ELIMINARCLIENTE = "{call ELIMINARCLIENTE(?)}";  
    private static final String MODIFICARCLIENTE = "{call MODIFICARCLIENTE(?,?,?,?)}";
    private static final String LISTARCLIENTES = "{?=call LISTARCLIENTES()}";
    private static final String BUSCARCLIENTECEDULA = "{?=call BUSCARCLIENTECEDULA(?)}";
    
    public boolean insertarCliente(Cliente cliente) throws GlobalException  	
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
            pstmt = conexion.prepareCall(INSERTARCLIENTE);
            pstmt.setString(1,cliente.getCedula());
            pstmt.setString(2,cliente.getNombre());
            pstmt.setString(3,cliente.getTelefono());
            pstmt.setString(4,cliente.getEmail());
            
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
    
    public ArrayList listarClientes() throws GlobalException  	
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
	Cliente cliente = null;
        try
        {
                pstmt = conexion.prepareCall(LISTARCLIENTES);
                pstmt.registerOutParameter(1, OracleTypes.CURSOR);
                pstmt.execute();
                rs = (ResultSet)pstmt.getObject(1);
                while (rs.next())
                {
                        cliente = new Cliente(rs.getString("CED_CLIENTE"),
                                            rs.getString("NOMBRE_CLIENTE"),
                                            rs.getString("TELEFONO_CLIENTE"),
                                            rs.getString("EMAIL_CLIENTE"));
                        coleccion.add(cliente);
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
    
     public boolean eliminarCliente(String codigo) throws GlobalException,SQLException  	
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
            pstmt = conexion.prepareCall(ELIMINARCLIENTE);
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
     
    public boolean editarCliente(Cliente cliente) throws GlobalException  	
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
            pstmt = conexion.prepareCall(MODIFICARCLIENTE);
            pstmt.setString(1,cliente.getCedula());
            pstmt.setString(2,cliente.getNombre());
            pstmt.setString(3,cliente.getTelefono());
            pstmt.setString(4,cliente.getEmail());
            
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
    
      public List buscarClienteCedula(String cedula) throws GlobalException, NoDataException{

		try{
			conectar();
		}catch (ClassNotFoundException e){
			throw new GlobalException("No se ha localizado el driver");
		}
		catch (SQLException e){
			throw new NoDataException("La base de datos no se encuentra disponible");
		}
		ResultSet rs = null;
		ArrayList coleccion = new ArrayList();
                Cliente cliente = null;    
		//Ciclo elciclo = null;
		CallableStatement pstmt = null;
		try{
			pstmt = conexion.prepareCall(BUSCARCLIENTECEDULA);
			pstmt.registerOutParameter(1, OracleTypes.CURSOR);
			pstmt.setString(2, cedula);
			pstmt.execute();
			rs = (ResultSet)pstmt.getObject(1);
			while (rs.next()){
                            cliente = new Cliente(rs.getString("CED_CLIENTE"),
                                            rs.getString("NOMBRE_CLIENTE"),
                                            rs.getString("TELEFONO_CLIENTE"),
                                            rs.getString("EMAIL_CLIENTE"));
                            coleccion.add(cliente);
			}
		}
		catch (SQLException e){
			e.printStackTrace();

			throw new GlobalException("Sentencia no valida");
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				desconectar();
			}
			catch (SQLException e){
				throw new GlobalException("Estatutos invalidos o nulos");
			}
		}
		if (coleccion == null || coleccion.size() == 0){
			throw new NoDataException("No hay datos");
		}
		return coleccion;
	}
    
    
}
