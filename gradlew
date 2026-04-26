#!/bin/sh
#
# Gradle wrapper script para Unix
#

# Tenta encontrar o JAVA_HOME se não estiver definido
if [ -z "$JAVA_HOME" ] ; then
    JAVACMD=java
else
    JAVACMD="$JAVA_HOME/bin/java"
fi

APP_HOME="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

exec "$JAVACMD" \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
