# 💸 ArCashApp

ArCashApp es una billetera virtual desarrollada con tecnologías modernas para gestionar transacciones financieras de forma segura, eficiente y simple.

ArCashApp is a virtual wallet app built with modern technologies to manage financial transactions securely, efficiently, and easily.

---

## 🧰 Tecnologías / Technologies

- Java 21
- Spring Boot (MVC, Data JPA, Security)
- Jakarta EE
- Lombok
- MySQL 8.0+

---

## 🚀 Funcionalidades / Features

- ✅ Gestión de transacciones financieras
- 🔐 Autenticación con JWT
- 📡 API RESTful
- 💾 Persistencia con MySQL

---

## ⚙️ Requisitos / Requirements

- JDK 21
- Maven
- MySQL 8.0 o superior / or later
- IntelliJ IDEA (recomendado / recommended)

---

## 🔧 Instalación y Configuración / Installation & Setup

### 🗃️ Base de Datos / Database

## SQL

+ CREATE DATABASE <DB_NAME>;

+ USE <DB_NAME>;




💻 Aplicación / Application
bash
+ git clone [URL_DEL_REPOSITORIO]
+ cd ArCashApp
+ git init
+ mvn clean install




Edita el archivo application.properties / Edit the application.properties file:

properties
- spring.datasource.url=jdbc:mysql://localhost:3306/arcash_db
- spring.datasource.username=tu_usuario
- spring.datasource.password=tu_contraseña
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


