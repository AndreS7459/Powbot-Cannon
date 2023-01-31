package Tasks;

import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Movement;

import static Main.Script.SafeSpotTile;

public class SafeSpot extends Task {


    public SafeSpot(String name) {
        super(name);
    }

    @Override
    public boolean validate() {


        if (SafeSpotTile.valid() && SafeSpotTile.distance() > 0)
            return true;

        return false;
    }

    @Override
    public void execute() {

        Movement.builder(SafeSpotTile).setRunMin(10).setRunMax(25).move();
        Condition.wait(() -> SafeSpotTile.distance() == 0, 150, 75);

    }
}
