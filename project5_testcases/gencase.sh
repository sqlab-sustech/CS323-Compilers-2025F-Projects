#!/bin/bash

testXX="$1"
test_cases=({01..05})

for id in "${test_cases[@]}"; do
    ."/bin/ref/${testXX}_ubsan" < "${testXX}/${id}.in" > "${testXX}/${id}.out"
done

