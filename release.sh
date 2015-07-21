#!/bin/bash
version=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)' | cut -d'-' -f 1)
echo "Updating POM to version $version"
mvn -P release versions:set -DnewVersion=$version
echo "Deploying to repository"
mvn -P release deploy scm:tag