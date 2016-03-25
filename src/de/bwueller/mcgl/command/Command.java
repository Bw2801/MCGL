package de.bwueller.mcgl.command;

public class Command {
    
    public String command;
    public CommandType type;

    public Command(String command, CommandType type) {
        this.command = command;
        this.type = type;
    }
}
