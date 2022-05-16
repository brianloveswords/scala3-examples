#!/usr/bin/env bash

set -euo pipefail

INPUT_DIR=_docs
OUTPUT_DIR=docs

function main {
    mkdir -p docs

    scaladoc \
        -project-version "1.0.0" \
        \
        -social-links:github::https://github.com/brianloveswords/scala3-examples,twitter::https://twitter.com/brianloveswords \
        -default-template "static-site-main" \
        -author \
        -groups \
        -d "${OUTPUT_DIR}" \
        -project "Scala 3 By Example" \
        -snippet-compiler:${INPUT_DIR}=compile \
        -siteroot ${INPUT_DIR} \
        -classpath "vendor/cats-core_3-2.7.0.jar:vendor/cats-effect_3-3.4-389-3862cf0.jar" \
        . # -Ygenerate-inkuire \

    ./serve.sh
}

main
