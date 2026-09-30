package com.brayansystem.analizadorsemantico.ast;

public class NodoIdentificador extends Nodo {

    private final String nombre;

    public NodoIdentificador(
            String nombre,
            int linea) {

        super(linea);
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return nombre;
    }
}