#!/bin/bash
if [ ! -z "$TRAVIS_TAG" ]; then
	echo "Updating POM to version $TRAVIS_TAG"
	mvn -P release versions:set -DnewVersion=$TRAVIS_TAG
	echo "Deploying to repository"
	mvn -P release deploy
else
	mvn test
fi
