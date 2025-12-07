#!/bin/bash

# --- 颜色定义 ---
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

if [ -n "$1" ]; then
    echo "Run TestCase Checker for $1"
else
    exit 1
fi

test_dir="$1"

test_cases=({01..05})

for id in "${test_cases[@]}"; do
    in_file="${test_dir}/${id}.in"
    out_file="${test_dir}/${id}.out"

    if [[ -f "$in_file" && -f "$out_file" ]]; then
        echo "Case ${id} exists."
    else
        echo "Case ${id} is missing."
        exit 1
    fi
done

# 3. 定义要测试的三个二进制文件路径
# 注意：这里对应 Makefile 中生成的路径规则
bin_std="bin/ref/${test_dir}"
bin_asan="bin/ref/${test_dir}_asan"
bin_ubsan="bin/ref/${test_dir}_ubsan"
binaries=("$bin_std" "$bin_asan" "$bin_ubsan")


for id in "${test_cases[@]}"; do
    in_file="${test_dir}/${id}.in"
    out_file="${test_dir}/${id}.out"

    case_name=$(basename "$in_file")
    temp_output="${test_dir}/${case_name}.runoutput"

    echo "Run Case ${id}"
    for binary in "${binaries[@]}"; do
        echo "Run ${binary}:"

        ./"$binary" < "$in_file" > "$temp_output"
        ret_code=$?

        if [ $ret_code -ne 0 ]; then
            echo "Failed!"
            exit 1
        else
            # 比较输出
            diff_output=$(diff -u -Z "$out_file" "$temp_output")
            if [ $? -eq 0 ]; then
                echo -e "Passed"
                rm "$temp_output"
            else
                echo -e "Failed"
                rm "$temp_output"
                exit 1
            fi
        fi
    done
    echo
done
