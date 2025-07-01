# DroneInterpreter
An antlr parser and interpreter to read domain-specific language for giving commands to a drone. Made as a group project in the subject IN2031 at the University of Oslo.

# How to run

1. Start by building the project using the command "./gradlew build"

2. To run the build use the command "java -jar .\build\libs\aeroscript-1.0.jar <path_to_program_file>". For example: java -jar .\build\libs\aeroscript-1.0.jar .\src\test\resources\testprogram.aero

3. Once in the REPL/terminal, you can now run commands such as:
- message "name of message": This will run the corresponding portion of the program. 
- info: This will print out the info of the Drone
- exit: This will exit the REPL
- You can also run "help" to find the commands
