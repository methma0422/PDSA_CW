# Troubleshooting Guide - 500 Internal Server Error

## Issue Fixed
I've added the Thymeleaf dependency which was missing. This was causing the 500 error because Spring Boot couldn't render the HTML templates.

## Steps to Fix

### Step 1: Rebuild the Project
You need to rebuild the project to download the new Thymeleaf dependency:

```bash
mvn clean install
```

Or if you're using an IDE:
- **IntelliJ IDEA**: Right-click on `pom.xml` → Maven → Reload Project
- **Eclipse**: Right-click on project → Maven → Update Project
- **VS Code**: Run `mvn clean install` in terminal

### Step 2: Restart the Application
After rebuilding, restart your Spring Boot application:

```bash
mvn spring-boot:run
```

### Step 3: Check Database Connection
Make sure MySQL is running and the database connection is working. The error might also be caused by database connection issues.

**Check:**
1. Is MySQL running?
2. Is the database `wood_carving` accessible?
3. Are the credentials in `application.properties` correct?

### Step 4: Check Application Logs
Look at the console output when starting the application. The actual error message will be there.

Common errors you might see:
- **Database connection error**: Check MySQL is running
- **Port already in use**: Another application is using port 8080
- **Missing dependency**: Run `mvn clean install` again

## What Was Changed

1. **Added Thymeleaf Dependency** in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-thymeleaf</artifactId>
   </dependency>
   ```

2. **Added Thymeleaf Configuration** in `application.properties`:
   ```properties
   spring.thymeleaf.prefix=classpath:/templates/
   spring.thymeleaf.suffix=.html
   spring.thymeleaf.mode=HTML
   spring.thymeleaf.cache=false
   ```

3. **Added WebConfig** for static resource handling

## Testing After Fix

1. Start the application: `mvn spring-boot:run`
2. Wait for "Started PdsaCwApplication" message
3. Open browser: `http://localhost:8080/login`
4. You should see the login page (not an error)

## If Still Getting Errors

### Check the Console Logs
The actual error will be in the console. Look for lines starting with:
- `ERROR`
- `Exception`
- `Caused by`

### Common Issues:

1. **Database Connection Failed**
   ```
   Error: Access denied for user 'root'@'localhost'
   ```
   **Solution**: Check MySQL credentials in `application.properties`

2. **Port 8080 Already in Use**
   ```
   Error: Port 8080 is already in use
   ```
   **Solution**: 
   - Stop other applications using port 8080
   - Or change port in `application.properties`: `server.port=8081`

3. **Thymeleaf Template Not Found**
   ```
   Error: Template might not exist
   ```
   **Solution**: Make sure HTML files are in `src/main/resources/templates/`

4. **Missing Dependencies**
   ```
   Error: ClassNotFoundException
   ```
   **Solution**: Run `mvn clean install` to download dependencies

## Quick Test

After rebuilding, test if the API is working:

```bash
# Test if server is running
curl http://localhost:8080/api/products

# Should return: [] (empty array) or JSON data
```

If this works but the HTML pages don't, the issue is with Thymeleaf configuration.

## Still Having Issues?

Share the error message from the console logs, and I can help you fix it!

