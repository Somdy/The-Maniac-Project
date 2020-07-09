package TheManiac.actions;

import TheManiac.cards.colorless.skill.Unravel;
import TheManiac.powers.RavelPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class UnravelAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(UnravelAction.class.getName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:UnravelAction");
    private static final String[] TEXT = uiStrings.TEXT;
    private ArrayList<AbstractCard> removeList = new ArrayList<>();
    
    public UnravelAction(AbstractCreature source, int amount) {
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            if (AbstractDungeon.player.hand.size() == 1) {
                AbstractCard ravel = AbstractDungeon.player.hand.getBottomCard();
                if (ravel != null) {
                    
                    if (ravel.cardID.equals(Unravel.ID)) {
                        this.isDone = true;
                        return;
                    }
                    
                    logger.info("彻悟" + ravel.name);
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                            new RavelPower(AbstractDungeon.player, amount, ravel)));
                    AbstractDungeon.player.hand.moveToExhaustPile(AbstractDungeon.player.hand.getBottomCard());
                }
            } else {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (card instanceof Unravel) {
                        removeList.add(card);
                    }
                }
                if (!removeList.isEmpty()) {
                    AbstractDungeon.player.hand.group.removeAll(removeList);
                }
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
            }
            this.tickDuration();
        }
        
        if (!AbstractDungeon.handCardSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                logger.info("彻悟" + card.name);
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                        new RavelPower(AbstractDungeon.player, amount, card)));
                AbstractDungeon.player.hand.moveToExhaustPile(card);
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        
        this.tickDuration();
        
        if (this.isDone && !removeList.isEmpty()) {
            for (AbstractCard card : removeList) {
                AbstractDungeon.player.hand.addToTop(card);
            }
            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }
}
