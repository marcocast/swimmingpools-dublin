# geoservices-base
Skeleton project for building geoservices based back end applications 

# Overview

# Requirements

Create an Heroku application :
_heroku create {heroku-app-name}_

Create an Backendless application :
_singup here https://backendless.com and use the same Heroku app name {heroku-app-name}_

Create an Stormpath application :
_singup here https://stormpath.com and use the same Heroku app name {heroku-app-name}_

Clone this project locally and attach it to Heroku : 
_git remote add heroku git@heroku.com:{heroku-app-name}.git_

# Get Started

Set the following properties using the Heroku CLI

1. heroku config:set appname={YOUR APP NAME}
1. heroku config:set backendless-application-id={BACKENDLESS APP ID}
1. heroku config:set backendless-secret-id={BACKENDLESS SECRET ID}}
1. heroku config:set stormpath-application-id={STORMPATH APP ID}
1. heroku config:set stormpath-secret-id={STORMPATH SECRET ID}

Push to heroku.

Done :)

    
