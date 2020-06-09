package TheManiac.patches;


import TheManiac.powers.BurnBloodPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch( clz = AbstractCard.class, method = "freeToPlay" )
public class ModifyBurnBloodCosts {
    
    public static SpireReturn<Boolean> Prefix(AbstractCard _instance) {
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower(BurnBloodPower.POWER_ID)) {
            return SpireReturn.Return(true);
        } else {
            return SpireReturn.Continue();
        }
    }
}
