#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Maven Start Up Batch script
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
#
# Optional ENV vars
# -----------------
#   M2_HOME - location of maven2's installed home dir
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
#     e.g. to debug Maven itself, use
#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ] ; then

  if [ -f /etc/mavenrc ] ; then
    . /etc/mavenrc
  fi

  if [ -f "$HOME/.mavenrc" ] ; then
    . "$HOME/.mavenrc"
  fi

fi

# OS specific support: $must be _win32 on Windows, or $must be "" otherwise.
cygwin=false;
darwin=false;
nonstop=false
case "`uname`" in
  CYGWIN* ) cygwin=true ;;
  Darwin* ) darwin=true ;;
  MINGW* ) nonstop=true ;;
esac

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done

SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Maven"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and MAVEN_OPTS to pass JVM options to this script.
MAVEN_CMD_LINE_ARGS="$MAVEN_CONFIG $@"
MAVEN_OPTS="$MAVEN_OPTS -Xmx512m"

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$cygwin" = "false" -a "$darwin" = "false" -a "$nonstop" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    JAVACMD=`cygpath --unix "$JAVACMD"`
    MAVEN_CMD_LINE_ARGS="$MAVEN_CMD_LINE_ARGS --%"
fi

# For Darwin, add options to specify how the application appears in the dock
if $darwin; then
    MAVEN_OPTS="$MAVEN_OPTS -Xdock:name=$APP_NAME -Xdock:icon=$APP_HOME/media/official.png"
fi

# Determine the Maven Wrapper JAR location
MAVEN_JAR_EXE="$APP_HOME/.mvn/wrapper/maven-wrapper.jar"
if [ -f "$MAVEN_JAR_EXE" ]; then
    chmod 755 "$MAVEN_JAR_EXE" 2>/dev/null
    exec "$JAVACMD" \
        $MAVEN_OPTS \
        $MAVEN_DEBUG_OPTS \
        -classpath "$MAVEN_JAR_EXE" \
        "-Dmaven.home=${M2_HOME}" "-Dmaven.multiModuleProjectDirectory=${APP_HOME}" \
        ${MAVEN_JAR_EXE} "$MAVEN_CMD_LINE_ARGS"
fi

if [ -f "$APP_HOME/.mvn/jvm.config" ] ; then
    . "$APP_HOME/.mvn/jvm.config"
fi

if [ -f "$APP_HOME/.mvn/maven.config" ] ; then
    . "$APP_HOME/.mvn/maven.config"
fi

exec "$JAVACMD" \
    $MAVEN_OPTS \
    $MAVEN_DEBUG_OPTS \
    -classpath "$APP_HOME/.mvn/wrapper/maven-wrapper.jar" \
    "-Dmaven.home=${M2_HOME}" "-Dmaven.multiModuleProjectDirectory=${APP_HOME}" \
    ${MAVEN_JAR_EXE} "$MAVEN_CMD_LINE_ARGS"
