package TheManiac.actions.PossessedMonsterAction;

import TheManiac.cards.colorless.skill.LostSoul;
import TheManiac.monsters.possessed_enemies.AbstractPossessedMonster;
import TheManiac.relics.PossessedManuscripts;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LostSoulAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(LostSoulAction.class.getName());
    private AbstractPossessedMonster m;
    private boolean toHand;
    private AbstractPlayer player = AbstractDungeon.player;

    public LostSoulAction(AbstractPossessedMonster m, boolean toHand) {
        this.m = m;
        this.toHand = toHand;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (player.getRelic(PossessedManuscripts.ID) == null) {
                this.isDone = true;
                return;
            }
            
            PossessedManuscripts r = (PossessedManuscripts) player.getRelic(PossessedManuscripts.ID);
            
            if (m.ownClass == AbstractPossessedMonster.PossessedClass.Glorious) {
                ShuffleLostSouls(player.discardPile, 2, r.activeAmounts.get(3).intValue());
            }
            else if (m.ownClass == AbstractPossessedMonster.PossessedClass.Uncertain) {
                ShuffleLostSouls(player.drawPile, 2, r.activeAmounts.get(3).intValue());
            }
            else if (m.ownClass == AbstractPossessedMonster.PossessedClass.Risky) {
                ShuffleLostSouls(player.drawPile, 3, r.activeAmounts.get(3).intValue());
                this.addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, r.activeAmounts.get(3).intValue()), r.activeAmounts.get(3).intValue()));
            } else {
                logger.info("ERROR：不在范围内的分类：" + m.ownClass);
                this.isDone = true;
            }
        }
        
        this.tickDuration();
        
        if (this.isDone && toHand) {
            ShuffleLostSouls(player.hand, 3, AbstractDungeon.cardRandomRng.random(1, 2));
        }
    }
    
    private void ShuffleLostSouls(CardGroup cardGroup, int maxIndex, int amount) {
        if (cardGroup != null && amount > 0) {
            if (maxIndex > 3)
                maxIndex = 3;
            for (int i = 0; i < amount; i++) {
                int index = AbstractDungeon.cardRandomRng.random(1, maxIndex);
                LostSoul soul = new LostSoul(index);
                if (cardGroup == player.drawPile)
                    this.addToBot(new MakeTempCardInDrawPileAction(soul, 1, true, true));
                else if (cardGroup == player.discardPile)
                    this.addToBot(new MakeTempCardInDiscardAction(soul, 1));
                else if (cardGroup == player.hand) {
                    if (player.hand.size() < 10) {
                        this.addToBot(new MakeTempCardInHandAction(soul, 1));
                    }
                    this.addToBot(new MakeTempCardInDiscardAction(soul, 1));
                } else {
                    logger.info("ERROR：不在范围内的CardGroup：" + cardGroup);
                    return;
                }
            }
        }
    }
}
