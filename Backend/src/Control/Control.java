/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Datos.GlobalException;
import Datos.NoDataException;
import Datos.ServicioAgencia;
import Datos.ServicioCliente;
import Datos.ServicioReserva;
import Datos.ServicioVehiculo;
import Entidades.Agencia;
import Entidades.Clasificacion;
import Entidades.Cliente;
import Entidades.Marca;
import Entidades.Reserva;
import Entidades.Transmision;
import Entidades.Vehiculo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class Control {
    private ServicioCliente servicioCliente;
    private ServicioAgencia servicioAgencia;
    private ServicioVehiculo servicioVehiculo;
    private ServicioReserva servicioReserva;
    
    private static Control uniqueInstance;
    public static Control instance()
    {
        if (uniqueInstance == null)
        {
            uniqueInstance = new Control();
        }
        return uniqueInstance;
    }
    public Control()
    {
        servicioCliente = new ServicioCliente();
        servicioAgencia = new ServicioAgencia();
        servicioVehiculo = new ServicioVehiculo();
        servicioReserva = new ServicioReserva();
    }
    
    //Clientes
    public void insertarCliente(Cliente cliente) throws Exception{
        servicioCliente.insertarCliente(cliente);
    }
    
   public void eliminarCliente(String cedula) throws Exception{
        servicioCliente.eliminarCliente(cedula);
    }
    
    public ArrayList<Cliente> listarClientes() throws GlobalException, NoDataException{
        return servicioCliente.listarClientes();
    }
    
    public void modificarCliente(Cliente cliente) throws Exception{
        servicioCliente.editarCliente(cliente);
    }
        
    public List<Cliente> buscarClienteCedula(String cedula) throws GlobalException, NoDataException{
        return (List<Cliente>)servicioCliente.buscarClienteCedula(cedula);
    }
    
    //Agencias
    public void insertarAgencia(Agencia agencia) throws Exception{
        servicioAgencia.insertarAgencia(agencia);
    }
    
    public void modificarAgencia(Agencia agencia) throws Exception{
        servicioAgencia.editarAgencia(agencia);
    }
    
   public void eliminarAgencia(String codigo) throws Exception{
        servicioAgencia.eliminarAgencia(codigo);
    }
    
    public ArrayList<Agencia> listarAgencias() throws GlobalException, NoDataException{
        return servicioAgencia.listarAgencias();
    }
    
    //Vehiculos
    public void insertarVehiculo(Vehiculo vehiculo) throws Exception{
        servicioVehiculo.insertarVehiculo(vehiculo);
    }
    
    public void modificarVehiculo(Vehiculo vehiculo) throws Exception{
        servicioVehiculo.editarVehiculo(vehiculo);
    }
    
   public void eliminarVehiculo(String codigo) throws Exception{
        servicioVehiculo.eliminarVehiculo(codigo);
    }
    
    public ArrayList<Vehiculo> listarVehiculos() throws GlobalException, NoDataException{
        return servicioVehiculo.listarVehiculos();
    }
    
    public ArrayList<Clasificacion> listarClasificaciones() throws GlobalException, NoDataException{
        return servicioVehiculo.listarClasificaciones();
    }
    
    public ArrayList<Marca> listarMarcas() throws GlobalException, NoDataException{
        return servicioVehiculo.listarMarcas();
    }
    
    public ArrayList<Transmision> listarTrasmisiones() throws GlobalException, NoDataException{
        return servicioVehiculo.listarTransmisiones();
    }
    
    //RESERVA
    public void insertarReserva(Reserva reserva) throws Exception{
        servicioReserva.insertarReserva(reserva);
    }
    
    public void modificarReserva(Reserva reserva) throws Exception{
        servicioReserva.editarReserva(reserva);
    }
    
   public void eliminarReserva(String codigo) throws Exception{
        servicioReserva.eliminarReserva(codigo);
    }
    
    public ArrayList<Reserva> listarReservas() throws GlobalException, NoDataException{
        return servicioReserva.listarReservas();
    }
    
    public ArrayList<Vehiculo> listarReservasVehiculos() throws GlobalException, NoDataException{
        return servicioReserva.listarReservasVehiculos();
    }
}
