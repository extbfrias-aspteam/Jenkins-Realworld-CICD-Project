# Setup de Entorno para Desarrolladores

## Configuración inicial (una sola vez por laptop)

### 1. Ejecutar el script de configuración automática

```bash
# Clonar el repositorio de automatización (si no lo tienes)
git clone http://GITLAB_HOST/infra-neo/automatizacion.git
cd automatizacion

# Configurar Maven para usar el Nexus corporativo
bash scripts/setup/configure-developer-env.sh NEXUS_HOST 8081 java

# Para proyectos Mule:
bash scripts/setup/configure-developer-env.sh NEXUS_HOST 8081 mule

# Para proyectos Java + Mule:
bash scripts/setup/configure-developer-env.sh NEXUS_HOST 8081 ambos
```

Reemplazar `NEXUS_HOST` con la IP del servidor Nexus (solicitar al equipo infra).

---

### 2. Configurar credenciales

Editar `~/.bashrc` (Linux/Mac) o `~/.zshrc` y agregar:

```bash
export NEXUS_USER="tu-usuario-nexus"
export NEXUS_PASS="tu-password-nexus"

# Opcional - si trabajas con Mule CloudHub
export MULESOFT_USER="tu-usuario-anypoint"
export MULESOFT_PASS="tu-password-anypoint"
```

Recargar:
```bash
source ~/.bashrc
```

**Windows (Git Bash o PowerShell):**
```powershell
[Environment]::SetEnvironmentVariable("NEXUS_USER","tu-usuario","User")
[Environment]::SetEnvironmentVariable("NEXUS_PASS","tu-password","User")
```

---

### 3. Verificar que Maven resuelve desde Nexus

```bash
# Verificar configuración
mvn help:effective-settings | grep -A5 "mirror"

# Probar descarga desde Nexus (en cualquier proyecto Maven)
mvn dependency:resolve -q
```

---

## Flujo de trabajo diario

### Compilar y probar localmente

```bash
# Build completo (compila + tests)
mvn clean verify

# Build rápido sin tests (para desarrollo iterativo)
mvn clean package -DskipTests

# Solo tests
mvn test

# Publicar en Nexus (solo desde branch develop o main)
mvn deploy
```

### Subir una versión a Nexus manualmente

Solo para casos especiales. El proceso normal es via CI/CD automático.

```bash
mvn deploy -DaltDeploymentRepository=nexus-releases::default::http://NEXUS_HOST:8081/repository/maven-releases/
```

---

## Configuración por tipo de proyecto

### Proyectos Java/Maven estándar

El `pom.xml` debe heredar del parent POM corporativo:

```xml
<parent>
  <groupId>com.empresa</groupId>
  <artifactId>parent-pom</artifactId>
  <version>1.0.0</version>
</parent>
```

Si no tienes el parent-pom, descargarlo de Nexus:
```bash
mvn dependency:get \
  -Dartifact=com.empresa:parent-pom:1.0.0:pom \
  -Dtransitive=false
```

### Proyectos Mule ESB

En el `pom.xml` de tu app Mule, agregar el repositorio de Mule:

```xml
<repositories>
  <repository>
    <id>nexus-mule-releases</id>
    <url>http://NEXUS_HOST:8081/repository/mule-releases/</url>
  </repository>
</repositories>
```

Usar siempre variables de entorno para secretos en Mule:
```xml
<!-- En global-config.xml -->
<secure-properties:config name="config" key="${mule.key}" file="app.properties"/>

<!-- En flows -->
<db:config password="${secure::db.password}"/>
```

---

## Descargar la última versión de un artefacto

### Desde Maven (en otro proyecto)

```xml
<!-- En pom.xml del proyecto que necesita el artefacto -->
<dependency>
  <groupId>com.empresa.api</groupId>
  <artifactId>mi-servicio</artifactId>
  <version>LATEST</version>
</dependency>
```

### Desde curl/wget

```bash
# Ver versiones disponibles
curl -u $NEXUS_USER:$NEXUS_PASS \
  "http://NEXUS_HOST:8081/service/rest/v1/components?repository=maven-releases&name=mi-servicio"

# Descargar versión específica
curl -u $NEXUS_USER:$NEXUS_PASS -O \
  "http://NEXUS_HOST:8081/repository/maven-releases/com/empresa/api/mi-servicio/1.2.0/mi-servicio-1.2.0.jar"
```

### Ver reporte de versiones completo

```bash
bash cicd/nexus/scripts/03-generate-version-report.sh \
  http://NEXUS_HOST:8081 $NEXUS_USER $NEXUS_PASS
```

---

## Troubleshooting rápido

**Maven descarga desde Internet en vez de Nexus:**
```bash
# Verificar mirror en settings.xml
cat ~/.m2/settings.xml | grep -A3 mirror
# Si no está configurado, re-ejecutar el script de setup
bash scripts/setup/configure-developer-env.sh NEXUS_HOST 8081 java
```

**Error 401 al hacer deploy:**
```bash
# Verificar credenciales
echo $NEXUS_USER && echo $NEXUS_PASS
# Verificar que el settings.xml usa ${env.NEXUS_USER}
grep NEXUS_USER ~/.m2/settings.xml
```

**"Could not resolve dependencies" al compilar:**
```bash
# Verificar que Nexus está disponible
curl -s http://NEXUS_HOST:8081/service/rest/v1/status
# Limpiar caché local y reintentar
mvn clean -U install
```

**Artefacto no aparece en Nexus después de deploy:**
```bash
# Verificar que el deploy fue exitoso en los logs
mvn deploy -X 2>&1 | grep -i "upload\|deploy\|error"
```

---

## Recursos

- Nexus UI: `http://NEXUS_HOST:8081`
- Jenkins: `http://JENKINS_HOST:8080`
- GitLab: `http://GITLAB_HOST`
- SonarQube: `http://SONAR_HOST:9000`
- Guía de migración: `docs/MIGRATION_GUIDE.md`
- Estándares CI/CD: `docs/CICD_STANDARDS.md`
