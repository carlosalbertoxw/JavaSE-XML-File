# JavaSE-XML-File

Aplicación de escritorio **Java SE (Swing)** con patrón **MVC** que gestiona un
directorio de personas (CRUD) usando un archivo **XML** como almacenamiento,
manipulado con [JDOM 2](https://www.jdom.org/).

Es un ejercicio de aprendizaje, no una aplicación pensada para producción.

## Características

- Alta, baja, modificación y listado de personas.
- Validación del formulario: el nombre es obligatorio y el correo, si se
  escribe, debe tener un formato básico.
- Persistencia en `persons.xml` (se crea automáticamente en el directorio de
  trabajo la primera vez que se guarda un registro).
- Lectura segura del XML: se rechazan los archivos con `DOCTYPE` para evitar
  ataques XXE.
- Interfaz Swing sencilla organizada según MVC:
  - `mvc.model.Person` — entidad y reglas de validación.
  - `mvc.dao.DAOPerson` — acceso a datos (lectura/escritura del XML).
  - `mvc.view.FramePerson` — ventana Swing.
  - `mvc.controller.ControllerPerson` — orquesta vista y DAO (punto de entrada).

Si `persons.xml` está dañado al arrancar, la aplicación muestra el error y se
cierra sin modificar el archivo.

## Requisitos

- **JDK 25** o superior.
- No necesitas instalar Maven: el proyecto incluye el **Maven Wrapper**
  (`mvnw` / `mvnw.cmd`), que descarga Maven la primera vez.

## Compilar y ejecutar

Con el wrapper (recomendado):

```bash
# Compilar y ejecutar la aplicación
./mvnw compile exec:java        # Windows: .\mvnw.cmd compile exec:java
```

> Si ves *"JAVA_HOME is not defined correctly"*, define `JAVA_HOME` con la ruta
> de tu JDK antes de ejecutar el wrapper.

## Ejecutar las pruebas

```bash
./mvnw test                     # Windows: .\mvnw.cmd test
```

Las pruebas cubren el acceso a datos y la validación, y no necesitan pantalla.
La interfaz Swing no tiene pruebas automáticas.

## Empaquetar un JAR ejecutable

```bash
./mvnw package                  # Windows: .\mvnw.cmd package
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
├── .github/workflows/      # Integración continua
└── src/
    ├── main/java/mvc/
    │   ├── model/Person.java
    │   ├── dao/DAOPerson.java
    │   ├── view/FramePerson.java
    │   └── controller/ControllerPerson.java
    └── test/java/mvc/
        ├── model/PersonTest.java
        └── dao/DAOPersonTest.java
```

El archivo `persons.xml` con los datos se crea en el directorio desde el que
ejecutes la aplicación y **no** se versiona (está en `.gitignore`).

## Licencia

[MIT](LICENSE).
