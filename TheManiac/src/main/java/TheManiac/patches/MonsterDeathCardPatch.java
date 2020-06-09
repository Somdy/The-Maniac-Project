package TheManiac.patches;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.minions.AbstractManiacMinion;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterDeathCardPatch {
    
    @SpirePatch( clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class onDeath {
        
        @SpireInsertPatch( rloc = 0 )
        public static void Insert(AbstractMonster _instance, boolean triggerRelics) {
            if (!_instance.isDying) {
                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (card instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) card).onMonsterDeath(_instance, false, true);
                    }
                }
                
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) card).onMonsterDeath(_instance, true, false);
                    }
                }
                
                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) card).onMonsterDeath(_instance, false, false);
                    }
                }

                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (card instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) card).onMonsterDeathInDeck(_instance);
                    }
                }
                
                if (AbstractDungeon.player instanceof TheManiacCharacter) {
                    if (((TheManiacCharacter) AbstractDungeon.player).hasMinions()) {
                        for (AbstractMonster minion : ((TheManiacCharacter) AbstractDungeon.player).minions.monsters) {
                            if (minion instanceof AbstractManiacMinion)
                                ((AbstractManiacMinion) minion).updateOnMonsterDeath(_instance);
                        }
                    }
                }
            }
        }
    }
}
