
# JobFit Checker App
This project consists of two main parts: a backend built with Java/Spring Boot and a frontend built with React (using Create React App). 
Below are instructions for setting up, running, and deploying both the backend and frontend, as well as how to manage the database.

## Prerequisites
Before you start, ensure you have the following installed on your system:
- Java (version 17 or higher)
- Maven (for building and running the backend)
- AWS CLI (for connecting with AWS resources)
- Node.js (for running the frontend)

## Environment Setup
### 1. Database (PostgreSQL)
Navigate to the backend directory:
```bash
cd JobFitCheckerApp
```
Use an `.env` file in the project root with the following content:
```
MY_DB_USERNAME=<your_db_username>
MY_DB_PASSWORD=<your_db_password>
OPEN_AI_API_KEY=<your_open_ai_api_key>
```
To connect with the database, run the following command in another terminal:
```bash
ssh -i private-subnet.pem -f -N -L 5432:resumeinfo-database.ch00ay8y21vy.us-west-2.rds.amazonaws.com:5432 ec2-user@54.185.183.14 -v
```

### 2. Backend (Spring Boot)
To set up and run the backend:

Navigate to the backend directory:
```bash
cd JobFitCheckerApp
```
Build the backend with Maven:
```bash
mvn clean install
```
Run the backend:
```bash
mvn spring-boot:run
```
The backend should now be running at `http://localhost:8080`.

### 3. Frontend (React)
To set up and run the frontend:

Navigate to the frontend directory:
```bash
cd Jobfit-Checker-Frontend
```
Install dependencies:
```bash
npm install
```
Run the frontend:
```bash
npm start
```
The frontend should now be running at `http://localhost:3000`.

### 4. Building and Serving the Frontend
To integrate the frontend with the backend for production deployment, you need to build the frontend and serve the build through the Spring Boot backend.

Build the frontend for production:
```bash
npm run build
```
This will create a `build/` folder with the production-ready frontend assets.

Move the frontend build to the backend by copying the contents of the `build/` folder to the `src/main/resources/static/` folder in your Spring Boot application:
```bash
cp -r build/* ../JobFitCheckerApp/src/main/resources/static/
```
Restart the backend: Now, when you access `http://localhost:8080`, it will serve the React app.

### 5. Running the Full Stack
Once both the backend and frontend are set up, you can run the full stack application with:

- **Run the backend:** Ensure the backend is running at `http://localhost:8080`.
- **Run the frontend (development mode):** Run the frontend in development mode at `http://localhost:3000` using:
  ```bash
  npm start
  ```
  OR
- **Build the frontend and serve it via the backend (production mode):** Build and copy the frontend as outlined above, and the frontend will be available at `http://localhost:8080`.

### 6. Troubleshooting
If the backend cannot connect to the PostgreSQL database, verify that:
- The database URL, username, and password are correct.
- Security groups in AWS (if applicable) allow access from your IP (this should be covered if it's in local).
- The PostgreSQL service is running and accessible.
- For issues with the frontend, check the browser console for errors and verify that the correct API endpoint for the backend is being used.

### 7. Tutorial Quotations
- Referred to [this video](https://www.youtube.com/watch?v=buqBSiEEdQc) for setting up the database
- [Configuring an Amazon RDS DB instance](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_RDS_Configuring.html)
- [Java, Spring Boot, PostgreSQL, Spring Data JPA, Hibernate CRUD RESTFul API Tutorial](https://www.youtube.com/watch?app=desktop&v=v1IFQWzuSrw)
- Referred to [this video](https://www.youtube.com/watch?v=qis9sMaiqN4) for connecting front end with the back end
- [Session management tutorial](https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html).
- [Build REST services with Spring](https://spring.io/guides/tutorials/rest)
- [Setup development environment for AWS SDK Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup.html#setup-credentials)
- [Using AI Services in Spring Boot Application](https://docs.langchain4j.dev/tutorials/ai-services/#using-ai-services-in-spring-boot-application)
- [Loading Amazon S3 Document in langchain4j](https://github.com/langchain4j/langchain4j/blob/main/document-loaders/langchain4j-document-loader-amazon-s3/src/test/java/dev/langchain4j/data/document/loader/amazon/s3/AmazonS3DocumentLoaderIT.java)
- [Configure Amazon Simple Queue Service](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configuring.html)
- [Task executing in Spring](https://docs.spring.io/spring-framework/reference/integration/scheduling.html)
- [Using React Router to navigate to pages](https://reactrouter.com/en/main)
- [Basic about React](https://qiita.com/watataku8911/items/d632e2274c6921f1e109)
- [React official site](https://react.dev/learn)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Boot Template](https://usfca.instructure.com/courses/1621500/assignments/7473801)
- Refereed to class materials for CS386 in USF for Front end part when using CSS, Javascript, react etc.