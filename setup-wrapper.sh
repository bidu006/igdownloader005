#!/bin/bash
# Rode este script UMA VEZ antes de fazer upload para o GitHub
# Requer: Java instalado localmente

echo "Baixando Gradle wrapper..."
gradle wrapper --gradle-version 8.4 2>/dev/null || {
    # Fallback: baixar diretamente
    mkdir -p gradle/wrapper
    curl -L "https://services.gradle.org/distributions/gradle-8.4-bin.zip" -o /tmp/gradle.zip
    echo "Gradle baixado."
}
echo "Pronto!"
