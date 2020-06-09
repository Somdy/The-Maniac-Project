package TheManiac.actions;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.patches.CardMarkFieldPatch;
import TheManiac.relics.VisionEye;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class FlashbackAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(FlashbackAction.class.getName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:FlashbackAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractCard actionCard;
    private AbstractCard.CardType cardType;
    private float startingDuration;
    private boolean modifyCostsForTurn;
    private CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    
    public FlashbackAction(AbstractCard card, int numCards, boolean modifyCostsForTurn) {
        this.actionCard = card;
        this.amount = numCards;
        this.modifyCostsForTurn = modifyCostsForTurn;
        this.cardType = null;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public FlashbackAction(AbstractCard card, int numCards, boolean modifyCostsForTurn, AbstractCard.CardType type) {
        this(card, numCards, modifyCostsForTurn);
        this.cardType = type;
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
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() <= 3) {
            this.addToBot(new TalkAction(true, TEXT[0], 2.0F, 2.0F));
            this.isDone = true;
            return;
        }
        
        if (this.duration == this.startingDuration) {
            this.amount = Math.min(this.amount, AbstractDungeon.actionManager.cardsPlayedThisCombat.size());
            initializeCardGroup(amount);
            
            if (tmpGroup.isEmpty()) {
                logger.info("ERROR: 无法从当前战斗中闪回任何卡牌。");
                this.isDone = true;
                return;
            }
            
            AbstractDungeon.gridSelectScreen.open(tmpGroup, 1, true, TEXT[1]);
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (AbstractDungeon.player.hand.size() < 10) {
                    tmpGroup.moveToHand(card);
                } else {
                    AbstractDungeon.player.createHandIsFullDialog();
                    tmpGroup.moveToDiscardPile(card);
                }
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            
            if (modifyCostsForTurn) {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    if (CardMarkFieldPatch.FlashbackField.flashbacked.get(card)) {
                        card.setCostForTurn(0);
                        CardMarkFieldPatch.FlashbackField.flashbacked.set(card, false);
                    }
                }
                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (CardMarkFieldPatch.FlashbackField.flashbacked.get(card)) {
                        card.setCostForTurn(0);
                        CardMarkFieldPatch.FlashbackField.flashbacked.set(card, false);
                    }
                }
            }
            
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }
        
        this.tickDuration();
        if (this.isDone) {
            tmpGroup.clear();
        }
    }
    
    private void initializeCardGroup(int num) {
        int attempt = 0;
        while (tmpGroup.size() != num) {
            attempt++;
            int endsize = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1;
            AbstractCard card = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.cardRandomRng.random(0, endsize)).makeCopy();
            if (allowedCard(card)) {
                boolean dupe = false;
                
                for (AbstractCard c : tmpGroup.group) {
                    if (c.name.equals(card.name)) {
                        dupe = true;
                        break;
                    }
                }
                
                if (!dupe) {
                    CardMarkFieldPatch.FlashbackField.flashbacked.set(card, true);
                    tmpGroup.addToRandomSpot(card);
                }
            }
            if (attempt > endsize ) {
                break;
            }
        }
    }
    
    private boolean allowedCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS) {
            return false;
        }
        if (card instanceof AbstractManiacCard && ((AbstractManiacCard) card).isUnreal && AbstractDungeon.player.getRelic(VisionEye.ID) == null) {
            logger.info("目前无法闪回虚幻的牌：" + card.name + "，重选。");
            return false;
        }
        if (this.cardType != null && card.type != this.cardType) {
            logger.info("取得 " + card.type + " " + card.name + "，与目标 " + this.cardType + " 不符。重选。");
            return false;
        }
        if (card.type == AbstractCard.CardType.POWER && this.cardType == null && AbstractDungeon.cardRandomRng.randomBoolean()) {
            return false;
        }
        
        return !card.cardID.equals(this.actionCard.cardID) && RarityFilter(card);
    }
    
    private boolean RarityFilter(AbstractCard card) {
        AbstractCard.CardRarity rarity;
        int roll = AbstractDungeon.cardRandomRng.random(99);
        
        if (roll < 21) {
            rarity = AbstractCard.CardRarity.COMMON;
        }
        else if (roll < 33) {
            rarity = AbstractCard.CardRarity.UNCOMMON;
        }
        else if (roll < 55) {
            rarity = AbstractCard.CardRarity.RARE;
        }
        else if (roll < 79) {
            rarity = AbstractCard.CardRarity.BASIC;
        } else {
            rarity = AbstractCard.CardRarity.SPECIAL;
        }
        
        if (card.rarity == rarity) {
            logger.info("随机取到 " + rarity.toString() +" " + card.name + "，弃置重选。");
        }
        
        return card.rarity != rarity;
    }
}
