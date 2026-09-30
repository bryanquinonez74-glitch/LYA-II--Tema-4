package com.braynsystem.generadorcodigoobjeto.model;

public class Variable {

    private String nombre;
    private String tipo;
    private String valor;
    private String direccionMemoria;
    private String registro;

    public Variable() {
    }

    public Variable(String nombre, String tipo, String valor,
                    String direccionMemoria, String registro) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.direccionMemoria = direccionMemoria;
        this.registro = registro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDireccionMemoria() {
        return direccionMemoria;
    }

    public void setDireccionMemoria(String direccionMemoria) {
        this.direccionMemoria = direccionMemoria;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    @Override
    public String toString() {
        return nombre + " : " + tipo;
    }
}