# 🏢 Sistema de Reservas de Salas de Coworking

Bienvenido al backend para la reserva de salas de coworking. Este sistema está construido con **Spring Boot** y maneja la administración de **Salas**, **Usuarios** y la orquestación de las **Reservas**, siguiendo estrictamente una lógica de negocio enfocada en validar disponibilidad, topes de tiempo y cálculos monetarios.

## 🗂️ Modelo de Datos (Entidades)

El esquema relacional de la base de datos se rige fielmente por la siguiente relación:  
`Sala (1) --- (N) Reserva (N) --- (1) Usuario`

1. **Sala (`Sala.java`)**: Representa el espacio físico. Contiene atributos detallados como `nombre`, `capacidad` y `precioHora`.
2. **Usuario (`Usuario.java`)**: Representa el cliente que hace la solicitud. Contiene `email` y un atributo fundamental: `esPremium` (boolean).
3. **Reserva (`Reserva.java`)**: La entidad pivote. Registra la `fecha`, `horaInicio`, `horaFin`, y guarda el `totalPagar`. Además, posee las anotaciones y llaves `@ManyToOne` para enlazarse con una `Sala` específica y a un `Usuario` específico.

---

## ⚙️ Lógica de Negocio y Reglas de la Aplicación

Toda la magia y las restricciones matemáticas del negocio ocurren dentro del archivo principal capa de servicio: **`ReservaService.java`**. 

Si revisas su método `crearReserva()`, verás que ejecuta de forma secuencial y limpia los siguientes 5 pasos de validación (basados al 100% en los diagramas de negocio) antes de consolidar el guardado:

### 1. 📅 Impedir reservas para fechas pasadas
* **Qué hace**: Evita incongruencias temporales verificando que nadie intente apartar una sala para el día de ayer.
* **Dónde mirarlo**: Método interno `validarFecha(fecha)`. Utiliza la evaluación contra `LocalDate.now()` para asegurar que la fecha entrante sea al menos la fecha actual.

### 2. ⏳ Validar que la duración no exceda 4 horas (Usuarios No Premium)
* **Qué hace**: Calcula la diferencia entre `horaInicio` y `horaFin`. Si el usuario **no es premium**, lanza un error automático y frena la transacción si la visita sobrepasa el límite estricto de las 4 horas.
* **Dónde mirarlo**: Método interno `validarDuracion(reserva, usuario)`.

### 3. 🛡️ Verificar disponibilidad de Horario (Sin solapamientos)
* **Qué hace**: En lugar de sobrecargar la memoria de la aplicación, aprovecha la gran eficiencia de base de datos llamando a un Query especializado en JPA: `existsBySalaIdAndFechaAndHoraInicioBeforeAndHoraFinAfter`. Esto verifica matemáticamente y al instante que el nuevo intervalo de tiempo solicitado no "cruce" con otra reserva del mismo día.
* **Dónde mirarlo**: Método interno `validarDisponibilidad(request, sala)`.

### 4. 🧮 Calcular el total a pagar
* **Qué hace**: Obtiene el tiempo exacto en medidas fraccionadas (Ej. 2 horas y 30 minutos equivale a un factor de 2.5) y lo multiplica por el valor numérico dinámico (`precioHora`) que cuesta esa sala en particular.
* **Dónde mirarlo**: Método interno `calcularPrecioFinal(reserva, sala, usuario)`.

### 5. 💎 Aplicar 15% de descuento automático (Usuarios Premium)
* **Qué hace**: Si la cuenta pre-liquidada de sala arrojó que el servicio costará $100, inmediatamente el código verifica si `usuario.getEsPremium()` se encuentra activado. Si lo está, multiplica el total por `0.85` (aplicando un preciso 15% de rebaja a la cuenta final de manera imperceptible para el usuario).
* **Dónde mirarlo**: Ejecutado al final del método `calcularPrecioFinal()`.

---

## 🚀 Controladores (La Puerta de Entrada REST)

Toda esta increíble lógica es accesible a internet o a las apps Frontend mediante los Controladores, implementados en el paquete `controller`:

* **`ReservaController.java` (`/api/reservas`)**: Recibe los llamados (GET/POST) cuando alguien presiona "Hacer Reserva" en su app. Recibe el cuerpo JSON y deriva la presión y procesamiento directamente al `ReservaService`.
* **`UsuarioController.java` (`/api/usuarios`)**: Endpoints de gestión para visualizar, crear o promocionar usuarios (hacerlos premium o standard).
* **`SalaController.java` (`/api/salas`)**: Funciones para agregar nuevas salas al sistema de coworking y asignarles su valor nominal y capacidad máxima.

---
Con esta estructura construida bajo **Clean Code** e inyección de dependencias (Controlador -> Servicio -> Repositorio), aseguramos un esquema predecible y blindado, alineado de principio a fin con los requerimientos originales de las salas de Coworking.
