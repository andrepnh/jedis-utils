#!/bin/bash
if [ ! -z "$TRAVIS_TAG" ]; then
	openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in secring.gpg.enc -out secring.gpg -d
	openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in pubring.gpg.enc -out pubring.gpg -d
	openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in settings.xml.enc -out settings.xml -d
	cp settings.xml ~/.m2/settings.xml
fi
