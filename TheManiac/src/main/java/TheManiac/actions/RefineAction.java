package TheManiac.actions;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class RefineAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:RefineAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private int numCard;
    private AbstractPlayer player;
    private ArrayList<AbstractCard> unenchantable = new ArrayList<>();
    
    public RefineAction(int amount) {
        numCard = amount;
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            
            for (AbstractCard card : player.hand.group) {
                if (card instanceof AbstractManiacCard) {
                    if (!((AbstractManiacCard) card).canEnchant()) {
                        this.unenchantable.add(card);
                    }
                } else {
                    this.unenchantable.add(card);
                }
            }
            
            if (this.unenchantable.size() == player.hand.group.size()) {
                this.isDone = true;
                return;
            }
            
            if (player.hand.group.size() - this.unenchantable.size() == numCard) {
                for (AbstractCard card : player.hand.group) {
                    if (card instanceof AbstractManiacCard) {
                        if (((AbstractManiacCard) card).canEnchant()) {
                            ((AbstractManiacCard) card).enchant();
                            card.superFlash(Color.PURPLE.cpy());
                            card.applyPowers();
                        }
                    }
                }
                this.isDone = true;
                return;
            }
            
            player.hand.group.removeAll(this.unenchantable);
            
            if (player.hand.group.size() > numCard) {
                if (numCard > 1) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[1], 2, false, false, false, false);
                } else {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                }
                this.tickDuration();
                return;
            }
            
            if (player.hand.group.size() == numCard) {
                for (AbstractCard card : player.hand.group) {
                    if (card instanceof AbstractManiacCard) {
                        ((AbstractManiacCard) card).enchant();
                        card.superFlash(Color.PURPLE.cpy());
                        card.applyPowers();
                    }
                }
                //returnCards();
                this.isDone = true;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (card instanceof AbstractManiacCard) {
                    ((AbstractManiacCard) card).enchant();
                    card.superFlash(Color.PURPLE);
                    card.applyPowers();
                    player.hand.addToTop(card);
                }
            }

            //returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
        
        if (this.isDone) {
            returnCards();
        }
    }
    
    private void returnCards() {
        for (AbstractCard card : this.unenchantable) {
            player.hand.addToTop(card);
        }
        player.hand.refreshHandLayout();
    }
}
