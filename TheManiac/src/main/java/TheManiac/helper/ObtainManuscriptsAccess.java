package TheManiac.helper;

import TheManiac.relics.PossessedManuscripts;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ObtainManuscriptsAccess {
    
    public static void ObtainManuscripts() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new PossessedManuscripts());
    }
    
    public static boolean doneFirstRun() {
        return AbstractDungeon.ascensionLevel >= 1;
    }
}
