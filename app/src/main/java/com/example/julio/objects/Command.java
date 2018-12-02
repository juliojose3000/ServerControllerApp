package com.example.julio.objects;

public class Command {
    private int idCommand;
    private String commandLine;

    public Command(String commandLine){
        this.commandLine = commandLine;
    }

    public int getIdCommand() {
        return idCommand;
    }

    public void setIdCommand(int idCommand) {
        this.idCommand = idCommand;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public String toString() {
        return "Command{" +
                "idCommand=" + idCommand +
                ", commandLine='" + commandLine + '\'' +
                '}';
    }
}
