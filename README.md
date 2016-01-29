# Installation

Build using maven

$ mvn clean package

This will create an executable jar/war file which will look for a data directory in ~/data and use 'localhost' as its mail server. You can overide these values by using
* -Dmail.host=mail.server.com
* -Ddata.dir=/data/directory
* -Dapp.prefix=http://localhost:8084/consent-app

Building for docker 

$ mvn clean package -Dmail.host=yourmail.server.com -Pdocker
$ docker run -p8080:8080 -v /my/data/dir:/data [image id]

# Creating a new instance

1. Create a directory to hold your instance. This directory will be used to name the url path for your instance. 
2. Create the following files or copy them from the sample directory here:
* config.json
<pre>
{ 
  "title": "Consent Test Site", 
  "mailFrom": "myemail@something.com", 
  "mailSubject": "Test Send" 
  "projectDescription": "This is the text that will appear on the website. It can contain html and links"
  "submissionComplete":"Thanks for responding",
  "adminEmails": ["yourmail@something.com","otheradmin@gmail.com"]
}
</pre>
* template.txt - The email template to send to people. The template is a velocity template and has the following variables available for use:
    * $projects - list of all projects this email address is attached to
    * $config - config object
    * $config.title - title of this project
    * $url - approval link for this user
* contacts.csv - your contacts file. It should contain the following columns with column headers
<pre>
"name","email","project","site"
"Mike","msmorul@sesync.org","test project","http://site"
"Mike 2","msmorul@sesync.org","test project asdf","http://site"
"Bob","bob@test","adf test project asdf","http://site"
</pre>

# More information

* Creating Velocity Templates: http://velocity.apache.org/engine/devel/user-guide.html
