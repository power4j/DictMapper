#!/bin/bash

./mvnw  release:clean -P 'oss-release' && \
./mvnw  release:prepare -P 'oss-release' && \
./mvnw  release:perform -P 'oss-release'