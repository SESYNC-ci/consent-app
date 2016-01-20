# Installation

* Edit src/main/resources/application.properties and customize the following for your site:
** configuration.dir=/home/ranger/consent
** mail.server=smtp.umiacs.umd.edu
** app.prefix=http://researchtestbox.research.sesync.org:8080
* mvn clean package
* java -jar target/consent-app-1.0-SNAPSHOT.war -or- deploy to your favorite tomcat/jetty container.

# Creating a new instance

1. Create a directory to hold your instance. This directory will be used to name the url path for your instance. 
2. Create the following files or copy them from the sample directory here:
* config.json
<pre>
{ 
  "title": "Consent Test Site", 
  "mailFrom": "yourmail@site.org", 
  "mailSubject": "Subject in mail" 
  "projectDescription": "This is the text that will appear on the website. It can contain html and links"
  "submissionComplete":"This is text thanking you for submitting",
  "adminEmails": ["admin1@yoursite.com","admin2@yoursite.com"]
}
</pre>
* template.txt - The email template to send to people. The template is a velocity template and has the following variables available for use:
** $projects - list of all projects this email address is attached to
** $config - config object
** $config.title - title of this project
** $url - approval link for this user
* contacts.csv - your contacts file. It should contain the following columns with column headers
<pre>
"name","email","project","site"
"Mike","msmorul@sesync.org","test project","http://site"
"Mike 2","msmorul@sesync.org","test project asdf","http://site"
"Bob","bob@test","adf test project asdf","http://site"
</pre>

# More information

* Creating Velocity Templates: http://velocity.apache.org/engine/devel/user-guide.html
