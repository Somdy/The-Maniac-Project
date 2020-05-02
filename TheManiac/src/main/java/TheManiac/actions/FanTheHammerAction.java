package TheManiac.actions;

import TheManiac.powers.NoAttackPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class FanTheHammerAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(FanTheHammerAction.class.getName());
    private AbstractCard useCard;
    private AbstractPlayer player;
    private boolean exhaustOnUse;
    
    public FanTheHammerAction(AbstractCard card, int amount, boolean exhaustOnUse) {
        this.player = AbstractDungeon.player;
        this.useCard = card;
        this.amount = amount;
        this.exhaustOnUse = exhaustOnUse;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            int Attacks = 0;
            for (AbstractCard c : this.player.hand.group) {
                if (c.type == AbstractCard.CardType.ATTACK) {
                    Attacks++;
                }
            }
            if (Attacks == 0) {
                this.isDone =true;
                return;
            }
            else {
                try {
                    for (int i = 0; i < Attacks; i++) {
                        AbstractCard card = this.player.hand.getRandomCard(AbstractCard.CardType.ATTACK, true);
                        AbstractMonster monster = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        this.player.hand.group.remove(card);
                        AbstractDungeon.getCurrRoom().souls.remove(card);
                        card.exhaustOnUseOnce = this.exhaustOnUse;
                        this.player.limbo.group.add(card);
                        card.current_y = this.useCard.current_y;
                        card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.scale; card.target_y = (float)Settings.HEIGHT / 2.0F;
                        card.targetAngle = 0.0F;
                        card.lighten(false);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        card.applyPowers();
                        logger.info("Get card " + card.name + " and try to play it on " + monster.name);
                        this.addToTop(new NewQueueCardAction(card, monster, false, true));
                        this.addToTop(new UnlimboAction(card));
                        if (!Settings.FAST_MODE) {
                            this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                        } else {
                            this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
                        }
                    }
                } catch (Exception e) {
                    logger.info("Failed to do Fan The Hammer Action. Catch: ");
                    e.printStackTrace();
                }
                
            }
            
            this.tickDuration();
        }
        
        if (this.isDone) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.player, this.player, new NoAttackPower(this.player, this.amount, true), this.amount));
        }
    }
}
