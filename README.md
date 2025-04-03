# blog-app
Blog-like application created using Spring Framework. Homework for the Java developer training course.

### **To run this application:**
* **Apache Maven** must be installed.
* **PostgreSQL** must be installed and running at **localhost:5432**.
Environment variables **'DB_USER'** and **'DB_PASS'** must be set for accessing database and schema named **practicum** must exist in database.
* **Apache Tomcat** server must be installed and running.

### **To build this application:**
* **mvn clean package** use this command to build the application, run tests and produce a **.war** file that can be deployed to Tomcat Server.
* **mvn clean package -D'tomcat.server'='_<path\to\Tomcat\webapps>_'** use this command to auto-deploy **.war** file to Tomcat Server.

#### Versions of software used in the development:
* Java JDK 21
* PostgreSQL 17
* Apache Tomcat 11.0
* Apache Maven 3.9.9