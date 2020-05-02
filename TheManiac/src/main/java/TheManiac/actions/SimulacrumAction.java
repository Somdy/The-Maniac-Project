package TheManiac.actions;

import TheManiac.cards.maniac_blue.skill.Simulacrum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class SimulacrumAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:SimulacrumAction");
    private static final String[] TEXT = uiStrings.TEXT;
    private boolean modifyCosts;
    private AbstractPlayer player;
    private ArrayList<AbstractCard> cardLists = new ArrayList<>();
    
    public SimulacrumAction(int amount, boolean modifyCosts) {
        this.amount = amount;
        this.modifyCosts = modifyCosts;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            
            for (AbstractCard card : this.player.hand.group) {
                if (card.cardID.equals(Simulacrum.ID) && !cardLists.contains(card)) {
                    cardLists.add(card);
                }
            }
            
            if (this.player.hand.size() - cardLists.size() == 0) {
                this.isDone = true;
                return;
            }
            
            if (this.player.hand.size() - cardLists.size() == 1) {
                for (AbstractCard c : this.player.hand.group) {
                    if (!c.cardID.equals(Simulacrum.ID)) {
                        for (int i = 0; i < this.amount; i++) {
                            AbstractCard tmp = c.makeStatEquivalentCopy();
                            tmp.purgeOnUse = true;
                            tmp.isEthereal = true;
                            if (modifyCosts) {
                                tmp.modifyCostForCombat(-9);
                            }
                            this.addToBot(new MakeTempCardInHandAction(tmp.makeStatEquivalentCopy()));
                        }
                        this.isDone = true;
                        return;
                    }
                }
            }
            
            this.player.hand.group.removeAll(this.cardLists);
            
            if (this.player.hand.group.size() > 1) {
                if (modifyCosts) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0] + TEXT[1], 1, false, false, false, false);
                }
                else {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                }
                this.tickDuration();
                return;
            }
            if (this.player.hand.group.size() == 1) {
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard tmp = this.player.hand.getTopCard().makeStatEquivalentCopy();
                    tmp.isEthereal = true;
                    tmp.purgeOnUse = true;
                    if (modifyCosts) {
                        tmp.modifyCostForCombat(-9);
                    }
                    this.addToBot(new MakeTempCardInHandAction(tmp.makeStatEquivalentCopy()));
                }
                refreshCards();
                this.isDone = true;
            }
        }
        
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.addToBot(new MakeTempCardInHandAction(card.makeStatEquivalentCopy()));
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard tmp = card.makeStatEquivalentCopy();
                    tmp.isEthereal = true;
                    tmp.purgeOnUse = true;
                    if (modifyCosts) {
                        tmp.modifyCostForCombat(-9);
                    }
                    this.addToBot(new MakeTempCardInHandAction(tmp.makeStatEquivalentCopy()));
                }
            }
            
            refreshCards();

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            
            this.isDone = true;
        }
        
        this.tickDuration();
    }
    
    private void refreshCards() {
        for (AbstractCard card : cardLists) {
            this.player.hand.addToTop(card);
        }
        this.player.hand.refreshHandLayout();
    }
}
