#!/usr/bin/env bash

BEFORE_HTTPFLOW_CHANGE=${PWD}
BASEDIR=$(dirname "$0")
cd "$BASEDIR/work"

java -jar ../lib/httpflow-consolw-0.0.1.jar $@

cd "$BEFORE_HTTPFLOW_CHANGE"