#!/usr/bin/env bash

git checkout gh-pages

./gradlew uploadArchives
git add repository/
git commit -m 'upload libraries archives'
git push