# 📚 Proyecto de Librería - Portafolio TDD y Testing Ágil

Este repositorio contiene el desarrollo de un proyecto de librería usando Java, SQLite y pruebas automatizadas con TDD (Test Driven Development) como parte del portafolio de los módulos 2 y 3.

---


# 🧩 4️⃣ Integración de Teoría (Módulo 2) con Práctica (Módulo 3)

## ✅ Historia(s) de Usuario

### 🧾 HU01 – Listar libros
**Como** usuario de la librería  
**Quiero** ver todos los libros disponibles  
**Para** conocer el catálogo completo.

**Criterios de aceptación:**
- El sistema debe listar todos los libros con los campos: título, autor, precio y descuento, si aplica.
- El listado debe excluir libros que hayan sido eliminados por falta de stock.
- Se mostrarán los libros listados por orden alfabético del título.

---

### 🔍 HU02 – Buscar por título
**Como** usuario  
**Quiero** buscar un libro por su título  
**Para** encontrarlo fácilmente.

**Criterios de aceptación:**
- La búsqueda debe aceptar coincidencias parciales (e.g. “Cien” devuelve “Cien años de soledad”).
- La búsqueda debe ser insensible a mayúsculas/minúsculas.
- Si no hay resultados, debe mostrarse un mensaje indicando que no se encontraron coincidencias.

---

### 🧑‍🎓 HU03 – Filtrar por autor
**Como** lector  
**Quiero** filtrar libros por autor  
**Para** revisar obras de mi autor favorito.

**Criterios de aceptación:**
- Al ingresar el nombre del autor, se deben mostrar todos sus libros disponibles.
- El filtro debe funcionar con coincidencias parciales del nombre del autor.
- El resultado debe excluir libros sin stock del autor seleccionado.

---

### 💰 HU04 – Modificar precio aplicando descuento
**Como** administrador  
**Quiero** modificar el precio aplicando un descuento  
**Para** actualizar promociones vigentes.

**Criterios de aceptación:**
- El nuevo precio debe calcularse correctamente según el porcentaje de descuento.
- Si el descuento es 0%, el precio original no debe cambiar.
- El descuento no debe ser negativo ni superar el 100% (se debe validar el valor ingresado).

---

### 🗑️ HU05 – Eliminar libro si no hay stock
**Como** administrador  
**Quiero** eliminar un libro del sistema si ya no está en stock  
**Para** mantener la base de datos limpia y actualizada.

**Criterios de aceptación:**
- Si el stock del libro llega a 0, debe eliminarse automáticamente de la base de datos o marcarse como inactivo.
- El libro eliminado ya no debe aparecer en búsquedas ni listados.
- Debe registrarse un log o mensaje de confirmación al eliminar un libro por falta de stock.

---

## ✅ Tipos de Pruebas

- **Unitarias**: pruebas sobre cada operación individual (`createBook`, `getBooks`, etc.).
- **Integración**: pruebas conectando con la base de datos real SQLite.
- **Aceptación**: validación de criterios funcionales completos usando ejemplos de historias de usuario.

---

## ✅ Definición de “Terminado”

- Código funcional implementado con TDD (ciclo Red-Green-Refactor).
- Todas las pruebas pasan correctamente.
- Se obtiene ≥80% de cobertura con JaCoCo.
- Repositorio limpio con commits frecuentes y mensajes descriptivos.
- Refactorizaciones documentadas aplicando principios SOLID.

---

## ✅ Plan de Ejecución de Pruebas durante el Sprint

- Cada historia de usuario se abordará con su respectivo test antes del código (TDD).
- Se integrará la validación con SQLite desde el inicio (usando repositorio y JDBC).
- Se verificará la cobertura y se refactorizará tras cada prueba exitosa.
- Se revisará funcionalidad contra los criterios de aceptación.
- Se ejecutarán pruebas automáticas con `mvn test` y se documentarán los resultados.

---

## ✅ Roles Involucrados en el Sprint

| Rol           | Responsabilidad                                                  |
|----------------|------------------------------------------------------------------|
| Developer      | Implementa lógica, pruebas unitarias y conexión BD              |
| QA             | Revisa criterios, realiza plan de pruebas, ejecuta pruebas de integración |
| Revisor (par)  | Evalúa código y da feedback sobre buenas prácticas              |
| Product owner  | Define historias de usuario, revisa cumplimiento de entregables y criterios |
| Scrum master   | Facilitador para el equipo de desarrollo                         |
