# TANI-back

Backend del sistema de comercio electrónico para la tienda de calzado **TANI**. Esta aplicación gestiona la autenticación de usuarios, administración de productos, procesamiento de pedidos y notificaciones, integrando tecnologías modernas y buenas prácticas de desarrollo backend con **Spring Boot**.

## 🚀 Descripción General

Este backend forma parte de una solución web completa diseñada para pequeñas y medianas empresas que buscan digitalizar su canal de ventas. El sistema permite:

- Registro y autenticación segura de usuarios (clientes y administradores)
- Gestión de productos, inventario y pedidos
- Envío de notificaciones y correos electrónicos
- Integración con frontend en Angular mediante API REST

## 🧱 Arquitectura

El backend está desarrollado en **Spring Boot** siguiendo el patrón de **arquitectura en capas** (controladores, servicios, repositorios) y el principio de diseño limpio.

### Componentes Principales

- **Controladores (Controllers)**: Manejan las solicitudes HTTP.
- **Servicios (Services)**: Contienen la lógica de negocio.
- **Repositorios (Repositories)**: Acceso a la base de datos (MySQL).
- **DTOs**: Separación entre entidades y datos expuestos al cliente.
- **Seguridad**: Implementación con JWT y cifrado de contraseñas.
- **Notificaciones**: Envío vía Firebase Cloud Messaging.
- **Correo electrónico**: Envío transaccional vía SMTP.

## 🛠️ Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL
- Firebase Admin SDK
- SMTP (Correo electrónico)
- Gradle

## 🔒 Seguridad

- Autenticación basada en JWT (Bearer Token)
- Cifrado de contraseñas
- Validaciones y filtros de seguridad
- Control de acceso por roles (cliente / administrador)
- HTTPS recomendado

## 📁 Endpoints Principales

| Recurso             | Descripción                                    |
|---------------------|------------------------------------------------|
| `/auth`             | Registro, login, recuperación de contraseña    |
| `/usuarios`         | Gestión de usuarios                            |
| `/productos`        | Consultas y administración de productos        |
| `/pedidos`          | Creación y consulta de pedidos                 |
| `/notificaciones`   | Envío y consulta de notificaciones             |

## 🧪 Pruebas

El proyecto incluye pruebas unitarias y de integración para validar el correcto funcionamiento de los módulos clave.

## 📚 Requisitos Funcionales Cubiertos

- Registro y login con validación de email
- Recuperación de contraseña segura
- Gestión de roles y permisos
- Proceso de compra con carrito y actualización de inventario
- Visualización personalizada del sistema según el rol del usuario

## 📋 Requisitos No Funcionales

- Alta disponibilidad (>99.5%)
- Interfaz segura y eficiente
- Escalabilidad y mantenibilidad
- Documentación y modularidad del código

## ✍️ Autores

- Miluzka Mary Saire Cusicuna  
- José Manuel Rojas Tovar  
- Óscar Orlando Peteví López
- Holdan Norbey López Segura

Proyecto desarrollado para la asignatura de **Ingeniería de Software III** – Universidad del Quindío.

---

