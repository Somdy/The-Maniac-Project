package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import TheManiac.cards.the_possessed.risks.AbstractRisksCard;
import TheManiac.cards.the_possessed.risks.UnwillingSacrifice;
import TheManiac.cards.the_possessed.uncertainties.AbstractUncertaintiesCard;
import TheManiac.relics.PossessedManuscripts;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

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
                int Increase1 = card.cost;
                int Increase2 = card.costForTurn;
                int increment = Math.max(Increase1, Increase2);
                smithRiskAndUncertainty(increment);
                modifySouls(card);
                player.hand.moveToExhaustPile(card);
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0] + this.amount, 1, false);
            }
            
            this.tickDuration();
        }
        else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                int Increase1 = card.cost;
                int Increase2 = card.costForTurn;
                int increment = Math.max(Increase1, Increase2);
                smithRiskAndUncertainty(increment);
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
    
    private void smithRiskAndUncertainty(int level) {
        if (!hasRiskOrUncertainty()) return;

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        
        for (AbstractCard card : player.masterDeck.group) {
            if ((card instanceof AbstractRisksCard || card instanceof AbstractUncertaintiesCard) && !card.cardID.equals(UnwillingSacrifice.ID))
                tmp.addToRandomSpot(card);
        }
        
        AbstractCard target = tmp.getRandomCard(true);
        
        if (target != null) {
            for (AbstractCard card : player.masterDeck.group) {
                if (card == target && (card instanceof AbstractRisksCard || card instanceof AbstractUncertaintiesCard)) {
                    ((ManiacRisksCard) card).smith(level);
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
                    this.addToBot(new WaitAction(Settings.ACTION_DUR_MED));
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
            souls += MathUtils.random(0, 1);
        }
        else if (card.rarity == AbstractCard.CardRarity.RARE || card.rarity == AbstractCard.CardRarity.CURSE) {
            souls += MathUtils.random(1, 2);
        }
        else if (card.rarity == ManiacRisksCard.Enum.THE_SHINIES || card.rarity == ManiacRisksCard.Enum.THE_UNCERTAINTIES) {
            souls += MathUtils.random(2, 3);
        }
        else if (card.rarity == ManiacRisksCard.Enum.THE_RISKS || card.rarity == ManiacRisksCard.Enum.THE_POSSESSED) {
            souls += MathUtils.random(2, 4);
        } else {
            souls += MathUtils.random(4, 5);
        }
    }
    
    private boolean hasRiskOrUncertainty() {
        if (player.masterDeck.isEmpty()) return false;
        for (AbstractCard card : player.masterDeck.group) {
            if (card instanceof AbstractRisksCard || card instanceof AbstractUncertaintiesCard)
                return true;
        }
        
        return false;
    }
}