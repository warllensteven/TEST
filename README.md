

# **Documentación del Proyecto - Soluciones Eficientes S.A.S.**

## **Descripción del Proyecto**

Soluciones Eficientes S.A.S. es una empresa colombiana que ofrece servicios de **outsourcing** en diversas áreas como Tecnología de la Información (TI), Limpieza, Seguridad, Administración de Recursos Humanos y Atención al Cliente. Este proyecto tiene como objetivo desarrollar una **aplicación Java** que permita gestionar y organizar la información de clientes, servicios, contratos, empleados, y proyectos de manera eficiente.

## **Objetivo**

El proyecto incluye:

1. **Modelado de base de datos** con relaciones entre las entidades Cliente, Servicio, Contrato, Empleado, Proyecto y Asignación.
2. **Diagrama de clases UML** que refleja las relaciones entre las diferentes entidades.
3. **Implementación de funcionalidades en Java** para gestionar operaciones CRUD y reportes sobre contratos y servicios.

## **Estructura del Diagrama de Clases UML**

El diagrama de clases UML de este sistema describe las siguientes entidades:

* **Cliente**: Contiene información sobre los clientes de la empresa.
* **Servicio**: Describe los diferentes servicios que la empresa ofrece a los clientes.
* **Contrato**: Representa los contratos entre el cliente y la empresa para la prestación de servicios.
* **Empleado**: Representa a los empleados que trabajan en la empresa, con información sobre su especialidad y proyectos asignados.
* **Proyecto**: Contiene los detalles de los proyectos asignados a los clientes.
* **Asignación**: Representa la relación entre un empleado y un proyecto, con horas trabajadas.

## **Requisitos**

* **StarUML**: Herramienta de modelado UML que utilizarás para visualizar el diagrama de clases.
* **Java**: Lenguaje de programación utilizado para desarrollar la aplicación.
* **MySQL**: Sistema de gestión de bases de datos para almacenar la información.
* **JDBC**: API para conectar Java con la base de datos MySQL.


### 1. **Diagrama entidad relacion**

```

enum Sector {
  Tecnología
  Salud
  Educación
  Comercio
  Manufactura
}

enum Categoria {
  TI
  Limpieza
  Seguridad
  Administración
}

enum EstadoContrato {
  Activo
  En_espera
  Finalizado
}

enum EstadoProyecto {
  En_curso
  Completado
  Cancelado
}

enum Especialidad {
  TI
  Administración
  Limpieza
  Seguridad
}

class Cliente {
  +ID: int
  +nombre: String
  +representante: String
  +correo: String
  +telefono: String
  +direccion: String
  +sector: Sector
  +consultarContratos(): void
  +listarProyectosActivos(): void
}

class Servicio {
  +ID: int
  +nombre: String
  +descripcion: String
  +precioPorHora: double
  +categoria: Categoria
  +calcularCostoTotal(horas: int): double
}

class Contrato {
  +ID: int
  +fechaInicio: Date
  +fechaFin: Date
  +costoTotal: double
  +estado: EstadoContrato
  +verEstadoContrato(): String
  +calcularCosto(): double
}

class Proyecto {
  +ID: int
  +nombre: String
  +fechaInicio: Date
  +fechaFin: Date
  +estado: EstadoProyecto
  +listarEmpleados(): List<Empleado>
  +calcularDuración(): int
}

class Empleado {
  +ID: int
  +nombre: String
  +cargo: String
  +salario: double
  +especialidad: Especialidad
  +asignarProyecto(proyecto: Proyecto): void
  +registrarHorasTrabajadas(horas: int): void
}

class Asignacion {
  +horasTrabajadas: int
  +fechaAsignacion: Date
}

Cliente "1" -- "0..*" Contrato : tiene >
Contrato "1" -- "1" Servicio : contrata >
Cliente "1" -- "0..*" Proyecto : desarrolla >
Proyecto "1" -- "0..*" Asignacion : asigna >
Empleado "1" -- "0..*" Asignacion : participa >


```

### 2. **Diagrama de clases**

```

Clase: Cliente
+ ID: int
+ nombre: String
+ representante: String
+ correo: String
+ telefono: String
+ direccion: String
+ sector: Enum
+ consultarContratos(): void
+ listarProyectosActivos(): void

Clase: Servicio
+ ID: int
+ nombre: String
+ descripcion: String
+ precioPorHora: double
+ categoria: Enum
+ calcularCostoTotal(horas: int): double

Clase: Contrato
+ ID: int
+ cliente: Cliente
+ servicio: Servicio
+ fechaInicio: Date
+ fechaFin: Date
+ costoTotal: double
+ estado: Enum
+ verEstadoContrato(): String
+ calcularCosto(): double

Clase: Empleado
+ ID: int
+ nombre: String
+ cargo: String
+ salario: double
+ especialidad: Enum
+ asignarProyecto(proyecto: Proyecto): void
+ registrarHorasTrabajadas(horas: int): void

Clase: Proyecto
+ ID: int
+ cliente: Cliente
+ nombre: String
+ fechaInicio: Date
+ fechaFin: Date
+ estado: Enum
+ listarEmpleados(): List<Empleado>
+ calcularDuración(): int

Clase: Asignación
+ empleado: Empleado
+ proyecto: Proyecto
+ horasTrabajadas: int
+ fechaAsignacion: Date


```


### 3. **Uso de la Aplicación Java**

* **Registrar un Cliente**: La aplicación permite registrar nuevos clientes, consultarlos y listar los proyectos asociados a cada cliente.
* **Registrar Servicios**: Se puede agregar nuevos servicios, consultar los existentes y calcular los costos asociados.
* **Gestionar Contratos**: Los contratos pueden ser registrados, consultados y actualizados según su estado (activo, en espera, finalizado).
* **Gestionar Proyectos y Empleados**: Los proyectos pueden ser asignados a los empleados, quienes también registran las horas trabajadas.


