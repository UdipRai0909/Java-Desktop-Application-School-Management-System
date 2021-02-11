#  Java Desktop Application School Management System

## Features

1. Swing GUI

1. Role-based Login Form with Validation

1. Interesting Dashboards


## Database used:

1. MySQL

## Language used:

1. JAVA

## Programs used

1. XAMPP 

1. MySQL Workbench 
(optional but better for coding experience) 

1. Browser (Chrome)

1. Eclipse
  1. [Required Jar Files](https://drive.google.com/drive/folders/1sE8euWJz6PLPXxhfulPdSVAHMNTCaGGe?usp=sharing)

## Video Demo

[Google drive link](https://drive.google.com/file/d/1L1XVzjjqdFpazz1sNo4FnCM9TJuDmHyU/view?usp=sharing)

## Steps to reproduce

1. Go to the github link -> Code -> download ZIP.

1. Open Eclipse and go to "Open projects from File System". Select the downloaded ZIP file after.

1. Create a source folder named as "images" in the 1st package.

1. Copy all the images from the "img" folder in the 2nd package. Paste them inside the "images" source folder.

1. Right click the project folder and go to -> Properties -> Java Build Path.

1. Delete the jar files inside modulepath and classpath.
   Inside the classpath only, 
   Go to -> Add Library -> JRE System Library.
   Now Go to -> Add External JARs and import the following jar files:
    1. mysql-connector.jar (to connect to the database)
    1. rs2xml.jar (to deal with SQL queries)
    1. jcalendar.jar (date component for Swing/Window Builder)
    
1. There should be no errors at this point.

1. Go to -> utility -> query.txt 
   Copy all inside the .txt file

1. Open up XAMPP control panel and start Apache and MySQL servers.
   The ports on Apache should look something like this - 4441, 8080.
   Memorize the port on the right hand side. (which in this case is 8080)

1. Head over to your browser and go to this link below.
   **localhost:8080/phpmyadmin**

1. Click on SQL which is alongside Databases and paste the copied value from before.
   Now, run then query by holding **Ctrl + Enter** in your keyboard at the same time.
   Verify that the table is created by looking inside your database in the left hand side.

1. Now go back to the project and navigate to view -> MenuPage.jave
   Right click on the file and Run as Java Application.
   
   Default email: admin@gmail.com
   Default pass: adminpass

## Future Improvements

**_Date: Feb 11, 2021_**

1.  No use of interfaces but I think that's the point of JFrames. Implemtation of classes can be made more easy to follow.

1.  I created functions which were repeated in many classes so I could work on that.

1.  Hard coding the SQL queries can be avoided with some implemetations of models. 
    But I will follow a MVC pattern in the next upcoming projects.

1.  Not just functions, the code were pretty much repeated in some cases so I will need to refractor my codes.

1.  Role based functionality can be upgraded such that the user can always know who and what role is logged in 
    OR what access rights and privileges does the user have.
