package Main.Framework;

import Main.Script;

import static Main.Script.*;

public abstract class Task {
    public String name;

    public Task(String name)
    {
        this.name = name;
    }


    public abstract boolean validate();

    public abstract void execute();


}
