package TheManiac.actions.ThePossessedAction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class GlyphAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(GlyphAction.class.getName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:GlyphAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private boolean modifyCostsForCombat;
    private int choice;
    private int num;
    
    public GlyphAction(int choice, int num, boolean modifyCostsForCombat) {
        this.choice = choice;
        this.num = num;
        this.modifyCostsForCombat = modifyCostsForCombat;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (choice <= 0) {
                this.isDone = true;
                return;
            }

            ArrayList<AbstractCard> colorlessChoice = gnrColorlessCardsChoice();
            ArrayList<AbstractCard> randomChoice = gnrCardsChoice();
            
            if (!colorlessChoice.isEmpty() && !randomChoice.isEmpty()) {
                randomChoice.addAll(colorlessChoice);
            }
            
            tmpGroup = getRandomCards(randomChoice);
            
            if (!tmpGroup.isEmpty()) {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.num, true, TEXT[0] + num + TEXT[1]);
            } else {
                logger.info("无法获得可用的随机卡牌。请联系作者。");
            }
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (modifyCostsForCombat) {
                    card.modifyCostForCombat(-9);
                } else {
                    card.setCostForTurn(0);
                }
                
                if (AbstractDungeon.player.hand.size() <= 9) {
                    tmpGroup.moveToHand(card);
                } else {
                    tmpGroup.moveToDiscardPile(card);
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
        
        if (this.isDone) {
            if (!tmpGroup.isEmpty()) {
                tmpGroup.clear();
            }
        }
    }
    
    private CardGroup getRandomCards(ArrayList<AbstractCard> choices) {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        
        while (tmpGroup.size() != this.choice) {
            AbstractCard card = choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
            if (!tmpGroup.contains(card)) {
                tmpGroup.addToTop(card);
            }
        }
        
        return tmpGroup;
    }
    
    private ArrayList<AbstractCard> gnrCardsChoice() {
        ArrayList<AbstractCard> tmpCards = new ArrayList<>();
        
        while (tmpCards.size() != this.choice) {
            boolean dupe = false;
            AbstractCard tmp = AbstractDungeon.returnTrulyRandomCardInCombat();
            
            for (AbstractCard card : tmpCards) {
                if (card.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            
            if (!dupe) {
                tmpCards.add(tmp.makeCopy());
            }
        }
        
        return tmpCards;
    }

    private ArrayList<AbstractCard> gnrColorlessCardsChoice() {
        ArrayList<AbstractCard> tmpCards = new ArrayList<>();

        while (tmpCards.size() != this.choice) {
            boolean dupe = false;
            AbstractCard tmp = AbstractDungeon.returnTrulyRandomColorlessCardInCombat();

            for (AbstractCard card : tmpCards) {
                if (card.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }

            if (!dupe) {
                tmpCards.add(tmp.makeCopy());
            }
        }

        return tmpCards;
    }
}
