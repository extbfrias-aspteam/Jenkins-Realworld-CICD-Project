# Guía de Migración: De laptops a Nexus

## Objetivo

Centralizar todos los artefactos Java/Maven/Mule compilados que están actualmente
en las laptops de los técnicos en el repositorio Nexus corporativo, estableciendo
un control de versiones único y accesible para todo el equipo.

---

## Resumen del proceso

```
Laptop técnico                 Servidor Nexus
─────────────          →       ──────────────────────
*.jar / *.war / *.ear          maven-releases/
                               maven-snapshots/
                               mule-releases/
                               legacy-artifacts/
```

El proceso tiene **3 pasos**:

1. **Escanear** la laptop para encontrar todos los artefactos
2. **Revisar** el CSV generado y ajustar metadatos si es necesario
3. **Subir** los artefactos a Nexus en lote

---

## Prerrequisitos

| Herramienta | Versión mínima | Verificar con |
|-------------|---------------|---------------|
| Maven       | 3.6+          | `mvn -version` |
| Java JDK    | 8+            | `java -version` |
| curl        | cualquiera    | `curl --version` |
| unzip       | cualquiera    | `unzip -v` |

Datos necesarios del servidor Nexus:
- URL: `http://NEXUS_HOST:8081`
- Usuario y contraseña de Nexus (solicitarlos al equipo de infraestructura)

---

## Paso 1: Configurar entorno local

Antes de subir artefactos, configurar el `settings.xml` de Maven para
que apunte al servidor Nexus corporativo:

```bash
# Desde la raíz del repositorio de automatización:
cd scripts/setup/
bash configure-developer-env.sh 192.168.1.100 8081 java
```

Reemplazar `192.168.1.100` con la IP real del servidor Nexus.

Para proyectos Mule:
```bash
bash configure-developer-env.sh 192.168.1.100 8081 mule
```

Configurar las credenciales en el archivo `~/.bashrc` o `~/.zshrc`:
```bash
export NEXUS_USER="mi-usuario"
export NEXUS_PASS="mi-password"
source ~/.bashrc
```

---

## Paso 2: Escanear la laptop

```bash
cd scripts/migration/
bash 01-scan-local-artifacts.sh /home/miusuario artefactos.csv
# Windows (Git Bash): bash 01-scan-local-artifacts.sh /c/Users/miusuario artefactos.csv
```

El script genera un CSV con columnas:
```
ruta_completa, nombre_archivo, extension, tamanio,
group_id, artifact_id, version, repo_nexus_destino
```

**Ejemplo de salida:**
```csv
"/home/juan/proyectos/mi-app/target/mi-app-1.2.jar","mi-app-1.2.jar","jar","2.3M","com.empresa","mi-app","1.2","maven-releases"
"/home/juan/builds/api-rest-2.0-SNAPSHOT.jar","api-rest-2.0-SNAPSHOT.jar","jar","1.1M","com.empresa","api-rest","2.0-SNAPSHOT","maven-snapshots"
"/home/juan/mule-apps/integracion.jar","integracion.jar","jar","5.0M","com.empresa.mule","integracion","1.0","mule-releases"
```

---

## Paso 3: Revisar y corregir el CSV

Abrir el CSV en Excel o un editor de texto. Verificar:

| Columna | Qué revisar |
|---------|-------------|
| `group_id` | Debe seguir el patrón: `com.empresa.nombre-area` |
| `artifact_id` | Nombre del artefacto sin versión ni extensión |
| `version` | Formato: `1.0.0` (release) o `1.0.0-SNAPSHOT` (desarrollo) |
| `repo_nexus_destino` | Ver tabla de repositorios abajo |

### Repositorios disponibles en Nexus

| Repositorio | Cuándo usar |
|-------------|-------------|
| `maven-releases` | Versiones estables de apps Java (`1.0.0`, `2.3.1`) |
| `maven-snapshots` | Versiones en desarrollo (`1.0.0-SNAPSHOT`) |
| `mule-releases` | Apps Mule ESB estables |
| `mule-snapshots` | Apps Mule en desarrollo |
| `legacy-artifacts` | Artefactos sin metadata Maven (última opción) |

### Convención de versiones

Usar **Semantic Versioning**: `MAJOR.MINOR.PATCH`

```
1.0.0   → Primera versión estable
1.0.1   → Bugfix sin cambios de API
1.1.0   → Nueva funcionalidad compatible
2.0.0   → Cambio incompatible (breaking change)
1.0.0-SNAPSHOT → En desarrollo
```

Si se desconoce la versión real del artefacto, usar:
- `1.0.0-legacy` para artefactos sin historial de versiones conocido

---

## Paso 4: Subir artefactos a Nexus

### Carga masiva (desde el CSV):
```bash
bash 02-bulk-upload.sh artefactos.csv http://192.168.1.100:8081 mi-usuario mi-password
```

El script:
- Verifica si el artefacto ya existe (evita duplicados)
- Sube artefactos Maven con coordenadas correctas (`mvn deploy:deploy-file`)
- Sube artefactos sin metadata al repositorio `legacy-artifacts`
- Genera log con resultado de cada artefacto

### Subida individual (modo interactivo):
```bash
bash 02-bulk-upload.sh
```
El script pedirá los datos por pantalla.

### Subida manual con Maven (un solo artefacto):
```bash
mvn deploy:deploy-file \
    -DgroupId=com.empresa.miapp \
    -DartifactId=mi-aplicacion \
    -Dversion=1.0.0 \
    -Dpackaging=jar \
    -Dfile=/ruta/al/mi-aplicacion-1.0.0.jar \
    -DrepositoryId=nexus-releases \
    -Durl=http://192.168.1.100:8081/repository/maven-releases/
```

---

## Paso 5: Verificar que el artefacto está en Nexus

1. Acceder a Nexus: `http://192.168.1.100:8081`
2. Login con credenciales
3. Browse → Seleccionar repositorio
4. Navegar hasta el artefacto por `groupId/artifactId/version`

O verificar con curl:
```bash
curl -u usuario:password \
  "http://192.168.1.100:8081/service/rest/v1/components?repository=maven-releases" \
  | python3 -m json.tool | grep "name\|version"
```

---

## Generar reporte de versiones

Para ver todos los artefactos publicados en Nexus:

```bash
cd cicd/nexus/scripts/
bash 03-generate-version-report.sh http://192.168.1.100:8081 usuario password
```

El reporte se guarda en `version-report-YYYYMMDD-HHMMSS.txt`.

---

## Troubleshooting

### Error 401 Unauthorized
```
Causa: Credenciales incorrectas
Solución: Verificar usuario/password en el settings.xml y variables de entorno
```

### Error 400 Bad Request al subir
```
Causa: groupId/artifactId/version con caracteres inválidos
Solución: Solo letras, números, puntos, guiones. Sin espacios ni caracteres especiales
```

### El artefacto ya existe y no se puede re-subir a maven-releases
```
Causa: Nexus no permite sobreescribir releases (correcto por diseño)
Solución: Incrementar la versión. Los releases son inmutables.
```

### Maven no puede resolver dependencias desde Nexus
```
Causa: settings.xml no actualizado o Nexus no accesible
Solución:
  1. Verificar: mvn help:effective-settings | grep mirror
  2. Verificar conectividad: curl http://NEXUS_HOST:8081/service/rest/v1/status
  3. Re-ejecutar: bash scripts/setup/configure-developer-env.sh
```

---

## Contacto

Para dudas sobre el proceso de migración, contactar al equipo de infraestructura.
