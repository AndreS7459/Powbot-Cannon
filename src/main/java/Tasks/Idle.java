package Tasks;

import Main.Framework.Task;

public class Idle extends Task {

    public Idle(String name) {
        super(name);
    }

    @Override
    public boolean validate() {

        return true;
    }

    @Override
    public void execute() {

    }
}
