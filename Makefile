ANTLR_JAR  := libs/antlr-4.13.2-complete.jar
OUTPUT_DIR := src/main/java/generated

GRAMMARS := $(basename $(wildcard *.g4))

# ANTLR_TARGETS := $(GRAMMARS:%=$(OUTPUT_DIR)/%Parser.java)

.DEFAULT = all
all: $(GRAMMARS)

$(GRAMMARS) : % : $(OUTPUT_DIR)/%Parser.java

$(OUTPUT_DIR)/%Parser.java : %.g4
	mkdir -p $(OUTPUT_DIR)
	@echo Building $(basename $<)
	java -jar $(ANTLR_JAR) -visitor -o $(OUTPUT_DIR)/$(basename $<) -package generated.$(basename $<) $<

clean:
	rm -rf $(OUTPUT_DIR)

jar:
	mvn jar:jar

-include conf/proj.mk
-include conf/handin.mk

