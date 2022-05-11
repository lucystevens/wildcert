#!/bin/bash
SERVICE_NAME=kotlin-app-template
SERVICE_VERSION=$("cat /services/$SERVICE_NAME/version")

LAST_JAR="/services/$SERVICE_NAME/$SERVICE_NAME-$SERVICE_VERSION.jar"
NEW_JAR="/services/$SERVICE_NAME/$SERVICE_NAME-$1.jar"

if [ $# -eq 0 ]; then
	echo "Please provide a version to deploy"
elif [ ! -f "$NEW_JAR" ]; then
	echo "$NEW_JAR not found."
else
	echo "$1" > "/services/$SERVICE_NAME/version"
	sudo systemctl restart kotlin-app-template.service
  # TODO remove all jars except for running and last jar
fi