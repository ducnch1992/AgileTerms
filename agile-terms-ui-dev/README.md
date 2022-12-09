
<div>

<div align="center">
<img src="./src/assets/img/logo1.png" height="180"/></div>
<h1 align="center" style="font-size:10rem">Agile term</h1>

</div>
 
## Features
Users can use these following features:

<ul>
<li>Sign up new account</li>
<li>Search for a term</li>
<li>Contribute a description for an existing term</li>
<li>Contribute a new term</li>
</ul>

![Agile term home page looks great!](/src/assets/img/readmehomepage.png "agile term home page")


## How to clone and run the project

```

#Clone a single branch from gitsource
$ git clone --single-branch --branch dev-EE https://gitsource.axonactive.com/hcmc-itclass/agile-terms-ui.git

#Navigate into the directory you've just cloned
#Install npm packages
$ npm install

#Run the project
$ npm start

```

## Deploy with Jenkins

<p>
	Go to http://192.168.70.67:8088/<br />
	<b>User name: hcmc-it</b><br />
	<b>Password: aavn123</b><br />
</p>

<p>
	<b>Build and deploy on test server for Spring Boot project: </b> Go to  <i>jenkins_fe_test</i> & click <i>Build Now</i> to build Test Server using Spring Boot<br />
	<b>Build and deploy on test server for Java EE project: </b>	Go to  <i>jenkins_fe_test_EE</i> & click <i>Build Now</i> to build Test Server using Java EE<br />
</p>
