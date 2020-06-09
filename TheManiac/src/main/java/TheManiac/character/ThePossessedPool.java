package TheManiac.character;

import com.megacrit.cardcrawl.cards.CardGroup;

public class ThePossessedPool {
    public static CardGroup shiniesPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup uncertaintiesPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup risksPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    
    public static void addCardPool() {
        shiniesPool.clear();
        uncertaintiesPool.clear();
        risksPool.clear();
        
        
    }
}
