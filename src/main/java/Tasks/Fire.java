package Tasks;

import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Varpbits;

import java.time.Duration;
import java.time.Instant;

import static Main.Script.CannonTile;
import static Main.Script.MaxReload;

public class Fire extends Task {

    public Fire(String name) {
        super(name);
    }

    boolean startedTimer;
    Instant botTimer;
    long timeRan;
    int timeRun;
    int cacheCannonBalls;
    int CannonballsInCannon;

    @Override
    public boolean validate() {


        GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
        CannonballsInCannon = Varpbits.varpbit(3);

        if (!cannon.valid())
            return false;

        if (!startedTimer)
        {
            botTimer = Instant.now();
            cacheCannonBalls = CannonballsInCannon;
        }
        startedTimer = true;
        Duration duration = Duration.between(botTimer, Instant.now());
        timeRan = duration.getSeconds();
        timeRun = (int) timeRan;
        if (timeRun > 5)
        {
            CannonballsInCannon = Varpbits.varpbit(3);
            if (CannonballsInCannon == cacheCannonBalls)
            {
                startedTimer = false;
                return true;
            }
            else {
                startedTimer = false;
                cacheCannonBalls = 99999;
            }
        }


        return false;
    }

    public boolean isFiring()
    {
        CannonballsInCannon = Varpbits.varpbit(3);
        if (CannonballsInCannon != cacheCannonBalls)
            return true;
        else
            return false;

    }

    @Override
    public void execute() {

        GameObject cannon = Objects.stream().within(CannonTile, 0).name("Dwarf multicannon").nearest().first();
        // interact with cannon
        cannon.interact("Fire");
        Condition.wait(() -> isFiring(), 150,75);

    }
}
