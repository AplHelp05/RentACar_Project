package com.example.mobilecontrol.LogicaNegocio;

import java.io.Serializable;
import java.util.Objects;

public class Reserva implements Serializable {
    String codigo;
    Agencia agencia;
    Cliente cliente;
    Vehiculo vehiculo;
    String fecha_inicio;
    String fecha_final;

    public Reserva() {
    }

    public Reserva(String codigo, Agencia agencia, Cliente cliente, Vehiculo vehiculo, String fecha_inicio, String fecha_final) {
        this.codigo = codigo;
        this.agencia = agencia;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    @Override
    public String toString() {
        return "Reserva{" + "codigo=" + codigo + ", agencia=" + agencia + ", cliente=" + cliente + ", vehiculo=" + vehiculo + ", fecha_inicio=" + fecha_inicio + ", fecha_final=" + fecha_final + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.codigo);
        hash = 67 * hash + Objects.hashCode(this.agencia);
        hash = 67 * hash + Objects.hashCode(this.cliente);
        hash = 67 * hash + Objects.hashCode(this.vehiculo);
        hash = 67 * hash + Objects.hashCode(this.fecha_inicio);
        hash = 67 * hash + Objects.hashCode(this.fecha_final);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reserva other = (Reserva) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.fecha_inicio, other.fecha_inicio)) {
            return false;
        }
        if (!Objects.equals(this.fecha_final, other.fecha_final)) {
            return false;
        }
        if (!Objects.equals(this.agencia, other.agencia)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.vehiculo, other.vehiculo)) {
            return false;
        }
        return true;
    }
}
