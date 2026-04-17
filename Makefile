# -------------------------------------------------------
# Makefile for CS-4390 Math Server/Client Project
#
# Mac/Linux: use as-is, all commands work natively
#
# Windows: the 'clean' target uses 'rm -f' which requires
#   Git Bash or WSL. If using Command Prompt, run instead:
#   del /Q *.class
# -------------------------------------------------------

# Compile all java files
all:
	javac *.java

# Run the server
server:
	java MathServer

# Run the client (run in a separate terminal from the server)
client:
	java MathClient

# Clean compiled .class files
# Mac/Linux: rm -f works natively
# Windows (Command Prompt): replace with -> del /Q *.class
clean:
	rm -f *.class
