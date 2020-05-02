package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Iterator;

public class HastenAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:HastenAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer player;
    private boolean doubleEnergy;
    private boolean drawCard;
    private int drawAmt;

    public HastenAction(boolean doubleEnergy, boolean drawCard, int drawAmt) {
        this.doubleEnergy = doubleEnergy;
        this.drawCard = drawCard;
        this.drawAmt = drawAmt;
        this.player = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            if (this.player.hand.isEmpty()) {
                this.isDone = true;
            }
            else if (this.player.hand.size() == 1) {
                if (this.player.hand.getBottomCard().costForTurn <= 0) {
                    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(EnergyPanel.getCurrentEnergy()));
                }
                else if (this.player.hand.getBottomCard().costForTurn > 0) {
                    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.player.hand.getBottomCard().costForTurn));
                }
                this.player.hand.moveToDiscardPile(this.player.hand.getBottomCard());
                this.tickDuration();
            }
            else {
                if (!doubleEnergy) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                }
                else {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[1], 1, false);
                }
                this.tickDuration();
            }
        }
        else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                AbstractCard card;
                int energyGain;

                for (AbstractCard abstractCard : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    card = abstractCard;
                    if (card.costForTurn <= 0) {
                        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(EnergyPanel.getCurrentEnergy()));
                    } else {
                        energyGain = card.costForTurn;
                        if (doubleEnergy) {
                            energyGain *= 2;
                        }
                        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energyGain));
                    }
                    this.player.hand.moveToDiscardPile(card);
                    card.triggerOnManualDiscard();
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }

            this.tickDuration();
        }
        
        if (this.isDone && this.drawCard) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.player, this.drawAmt, false));
        }
    }
}
