package Tasks;

import Main.Script;
import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

import static Main.Script.*;

public class Fix extends Task {


    public Fix(String name) {
        super(name);
    }

    @Override
    public boolean validate() {

        // check if cannon has broken
        GameObject brokenCannon = Objects.stream().within(CannonTile, 0).name("Broken multicannon").nearest().first();
        // check if cannon is in view
        if (brokenCannon.inViewport())
        {
            // check if player has a hammer
            Item hammer = Inventory.stream().name("Hammer").first();
            return hammer.valid();

        }
        return false;
    }

    public static boolean hasRepaired()
    {
        // see if working cannon is on ground if so it has been repaired
        GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
        if (cannon.valid())
            return true;

        return false;
    }

    @Override
    public void execute() {

        GameObject brokenCannon = Objects.stream().within(CannonTile, 0).name("Broken multicannon").nearest().first();
        // interact with cannon
        brokenCannon.interact("Repair");
        // wait for success confirmation or timeout failure
        Condition.wait(() -> hasRepaired(), 150,75);

    }
}
