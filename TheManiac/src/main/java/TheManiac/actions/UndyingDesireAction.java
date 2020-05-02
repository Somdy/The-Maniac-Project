package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UndyingDesireAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(UndyingDesireAction.class.getName());
    public int energyGain;

    public UndyingDesireAction(int costs, int energyGain) {
        this.target = AbstractDungeon.player;
        this.source = AbstractDungeon.player;
        this.amount = costs;
        this.energyGain = energyGain;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        for (AbstractCard card : DrawCardAction.drawnCards) {
            if (card.cost >= this.amount) {
                this.addToBot(new GainEnergyAction(this.energyGain));
                break;
            }
        }
        this.isDone = true;
    }
}
