#!/usr/bin/env bash

git branch -d gh-pages
git checkout -b gh-pages
./gradlew uploadArchives

git add repository/
git commit -m 'upload libraries archives'
git push -f --set-upstream origin gh-pages