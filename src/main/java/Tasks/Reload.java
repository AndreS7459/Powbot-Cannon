package Tasks;

import Main.Script;
import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

import static Main.Script.*;


public class Reload extends Task{

    //public GameObject cannon = Objects.stream().within(15).name("Dwarf multicannon").nearest().first();
    //public int CannonballsInCannon = Varpbits.varpbit(3);

    public static int balls;

    @Override
    public boolean validate() {

        // gets how many cannonballs are left in cannon
        // check if should reload cannon
        int CannonballsInCannon = Varpbits.varpbit(3);
        if (CannonballsInCannon <= randomNumber) {

            Item cannonball = Inventory.stream().name("Cannonball").first();
            if (cannonball.valid())
            {
                GameObject cannon = Objects.stream().within(15).name("Dwarf multicannon").nearest().first();
                // check if cannon is in view
                if (cannon.inViewport())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {

        GameObject cannon = Objects.stream().within(15).name("Dwarf multicannon").nearest().first();
        // interact with cannon
        cannon.interact("Fire");
        // wait for success confirmation or timeout failure
        Condition.wait(() -> message.contains("You load the cannon"), 150,25);
        // clear the reload message (otherwise it will spam)
        message = "";
        // generate a new ball reload number
        randomNumber = Random.nextInt(1, 4);

    }
}
