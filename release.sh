#!/bin/bash
version=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)' | cut -d'-' -f 1)
mvn versions:set -DnewVersion=$version
mvn deploy scm:tag