package Main;

import Main.Framework.Task;
import Tasks.*;
import com.google.common.eventbus.Subscribe;
import org.powbot.api.*;
import org.powbot.api.Random;
import org.powbot.api.event.GameObjectActionEvent;
import org.powbot.api.event.MessageEvent;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.event.TickEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackSkillOption;
import org.powbot.mobile.SettingsManager;
import org.powbot.mobile.ToggleId;
import org.powbot.mobile.drawing.Rendering;
import org.powbot.mobile.script.ScriptManager;

import java.awt.event.ItemListener;
import java.util.*;



@ScriptConfiguration.List(
        {
                @ScriptConfiguration(
                        name = "Min Reload",
                        description = "Minimum balls left in cannon before reload",
                        optionType = OptionType.INTEGER,
                        defaultValue = "1"
                ),
                @ScriptConfiguration(
                        name = "Max Reload",
                        description = "Maximum balls left in cannon before reload",
                        optionType = OptionType.INTEGER,
                        defaultValue = "4"
                ),
                @ScriptConfiguration(
                        name = "Drink Range Potion (Optional)",
                        description = "Drink range potion (Optional)",
                        optionType = OptionType.BOOLEAN
                ),
                @ScriptConfiguration(
                        name = "Cannon Tile",
                        description = "Select the tile of your cannon",
                        optionType = OptionType.TILE
                ),
                @ScriptConfiguration(
                        name = "SafeSpot Tile (Optional)",
                        description = "Select the tile of your safespot (Optional)",
                        optionType = OptionType.TILE
                ),
                @ScriptConfiguration(
                        name = "High Alch (Optional)",
                        description = "High Alch whilst cannoning (Optional)",
                        optionType = OptionType.BOOLEAN
                ),
                @ScriptConfiguration(
                        name = "High Alch Item",
                        description = "Item to high alch",
                        allowedValues = {"Black d'hide body", " Red d'hide body", "Blue d'hide body", "Adamant platebody", "Redwood shield", "Mithril pickaxe",
                        "Green d'hide body", "Rune dagger", "Mithril platebody", "Adamant kiteshield", "Dragonstone bolts (e)", " Dragon javelin heads"},
                        optionType = OptionType.STRING
                ),
        }
)

@ScriptManifest(name = "Cannon Reloader", description = "Reloads and fixes cannon", version = "1")
public class Script extends AbstractScript {


    // TODO

    // DONE add safe spot location to run back to after reload
    // DONE drink range potion
    // once checking for viewport if cant see turn viewport to face

    // add high alching
    // add fletch darts/bolts
    // add also attack the monsters
    // use prayers and food
    //



    public static int randomNumber = 0;
    public static boolean StopScript = false;
    public static int CannonballsInCannon = 0;


    ArrayList<Task> tasks = new ArrayList<Task>();


    public static Task currentTask = null;
    public static int MinReload;
    public static int MaxReload;
    public static boolean DrinkRangePot;
    public static Tile CannonTile;
    public static Tile SafeSpotTile;
    public static boolean HighAlch;
    public static String AlchItem;



    @Override
    public void onStart() {

        SettingsManager.set(ToggleId.DismissLevelUps, true);


        MinReload = getOption("Min Reload");
        MaxReload = getOption("Max Reload");
        DrinkRangePot = getOption("Drink Range Potion (Optional)");
        CannonTile = getOption("Cannon Tile");
        SafeSpotTile = getOption("SafeSpot Tile (Optional)");
        HighAlch = getOption("High Alch (Optional)");
        AlchItem = getOption("High Alch Item");

        // check if player has a hammer to repair cannon
        Item hammer = Inventory.stream().name("Hammer").first();
        if (!hammer.valid())
        {
            StopScript = true;
            Notifications.showNotification("Please start script with a hammer in inventory");
        }

        // make sure they have selected a cannon tile
        if (!CannonTile.valid())
        {
            StopScript = true;
            Notifications.showNotification("Please select cannon tile");
        }

        // make sure they have correct runes for high alch (not using firestaff as probs want bow for extra cannon accuracy)
        //  make sure they have the high alch item
        // make sure they are on correct magic book
        if (HighAlch)
        {
            Item natureRunes = Inventory.stream().name("Nature rune").first();
            Item fireRunes = Inventory.stream().name("Fire rune").first();
            Item HighAlchItem = Inventory.stream().name(AlchItem).first();
            if (!natureRunes.valid() || !fireRunes.valid())
            {
                StopScript = true;
                Notifications.showNotification("Please start with nature and fire runes in inventory");
            }
            if (!HighAlchItem.valid())
            {
                StopScript = true;
                Notifications.showNotification("Please start with the alch item in inventory");
            }
            if (Magic.book() != Magic.Book.MODERN)
            {
                StopScript = true;
                Notifications.showNotification("Please start on normal spell book");
            }
        }


        // generate a new ball reload number
        randomNumber = Random.nextInt(MinReload, MaxReload);
        CannonballsInCannon = Varpbits.varpbit(3);


        Paint paint = PaintBuilder.newBuilder()
                .x(50)
                .y(70)
                .addString("Task: ", () -> {
                    if (currentTask == null) return "Setting up";
                    return currentTask.name;
                })
                .addString("Cannonballs: ", () -> Integer.toString(CannonballsInCannon))
                .addString("Next reload: ", () -> Integer.toString(randomNumber))
                .trackSkill(Skill.Ranged)
                .trackSkill(Skill.Hitpoints)
                .trackSkill(Skill.Magic)
                .build();
        addPaint(paint);


        tasks.add(new SafeSpot("Walking to safe spot"));
        tasks.add(new RangePot("Drinking range potion"));
        tasks.add(new Fix("Fixing the cannon"));
        tasks.add(new InvCannonBalls("Picking up cannon (out of cannonballs)"));
        tasks.add(new Reload("Reloading the cannon"));
        tasks.add(new HighAlch("High Alching"));
        tasks.add(new Idle("Idling"));

    }


    @Override
    public void poll() {

        CannonballsInCannon = Varpbits.varpbit(3);


        if (StopScript)
            ScriptManager.INSTANCE.stop();

        for (Task task : tasks)
        {
            if (task.validate())
            {
                currentTask = task;
                task.execute();
            }
        }
        //tasks.forEach(Task::run);



    }



    public static void main(String[] args) {
        // Start your script with this function. Make sure your device is connected via ADB, and only one is connected
        new Script().startScript();
    }
}
