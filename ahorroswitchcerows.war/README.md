![Logo](https://www.aspintegraopciones.com/fr/images/logos/logo_asp_blanco.png)


#  AhorroSwitchCeroWS.war

Descripción del contenido


## Numero de Requerimiento

RQM 880 Plasticos Dock

## Servidor Contendor

**Desarrollo**: 172.17.7.100

**Pruebas**: 172.17.7.183

**Produccion**: 172.17.8.11

## Objetivos

Proporcionar un puente entre el cliente y los servicios o interfaces para poder realizar operaciones de 
consulta de información, depositos, retiros entre cuentas del sistema de ahorro en procrea como los productos 
de mi ahorro, mi cuenta y mi tanda, asi como de productos del sistema de Cero como Mi Debito, Ahorro Simplificada, etc.


## Deployment
- Instalar extensiones o dependencias por medio de maven llamadas WsAsp_client01.jar. ya que son necesarios para la compilación del proyecto.
- Generar ejecutable o compilado .war del proyecto con el nombre AhorroSwitchCeroWS.war por medio de maven o IDE.
- Colocar el .war dentro de su respecitva carpeta dentro del directorio `\\172.17.10.98\sistemas\ACTUALIZACION`
dependiendo del día y el mes en el que vaya a ser la actualización
- Generar un registro dentro de la minuta de aplicaciones vigente para registrar la actualización del aplicativo.
- Registrar el .war en la siguiente ruta dentro del servidor para ser deployado:
`/opt/wildfly-10.1.0.Final/standalone/deployments`
- Esperar por la confirmación de infraestructura cuando la aplicación haya sido publicado en el servidor.

## Environment Variables

Para correr el proyecto en sus respectivos ambientes modificar:

path.properties

`valueWin` = ruta donde se encuentre el archivo ahorro.properties. Ej. `C:/Program Files/wildfly-10.1.0.Final/portal/config`

## Documentacion

[Documentacion](https://linktodocumentation)


## Bases de datos

- N/A


## Ejecutar en ambiente de pruebas

- Generar ejecutable o compilado .war del proyecto con el nombre AhorroSwitchCeroWS.war por medio de maven o IDE.
- Colocar el .war en la siguiente ruta dentro del servidor a deployar:
  `/opt/wildfly-10.1.0.Final/standalone/deployments`

## Ejecutar Localmente

1. Clonar el proyecto

```bash
 git clone git@gitlab.integraopciones.mx:repositorios_asp/plataforma_cero/ahorro/negocio/ahorroswitchcerows.war.git
```
o
```bash
git clone https://gitlab.integraopciones.mx/repositorios_asp/plataforma_cero/ahorro/negocio/ahorroswitchcerows.war.git
```

2. Descargar fuente del proyecto.
3. Instalar extensiones o dependencias por medio de maven llamadas WsAsp_client01.jar. ya que son necesarios para la compilación del proyecto. 
Estos vienen en la carpeta de add-ons y pueden instalarse por medio de los archivos .bat que vienen en el directorio.
4. Configurar entorno de desarrollo para el aplicativo de wildfly 10.1.0 en tu entorno local en caso de ser necesario para pruebas locales.
5. Configurar IDE (Eclipse, IntelliJ, Visual Code, etc.) para trabajar con Wildfly en caso de ser necesario. Si no lo fuera, favor de ignorar este paso.


## Tecnologias

- **Contenedor:** Wildfly 10.1.0.
- **Frameworks:** Spring Framework 4.2.3.RELEASE
- **Lenguaje de programación:** Java 1.8

## Usado en...

Proyectos que hacen uso de la aplicación (aplicacion/servicio/proceso) :

- mule_teller

## Dependende de...

Proyectos que necesita la aplicación  (aplicacion/servicio/proceso):

- AhorroSimplificadaCeroWS
- NucleoCentralCarteraWS
- AhorroCeroWS
- VentanillaFacilWS