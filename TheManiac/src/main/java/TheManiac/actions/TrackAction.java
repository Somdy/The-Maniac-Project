package TheManiac.actions;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.relics.VisionEye;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TrackAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(TrackAction.class.getName());
    public CardGroup cardGroup;
    private AbstractCard.CardType cardType;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:TrackAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private float startingDuration;
    private CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public TrackAction(int numCards, CardGroup cardGroup) {
        this.amount = numCards;
        this.cardGroup = cardGroup;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public TrackAction(int numCards, CardGroup cardGroup, AbstractCard.CardType cardType) {
        this.amount = numCards;
        this.cardGroup = cardGroup;
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

            if (this.cardGroup == AbstractDungeon.player.exhaustPile) {
                int usefulCardsUnique = 0;
                if (this.cardType != null) {
                    try {
                        usefulCardsUnique = getNumUsefulExCards(this.cardType);
                    } catch (Exception e) {
                        logger.info("Unable to count available exhaust cards when Tracking -- Type. Report this if you see it." + e);
                        e.printStackTrace();
                    }
                    
                    if (usefulCardsUnique > 0) {
                        try {
                            tmpGroup = getUsefulExhaustedCards(this.cardType);
                        } catch (Exception e) {
                            logger.info("Unable to get available exhaust cards when Tracking -- Type. Report this if you see it." + e);
                            e.printStackTrace();
                        }
                    }
                    else {
                        this.isDone = true;
                        return;
                    }
                }
                else {
                    try {
                        usefulCardsUnique = getNumUsefulExCards();
                    } catch (Exception e) {
                        logger.info("Unable to count available exhaust cards when Tracking -- Not Type. Report this if you see it.");
                        e.printStackTrace();
                    }
                    
                    if (usefulCardsUnique > 0) { 
                        try {
                            tmpGroup = getUsefulExhaustedCards();
                        } catch (Exception e) {
                            logger.info("Unable to get available exhaust cards when Tracking -- Not Type. Report this if you see it.");
                            e.printStackTrace();
                        }
                    }
                    else {
                        this.isDone = true;
                        return;
                    }
                }
            }
            else {
                if (this.amount != -1) {
                    for (int i = 0; i < Math.min(this.amount, this.cardGroup.size()); i++) {
                        AbstractCard card = this.cardGroup.group.get(this.cardGroup.size() - i - 1);
                        if (this.cardType != null) {
                            if (card.type == this.cardType) { tmpGroup.addToTop(card); }
                        }
                        else { tmpGroup.addToTop(card); }
                    }
                }
                else {
                    for (AbstractCard card : this.cardGroup.group) {
                        if (this.cardType != null) {
                            if (card.type == this.cardType) { tmpGroup.addToTop(card); }
                        }
                        else { tmpGroup.addToTop(card); }
                    }
                }
            }


            if (this.cardGroup != AbstractDungeon.player.exhaustPile) {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
            }
            else {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[1]);
            }
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (this.cardGroup != AbstractDungeon.player.exhaustPile) {
                for (AbstractCard card : tmpGroup.group) {
                    if (AbstractDungeon.gridSelectScreen.selectedCards.contains(card)) {
                        this.cardGroup.moveToHand(card);
                    }
                    else {
                        this.cardGroup.moveToExhaustPile(card);
                    }
                }
            }
            else {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    this.cardGroup.moveToHand(card);
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();

        if (this.isDone) {
            for (AbstractCard card : this.cardGroup.group) {
                if (card instanceof AbstractManiacCard) {
                    ((AbstractManiacCard) card).triggerOnTrack();
                }
            }
        }
    }

    private int getNumUsefulExCards() {
        int usefulCards = 0;
        ArrayList<AbstractCard> tmpCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                if (card instanceof AbstractManiacCard) {
                    if (!((AbstractManiacCard) card).isUnreal || AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                        if (!tmpCards.contains(card)) {
                            usefulCards++;
                            tmpCards.add(card);
                        }
                    }
                }
                else {
                    if (!card.cardID.equals(Exhume.ID)) {
                        if (!tmpCards.contains(card)) {
                            usefulCards++;
                            tmpCards.add(card);
                        }
                    }
                }
            }
        }
        return usefulCards;
    }

    private int getNumUsefulExCards(AbstractCard.CardType cardType) {
        int usefulCards = 0;
        ArrayList<AbstractCard> tmpCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card.type == cardType) {
                if (card instanceof AbstractManiacCard) {
                    if (!((AbstractManiacCard) card).isUnreal || AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                        if (!tmpCards.contains(card)) {
                            usefulCards++;
                            tmpCards.add(card);
                        }
                    }
                }
                else {
                    if (!card.cardID.equals(Exhume.ID)) {
                        if (!tmpCards.contains(card)) {
                            usefulCards++;
                            tmpCards.add(card);
                        }
                    }
                }
            }
        }
        return usefulCards;
    }

    private CardGroup getUsefulExhaustedCards() {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.amount != -1) {
            for (int i = 0; i < Math.min(this.amount, this.cardGroup.size()); i++) {
                AbstractCard card = this.cardGroup.group.get(this.cardGroup.size() - i - 1);
                if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                    if (card instanceof AbstractManiacCard) {
                        if (!((AbstractManiacCard) card).isUnreal || AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                    else {
                        if (!card.cardID.equals(Exhume.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                }
            }
        }
        else {
            for (AbstractCard card : this.cardGroup.group) {
                if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                    if (card instanceof AbstractManiacCard) {
                        if (!((AbstractManiacCard) card).isUnreal || AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                    else {
                        if (!card.cardID.equals(Exhume.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                }
            }
        }
        return tmpGroup;
    }

    private CardGroup getUsefulExhaustedCards(AbstractCard.CardType cardType) {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.amount != -1) {
            for (int i = 0; i < Math.min(this.amount, this.cardGroup.size()); i++) {
                AbstractCard card = this.cardGroup.group.get(this.cardGroup.size() - i - 1);
                if (card.type == cardType) {
                    if (card instanceof AbstractManiacCard) {
                        if (!((AbstractManiacCard) card).isUnreal || AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                    else {
                        if (!card.cardID.equals(Exhume.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                }
            }
        }
        else {
            for (AbstractCard card : this.cardGroup.group) {
                if (card.type == cardType) {
                    if (card instanceof AbstractManiacCard) {
                        if (!((AbstractManiacCard) card).isUnreal || AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                    else {
                        if (!card.cardID.equals(Exhume.ID)) {
                            if (!tmpGroup.contains(card)) { tmpGroup.addToTop(card); }
                        }
                    }
                }
            }
        }
        return tmpGroup;
    }
}
