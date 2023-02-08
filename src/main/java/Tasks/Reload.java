package Tasks;

import Main.Script;
import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

import static Main.Script.*;


public class Reload extends Task{


    public Reload(String name) {
        super(name);
    }

    @Override
    public boolean validate() {

        // gets how many cannonballs are left in cannon
        // check if should reload cannon
        int CannonballsInCannon = Varpbits.varpbit(3);
        if (CannonballsInCannon <= randomNumber) {

            Item cannonball = Inventory.stream().name("Cannonball").first();
            if (cannonball.valid())
            {
                GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
                // check if cannon is in view
                if (cannon.inViewport())
                    return true;
            }
        }
        return false;
    }

    public boolean hasReloaded()
    {
        int CannonballsInCannon = Varpbits.varpbit(3);
        if (CannonballsInCannon >= MaxReload + 1 || CannonballsInCannon >= 25)
            return true;

        return false;
    }

    @Override
    public void execute() {

        GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
        // interact with cannon
        if(cannon.interact("Fire"))
        {
            // wait for success confirmation or timeout failure
            Condition.wait(() -> hasReloaded(), 150,75);

            // generate a new ball reload number
            randomNumber = Random.nextInt(MinReload, MaxReload);
        }
    }
}
