# DumpTomcatSessions

[![CI](https://github.com/mirchr/DumpTomcatSessions/actions/workflows/ci.yml/badge.svg)](https://github.com/mirchr/DumpTomcatSessions/actions/workflows/ci.yml)

A simple command line utility to dump Tomcat sessions using [JMX](https://tomcat.apache.org/tomcat-8.0-doc/monitoring.html#Enabling_JMX_Remote) for fun or profit.

## Usage

```
java -jar DumpTomcatSessions-<version>-jdk<N>.jar server port [user password]
```

Each release publishes one jar per supported JDK (21, 22, 23, 24, 25). Pick the
classifier matching your installed JDK — a `-jdk25` jar will not run on JDK 21,
but a `-jdk21` jar runs on every JDK from 21 up. The JDK each jar was built
against is also recorded in its manifest as `Build-Jdk-Spec`.

Exit codes: `0` success, `1` invalid arguments, `2` connection or JMX error.

## Example

```
$ java -jar DumpTomcatSessions-1.0.0-jdk21.jar 192.168.1.2 1099
Catalina:type=Manager,context=/examples,host=localhost total sessions = 5
3D92A3204B58158299BC70B8E51F668B
E7C65D73EF4D887ACCD346E54E1AAB90
22654CB846F07225C7F3B5F74A2330C0
F57BDBD69C21AE9A049997D85A110458
C3820136711E9BFC01752AFE10C6D75F
Catalina:type=Manager,context=/manager,host=localhost total sessions = 1
87D328E47EECF9C710955ACDD134D931
Catalina:type=Manager,context=/sample,host=localhost total sessions = 3
3A033B7E8DE92A720DBDCE49419BFC2D
2EB4916DAC717C9E2670CF30CAAC447D
9F3069539D9169F466C92656530A642C
```

## Building from source

Requires a JDK between 21 and 25. Gradle's toolchain auto-provisions the
requested JDK via [Foojay](https://foojay.io/) if you don't have it installed.

Build for the default JDK (25):

```
./gradlew build
```

Build against a specific JDK:

```
./gradlew build -PjdkVersion=21
```

The runnable jar is written to `build/libs/DumpTomcatSessions-<version>-jdk<N>.jar`.

## Releasing

Releases are produced by GitHub Actions on tag push:

```
git tag v1.0.0
git push origin v1.0.0
```

The [`Release`](.github/workflows/release.yml) workflow runs a matrix build across JDK 21, 22, 23, 24, and 25 in parallel, then creates a single GitHub Release with auto-generated notes and all five jars attached (one per JDK).
