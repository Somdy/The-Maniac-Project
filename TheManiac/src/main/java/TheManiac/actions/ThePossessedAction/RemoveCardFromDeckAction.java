package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

public class RemoveCardFromDeckAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:RemoveCardFromDeckAction");
    private static final String[] TEXT = uiStrings.TEXT;
    private boolean freeToChoose;
    private boolean random;
    private CardGroup selects = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    
    public RemoveCardFromDeckAction(int amount, boolean freeToChoose, boolean random) {
        this.amount = amount;
        this.freeToChoose = freeToChoose;
        this.random = random;
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            AbstractPlayer player = AbstractDungeon.player;
            if (player.masterDeck.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            selects.clear();
            for (AbstractCard c : player.masterDeck.group) {
                if (removableCard(c)) selects.addToTop(c);
            }
            
            if (selects.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(selects, amount, freeToChoose, (TEXT[0] + amount + TEXT[1]));
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            removeCards(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        
        this.tickDuration();
        
        if (this.isDone) {
            selects.clear();
        }
    }
    
    private boolean removableCard(AbstractCard card) {
        return !(card instanceof ManiacRisksCard);
    }
    
    private void removeCards(ArrayList<AbstractCard> removes) {
        if (removes.isEmpty()) return;
        
        float displayCount = 0F;
        for (AbstractCard card : removes) {
            card.untip();
            card.unhover();
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F));
            displayCount += Settings.WIDTH / 6.0F;
            AbstractDungeon.player.masterDeck.removeCard(card);
        }
    }
}
