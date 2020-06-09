package TheManiac.actions;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.relics.SmartDetector;
import TheManiac.relics.VisionEye;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DetectAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(DetectAction.class.getName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:DetectAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private CardGroup cardGroup;
    private AbstractCard.CardType cardType;
    private float startingDuration;
    private CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    
    public DetectAction(int numCards, CardGroup cardGroup) {
        this.amount = numCards;
        this.cardGroup = cardGroup;
        this.cardType = null;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public DetectAction(int numCards, CardGroup cardGroup, AbstractCard.CardType cardType) {
        this(numCards, cardGroup);
        this.cardType = cardType;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }
    
    @Override
    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            return;
        }
        if (this.cardGroup.isEmpty()) {
            this.isDone = true;
            return;
        }
        
        if (this.duration == this.startingDuration) {
            if (this.cardType == null) {
                int allows = allowNum(this.cardGroup);
                if (allows == 0) {
                    this.isDone = true;
                    return;
                }
                if (this.amount != -1) {
                    int num = Math.min(this.amount, allows);
                    tmpGroup = getRandomUnspecifiedCards(num, this.cardGroup);
                } else {
                    tmpGroup = getRandomUnspecifiedCards(allows, this.cardGroup);
                }
            } else {
                int allows = allowNum(this.cardGroup, this.cardType);
                if (allows == 0) {
                    this.isDone = true;
                    return;
                }
                if (this.amount != -1) {
                    int num = Math.min(allows, this.amount);
                    tmpGroup = getRandomTypeCards(num, this.cardGroup, this.cardType);
                } else {
                    tmpGroup = getRandomTypeCards(allows, this.cardGroup, this.cardType);
                }
            }
            
            if (tmpGroup.isEmpty()) {
                logger.info("ERROR: 无法从" + this.cardGroup.toString() + "中获取任何牌");
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(tmpGroup, 1, true, TEXT[0]);
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : tmpGroup.group) {
                if (AbstractDungeon.gridSelectScreen.selectedCards.contains(card)) {
                    if (AbstractDungeon.player.hand.size() < 10) {
                        this.cardGroup.moveToHand(card);
                    } else {
                        AbstractDungeon.player.createHandIsFullDialog();
                        this.cardGroup.moveToDiscardPile(card);
                    }
                }
                
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        
        this.tickDuration();
        
        if (this.isDone) {
            for (AbstractCard card : tmpGroup.group) {
                if (card instanceof AbstractManiacCard) {
                    ((AbstractManiacCard) card).WhenDetected();
                }
            }
            tmpGroup.clear();
        }
    }
    
    private int allowNum(CardGroup cardGroup) {
        if (cardGroup.size() <= 0) {
            return 0;
        }
        
        int num = 0;
        
        for (AbstractCard card : cardGroup.group) {
            if (allowedCard(card)) {
                num++;
            }
        }
        
        return num;
    }

    private int allowNum(CardGroup cardGroup, AbstractCard.CardType type) {
        if (cardGroup.size() <= 0) {
            return 0;
        }
        
        int num = 0;

        for (AbstractCard card : cardGroup.group) {
            if (allowedCard(card, type)) {
                num++;
            }
        }

        return num;
    }
    
    private boolean allowedCard(AbstractCard card) {
        if ( (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) && AbstractDungeon.player.getRelic(SmartDetector.ID) != null) {
            return false;
        }
        if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).isUnreal && AbstractDungeon.player.getRelic(VisionEye.ID) == null) {
            logger.info("目前无法侦测到虚幻的牌，重选。");
            return false;
        }
        
        return true;
    }

    private boolean allowedCard(AbstractCard card, AbstractCard.CardType type) {
        if ( (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) && AbstractDungeon.player.getRelic(SmartDetector.ID) != null) {
            return false;
        }
        if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).isUnreal && AbstractDungeon.player.getRelic(VisionEye.ID) == null) {
            logger.info("目前无法侦测到虚幻的牌，重选。");
            return false;
        }

        return card.type == type;
    }
    
    private CardGroup getRandomUnspecifiedCards(int num, CardGroup cardGroup) {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        
        while (tmpGroup.size() != num) {
            AbstractCard card = cardGroup.getRandomCard(AbstractDungeon.cardRandomRng);
            if (allowedCard(card)) {
                if (!tmpGroup.contains(card)) {
                    tmpGroup.addToRandomSpot(card);
                }
            }
        }
        
        if (tmpGroup.isEmpty()) {
            logger.info("ERROR: 在" + cardGroup.toString() + "中没有足够的牌");
        }
        
        return tmpGroup;
    }
    
    private CardGroup getRandomTypeCards(int num, CardGroup cardGroup, AbstractCard.CardType type) {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        while (tmpGroup.size() != num) {
            AbstractCard card = cardGroup.getRandomCard(AbstractDungeon.cardRandomRng);
            if (allowedCard(card, type)) {
                if (!tmpGroup.contains(card)) {
                    tmpGroup.addToRandomSpot(card);
                }
            }
        }

        if (tmpGroup.isEmpty()) {
            logger.info("ERROR: 在" + cardGroup.toString() + "中没有足够的" + type.toString());
        }

        return tmpGroup;
    }
}
