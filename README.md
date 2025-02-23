# Sistema de Gestión de Transacciones y Categorías

Este sistema permite gestionar transacciones y categorías con una arquitectura basada en **microservicios**, utilizando **Spring Boot**, **Vue.js**, **Docker Compose** y bases de datos **MySQL**.

## **Requisitos Previos**
Antes de desplegar el sistema, asegúrate de cumplir con los siguientes requisitos:

### **1. Instalación de Docker Desktop**

#### **Windows (Intel / AMD)**
1. Descarga Docker Desktop desde: [Docker Desktop](https://www.docker.com/products/docker-desktop/)
2. Instala Docker Desktop y habilita la opción **"Use WSL 2 instead of Hyper-V"** si está disponible.
3. Reinicia el sistema después de la instalación.

#### **Mac (Intel / Apple Silicon - M1, M2, M3)**
1. Descarga la versión de Docker Desktop adecuada para tu procesador desde: [Docker Desktop para Mac](https://www.docker.com/products/docker-desktop/)
2. Instala la aplicación y sigue las instrucciones.
3. Asegúrate de que Docker esté ejecutándose antes de continuar.

#### **Linux**
1. Sigue las instrucciones oficiales para tu distribución desde: [Docker Engine](https://docs.docker.com/engine/install/)
2. Instala `docker-compose` con:
   ```bash
   sudo apt install docker-compose -y
   ```
3. Agrega tu usuario al grupo Docker (opcional):
   ```bash
   sudo usermod -aG docker $USER
   ```
4. Reinicia la terminal o la máquina.

### **2. Habilitar Virtualización en BIOS**
Si Docker no funciona correctamente, verifica que la virtualización esté habilitada en tu BIOS:

#### **Intel**
1. Reinicia tu PC y entra en la BIOS (presionando `F2`, `F10` o `DEL` al inicio).
2. Busca una opción llamada **"Intel VT-x"** o **"Intel Virtualization Technology"** y habilítala.
3. Guarda los cambios y reinicia el equipo.

#### **AMD**
1. Reinicia tu PC y accede a la BIOS (`F2`, `F10` o `DEL` al iniciar).
2. Busca la opción **"SVM Mode"** (Secure Virtual Machine) y habilítala.
3. Guarda los cambios y reinicia.

#### **Apple Mac (M1/M2/M3)**
Apple Silicon usa virtualización nativa y no requiere configuraciones adicionales.

---

Configuración del Entorno para AWS

Si deseas desplegar este proyecto en AWS, necesitas modificar las variables de entorno en docker-compose.yml.

1. Modificar Variables en docker-compose.yml

Abre el archivo docker-compose.yml y edita los valores de las siguientes variables en la sección environment de cada servicio:

Microservicio de Categorías

  lotto-category:
    environment:
      SERVER_PORT: 8081
      SPRING_DATASOURCE_URL: jdbc:mysql://<AWS_RDS_CATEGORIES_ENDPOINT>:<AWS_RDS_CATEGORIES_PORT>/lotto_categories
      SPRING_DATASOURCE_USERNAME: <TU_USUARIO_CATEGORIES>
      SPRING_DATASOURCE_PASSWORD: <TU_CONTRASEÑA_CATEGORIES>
      SPRING_APPLICATION_NAME: lotto-category
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://<AWS_EUREKA_ENDPOINT>:8761/eureka/

Microservicio de Transacciones

  lotto-transaction:
    environment:
      SERVER_PORT: 8082
      SPRING_DATASOURCE_URL: jdbc:mysql://<AWS_RDS_TRANSACTIONS_ENDPOINT>:<AWS_RDS_TRANSACTIONS_PORT>/lotto_transactions
      SPRING_DATASOURCE_USERNAME: <TU_USUARIO_TRANSACTIONS>
      SPRING_DATASOURCE_PASSWORD: <TU_CONTRASEÑA_TRANSACTIONS>
      SPRING_APPLICATION_NAME: lotto-transaction
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://<AWS_EUREKA_ENDPOINT>:8761/eureka/

Nota: Reemplaza <AWS_RDS_CATEGORIES_ENDPOINT>, <AWS_RDS_CATEGORIES_PORT>, <AWS_RDS_TRANSACTIONS_ENDPOINT>, <AWS_RDS_TRANSACTIONS_PORT>, <TU_USUARIO_CATEGORIES>, <TU_CONTRASEÑA_CATEGORIES>, <TU_USUARIO_TRANSACTIONS>, <TU_CONTRASEÑA_TRANSACTIONS>, y <AWS_EUREKA_ENDPOINT> con los valores correspondientes en AWS.

2. Desplegar en AWS ECS con Docker Compose

Si deseas desplegar en AWS ECS (Elastic Container Service), sigue estos pasos:

Instala la CLI de AWS si aún no la tienes:

curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o "AWSCLIV2.pkg"
sudo installer -pkg AWSCLIV2.pkg -target /

Inicia sesión en AWS:

aws configure

Crea un repositorio en Amazon Elastic Container Registry (ECR):

aws ecr create-repository --repository-name lotto-project

Construye y sube la imagen del frontend al ECR:

docker build -t lotto-frontend .
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com
docker tag lotto-frontend:latest <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/lotto-frontend
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/lotto-frontend

Ejecuta los contenedores en ECS:

aws ecs create-cluster --cluster-name lotto-cluster
aws ecs create-service --cluster lotto-cluster --service-name lotto-frontend --task-definition lotto-frontend


## **Despliegue del Proyecto**

### **1. Clonar el Repositorio**
```bash
git clone https://github.com/Trochez/lotto_test.git
git checkout master
cd lotto_test
```

### **2. Construir y Ejecutar los Contenedores**
Ejecuta el siguiente comando para construir y desplegar todos los servicios:
```bash
docker-compose up --build
```
Esto iniciará los siguientes contenedores:
- **Eureka Server** (`http://localhost:8761`)
- **Base de datos de categorías (MySQL, puerto 3307)**
- **Base de datos de transacciones (MySQL, puerto 3308)**
- **Microservicio de Categorías (`http://localhost:8081`)**
- **Microservicio de Transacciones (`http://localhost:8082`)**
- **Frontend Vue.js (`http://localhost:8080`)**

### **3. Verificar el Estado de los Servicios**
Puedes verificar los contenedores con:
```bash
docker ps
```
Si algún contenedor falló, revisa los logs con:
```bash
docker-compose logs -f <nombre_del_contenedor>
```

---

## **Uso del Sistema**

### **Acceder al Frontend**
Abre en el navegador:
```
http://localhost:8080
```
Desde aquí puedes realizar las siguientes acciones:
- **Gestión de Categorías:** Crear, listar y consultar categorías.
- **Gestión de Transacciones:** Crear, listar, filtrar y actualizar transacciones.
- **Ejecutar Seeders:** Insertar datos de prueba automáticamente.
- **Nota:** Las funciones de crear y actualizar pueden presentar inconvenientes debido a las politicas de CORS, sin embargo, estas funcionalidades se pueden probar con la interfaz swagger

### **Acceder a Swagger para Ver la API**
Cada microservicio tiene una interfaz Swagger disponible en:
- **Categorías:** [`http://localhost:8081/swagger-ui.html`](http://localhost:8081/swagger-ui.html)
- **Transacciones:** [`http://localhost:8082/swagger-ui.html`](http://localhost:8082/swagger-ui.html)

---

## **Solución de Problemas**

### **Error: "ERR_NAME_NOT_RESOLVED" en el frontend**
 **Solución:** Modifica las URLs en `categories.js` y `transactions.js` para apuntar a `http://localhost:8081` y `http://localhost:8082` en lugar de nombres de contenedor.

### **Error: "Address already in use" al ejecutar Docker Compose**
 **Solución:** Verifica qué servicios están usando los puertos con:
```bash
netstat -ano | findstr :8080
```
Luego, detén el proceso en uso:
```bash
taskkill /PID <PID> /F
```

### **Error: "Database connection refused" en los microservicios**
 **Solución:** Asegúrate de que las bases de datos estén levantadas con:
```bash
docker-compose ps
```
Si MySQL no está corriendo, reinicia los contenedores con:
```bash
docker-compose restart category-db transaction-db
```

---

## **Comandos Útiles**
| Comando | Descripción |
|---------|------------|
| `docker-compose up --build` | Construye y ejecuta todos los servicios |
| `docker-compose down` | Apaga y elimina todos los contenedores |
| `docker ps` | Lista los contenedores en ejecución |
| `docker logs -f <contenedor>` | Ver logs de un contenedor en tiempo real |
| `docker exec -it <contenedor> bash` | Acceder a la terminal de un contenedor |

---

Ejemplos de Uso de los Endpoints

Una vez desplegados los microservicios, puedes probar los endpoints con curl o Postman.

1. Obtener todas las categorías

curl -X GET http://localhost:8081/categories

2. Crear una nueva categoría

curl -X POST http://localhost:8081/categories \
     -H "Content-Type: application/json" \
     -d '{"name": "Entretenimiento"}'

3. Obtener todas las transacciones

curl -X GET http://localhost:8082/transactions

4. Crear una nueva transacción

curl -X POST http://localhost:8082/transactions \
     -H "Content-Type: application/json" \
     -d '{"card_number": 123456, "amount": 50.0, "date": "2025-02-23", "status": "PENDIENTE", "categoryId": 1}'

5. Filtrar transacciones por estado

curl -X GET http://localhost:8082/transactions/status/PENDIENTE

6. Filtrar transacciones por categoría

curl -X GET http://localhost:8082/transactions/category/1

7. Ejecutar Seeder de Categorías

curl -X GET http://localhost:8081/category-seeder/seed

8. Ejecutar Seeder de Transacciones

curl -X GET http://localhost:8082/transaction-seeder/seed

---

Ejecución de Pruebas Unitarias

Para ejecutar las pruebas unitarias de los microservicios, usa los siguientes comandos dentro del contenedor o en la máquina local si tienes el entorno configurado:

Ejecutar pruebas en el microservicio de categorías

docker-compose exec lotto-category mvn test

Ejecutar pruebas en el microservicio de transacciones

docker-compose exec lotto-transaction mvn test

Ejecutar pruebas en todos los servicios de forma manual. Para este comando es necesario estar dentro de la carpeta del proyecto especifico (ej: proyecto_lotto/lotto_category/) y reemplazar las variables de entorno definidas en el archvo proyecto_lotto/lotto_category/src/main/resources/application.properties por los valores finales o definitivos

mvn test

Si prefieres ejecutar las pruebas fuera de Docker, dentro del proyecto de cada servicio:

Justificación del Lenguaje Backend

Elección de Java con Spring Boot

El backend del sistema ha sido implementado en Java con Spring Boot, debido a las siguientes razones:

Rendimiento y Eficiencia

Java es un lenguaje compilado con ejecución en la Java Virtual Machine (JVM), lo que permite una optimización del rendimiento y una ejecución eficiente del código.

Spring Boot optimiza el manejo de recursos con un sistema de administración de hilos eficiente y la posibilidad de utilizar servidores embebidos como Tomcat, Jetty o Undertow.

Escalabilidad

Spring Boot facilita el desarrollo de microservicios, permitiendo la escalabilidad horizontal mediante la ejecución de múltiples instancias de la aplicación en contenedores Docker o Kubernetes.

Se integra fácilmente con Eureka Server, lo que permite un descubrimiento de servicios eficiente y balanceo de carga dinámico.

Compatibilidad con el Entorno del Proyecto

Spring Boot es altamente compatible con Docker y AWS, permitiendo una configuración simplificada para despliegues en la nube.

Existen múltiples herramientas y bibliotecas para la integración con bases de datos, colas de mensajes (RabbitMQ, Kafka) y sistemas de monitoreo (Prometheus, Grafana).

Naturaleza del proyecto

El proyecto demanda representacion de entidades puntuales como transacciones y categorias, estas entidades se pueden implementar facilmente en java debido a que este lenguaje presenta una gran facilidad para orientarse a objetos, adicionalmente, los objetos representados se pueden persistir facilmente en la base de datos mediante el uso de librerias ya contruidas

Mantenimiento y Soporte

Java es un lenguaje con soporte a largo plazo (LTS), lo que garantiza estabilidad y actualizaciones constantes.

Spring Boot cuenta con una gran comunidad de desarrolladores y documentación extensiva para resolver cualquier problema.

No tiene licencia.

La version openjdk permite utilizar java sin necesidad de pagar una licencia evitando estos costos

Justificación de la Base de Datos Utilizada

Elección de MySQL

Se ha seleccionado MySQL como base de datos relacional por las siguientes razones:

Rendimiento y Optimización

Soporta índices avanzados como B-Tree y Hash, optimizando las consultas de alto volumen.

Utiliza InnoDB como motor de almacenamiento, lo que garantiza transacciones ACID y alta consistencia de datos.

Escalabilidad y Distribución

MySQL permite replicación maestro-esclavo, lo que facilita la escalabilidad en entornos de producción.

Compatible con AWS RDS, lo que permite un escalado automatizado y gestión eficiente en la nube.

Compatibilidad con Spring Boot

Spring Boot ofrece soporte nativo para Spring Data JPA, permitiendo la interacción con MySQL de manera eficiente sin escribir consultas SQL manuales.

El pool de conexiones administrado por HikariCP optimiza el acceso concurrente a la base de datos.

Naturaleza del proyecto

El proyecto demanda representacion de entidades puntuales como transacciones y categorias, estas entidades se pueden representar facilmente en tablas MySQL debido a que tienen bien definidos sus propiedades que se representarian como campos de la tabla.

Soporte y Comunidad

MySQL es una de las bases de datos más populares, con una amplia documentación y soporte de la comunidad.

Integración sencilla con herramientas de monitoreo como Prometheus y Grafana para medir el rendimiento.

No tiene licencia.

La version community de MySQL permite utilizar dicha base de datos sin necesidad de pagar una licencia pagar una licencia

## **Conclusión**
¡Felicidades! 🎉 Ahora tienes el sistema de gestión de transacciones y categorías corriendo con **microservicios en Docker**. Si tienes dudas o mejoras, ¡contribuye al proyecto o abre un issue! 🚀

