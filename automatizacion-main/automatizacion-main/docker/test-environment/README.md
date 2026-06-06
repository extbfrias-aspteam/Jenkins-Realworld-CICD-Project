# Ambiente de Prueba con Contenedores

Ambiente ligero para probar pipelines de Jenkins con servidores destino reales.

## Arquitectura

```
┌─────────────────────────────────────────────────────┐
│              Red: test-network (bridge)              │
│                                                      │
│  ┌─────────────────┐    ┌──────────────────────┐    │
│  │    Jenkins      │    │       Nexus          │    │
│  │  (orquestador)  │◄──►│  (repositorio)       │    │
│  │   :8080         │    │   :8081              │    │
│  └────────┬────────┘    └──────────────────────┘    │
│           │ SSH                                      │
│    ┌──────┼──────┐                                   │
│    ▼      ▼      ▼                                   │
│  ┌────┐ ┌────┐ ┌────┐                               │
│  │srv1│ │srv2│ │srv3│  ← Ubuntu 22.04 + SSH         │
│  │:22 │ │:22 │ │:22 │                               │
│  └────┘ └────┘ └────┘                               │
└─────────────────────────────────────────────────────┘
```

## Inicio Rápido

```bash
cd docker/test-environment
bash setup/start-test-env.sh
```

El script hace todo automáticamente:
1. Genera claves SSH
2. Construye las imágenes
3. Levanta los contenedores
4. Espera que Jenkins y Nexus estén disponibles
5. Configura la credencial SSH en Jenkins
6. Muestra las URLs de acceso

## Acceso

| Servicio | URL | Usuario | Contraseña |
|----------|-----|---------|------------|
| Jenkins | http://localhost:8080 | admin | admin123 |
| Nexus | http://localhost:8081 | admin | nexus123 |
| Server 1 SSH | ssh -p 2221 deploy@localhost | deploy | (clave SSH) |
| Server 2 SSH | ssh -p 2222 deploy@localhost | deploy | (clave SSH) |
| Server 3 SSH | ssh -p 2223 deploy@localhost | deploy | (clave SSH) |

## Pipeline de Prueba

El pipeline `jenkins/Jenkinsfile-file-transfer` demuestra:

1. **Crear archivo** en target-server-1
2. **Subir a Nexus** como repositorio intermedio
3. **Transferir** server-1 → server-2 (SCP via Jenkins)
4. **Distribuir** Nexus → server-3
5. **Verificar** integridad en todos los destinos

Para usarlo en Jenkins:
1. Ir a Jenkins → New Item → Pipeline
2. En Pipeline Definition: seleccionar "Pipeline script from SCM" o pegar el contenido de `jenkins/Jenkinsfile-file-transfer`

## Comandos Útiles

```bash
# Ver logs en tiempo real
docker compose -f docker-compose.test.yml logs -f

# Ver logs de un servicio específico
docker compose -f docker-compose.test.yml logs -f jenkins-test

# SSH manual a un server destino
ssh -i setup/ssh-keys/jenkins_rsa -p 2221 deploy@localhost

# Apagar el ambiente
docker compose -f docker-compose.test.yml down

# Apagar y borrar volúmenes (reset completo)
docker compose -f docker-compose.test.yml down -v
```

## Estructura de Archivos

```
docker/test-environment/
├── docker-compose.test.yml       # Definición de los contenedores
├── Dockerfile.target-server      # Imagen de los servers destino (SSH)
├── .env.test                     # Variables de entorno
├── jenkins-config/
│   ├── casc.yaml                 # Jenkins Configuration as Code
│   ├── plugins.txt               # Lista de plugins a instalar
│   └── init.groovy.d/            # Scripts de inicialización
└── setup/
    ├── start-test-env.sh         # Script maestro de arranque
    ├── generate-ssh-keys.sh      # Generador de claves SSH
    ├── entrypoint-target.sh      # Entrypoint de los servers destino
    └── ssh-keys/                 # Claves SSH generadas (git-ignorado)
```
