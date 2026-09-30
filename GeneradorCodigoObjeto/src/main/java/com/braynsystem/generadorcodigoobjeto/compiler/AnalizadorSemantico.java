package com.braynsystem.generadorcodigoobjeto.compiler;

import com.braynsystem.generadorcodigoobjeto.model.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorSemantico {

    private final List<Variable> variables;
    private final List<String> errores;

    private int siguienteDireccion;

    public AnalizadorSemantico() {
        variables = new ArrayList<>();
        errores = new ArrayList<>();
        siguienteDireccion = 1000;
    }

    public void analizar(String codigo) {

        variables.clear();
        errores.clear();
        siguienteDireccion = 1000;

        if (codigo == null || codigo.isBlank()) {
            errores.add("El código fuente está vacío.");
            return;
        }

        String[] lineas = codigo.split("\\R");

        for (String linea : lineas) {

            linea = linea.trim();

            if (linea.isEmpty()) {
                continue;
            }

            analizarLinea(linea);
        }
    }

    private void analizarLinea(String linea) {

        Pattern patronDeclaracion = Pattern.compile(
                "^(int|float|double|string|boolean)\\s+" +
                        "([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(.+);$"
        );

        Matcher matcher = patronDeclaracion.matcher(linea);

        if (matcher.matches()) {

            String tipo = matcher.group(1);
            String nombre = matcher.group(2);
            String valor = matcher.group(3).trim();

            // Verificar variable duplicada
            if (buscarVariable(nombre) != null) {

                errores.add(
                        "La variable '" + nombre +
                                "' ya fue declarada."
                );

                return;
            }

            // Verificar expresión
            verificarExpresion(tipo, valor, nombre);

            // Crear variable
            Variable variable = new Variable(
                    nombre,
                    tipo,
                    valor,
                    String.valueOf(siguienteDireccion),
                    "Sin asignar"
            );

            variables.add(variable);

            siguienteDireccion += 4;

            return;
        }

        // Reasignación
        Pattern patronReasignacion = Pattern.compile(
                "^([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(.+);$"
        );

        Matcher matcherReasignacion =
                patronReasignacion.matcher(linea);

        if (matcherReasignacion.matches()) {

            String nombre = matcherReasignacion.group(1);
            String valor = matcherReasignacion.group(2).trim();

            Variable variable = buscarVariable(nombre);

            if (variable == null) {

                errores.add(
                        "La variable '" + nombre +
                                "' no ha sido declarada."
                );

                return;
            }

            verificarExpresion(
                    variable.getTipo(),
                    valor,
                    nombre
            );

            variable.setValor(valor);

            return;
        }

        errores.add(
                "Sintaxis no reconocida: " + linea
        );
    }

    private void verificarExpresion(
            String tipo,
            String expresion,
            String variableDestino) {

        // División entre cero
        if (expresion.matches(".*\\/\\s*0+(\\.0+)?$")) {

            errores.add(
                    "División entre cero en la variable '" +
                            variableDestino + "'."
            );
        }

        // INT
        if (tipo.equals("int")) {

            verificarEnteros(expresion, variableDestino);
        }

        // FLOAT / DOUBLE
        else if (
                tipo.equals("float") ||
                        tipo.equals("double")
        ) {

            verificarDecimales(expresion, variableDestino);
        }

        // STRING
        else if (tipo.equals("string")) {

            if (!esCadena(expresion)) {

                errores.add(
                        "La variable '" + variableDestino +
                                "' debe contener una cadena de texto."
                );
            }
        }

        // BOOLEAN
        else if (tipo.equals("boolean")) {

            if (!esBooleano(expresion)) {

                errores.add(
                        "La variable '" + variableDestino +
                                "' debe contener true o false."
                );
            }
        }
    }

    private void verificarEnteros(
            String expresion,
            String variableDestino) {

        if (expresion.matches("-?\\d+")) {
            return;
        }

        verificarOperadores(
                expresion,
                "\\d+",
                variableDestino
        );
    }

    private void verificarDecimales(
            String expresion,
            String variableDestino) {

        if (expresion.matches(
                "-?\\d+(\\.\\d+)?"
        )) {
            return;
        }

        verificarOperadores(
                expresion,
                "\\d+(\\.\\d+)?",
                variableDestino
        );
    }

    private void verificarOperadores(
            String expresion,
            String patronNumero,
            String variableDestino) {

        String[] elementos =
                expresion.split("[+\\-*/]");

        for (String elemento : elementos) {

            elemento = elemento.trim();

            if (elemento.isEmpty()) {
                continue;
            }

            if (elemento.matches(patronNumero)) {
                continue;
            }

            Variable variable =
                    buscarVariable(elemento);

            if (variable == null) {

                errores.add(
                        "La variable '" + elemento +
                                "' utilizada en '" +
                                variableDestino +
                                "' no ha sido declarada."
                );
            }
        }
    }

    private boolean esCadena(String valor) {

        return valor.matches(
                "\".*\""
        );
    }

    private boolean esBooleano(String valor) {

        return valor.equals("true") ||
                valor.equals("false");
    }

    private Variable buscarVariable(String nombre) {

        for (Variable variable : variables) {

            if (variable.getNombre()
                    .equals(nombre)) {

                return variable;
            }
        }

        return null;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<String> getErrores() {
        return errores;
    }

    public boolean esValido() {
        return errores.isEmpty();
    }
}