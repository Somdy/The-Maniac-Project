package TheManiac.helper;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ThePossessedPool {
    public static CardGroup ShiniesPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup UncertaintiesPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup RisksPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup PossessionsPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    
    public static void addCardPool() {
        ShiniesPool.clear();
        UncertaintiesPool.clear();
        RisksPool.clear();
        PossessionsPool.clear();
        
        for (AbstractCard card : ThePossessedInitializer.Shinies) {
            ShiniesPool.addToTop(card.makeCopy());
        }
        for (AbstractCard card : ThePossessedInitializer.Uncertainties) {
            UncertaintiesPool.addToTop(card.makeCopy());
        }
        for (AbstractCard card : ThePossessedInitializer.Risks) {
            RisksPool.addToTop(card.makeCopy());
        }
        for (AbstractCard card : ThePossessedInitializer.Possessed) {
            PossessionsPool.addToTop(card.makeCopy());
        }
    }
    
    public static void reloadCardPool() {
        ShiniesPool.clear();
        UncertaintiesPool.clear();
        RisksPool.clear();
        PossessionsPool.clear();
        
        for (AbstractCard card : ThePossessedInitializer.Shinies) {
            if (AbstractDungeon.player.masterDeck.findCardById(card.cardID) != null) {
                continue;
            }
            ShiniesPool.addToTop(card.makeCopy());
        }
        for (AbstractCard card : ThePossessedInitializer.Uncertainties) {
            if (AbstractDungeon.player.masterDeck.findCardById(card.cardID) != null) {
                continue;
            }
            UncertaintiesPool.addToTop(card.makeCopy());
        }
        for (AbstractCard card : ThePossessedInitializer.Risks) {
            if (AbstractDungeon.player.masterDeck.findCardById(card.cardID) != null) {
                continue;
            }
            RisksPool.addToTop(card.makeCopy());
        }
        for (AbstractCard card : ThePossessedInitializer.Possessed) {
            if (AbstractDungeon.player.masterDeck.findCardById(card.cardID) != null) {
                continue;
            }
            PossessionsPool.addToTop(card.makeCopy());
        }
    }
    
    public static void removeCardFromPool(ManiacRisksCard card) {
        ShiniesPool.removeCard(card.cardID);
        UncertaintiesPool.removeCard(card.cardID);
        RisksPool.removeCard(card.cardID);
        PossessionsPool.removeCard(card.cardID);
    }
}