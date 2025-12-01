#!/bin/bash

# --- 颜色定义 ---
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

make genir || exit 1
make compileir || exit 1

# 遍历当前目录下所有 test 开头且后接数字的目录
# sort 确保按 test01, test02 顺序执行
for test_dir in $(ls -d test[0-9]* 2>/dev/null | sort); do

    # 1. 动态查找所有的 .in 文件
    # 使用 ls 获取所有 .in 文件并排序
    mapfile -t test_files < <(ls "$test_dir"/*.in 2>/dev/null | sort)

    # 如果没有找到任何 .in 文件，跳过该目录
    if [ ${#test_files[@]} -eq 0 ]; then
        echo "Skipping $test_dir (No .in files found)"
        continue
    fi

    # 检查是否有对应的 .out 文件
    # 这里我们做一个简单的验证：只要有成对的 .in/.out 就认为是有效的测试目录
    has_valid_pair=false
    for input_file in "${test_files[@]}"; do
        base_name=$(basename "$input_file" .in)
        if [ -f "$test_dir/$base_name.out" ]; then
            has_valid_pair=true
            break
        fi
    done

    if [ "$has_valid_pair" = false ]; then
        echo -e "${YELLOW}Skipping ${test_dir} (No matching .out files found)${NC}"
        continue
    fi

    echo "--------------------------------------------------"
    echo -e "${BLUE}${test_dir} - Found ${#test_files[@]} test cases${NC}"

    binary="bin/${test_dir}"

    # 遍历所有找到的测试用例 (.in 文件)
    for input_file in "${test_files[@]}"; do
        
        case_name=$(basename "$input_file" .in)
        expected_file="${test_dir}/${case_name}.out"
        temp_output="${binary}_${case_name}.output"

        # 运行程序
        # 1. 输入重定向 < input_file
        # 2. 输出重定向 > temp_output
        # 3. 2>&1 视情况而定，通常 .out 文件只包含标准输出。
        #    ASan/UBSan 的报错通常在 stderr，如果不希望 diff 报错信息，这里只重定向 stdout。
        ./"$binary" < "$input_file" > "$temp_output"

        # 检查返回值 (ASan/UBSan 报错通常会返回非0)
        ret_code=$?

        if [ $ret_code -ne 0 ]; then
            echo -e "Case ${case_name}: ${RED}CRASH/ERROR (Code $ret_code)${NC}"
            # ASan 的错误信息直接输出到了屏幕(stderr)，这里不用cat
        else
            # 比较输出
            diff_output=$(diff -u -Z "$expected_file" "$temp_output")
            if [ $? -eq 0 ]; then
                echo -e "Case ${case_name}: ${GREEN}PASS${NC}"
                # 测试通过后删除临时文件，保持目录整洁
                rm "$temp_output"
            else
                echo -e "Case ${case_name}: ${RED}FAIL${NC}"
                echo "Diff:"
                echo "$diff_output"
                exit 1
            fi
        fi
    done
done

echo "--------------------------------------------------"
echo "Done."