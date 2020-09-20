# PAAS

[Pizza As A Service!](http://abraham-kurnanto.com/PAAS/)

Build your own Pizza! Any toppings, all one price.

### Description
This is an assignment project.

The project has a database management application develop using Java and web application develop using vanilla PHP-JavaScript.

I enjoyed developing this assignment and had a great team.

We understand each other and maintain cohesiveness with good communication.

### Tech stack
Java, JavaFX 2.0, MariaDB

PHP, JavaScript, PixiJS, jQuery

### AWS
This is my first try using AWS service. I deployed this project to AWS EC2, EBS and RDS.

This project has two side, Java application and web application. Database is what bridging the two applications together.

This set up becoming problematic as the application grow more complex.

Each database evolve independently. When the other team made changes to the database (table structure, relations, column definition and structure) the other team may not aware of it.

I decided to deploy the database to RDS so the two applications can connect to the same database. This way the two team can made change to the database and tested it on both sides instantly.

I noticed that the Java application took a while to access RDS. It wasn't sure what caused it, either the Java MySQL driver used or something else.

In our demo day, I deploy the whole project to EC2 - EBS. The deployment process were simple and fast since our project is quite simplistic.
