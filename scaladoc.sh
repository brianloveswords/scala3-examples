#!/usr/bin/env bash

set -euo pipefail

function main {
    mkdir -p output

    scaladoc \
        -project-version "1.0.0" \
        \
        -social-links:github::https://github.com/brianloveswords/scala3-examples,twitter::https://twitter.com/brianloveswords \
        -default-template "static-site-main" \
        -author \
        -groups \
        -d "output" \
        -project "Scala 3 By Example" \
        -snippet-compiler:docs=compile \
        -siteroot "docs" \
        -classpath "vendor/cats-core_3-2.7.0.jar:vendor/cats-effect_3-3.4-389-3862cf0.jar" \
        . # -Ygenerate-inkuire \

    ./serve.sh
}

main
