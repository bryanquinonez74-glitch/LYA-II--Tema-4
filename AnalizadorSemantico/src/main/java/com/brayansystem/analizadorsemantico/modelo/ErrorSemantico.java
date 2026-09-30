package com.brayansystem.analizadorsemantico.modelo;

public class ErrorSemantico {

    private final String mensaje;
    private final int linea;

    public ErrorSemantico(
            String mensaje,
            int linea) {

        this.mensaje = mensaje;
        this.linea = linea;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getLinea() {
        return linea;
    }

    @Override
    public String toString() {

        if (linea <= 0) {
            return mensaje;
        }

        return "Línea "
                + linea
                + ": "
                + mensaje;
    }
}