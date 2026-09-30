package com.braynsystem.generadorcodigoobjeto.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneradorIntermedio {

    private final List<String> instrucciones;

    public GeneradorIntermedio() {
        instrucciones = new ArrayList<>();
    }

    public void generar(String codigo) {

        instrucciones.clear();

        if (codigo == null || codigo.isBlank()) {
            return;
        }

        String[] lineas = codigo.split("\\n");

        for (String linea : lineas) {

            linea = linea.trim();

            if (linea.isEmpty()) {
                continue;
            }

            procesarLinea(linea);
        }
    }

    private void procesarLinea(String linea) {

        /*
         * Ejemplo:
         *
         * int a = 10;
         *
         * Se convierte en:
         *
         * MOV a, 10
         */

        Pattern asignacion = Pattern.compile(
                "^(?:int|float|double|string|boolean)\\s+" +
                        "([a-zA-Z_][a-zA-Z0-9_]*)" +
                        "\\s*=\\s*(.+);$"
        );

        Matcher matcherAsignacion = asignacion.matcher(linea);

        if (matcherAsignacion.matches()) {

            String variable = matcherAsignacion.group(1);
            String expresion = matcherAsignacion.group(2).trim();

            generarInstruccion(variable, expresion);

            return;
        }

        /*
         * También permitimos:
         *
         * c = a + b;
         */

        Pattern reasignacion = Pattern.compile(
                "^([a-zA-Z_][a-zA-Z0-9_]*)" +
                        "\\s*=\\s*(.+);$"
        );

        Matcher matcherReasignacion = reasignacion.matcher(linea);

        if (matcherReasignacion.matches()) {

            String variable = matcherReasignacion.group(1);
            String expresion = matcherReasignacion.group(2).trim();

            generarInstruccion(variable, expresion);
        }
    }

    private void generarInstruccion(
            String variable,
            String expresion) {

        /*
         * Operaciones:
         *
         * a + b
         * a - b
         * a * b
         * a / b
         */

        Pattern operacion = Pattern.compile(
                "^([a-zA-Z_][a-zA-Z0-9_]*|\\d+(?:\\.\\d+)?)" +
                        "\\s*([+\\-*/])\\s*" +
                        "([a-zA-Z_][a-zA-Z0-9_]*|\\d+(?:\\.\\d+)?)$"
        );

        Matcher matcherOperacion = operacion.matcher(expresion);

        if (matcherOperacion.matches()) {

            String operando1 = matcherOperacion.group(1);
            String operador = matcherOperacion.group(2);
            String operando2 = matcherOperacion.group(3);

            String instruccion = "";

            switch (operador) {

                case "+":
                    instruccion =
                            "ADD " + variable + ", " +
                                    operando1 + ", " + operando2;
                    break;

                case "-":
                    instruccion =
                            "SUB " + variable + ", " +
                                    operando1 + ", " + operando2;
                    break;

                case "*":
                    instruccion =
                            "MUL " + variable + ", " +
                                    operando1 + ", " + operando2;
                    break;

                case "/":
                    instruccion =
                            "DIV " + variable + ", " +
                                    operando1 + ", " + operando2;
                    break;
            }

            instrucciones.add(instruccion);

            return;
        }

        /*
         * Si es una asignación directa:
         *
         * a = 10
         *
         * genera:
         *
         * MOV a, 10
         */

        instrucciones.add(
                "MOV " + variable + ", " + expresion
        );
    }

    public List<String> getInstrucciones() {
        return instrucciones;
    }

    public String obtenerCodigo() {

        if (instrucciones.isEmpty()) {
            return "";
        }

        return String.join("\n", instrucciones);
    }
}