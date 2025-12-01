#!/bin/bash

# --- 颜色定义 ---
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

make clean || exit 1

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

    # 2. 运行 Make 编译 (Standard, ASan, UBSan)
    echo -e "Compiling ${test_dir}..."
    make "$test_dir" > /dev/null

    if [ $? -ne 0 ]; then
        echo -e "${RED}Compilation Failed for ${test_dir}${NC}"
        exit 1
    fi

    # 3. 定义要测试的三个二进制文件路径
    # 注意：这里对应 Makefile 中生成的路径规则
    bin_std="bin/ref/${test_dir}"
    bin_asan="bin/ref/${test_dir}_asan"
    bin_ubsan="bin/ref/${test_dir}_ubsan"

    binaries=("$bin_std" "$bin_asan" "$bin_ubsan")

    # 4. 遍历每个二进制文件进行测试
    for binary in "${binaries[@]}"; do
        # 提取二进制文件的后缀名用于显示 (e.g., STD, ASAN, UBSAN)
        if [[ "$binary" == *"_asan" ]]; then
            type="ASAN "
        elif [[ "$binary" == *"_ubsan" ]]; then
            type="UBSAN"
        else
            type="STD  "
        fi

        # 遍历所有找到的测试用例 (.in 文件)
        for input_file in "${test_files[@]}"; do
            # 获取文件名核心部分 (例如 "01", "test_case_2" 等)
            case_name=$(basename "$input_file" .in)

            expected_file="${test_dir}/${case_name}.out"

            # 确保存在对应的 .out 文件，否则跳过该用例
            if [ ! -f "$expected_file" ]; then
                echo -e "[${YELLOW}${type}${NC}] Case ${case_name}: ${YELLOW}SKIP (No .out file)${NC}"
                continue
            fi

            # 临时输出文件: bin/test01_01_asan.output
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
                echo -e "[${YELLOW}${type}${NC}] Case ${case_name}: ${RED}CRASH/ERROR (Code $ret_code)${NC}"
                # ASan 的错误信息直接输出到了屏幕(stderr)，这里不用cat
            else
                # 比较输出
                diff_output=$(diff -u -Z "$expected_file" "$temp_output")
                if [ $? -eq 0 ]; then
                    echo -e "[${YELLOW}${type}${NC}] Case ${case_name}: ${GREEN}PASS${NC}"
                    # 测试通过后删除临时文件，保持目录整洁
                    rm "$temp_output"
                else
                    echo -e "[${YELLOW}${type}${NC}] Case ${case_name}: ${RED}FAIL${NC}"
                    echo "Diff:"
                    echo "$diff_output"
                    exit 1
                fi
            fi
        done
    done
done

echo "--------------------------------------------------"
echo "Done."