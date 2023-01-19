package Main;

import Main.Framework.Task;
import Tasks.Fix;
import Tasks.InvCannonBalls;
import Tasks.Reload;
import com.google.common.eventbus.Subscribe;
import org.powbot.api.Color;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.event.MessageEvent;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.script.*;
import org.powbot.mobile.SettingsManager;
import org.powbot.mobile.ToggleId;
import org.powbot.mobile.drawing.Rendering;

import java.util.ArrayList;




@ScriptManifest(name = "Cannon Reloader", description = "Reloads and fixes cannon", version = "1")
public class Script extends AbstractScript {




    public static String message = "";
    public static int randomNumber = 0;



    ArrayList<Task> tasks = new ArrayList<Task>();


    @Override
    public void onStart() {
        // generate a new ball reload number
        randomNumber = Random.nextInt(1, 4);

        SettingsManager.set(ToggleId.DismissLevelUps, true);

        tasks.add(new Fix());
        tasks.add(new InvCannonBalls());
        tasks.add(new Reload());
    }



    @Override
    public void poll() {

        tasks.forEach(Task::run);



    }

    @Subscribe
    public void onMessageEvent(MessageEvent e)
    {
        // get the confirmation message that we reloaded cannon
        message = e.getMessage();
    }

    @Subscribe
    public void onRender(RenderEvent r)
    {
        Rendering.setScale(2.0f);
        Rendering.setColor(Color.getCYAN());
        //Rendering.drawString("Hello!", 50, 50);

        //debug shit
        int CannonballsInCannon = Varpbits.varpbit(3);
        Rendering.drawString("Cannonballs: " + CannonballsInCannon, 50, 70);
        Rendering.drawString("Next reload: " + randomNumber, 50, 90);
    }



    public static void main(String[] args) {
        // Start your script with this function. Make sure your device is connected via ADB, and only one is connected
        new Script().startScript();
    }
}
