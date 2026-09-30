package com.brayansystem.analizadorsemantico.ast;

import java.util.List;

public class NodoIf extends Nodo {

    private final Nodo condicion;
    private final List<Nodo> instrucciones;

    public NodoIf(
            Nodo condicion,
            List<Nodo> instrucciones,
            int linea) {

        super(linea);

        this.condicion = condicion;
        this.instrucciones = instrucciones;
    }

    public Nodo getCondicion() {
        return condicion;
    }

    public List<Nodo> getInstrucciones() {
        return instrucciones;
    }

    @Override
    public String getDescripcion() {

        return "if ("
                + condicion.getDescripcion()
                + ")";
    }
}