package com.braynsystem.generadorcodigoobjeto.model;

public class Registro {

    private String nombre;
    private String valor;
    private boolean ocupado;

    public Registro(String nombre) {
        this.nombre = nombre;
        this.ocupado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void asignarValor(String valor) {
        this.valor = valor;
        this.ocupado = true;
    }

    public void liberar() {
        this.valor = null;
        this.ocupado = false;
    }
}