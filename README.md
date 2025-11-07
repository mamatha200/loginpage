 **Login & Registration System (Spring Boot+Spring Security+ PostgreSQL) 
**
  

**Overview** 

This project demonstrates a simple Login and Registration System using Spring Boot (v3.3.4),Thymeleaf, and PostgreSQL.   

Users can register, log in, and access protected pages such as the `/admin` route (available only for admin user) 

**Tech Stack **

-Backend:Spring Boot 3.3.4   

- Frontend: Thymeleaf   

- Database:PostgreSQL   

- Security:Spring Security  

- ORM:Spring Data JPA   

 It’s a Spring Boot + Spring Security + Thymeleaf application that uses: 

Session-based authentication (the default in Spring Security), 

Password encryption using BCryptPasswordEncoder, 

Role-based access control (ROLE_USER and ROLE_ADMIN), 

And audit logging for login/logout activities. 

What happens right now 
When you log in: 

Spring Security creates a session cookie (JSESSIONID). 

Your login state is stored on the server, not as a token. 

When you visit /admin, Spring checks your session and roles before granting access. 

   


  
