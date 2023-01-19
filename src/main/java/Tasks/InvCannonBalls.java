package Tasks;

import Main.Script;
import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

import static Main.Script.*;

public class InvCannonBalls extends Task {

    //public GameObject cannon = Objects.stream().within(15).name("Dwarf multicannon").nearest().first();
    //public int CannonballsInCannon = Varpbits.varpbit(3);


    @Override
    public boolean validate() {

        // check if player has cannon balls in inventory
        Item cannonball = Inventory.stream().name("Cannonball").first();
        if (!cannonball.valid())
        {
            int CannonballsInCannon = Varpbits.varpbit(3);
            if (CannonballsInCannon == 0)
            {
                GameObject cannon = Objects.stream().within(15).name("Dwarf multicannon").nearest().first();
                // check if cannon is in view
                if (cannon.inViewport()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void execute() {

        GameObject cannon = Objects.stream().within(15).name("Dwarf multicannon").nearest().first();
        // interact with cannon
        cannon.interact("Pick-up");
        // wait for success confirmation or timeout failure
        Condition.wait(() -> message.contains("You pick up the cannon"), 150, 25);

    }
}
