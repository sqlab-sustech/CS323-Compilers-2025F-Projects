CC      = clang-19
OPT     = opt-19
CFLAGS  = -Wall -Werror -g
CRT 	= splc_crt.c

# Modify this to your IDEA output directory.
IDEA_TARGET	= ../target/production/CS323-Compilers-2025F-Projects

# Java Options
JAVA_BIN 	= java
JVM_OPTS	= -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
CLASSPATH	= $(IDEA_TARGET):../libs/antlr-4.13.2-complete.jar
MAIN_CLASS	= framework.project5.Grader

IDEA_TARGET_UPDATE_HINT	= $(shell find $(IDEA_TARGET) -name "*.class")

# 将所有 testXX 普通、带ASAN、带UBSan 直接通过 clang 编译到该目录下
REF_OUTPUT=bin/ref

REF_SOURCES		:= $(wildcard test[0-9]*/splc.c)
REF_DIRS 		:= $(patsubst %/splc.c,%,$(REF_SOURCES))
REF_TARGETS_STD 	:= $(patsubst %,$(REF_OUTPUT)/%,$(REF_DIRS))
REF_TARGETS_ASAN  	:= $(patsubst %,$(REF_OUTPUT)/%_asan,$(REF_DIRS))
REF_TARGETS_UBSAN 	:= $(patsubst %,$(REF_OUTPUT)/%_ubsan,$(REF_DIRS))

# genir: testcase --- java ---> IR file.
REF_IR_TARGETS	:= $(patsubst %,ir/%.ll,$(REF_DIRS))

# 将所有 testXX 通过我们的编译器翻译到 ir 下面，然后使用 clang 带 ASAN 编译到 bin/ 下面
IR_SOURCES 	:= $(wildcard ir/test*.ll)
IR_DIRS 	:= $(patsubst ir/%.ll,%,$(IR_SOURCES))
IR_TARGETS 	:= $(patsubst %,bin/%,$(IR_DIRS))
IR_TARGETS_ASAN  := $(patsubst %,bin/%_asan,$(IR_DIRS))

# Shell Colors
RESET  = \033[0m
BOLD   = \033[1m
GREEN  = \033[32m

# 伪目标
.PHONY: refs clean genir compileir testir ir checkcases

genir: $(REF_IR_TARGETS)

checkcases:
	@bash check_testcases.sh

testir:
	@bash testir.sh

compileir: $(IR_TARGETS) $(IR_TARGETS_ASAN)

refs: $(REF_DIRS)

ir:
	@$(MAKE) genir
	@$(MAKE) compileir

# --- For Grader -------------------

# Single Target for each ref
CHECK_REF_SINGLE 		:= $(patsubst %,check_%,$(REF_DIRS))

$(CHECK_REF_SINGLE): check_%: %
	@bash check.sh $*

# -------------------

$(REF_IR_TARGETS): ir/%.ll: %/splc.c $(IDEA_TARGET_UPDATE_HINT)
	@mkdir -p ir
	@echo "$(GREEN)Generating $@ from $< ...$(RESET)"
	@$(JAVA_BIN) $(JVM_OPTS) -classpath $(CLASSPATH) $(MAIN_CLASS) $< $@

ir/opt_%.ll: ir/%.ll
	@$(OPT) -passes='debugify' -S $< -o $@

$(IR_TARGETS): bin/%: ir/opt_%.ll $(CRT)
	@mkdir -p bin
	@echo "$(GREEN)Compiling $< to $@ ...$(RESET)"
	@$(CC) $(CFLAGS) -Wno-override-module -o $@ $^

$(IR_TARGETS_ASAN): bin/%_asan: ir/opt_%.ll $(CRT)
	@mkdir -p bin
	@echo "$(GREEN)Compiling $< with ASAN to $@ ...$(RESET)"
	@$(CC) $(CFLAGS) -Wno-override-module -fsanitize=address -fno-sanitize-recover=all -o $@ $^

$(REF_DIRS): %: $(REF_OUTPUT)/% $(REF_OUTPUT)/%_asan $(REF_OUTPUT)/%_ubsan

$(REF_TARGETS_STD): $(REF_OUTPUT)/%: %/splc.c $(CRT)
	@mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -o $@ $^

$(REF_TARGETS_ASAN): $(REF_OUTPUT)/%_asan: %/splc.c $(CRT)
	@mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -fsanitize=address -fno-sanitize-recover=all -o $@ $^

$(REF_TARGETS_UBSAN): $(REF_OUTPUT)/%_ubsan: %/splc.c $(CRT)
	@mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -fsanitize=undefined -fno-sanitize-recover=all -o $@ $^

clean:
	rm -rf bin ir