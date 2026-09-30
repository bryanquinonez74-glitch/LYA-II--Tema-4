package com.braynsystem.generadorcodigoobjeto.model;

public class Instruccion {

    private String operacion;
    private String destino;
    private String operando1;
    private String operando2;

    public Instruccion(
            String operacion,
            String destino,
            String operando1,
            String operando2) {

        this.operacion = operacion;
        this.destino = destino;
        this.operando1 = operando1;
        this.operando2 = operando2;
    }

    public String getOperacion() {
        return operacion;
    }

    public String getDestino() {
        return destino;
    }

    public String getOperando1() {
        return operando1;
    }

    public String getOperando2() {
        return operando2;
    }
}