package Tasks;

import Main.Framework.Task;
import Main.Script;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;

public class RangePot extends Task {


    public RangePot(String name) {
        super(name);
    }

    public static int Level = Skills.realLevel(Skill.Ranged);
    public static int BoostedLevel = Skills.level(Skill.Ranged);
    public static Item RangePotion;

    public static void UpdateValues()
    {
        // update this shit
        Level = Skills.realLevel(Skill.Ranged);
        BoostedLevel = Skills.level(Skill.Ranged);
    }

    @Override
    public boolean validate() {

        UpdateValues();

        // if they dont want to drink range pot return false
        if (!Script.DrinkRangePot)
            return false;

        // return false if level and boostedlevel is not the same (already potted)
        if (Level != BoostedLevel)
            return false;

        // makes sure inventory is open
        if (Game.tab(Game.Tab.INVENTORY))

        // search inventory for range pots
        for (int i = 1; i <= 4; i++) {
            RangePotion = Inventory.stream().name("Ranging potion" + "(" + i + ")").first();
            if (RangePotion.valid())
                return true;
        }


        return false;
    }

    public static boolean hasDrunk()
    {
        UpdateValues();
        if (Level != BoostedLevel)
            return true;

        return false;
    }

    @Override
    public void execute() {


        // drink pot
        RangePotion.interact("Drink");
        // wait for success confirmation or timeout failure
        Condition.wait(() -> hasDrunk(), 150, 75);

        // double check the drinking was successful
        if (Level == BoostedLevel)
            execute();


    }
}
