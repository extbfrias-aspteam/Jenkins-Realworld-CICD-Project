![Logo](/uploads/bcc852c8ee1cb4ef0665396349957292/ASP_Logo.png)

# Origen
svn://172.17.7.109/CERO/SPEI_BLU/PRODUCCION/NEGOCIO/1.36.0/ServiciosSTD_WS

#  ReportesCero.war

Descripción del contenido


## Numero de Requerimiento

RQM 999 NOMBRE

## Servidor Contendor

**Pruebas**: 172.17.7.x

**Produccion**: 172.17.8.x

## Objetivos

Objetivos del proyecto


## Deployment

Copiar .war en la ruta

`/opt/wildfly-10.1.0.Final/standalone/deployments`

## Environment Variables

Para correr el proyecto en sus respectivos ambientes modificar:

spring.properties

`HOST.NAME`

`URL`


## Documentacion

[Documentacion](https://linktodocumentation)


## Bases de datos

pg-cero.integraopciones.mx cero
pg-cierre.integraopciones.mx	Bases de Datos de Cierre
pg-izel.integraopciones.mx	Izel
pg-izel-batch.integraopciones.mx	Izel_batch
pg-izelbpm.integraopciones.mx	izelBPM
pg-izelmigracion.integraopciones.mx	izelMigracion
pg-izelsic.integraopciones.mx	izelSIC
pg-izelsti.integraopciones.mx	izelSTI
pg-ldap.integraopciones.mx	Ldap
pg-pld.integraopciones.mx	Pld
pg-pld-asp.integraopciones.mx	Pld_asp
pg-pld-comercializadora.integraopciones.mx	Pld_comercializadora
pg-procrea.integraopciones.mx	Procrea
pg-procrea-correo.integraopciones.mx	Procrea
pg-procrea-extrajudicial.integraopciones.mx	Procrea_extrajudicial
pg-quartz.integraopciones.mx	Quartz
pg-reportesfl.integraopciones.mx	Reportesfl
pg-seguimiento.integraopciones.mx	Seguimiento
pg-seguimiento-scoring.integraopciones.mx	Seguimiento_scoring
pg-mensajeria.integraopciones.mx 	mensajeria
pg-analisis-riesgos.integraopciones.mx	analisis_riesgos  
sq-contpaq.integraopciones.mx	ctopciones_validador
pg-mensajeria.integraopciones.mx 	mensajeria
pg-procrea-temprano.integraopciones.mx	procrea_historico
pg-procrea-tardio.integraopciones.mx	procrea_historico
pg-procrea-respaldo.integraopciones.mx	procrea_historico



## Ejecutar en ambiente de pruebas
Pasos

## Ejecutar Localmente

Clonar el proyecto

```bash
 git clone https://link-to-project
```

Abrir el proyecto en Eclipse

Ejecutar el servidor local WildFly

Ejecutar en el servidor


## Tecnologias

**Servidor:** Spring, Quartz


## Uso/Ejemplos

```java
private static void initialized() {

        try {
            Apps s = Apps.getInstance();
            synchronized (Apps.class) {
                if (apps == null) // si la referencia es null ...
                    apps = s; // ... agrega la clase singleton
            }
            
            cfDao = (CambioFechaSpeiDAO) s.getApplicationContext().getBean("CambioFechaSpeiDAO");
            
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
```


## Usado en...

Proyectos que hacen uso de la aplicación (aplicacion/servicio/proceso) :

- [CeroAhorroWS](https://gitlab.integraopciones.mx/repositorios_asp/servicios/ceroahorrows.war)
- Proyecto 2

## Dependende de...

Proyectos que necesita la aplicación  (aplicacion/servicio/proceso):

- [CeroAhorroWS](https://gitlab.integraopciones.mx/repositorios_asp/servicios/ceroahorrows.war)
- AdminSeg.war
- Proyecto 2
