# Compile all java files
all:
	javac *.javac

# Run the server
server:
	java MathServer

# Run the client
client:
	java MathClient

# Clean compiled files
clean:
	del /Q *.class