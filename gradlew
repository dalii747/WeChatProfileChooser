#!/bin/sh

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"

warn() {
    echo "$*"
} >&2

die() {
    echo
    echo "$*"
    echo
    exit 1
} >&2

case "$(uname)" in
CYGWIN* | MINGW*)
    APP_HOME="$(cygpath --path --mixed "$APP_HOME")"
    GRADLE_USER_HOME="$(cygpath --path --mixed "$GRADLE_USER_HOME")"
    ;;
esac

APP_HOME="$(cd "$(dirname "$0")" && pwd -P)" || die "Cannot determine application directory"

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

exec java -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
