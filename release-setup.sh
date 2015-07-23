#!/bin/bash
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in secring.gpg.enc -out secring.gpg -d
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in pubring.gpg.enc -out pubring.gpg -d
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in settings.xml.enc -out settings.xml -d
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in jedis-util-github.enc -out jedis-util-github -d
cp settings.xml ~/.m2/settings.xml
ssh-agent -s
ssh-add jedis-util-github
git config --global user.email "andrepnh@users.noreply.github.com"
git config --global user.name "maven release plugin"