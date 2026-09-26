# webapp — Jenkins Build/Test/Package/Deploy Demo

A minimal Java web app with **no external runtime dependencies** (uses the
JDK's built-in `com.sun.net.httpserver`), packaged as a runnable `.jar`.

## Contents

- `webapp-1.0.0.jar` — pre-built, runnable jar (already compiled & tested for you)
- `pom.xml` — Maven project file (build/test/package with `mvn`)
- `Jenkinsfile` — sample declarative pipeline: Build → Test → Package → Deploy
- `src/main/java/...` — application source
- `src/test/java/...` — JUnit 5 tests

## Run it right now

```bash
java -jar webapp-1.0.0.jar        # starts on port 8080
java -jar webapp-1.0.0.jar 9090   # or pick a port
```

Then open http://localhost:8080/ (homepage) and http://localhost:8080/health
(JSON health check, handy as a post-deploy smoke test).

## Using it in Jenkins

1. Put this whole folder in a Git repo (Jenkins pipelines expect source control).
2. Create a Pipeline job pointing at that repo — it will pick up the `Jenkinsfile`.
3. Make sure your Jenkins agent has:
   - a JDK (17+) configured as tool name `jdk17`
   - Maven configured as tool name `maven3`
   - internet access to Maven Central (needed to download JUnit for the Test stage)
   (Adjust the tool names in `Jenkinsfile` to match whatever you've configured
   under *Manage Jenkins → Tools*.)
4. The pipeline stages:
   - **Build** — `mvn compile`
   - **Test** — `mvn test` (JUnit 5), publishes results via `junit` step
   - **Package** — `mvn package`, archives `target/webapp-1.0.0.jar` as a build artifact
   - **Deploy** — placeholder `scp`/`ssh` commands — replace with your real deploy step
     (copy to a server, Docker, Kubernetes, etc.)

## Building manually without Maven

Since the app has zero dependencies, you can also build it with plain `javac`/`jar`
if Maven/internet isn't available:

```bash
javac -d target/classes $(find src/main/java -name "*.java")
jar cfe webapp-1.0.0.jar com.example.webapp.App -C target/classes .
```
