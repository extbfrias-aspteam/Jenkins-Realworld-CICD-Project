package com.test;

public class SecretTest {
    // 1. Simular un Token de AWS (Usa el prefijo oficial AKIA que busca SonarQube)
    private static final String AWS_KEY = "AKIAIOSFODNN7EXAMPLE"; 
    private static final String AWS_SECRET = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    // 2. Asignación explícita con alta entropía (texto largo y complejo)
    public void connectDatabase() {
        String db_password = "g8X#mK2!pL9QzWvR4tY7uE1iO0pA3sD6"; // Contraseña compleja hardcodeada
        String apiKey = "ghp_vR4tY7uE1iO0pA3sD6mK2pL9QzWvR4tY7uE1"; // Patrón de Token de GitHub
    }
}