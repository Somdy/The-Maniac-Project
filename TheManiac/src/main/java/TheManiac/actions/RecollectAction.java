package TheManiac.actions;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.relics.VisionEye;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RecollectAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(RecollectAction.class.getName());
    public AbstractCard cardToGet;
    private boolean noCostForCombat;

    public RecollectAction(AbstractCreature owner, int amount, boolean noCostForCombat) {
        this.target = owner;
        this.amount = amount;
        this.noCostForCombat = noCostForCombat;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int usefulCards = 0;

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && c.rarity != AbstractCard.CardRarity.BASIC) {
                usefulCards++;
            }
            if (c instanceof AbstractManiacCard && ((AbstractManiacCard) c).isUnreal && !AbstractDungeon.player.hasRelic(VisionEye.ID)) {
                usefulCards--;
            }
        }

        logger.info("Current valid cards: " + usefulCards + ". Start recollecting.");

        if (usefulCards > 0) {
            for (int i = 0; i < this.amount; i++) {
                cardToGet = AbstractDungeon.player.exhaustPile.getRandomCard(AbstractDungeon.cardRandomRng);
                while ( (cardToGet instanceof AbstractManiacCard && ((AbstractManiacCard) cardToGet).isUnreal && !AbstractDungeon.player.hasRelic(VisionEye.ID)) ||
                        cardToGet.type == AbstractCard.CardType.CURSE || cardToGet.type == AbstractCard.CardType.STATUS || cardToGet.rarity == AbstractCard.CardRarity.BASIC) {
                    logger.info("Get invalid card: " + cardToGet.name + ". Roll again.");
                    cardToGet = AbstractDungeon.player.exhaustPile.getRandomCard(AbstractDungeon.cardRandomRng);
                }

                if (noCostForCombat) {
                    cardToGet.modifyCostForCombat(-9);
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(cardToGet, 1));
            }
        }
        else {
            this.isDone = true;
        }

        this.isDone = true;
    }
}
