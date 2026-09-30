package com.brayansystem.analizadorsemantico.lexer;

import com.brayansystem.analizadorsemantico.modelo.Token;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    private final List<Token> tokens = new ArrayList<>();

    private int posicion;
    private int linea;
    private int columna;

    public List<Token> analizar(String codigo) {

        tokens.clear();

        posicion = 0;
        linea = 1;
        columna = 1;

        if (codigo == null || codigo.isEmpty()) {
            agregarToken("EOF", "EOF", linea, columna);
            return new ArrayList<>(tokens);
        }

        while (posicion < codigo.length()) {

            char actual = codigo.charAt(posicion);

            if (Character.isWhitespace(actual)) {
                avanzar(codigo);
                continue;
            }

            if (actual == '/' && siguiente(codigo) == '/') {
                comentarioLinea(codigo);
                continue;
            }

            if (actual == '/' && siguiente(codigo) == '*') {
                comentarioBloque(codigo);
                continue;
            }

            if (Character.isLetter(actual) || actual == '_') {
                identificador(codigo);
                continue;
            }

            if (Character.isDigit(actual)) {
                numero(codigo);
                continue;
            }

            if (actual == '"') {
                cadena(codigo);
                continue;
            }

            simbolo(codigo);
        }

        agregarToken("EOF", "EOF", linea, columna);

        return new ArrayList<>(tokens);
    }

    private void identificador(String codigo) {

        int lineaInicial = linea;
        int columnaInicial = columna;

        StringBuilder lexema = new StringBuilder();

        while (posicion < codigo.length()) {

            char actual = codigo.charAt(posicion);

            if (Character.isLetterOrDigit(actual) || actual == '_') {

                lexema.append(actual);
                avanzar(codigo);

            } else {
                break;
            }
        }

        String palabra = lexema.toString();

        agregarToken(
                palabra,
                tipoPalabra(palabra),
                lineaInicial,
                columnaInicial
        );
    }


    private String tipoPalabra(String palabra) {

        return switch (palabra) {

            case "int",
                 "double",
                 "String",
                 "boolean" -> "TIPO_DATO";

            case "true",
                 "false" -> "BOOLEANO";

            case "if" -> "IF";

            case "else" -> "ELSE";

            case "while" -> "WHILE";

            case "for" -> "FOR";

            case "return" -> "RETURN";

            default -> "IDENTIFICADOR";
        };
    }

    private void numero(String codigo) {

        int lineaInicial = linea;
        int columnaInicial = columna;

        StringBuilder numero = new StringBuilder();

        boolean decimal = false;

        while (posicion < codigo.length()) {

            char actual = codigo.charAt(posicion);

            if (Character.isDigit(actual)) {

                numero.append(actual);
                avanzar(codigo);

            } else if (actual == '.' && !decimal) {

                decimal = true;
                numero.append(actual);
                avanzar(codigo);

            } else {
                break;
            }
        }

        agregarToken(
                numero.toString(),
                decimal ? "DECIMAL" : "ENTERO",
                lineaInicial,
                columnaInicial
        );
    }

    private void cadena(String codigo) {

        int lineaInicial = linea;
        int columnaInicial = columna;

        StringBuilder cadena = new StringBuilder();

        cadena.append('"');
        avanzar(codigo);

        boolean cerrada = false;

        while (posicion < codigo.length()) {

            char actual = codigo.charAt(posicion);

            if (actual == '"') {

                cadena.append('"');
                avanzar(codigo);

                cerrada = true;
                break;
            }

            cadena.append(actual);
            avanzar(codigo);
        }

        agregarToken(
                cadena.toString(),
                cerrada ? "CADENA" : "ERROR_CADENA",
                lineaInicial,
                columnaInicial
        );
    }
    private void simbolo(String codigo) {

        int lineaInicial = linea;
        int columnaInicial = columna;

        char actual = codigo.charAt(posicion);

        switch (actual) {

            case '+' -> agregarYAvanzar(
                    codigo, "+", "SUMA",
                    lineaInicial, columnaInicial
            );

            case '-' -> agregarYAvanzar(
                    codigo, "-", "RESTA",
                    lineaInicial, columnaInicial
            );

            case '*' -> agregarYAvanzar(
                    codigo, "*", "MULTIPLICACION",
                    lineaInicial, columnaInicial
            );

            case '/' -> agregarYAvanzar(
                    codigo, "/", "DIVISION",
                    lineaInicial, columnaInicial
            );

            case '%' -> agregarYAvanzar(
                    codigo, "%", "MODULO",
                    lineaInicial, columnaInicial
            );
            case ';' -> agregarYAvanzar(
                    codigo, ";", "PUNTO_COMA",
                    lineaInicial, columnaInicial
            );

            case ',' -> agregarYAvanzar(
                    codigo, ",", "COMA",
                    lineaInicial, columnaInicial
            );

            case '(' -> agregarYAvanzar(
                    codigo, "(", "PARENTESIS_ABRE",
                    lineaInicial, columnaInicial
            );

            case ')' -> agregarYAvanzar(
                    codigo, ")", "PARENTESIS_CIERRA",
                    lineaInicial, columnaInicial
            );

            case '{' -> agregarYAvanzar(
                    codigo, "{", "LLAVE_ABRE",
                    lineaInicial, columnaInicial
            );

            case '}' -> agregarYAvanzar(
                    codigo, "}", "LLAVE_CIERRA",
                    lineaInicial, columnaInicial
            );
            case '=' -> {

                if (siguiente(codigo) == '=') {

                    agregarToken(
                            "==",
                            "IGUAL_QUE",
                            lineaInicial,
                            columnaInicial
                    );

                    avanzar(codigo);
                    avanzar(codigo);

                } else {

                    agregarYAvanzar(
                            codigo,
                            "=",
                            "ASIGNACION",
                            lineaInicial,
                            columnaInicial
                    );
                }
            }
            case '!' -> {

                if (siguiente(codigo) == '=') {

                    agregarToken(
                            "!=",
                            "DIFERENTE",
                            lineaInicial,
                            columnaInicial
                    );

                    avanzar(codigo);
                    avanzar(codigo);

                } else {

                    agregarYAvanzar(
                            codigo,
                            "!",
                            "NEGACION",
                            lineaInicial,
                            columnaInicial
                    );
                }
            }
            case '<' -> {

                if (siguiente(codigo) == '=') {

                    agregarToken(
                            "<=",
                            "MENOR_IGUAL",
                            lineaInicial,
                            columnaInicial
                    );

                    avanzar(codigo);
                    avanzar(codigo);

                } else {

                    agregarYAvanzar(
                            codigo,
                            "<",
                            "MENOR",
                            lineaInicial,
                            columnaInicial
                    );
                }
            }
            case '>' -> {

                if (siguiente(codigo) == '=') {

                    agregarToken(
                            ">=",
                            "MAYOR_IGUAL",
                            lineaInicial,
                            columnaInicial
                    );

                    avanzar(codigo);
                    avanzar(codigo);

                } else {

                    agregarYAvanzar(
                            codigo,
                            ">",
                            "MAYOR",
                            lineaInicial,
                            columnaInicial
                    );
                }
            }
            case '&' -> {

                if (siguiente(codigo) == '&') {

                    agregarToken(
                            "&&",
                            "AND",
                            lineaInicial,
                            columnaInicial
                    );

                    avanzar(codigo);
                    avanzar(codigo);

                } else {

                    agregarYAvanzar(
                            codigo,
                            "&",
                            "ERROR",
                            lineaInicial,
                            columnaInicial
                    );
                }
            }
            case '|' -> {

                if (siguiente(codigo) == '|') {

                    agregarToken(
                            "||",
                            "OR",
                            lineaInicial,
                            columnaInicial
                    );

                    avanzar(codigo);
                    avanzar(codigo);

                } else {

                    agregarYAvanzar(
                            codigo,
                            "|",
                            "ERROR",
                            lineaInicial,
                            columnaInicial
                    );
                }
            }
            default -> {

                agregarYAvanzar(
                        codigo,
                        String.valueOf(actual),
                        "ERROR",
                        lineaInicial,
                        columnaInicial
                );
            }
        }
    }

    private void comentarioLinea(String codigo) {

        while (posicion < codigo.length()
                && codigo.charAt(posicion) != '\n') {

            avanzar(codigo);
        }
    }

    private void comentarioBloque(String codigo) {

        avanzar(codigo); // /
        avanzar(codigo); // *

        while (posicion < codigo.length()) {

            if (codigo.charAt(posicion) == '*'
                    && siguiente(codigo) == '/') {

                avanzar(codigo);
                avanzar(codigo);

                return;
            }

            avanzar(codigo);
        }
    }

    private void agregarYAvanzar(
            String codigo,
            String lexema,
            String tipo,
            int linea,
            int columna) {

        agregarToken(lexema, tipo, linea, columna);
        avanzar(codigo);
    }

    private void agregarToken(
            String lexema,
            String tipo,
            int linea,
            int columna) {

        tokens.add(
                new Token(
                        lexema,
                        tipo,
                        linea,
                        columna
                )
        );
    }

    private char siguiente(String codigo) {

        if (posicion + 1 >= codigo.length()) {
            return '\0';
        }

        return codigo.charAt(posicion + 1);
    }

    private void avanzar(String codigo) {

        if (posicion >= codigo.length()) {
            return;
        }

        char actual = codigo.charAt(posicion);

        if (actual == '\n') {

            linea++;
            columna = 1;

        } else {

            columna++;
        }

        posicion++;
    }
    public List<Token> getTokens() {

        return new ArrayList<>(tokens);
    }
}