package Tasks;

import Main.Framework.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import static Main.Script.AlchItem;
import static Main.Script.SafeSpotTile;

public class HighAlch extends Task {

    public HighAlch(String name) {
        super(name);
    }

    @Override
    public boolean validate() {

        // make sure we in safe spot
        if (SafeSpotTile.valid() && SafeSpotTile.distance() > 0)
            return false;


        // check have enough runes and alch items
        Item natureRunes = Inventory.stream().name("Nature rune").first();
        Item fireRunes = Inventory.stream().name("Fire rune").first();
        Item HighAlchItem = Inventory.stream().name(AlchItem).first();
        if (!natureRunes.valid() || !fireRunes.valid() || !HighAlchItem.valid())
            return false;

        // check player is not animating
        if (Players.local().animation() != -1)
            return false;

        // check magic book again
        if (Magic.book() != Magic.Book.MODERN)
            return false;



        return true;
    }

    @Override
    public void execute() {

        Item HighAlchItem = Inventory.stream().name(AlchItem).first();

        // check if selected spell successfully
        if (Magic.Spell.HIGH_ALCHEMY.cast("Cast"))
        {
            HighAlchItem.interact("Cast");
        }


    }
}
