# This was done using Netbeans 12. 
# Requirements to run this program.
 This was made in Java with Ant Web Application.
 Java EE Version should be Java EE 5.
# Libraries Used
    - JDK Version: JDK 1.8
    - Glassfish Server
    - commons-codec-1.9.jar
    
# Database Setup
   - Java DB (Derby)
   - The database should be UserDB, and the username, and password set to "app". You can change the username and password to your preferences.
   - Note: for changing of username and password you should also change the param-value of both param-name "dbUsername" and "dbPassword" to your preffered username and password on web.xml under WEB-INF file

# Original Code:
        <init-param>
            <param-name>dbUsername</param-name>
            <param-value>app</param-value>
        </init-param>
        <init-param>
            <param-name>dbPassword</param-name>
            <param-value>app</param-value>
        </init-param>
# Setting your preferred username and password
        <init-param>
            <param-name>dbUsername</param-name>
            <param-value>#Insert new username here</param-value>
        </init-param>
        <init-param>
            <param-name>dbPassword</param-name>
            <param-value>#Insert new password here</param-value>
        </init-param>

# Note: Make sure you are specifically connected to UserDB for this to work
        <init-param>
            <param-name>jdbcUrl</param-name>
            <param-value>jdbc:derby://localhost:1527/UserDB</param-value>
        </init-param>
        
# Note: Make you have JavaDB installed for the database to work
# UserDB SQL Codes using the executable command: You can create as many usernames, passwords, and roles as you want
     CREATE TABLE USER_INFO (
      username VARCHAR(30),
      password VARCHAR(20),
      role VARCHAR(10)
      );

        INSERT INTO USER_INFO (username, password, role) VALUES ('admin1', 'admin1', 'admin');
        INSERT INTO USER_INFO (username, password, role) VALUES ('guest1', 'guest1', 'guest');
        
