SRC = src/Converter.java
CLASS = Converter
LIB_DIR = lib

CP = ".:$(LIB_DIR)/commons-scxml-0.9.jar:$(LIB_DIR)/commons-jexl-1.1.jar:$(LIB_DIR)/commons-logging-1.2.jar:$(LIB_DIR)/commons-digester-1.8.jar:$(LIB_DIR)/commons-beanutils-1.7.0.jar:$(LIB_DIR)/xalan-2.7.2.jar:$(LIB_DIR)/serializer-2.7.2.jar"

default: run

compile:
	mkdir -p bin
	javac -d bin -cp $(CP) $(SRC)

run:
	java -cp "bin:"$(CP) $(CLASS)

clean:
	rm -rf bin/*


