#!/usr/bin/env bash


./gradlew uploadArchives
git add repository/
git commit -m 'upload libraries archives'

git branch -d gh-pages
git checkout -b gh-pages
git push -f --set-upstream origin gh-pages