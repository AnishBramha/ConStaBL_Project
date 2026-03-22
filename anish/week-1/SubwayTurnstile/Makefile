LIB_DIR = ../../lib

CP = ".:$(LIB_DIR)/commons-scxml-0.9.jar:$(LIB_DIR)/commons-jexl-1.1.jar:$(LIB_DIR)/commons-logging-1.2.jar:$(LIB_DIR)/commons-digester-1.8.jar:$(LIB_DIR)/commons-beanutils-1.7.0.jar:$(LIB_DIR)/xalan-2.7.2.jar:$(LIB_DIR)/serializer-2.7.2.jar"

MAIN_CLASS = $(basename $(notdir $(wildcard src/*.java)))

default: all

all: compile run

compile:
	mkdir -p bin
	javac -d bin -cp $(CP) src/*.java

run:
	java -cp "bin:"$(CP) $(MAIN_CLASS)

clean:
	rm -rf bin/*


