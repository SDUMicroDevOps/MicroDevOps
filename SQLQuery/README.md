# Build
To build the program run the command "mvn clean compile assembly:single".
Then to run it run it with "java -jar target/SQLQuery-1.0.jar" and then any args you want.
Or build and run the docker file
# Run
To run the program 4 env variables needs to be set
DB_USER: The username of the database
DB_PASS: The password of the user
DB_NAME: The name of the database
CONNECTION_NAME: The connection name given by google.
Just need a change I think
