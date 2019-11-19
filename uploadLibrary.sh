#!/usr/bin/env bash

./gradlew uploadArchives
git add repository/
git commit -m 'upload libraries archives'

git push -f --set-upstream origin gh-pages