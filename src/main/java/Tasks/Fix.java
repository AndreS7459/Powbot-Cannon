package Tasks;

import Main.Script;
import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

import static Main.Script.*;

public class Fix extends Task {







    @Override
    public boolean validate() {

        // check if cannon has broken
        GameObject brokenCannon = Objects.stream().within(15).name("Broken multicannon").nearest().first();
        // check if cannon is in view
        if (brokenCannon.inViewport())
        {
            // check if player has a hammer
            Item hammer = Inventory.stream().name("Hammer").first();
            if (!hammer.valid())
                return false;
            else
                return true;

        }
        return false;
    }

    @Override
    public void execute() {

        GameObject brokenCannon = Objects.stream().within(15).name("Broken multicannon").nearest().first();
        // interact with cannon
        brokenCannon.interact("Repair");
        // wait for success confirmation or timeout failure
        Condition.wait(() -> message.contains("You repair your cannon"), 150,25);
        // clear the reload message (otherwise it will spam)
        message = "";

    }
}
