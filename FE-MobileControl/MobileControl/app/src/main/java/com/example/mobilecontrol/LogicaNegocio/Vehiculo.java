package com.example.mobilecontrol.LogicaNegocio;

import java.io.Serializable;
import java.util.Objects;

public class Vehiculo implements Serializable {
    String codigo;
    Clasificacion clasificacion;
    Transmision transmision;
    Marca marca;
    int capacidad;
    String placa;
    String descripcion;
    int precio;

    public Vehiculo() {
    }

    public Vehiculo(String codigo, Clasificacion clasificacion, Transmision transmision, Marca marca, int capacidad, String placa, String descripcion, int precio) {
        this.codigo = codigo;
        this.clasificacion = clasificacion;
        this.transmision = transmision;
        this.marca = marca;
        this.capacidad = capacidad;
        this.placa = placa;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Transmision getTransmision() {
        return transmision;
    }

    public void setTransmision(Transmision transmision) {
        this.transmision = transmision;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "codigo=" + codigo + ", clasificacion=" + clasificacion + ", transmision=" + transmision + ", marca=" + marca + ", capacidad=" + capacidad + ", placa=" + placa + ", descripcion=" + descripcion + ", precio=" + precio + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.codigo);
        hash = 41 * hash + Objects.hashCode(this.clasificacion);
        hash = 41 * hash + Objects.hashCode(this.transmision);
        hash = 41 * hash + Objects.hashCode(this.marca);
        hash = 41 * hash + this.capacidad;
        hash = 41 * hash + Objects.hashCode(this.placa);
        hash = 41 * hash + Objects.hashCode(this.descripcion);
        hash = 41 * hash + this.precio;
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
        final Vehiculo other = (Vehiculo) obj;
        if (this.capacidad != other.capacidad) {
            return false;
        }
        if (this.precio != other.precio) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.placa, other.placa)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.clasificacion, other.clasificacion)) {
            return false;
        }
        if (!Objects.equals(this.transmision, other.transmision)) {
            return false;
        }
        if (!Objects.equals(this.marca, other.marca)) {
            return false;
        }
        return true;
    }
}
