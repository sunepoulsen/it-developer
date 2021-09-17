#!/usr/bin/env sh

java -jar ~/.m2/repository/org/perf4j/perf4j/0.9.16/perf4j-0.9.16.jar -f text -t 600000 ../logs/*.log
