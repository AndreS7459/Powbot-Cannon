package Tasks;

import Main.Script;
import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.Notifications;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.dax.shared.json.ItemList;
import org.powbot.mobile.script.ScriptManager;

import java.util.List;

import static Main.Script.*;

public class InvCannonBalls extends Task {

    public InvCannonBalls(String name) {
        super(name);
    }

    @Override
    public boolean validate() {

        // check if player has cannon balls in inventory
        Item cannonball = Inventory.stream().name("Cannonball").first();
        if (!cannonball.valid())
        {
            int CannonballsInCannon = Varpbits.varpbit(3);
            if (CannonballsInCannon == 0)
            {
                GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
                // check if cannon is in view
                if (cannon.inViewport()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasPickedUp()
    {
        Item CannonBase = Inventory.stream().name("Cannon base").first();
        Item CannonStand = Inventory.stream().name("Cannon stand").first();
        Item CannonBarrels = Inventory.stream().name("Cannon barrels").first();
        Item CannonFurnace = Inventory.stream().name("Cannon furnace").first();
        if (CannonBase.valid() && CannonStand.valid() && CannonBarrels.valid() && CannonFurnace.valid())
            return true;

        return false;
    }

    @Override
    public void execute() {

        GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
        // interact with cannon
        cannon.interact("Pick-up");
        // wait for success confirmation or timeout failure
        Condition.wait(() -> hasPickedUp(), 150, 75);

        if (!hasPickedUp())
            execute();

        StopScript = true;
        Notifications.showNotification("Stopping script because out of cannonballs");

    }
}
