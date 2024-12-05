# Emergency Alert Application

## Documentation

### Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Running the application](#running-the-application)
4. [Contribution](#contribution)
5. [License](#license)
6. [Documentation](#documentation)


### Introduction
This is an application for Estonian Wildlife Center to manage alerts made by member of the public about injured animals.
The applications main purpose is to provide a platform for the public to report injured animals and for the center's volunteers to manage these alerts.
Additionally, the application provides the following features:
- A ticketing system for managing alerts
- A knowledge base for common injuries and how to handle them
- A user management system for managing users and roles
- A statistics page for viewing the number of alerts and their status
- An applications management system for managing the new applications to become a volunteer
- A settings page for managing the application settings like tags and functions

### Features
- Ticketing system (general and personal)
- Knowledge base
- User management
- Statistics
- Applications management
- Settings
- Profile

### Running the application
#### Backend
1. Clone the repository
2. Create a database named 'EAA' to your database server (e.g. PgAdmin)
3. Open the project in your IDE 
4. Set database variables to your environment variables (see application.properties)

You will need to set the following variables:
* DB_USERNAME - your database username
* DB_PASSWORD - database password
* JWT_TOKEN - generate a jwt secret (see step 5)
* LOCATIONIQ_TOKEN - location api key (ask from the project developers)
* drive credentials (ask from the project developers)

5. Set jwt secret to your environment variables (see SecurityConfig.java)

The following code can be used to generate a jwt secret for your variable.
```
import java.security.SecureRandom;
import java.util.Base64;

public class GenerateJWTSecret {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[64]; // 512 bits
        random.nextBytes(key);
        String jwtSecret = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated JWT Secret: " + jwtSecret);
    }
}
```
6. Run the application in your IDE
7. The application will run on port 8080

#### Frontend
1. Move to the frontend directory
2. Run `npm install`
3. Run `npm run serve`
4. The frontend will run on port 8081


In order to test different roles and see the permitted views of each user role you can test with the following users (log in with the following credentials):
1. alice.johnson@example.com
2. jane.doe@example.com
3. john.smith@example.com
4. test@test.com

NB! Password for all is "password123"

You are able to see their role in the profile page. Use this set of credentials to test the different roles and their permissions.

Running on **Docker** is also available. There are Dockerfiles and docker-compose file made specially for that.
To run the application on Docker, you need to have Docker installed on your machine and running.
The docker-compose file will create the following containers:
- eaa-backend
- eaa-frontend
- eaa-db

There is also a **.jar file** available in the root folder. You will need PostgreSQL installed with a database named 'EAA' to run the application.

### Contributing
The following members contributed to this project:
- Elisabet Hein
- Annabel Maksimov
- Ariana Jõgi
- Vanessa Apuhtin


### License
This project is owned by the Estonian Wildlife Center.

### Description of the general workflow:
- The user opens the application and is on a welcome page that describes the organization.

What the user can do without logging in:
- The user can navigate to the alert page to report an injured animal.
- The user can navigate to the registration page to create an application to become a volunteer.
- The user can navigate to the login page and insert their email and password to log in.

What happens when user logs in:
- The server create a token for them, which they can use to access the main application and its features.
- The token will expire after 1 hour.
- If the token is expired the frontend should redirect the user to the login page.

What the user can do after logging in:
- The user can navigate between the different pages of the application defined in the navigation bar. (the pages depend on the uer role = rights)
- The user can go to their own profile. 
- The user can log out.

### Backend Documentation
#### Entities
These are the entities used in the application. They are used to represent the database tables.
* Users are connected to roles, regions, species and tags and tickets.
* Tickets are connected to users, animaltags, photos, and have a foreign key to region, status, species, upper species and resolution.
* Applications are connected to tags.
* Species are connected to upper species and tickets.

#### Repositories
These are the repositories used in the application. They are used to interact with the database.

#### Services
These are the services used in the application. They are used to interact with the repositories and perform business logic.

#### Controllers
These are the controllers used in the application. They are used to handle the requests from the frontend and send responses back to the frontend.

#### DTOs
These are the DTOs used in the application. They are used to transfer data between the frontend and the backend.

#### Components
There is a component for data initialization. It is used to initialize the database with some data.
The data is initialized in a table, in case the table is empty.

#### Configurations
These are the classes used for security in the application. They are used to authenticate and authorize users.
* SecurityConfig is used to configure the security settings. It uses JWT for authentication. All requests are authenticated except for requests that are used without logging in.
* JwtAuthorizationFilter is used to filter the requests and validate the JWT token.
* CustomAuthenticationFilter is used to authenticate the user.

### Frontend Documentation
#### Components
These are the components used in the application. They are used on the pages - e.g pop-ups.

#### Pages
These are the pages used in the application. They are used to display the components. They will perform most of the logic.

#### Routes
These are the routes used in the application. They are used to navigate between the pages.

#### Store
This is the store used in the application. It is used to store the data and manage the state of the application.
The logged-in user is stored in the store.