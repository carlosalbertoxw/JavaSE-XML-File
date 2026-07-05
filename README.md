# JavaSE-XML-File

Aplicación de escritorio **Java SE (Swing)** con patrón **MVC** que gestiona un
directorio de personas (CRUD) usando un archivo **XML** como almacenamiento,
manipulado con [JDOM 2](http://www.jdom.org/).

## Características

- Alta, baja, modificación y listado de personas.
- Persistencia en `persons.xml` (se crea automáticamente en el directorio de
  trabajo la primera vez que se guarda un registro).
- Interfaz Swing sencilla organizada según MVC:
  - `mvc.model.Person` — entidad.
  - `mvc.dao.DAOPerson` — acceso a datos (lectura/escritura del XML).
  - `mvc.view.FramePerson` — ventana Swing.
  - `mvc.controller.ControllerPerson` — orquesta vista y DAO (punto de entrada).

## Requisitos

- **JDK 17** o superior.
- No necesitas instalar Maven: el proyecto incluye el **Maven Wrapper**
  (`mvnw` / `mvnw.cmd`), que descarga Maven la primera vez.

## Compilar y ejecutar

Con el wrapper (recomendado):

```bash
# Compilar
./mvnw compile          # Windows: mvnw.cmd compile

# Ejecutar la aplicación
./mvnw exec:java        # Windows: mvnw.cmd exec:java
```

> En Windows con Git Bash, si ves *"JAVA_HOME is not defined correctly"*, exporta
> la ruta del JDK antes de ejecutar, por ejemplo:
> `export JAVA_HOME="C:\Program Files\Java\jdk-26.0.1"`.
> Usando `mvnw.cmd` desde PowerShell/CMD normalmente no hace falta.

## Empaquetar un JAR ejecutable

```bash
./mvnw package
java -jar target/javase-xml-file.jar
```

El plugin `maven-shade-plugin` genera `target/javase-xml-file.jar` con las
dependencias incluidas (JDOM), listo para ejecutar con doble clic o `java -jar`.

## Estructura del proyecto

```
.
├── pom.xml                 # Configuración Maven (dependencias, plugins)
├── mvnw / mvnw.cmd         # Maven Wrapper
├── .mvn/wrapper/           # Configuración del wrapper
└── src/main/java/mvc/
    ├── model/Person.java
    ├── dao/DAOPerson.java
    ├── view/FramePerson.java
    └── controller/ControllerPerson.java
```

El archivo `persons.xml` con los datos se crea junto al directorio desde el que
ejecutes la aplicación y **no** se versiona (está en `.gitignore`).
