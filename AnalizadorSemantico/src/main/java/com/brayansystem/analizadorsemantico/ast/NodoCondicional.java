package com.brayansystem.analizadorsemantico.ast;

public class NodoCondicional extends Nodo {

    private final Nodo condicion;
    private final NodoBloque entonces;
    private final NodoBloque sino;

    public NodoCondicional(
            Nodo condicion,
            NodoBloque entonces,
            NodoBloque sino,
            int linea) {

        super(linea);

        this.condicion = condicion;
        this.entonces = entonces;
        this.sino = sino;
    }

    public Nodo getCondicion() {
        return condicion;
    }

    public NodoBloque getEntonces() {
        return entonces;
    }

    public NodoBloque getSino() {
        return sino;
    }

    @Override
    public String getDescripcion() {

        String resultado =
                "if ("
                        + condicion.getDescripcion()
                        + ") "
                        + entonces.getDescripcion();

        if (sino != null) {
            resultado +=
                    " else "
                            + sino.getDescripcion();
        }

        return resultado;
    }
}