#!/usr/bin/env bash

BASEDIR=$(dirname "$0")

java -DHttpFlowHome=$BASEDIR\.. -jar ../lib/httpflow-consolw-0.0.1.jar $@