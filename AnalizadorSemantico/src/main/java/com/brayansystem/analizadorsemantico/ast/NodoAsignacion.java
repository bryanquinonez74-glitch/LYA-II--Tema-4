package com.brayansystem.analizadorsemantico.ast;

public class NodoAsignacion extends Nodo {

    private final String nombre;
    private final Nodo valor;

    public NodoAsignacion(
            String nombre,
            Nodo valor,
            int linea) {

        super(linea);
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public Nodo getValor() {
        return valor;
    }

    @Override
    public String getDescripcion() {

        return nombre
                + " = "
                + valor.getDescripcion();
    }
}