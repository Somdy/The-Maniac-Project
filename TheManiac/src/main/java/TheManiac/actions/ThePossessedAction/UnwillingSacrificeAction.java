package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import TheManiac.relics.PossessedManuscripts;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class UnwillingSacrificeAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:SacrificeAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer player;
    private int souls;
    
    public UnwillingSacrificeAction(int increaseAmt) {
        this.amount = increaseAmt;
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            if (player.hand.size() == 1) {
                AbstractCard card = player.hand.getBottomCard();
                int hpIncrease1 = card.cost;
                int hpIncrease2 = card.costForTurn;
                int increment = Math.max(hpIncrease1, hpIncrease2);
                player.increaseMaxHp(increment, true);
                modifySouls(card);
                player.hand.moveToExhaustPile(card);
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0] + this.amount, 1, false);
            }
            
            this.tickDuration();
        }
        else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                int hpIncrease1 = card.cost;
                int hpIncrease2 = card.costForTurn;
                int increment = Math.max(hpIncrease1, hpIncrease2);
                player.increaseMaxHp(increment, true);
                modifySouls(card);
                player.hand.moveToExhaustPile(card);
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        
        this.tickDuration();
        if (this.isDone) {
            if (souls <= 0) {
                souls = 1;
            }
            if (player.getRelic(PossessedManuscripts.ID) != null) {
                for (AbstractRelic relic : player.relics) {
                    if (relic instanceof PossessedManuscripts) {
                        relic.flash();
                        ((PossessedManuscripts) relic).activeEffects.set(3, true);
                        ((PossessedManuscripts) relic).modifyActiveAmount(3, souls);
                    }
                }
            }
        }
    }
    
    private void modifySouls(AbstractCard card) {
        if (card.cost > 0) {
            souls = card.cost;
        } else {
            souls = 1;
        }
        
        if (card.rarity == AbstractCard.CardRarity.COMMON || card.rarity == AbstractCard.CardRarity.UNCOMMON
        || card.rarity == AbstractCard.CardRarity.BASIC || card.rarity == AbstractCard.CardRarity.SPECIAL) {
            souls += 1;
        }
        else if (card.rarity == AbstractCard.CardRarity.RARE || card.rarity == AbstractCard.CardRarity.CURSE) {
            souls += 2;
        }
        else if (card.rarity == ManiacRisksCard.Enum.THE_SHINIES || card.rarity == ManiacRisksCard.Enum.THE_UNCERTAINTIES) {
            souls += 3;
        }
        else if (card.rarity == ManiacRisksCard.Enum.THE_RISKS || card.rarity == ManiacRisksCard.Enum.THE_POSSESSED) {
            souls += 4;
        } else {
            souls += 5;
        }
    }
}