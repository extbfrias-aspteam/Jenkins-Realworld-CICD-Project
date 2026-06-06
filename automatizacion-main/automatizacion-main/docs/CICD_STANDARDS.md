# Estándares CI/CD - Infraestructura de Automatización

## Arquitectura general

```
GitLab (código fuente)
       │
       ▼ push/merge request
Jenkins (orquestador CI/CD)
       │
       ├─→ Nexus (artefactos versionados)
       ├─→ SonarQube (calidad de código)
       └─→ Servidores destino (deploy vía SSH/API)
```

---

## Estructura de repositorios Git

Cada proyecto de aplicación debe seguir esta estructura mínima:

```
mi-aplicacion/
├── src/
│   ├── main/java/          ← código fuente
│   ├── main/resources/     ← configuración de la app
│   └── test/java/          ← tests unitarios
├── pom.xml                 ← hereda de parent-pom
├── .gitlab-ci.yml          ← include del template estándar
├── Jenkinsfile             ← pipeline Jenkins
└── owasp-suppressions.xml  ← excepciones de seguridad documentadas
```

**Regla:** No subir al repositorio:
- Archivos `.jar`, `.war`, `.ear` (van a Nexus)
- Archivos `.env`, `config.env`, `application-prod.properties`
- Certificados: `.pem`, `.key`, `.p12`, `.jks`
- El archivo `~/.m2/settings.xml` (tiene credenciales)

---

## Convención de ramas Git (GitFlow)

```
main/master    → código de producción (protegida, solo merge via MR)
develop        → integración continua
feature/*      → nuevas funcionalidades
bugfix/*       → correcciones
release/*      → preparación de release
hotfix/*       → correcciones urgentes en producción
```

---

## Nomenclatura de artefactos en Nexus

### GroupId
Estructura: `com.empresa.<area>.<subaarea-opcional>`

| Area | GroupId |
|------|---------|
| APIs REST | `com.empresa.api` |
| Integraciones Mule | `com.empresa.mule` |
| Servicios batch | `com.empresa.batch` |
| Librerías comunes | `com.empresa.commons` |
| Frontend/Web | `com.empresa.web` |

### ArtifactId
- Solo minúsculas y guiones
- Sin versión en el nombre
- Descriptivo del dominio de negocio

**Correcto:** `facturacion-service`, `inventario-api`, `mule-clientes-integration`
**Incorrecto:** `MiApp`, `app_v2`, `proyecto-final-1.0`

### Versionado (Semantic Versioning)
```
MAJOR.MINOR.PATCH[-SNAPSHOT]
  │     │     └── Bugfix/patch sin cambio de API
  │     └──────── Nueva funcionalidad compatible
  └────────────── Cambio incompatible (breaking change)
```

---

## Configuración de un proyecto nuevo

### 1. Crear POM con herencia del parent

```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <!-- Heredar configuración estándar -->
  <parent>
    <groupId>com.empresa</groupId>
    <artifactId>parent-pom</artifactId>
    <version>1.0.0</version>
    <relativePath/>
  </parent>

  <groupId>com.empresa.api</groupId>
  <artifactId>mi-nueva-app</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>
</project>
```

### 2. Incluir template CI en .gitlab-ci.yml

```yaml
# .gitlab-ci.yml del proyecto
include:
  - project: 'infra-neo/automatizacion'
    ref: main
    file: '/cicd/gitlab/templates/java-maven.gitlab-ci.yml'

# Sobreescribir variables si es necesario
variables:
  ENVIRONMENT: qa
```

Para proyectos Mule:
```yaml
include:
  - project: 'infra-neo/automatizacion'
    ref: main
    file: '/cicd/gitlab/templates/mule.gitlab-ci.yml'
```

### 3. Agregar Jenkinsfile al proyecto

```groovy
// Jenkinsfile
// Cargar pipeline estándar desde la librería compartida
@Library('cicd-shared-library') _

// Usar el pipeline estándar Java (sin necesidad de duplicar lógica)
// Si se requiere personalización, copiar Jenkinsfile.standard-java
// desde cicd/jenkins/pipelines/ y modificar
```

---

## Reglas de calidad que bloquean el pipeline

| Regla | Umbral | Acción al fallar |
|-------|--------|-----------------|
| Cobertura de tests | < 70% | Bloquea el build |
| CVE de seguridad | CVSS >= 7.0 | Bloquea el build |
| Secretos en código | Cualquiera | Bloquea inmediatamente |
| SonarQube Quality Gate | Failed | Bloquea el deploy |
| Certificados en repo | Cualquiera | Bloquea inmediatamente |

---

## Manejo de secretos y configuración sensible

### Regla fundamental
**NUNCA** poner contraseñas, tokens, IPs de producción, claves SSL en el código fuente.

### Dónde guardar los secretos

| Tipo de secreto | Dónde va |
|----------------|----------|
| Credenciales de BD producción | HashiCorp Vault |
| Credenciales de BD QA/staging | Variables CI/CD de GitLab (masked) |
| Tokens de APIs externas | Vault o variables CI/CD |
| Configuración por ambiente | `config-repos/` (repositorio separado) |
| Certificados SSL/TLS | Vault PKI secrets engine |
| Credenciales de despliegue | Credenciales Jenkins (encrypted) |

### En código Java/Mule

```properties
# application.properties - CORRECTO
db.password=${DB_PASSWORD}
api.token=${API_TOKEN}

# application.properties - INCORRECTO
db.password=mipassword123
api.token=abc123def456
```

---

## Proceso de release (poner código en producción)

```
1. Desarrollador crea rama feature/nombre-funcionalidad
2. Desarrollador hace MR hacia develop
3. Pipeline CI ejecuta automáticamente:
   - Escaneo de secretos
   - Build + Tests
   - OWASP + SonarQube
4. Si todo pasa + aprobación de peer review → merge a develop
5. En develop: pipeline publica artefacto SNAPSHOT en Nexus
6. Para release: crear rama release/1.x.x
   - Cambiar version a 1.x.x (sin SNAPSHOT)
   - Pipeline publica artefacto release en Nexus
7. MR de release → main
8. Deploy a producción requiere aprobación manual del grupo 'implementacion'
```

---

## Pipelines disponibles

| Archivo | Tipo | Dónde usar |
|---------|------|-----------|
| `cicd/jenkins/pipelines/Jenkinsfile.standard-java` | Jenkins | Proyectos Java/Maven |
| `cicd/jenkins/pipelines/Jenkinsfile.mule` | Jenkins | Proyectos Mule ESB |
| `cicd/gitlab/templates/java-maven.gitlab-ci.yml` | GitLab CI | Proyectos Java vía GitLab |
| `cicd/gitlab/templates/mule.gitlab-ci.yml` | GitLab CI | Proyectos Mule vía GitLab |

---

## Acceso y permisos

| Grupo | Permisos |
|-------|---------|
| `implementacion` | Deploy a producción, aprobación de releases |
| `developers` | Push a develop/feature, deploy a QA/staging |
| `readonly` | Solo lectura de repositorios y pipelines |

Acceso a Nexus:
- Todos los desarrolladores tienen permiso de **descarga** del grupo `maven-public`
- Solo CI/CD (Jenkins/GitLab Runner) tiene permiso de **publicación**

---

## Monitoreo de artefactos

Generar reporte de versiones en Nexus:
```bash
bash cicd/nexus/scripts/03-generate-version-report.sh \
    http://NEXUS_HOST:8081 usuario password
```

Ver historial de builds en Jenkins: `http://jenkins:8080/job/<nombre-proyecto>`

Ver análisis de calidad: `http://sonarqube:9000/projects`
