#!/usr/bin/env bash

# when there is no files added to git
# see https://unix.stackexchange.com/questions/155046/determine-if-git-working-directory-is-clean-from-a-script
if [ -z "$(git status --untracked-files=no --porcelain)" ]; then 
  g add public/ && g commit -m "Publish" && git push origin `git subtree split --prefix public master`:gh-pages --force
  git reset --soft HEAD^
  git restore --staged public/*
fi

