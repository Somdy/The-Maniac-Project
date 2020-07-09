package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class WitnessTrueSorceryAction extends AbstractGameAction {
    private boolean modifiedCosts;
    private boolean retrieveCard = false;
    private boolean returnColorless = false;
    private AbstractCard.CardType cardType = null;
    private int numCards;

    public WitnessTrueSorceryAction(int amount, int numCards, boolean modifiedCosts) {
        this.numCards = numCards;
        this.amount = amount;
        this.modifiedCosts = modifiedCosts;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        ArrayList generateCards;
        if (this.numCards > 6) {
            this.numCards = 6;
        }
        if (this.returnColorless) {
            generateCards = this.gnrColorlessCardsChoices();
        }
        else {
            generateCards = this.gnrCardsChoices(this.cardType);
        }

        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCards, CardRewardScreen.TEXT[1], this.cardType != null);
        }
        else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard posCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    AbstractCard posCard2 = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        posCard.upgrade();
                        posCard2.upgrade();
                    }

                    if (this.modifiedCosts) {
                        posCard.modifyCostForCombat(-9);
                        posCard2.modifyCostForCombat(-9);
                    }
                    else {
                        posCard.setCostForTurn(0);
                        posCard2.setCostForTurn(0);
                    }
                    posCard.current_x = -1000.0F * Settings.scale;
                    posCard2.current_x = -1000.0F * Settings.scale + AbstractCard.IMG_HEIGHT_S;

                    if (this.amount == 1) {
                        if (AbstractDungeon.player.hand.size() < 10) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(posCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(posCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        }

                    }
                    else if (AbstractDungeon.player.hand.size() + this.amount <= 10) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(posCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(posCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }
                    else if (AbstractDungeon.player.hand.size() == 9) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(posCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(posCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }
                    else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(posCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(posCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }

                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }

                this.retrieveCard = true;
            }

        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> gnrColorlessCardsChoices() {
        ArrayList<AbstractCard> choice = new ArrayList<>();

        while (choice.size() != this.numCards) {
            boolean dupe = false;
            AbstractCard tmpCard = AbstractDungeon.returnTrulyRandomColorlessCardInCombat();

            for (AbstractCard c : choice) {
                if (c.cardID.equals(tmpCard.cardID)) {
                    dupe = true;
                    break;
                }
            }

            if (!dupe) {
                choice.add(tmpCard.makeCopy());
            }
        }

        return choice;
    }

    private ArrayList<AbstractCard> gnrCardsChoices(AbstractCard.CardType cardType) {
        ArrayList<AbstractCard> choice = new ArrayList<>();

        while(choice.size() != this.numCards) {
            boolean dupe = false;
            AbstractCard tmpCard;
            if (cardType == null) {
                tmpCard = AbstractDungeon.returnTrulyRandomCardInCombat();
            } else {
                tmpCard = AbstractDungeon.returnTrulyRandomCardInCombat(cardType);
            }

            for (AbstractCard c : choice) {
                if (c.cardID.equals(tmpCard.cardID)) {
                    dupe = true;
                    break;
                }
            }

            if (!dupe) {
                choice.add(tmpCard.makeCopy());
            }
        }

        return choice;
    }
}
