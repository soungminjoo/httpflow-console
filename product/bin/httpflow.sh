#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
cd "$BASEDIR/work"

java -jar ../lib/httpflow-consolw-0.0.1.jar $@