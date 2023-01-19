package Main.Framework;

import static Main.Script.*;

public abstract class Task {

    public abstract boolean validate();

    public abstract void execute();

    public void run() {
        if (validate())
            execute();
    }

}
