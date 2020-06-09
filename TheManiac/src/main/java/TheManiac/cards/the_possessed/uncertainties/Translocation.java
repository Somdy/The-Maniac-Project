package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.possessed.AbstractPossessedCard;
import TheManiac.patches.CardMarkFieldPatch;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Translocation extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("Translocation");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/translocation.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    private boolean activeGateway;
    
    public Translocation() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.combatCounter = 0;
        this.activeGateway = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.activeGateway = true;
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (this.activeGateway && !AbstractDungeon.player.masterDeck.isEmpty() 
                && !CardMarkFieldPatch.TranslocationCopyField.isGatewayCopy.get(card) 
                && !CardMarkFieldPatch.TranslocationCopyField.isGatewayNega.get(card)) {
            AbstractCard c = AbstractDungeon.player.masterDeck.getRandomCard(true).makeStatEquivalentCopy();
            if (c == null || c instanceof AbstractPossessedCard) {
                c = AbstractDungeon.player.masterDeck.getRandomCard(true).makeStatEquivalentCopy();
            }
            c.isEthereal = true;
            c.purgeOnUse = true;
            c.modifyCostForCombat(-9);
            c.isCostModified = true;
            c.glowColor = Color.GRAY;
            CardMarkFieldPatch.TranslocationCopyField.isGatewayCopy.set(c, true);
            this.addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy(), 1));
            this.combatCounter++;
        }
        
        if (this.combatCounter > 3) {
            if (this.activeGateway && !AbstractDungeon.player.masterDeck.isEmpty()
                    && !CardMarkFieldPatch.TranslocationCopyField.isGatewayCopy.get(card)
                    && !CardMarkFieldPatch.TranslocationCopyField.isGatewayNega.get(card)) {
                AbstractCard c = AbstractDungeon.player.masterDeck.getRandomCard(true).makeStatEquivalentCopy();
                if (c == null || c instanceof AbstractPossessedCard) {
                    c = AbstractDungeon.player.masterDeck.getRandomCard(true).makeStatEquivalentCopy();
                }
                c.purgeOnUse = true;
                c.glowColor = Color.GRAY;
                CardMarkFieldPatch.TranslocationCopyField.isGatewayNega.set(c, true);
                this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), 1, true, true));
                this.combatCounter++;
                System.out.println(this.name + " has detected a fresh player!");
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        this.activeGateway = false;
        this.combatCounter = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (CardMarkFieldPatch.TranslocationCopyField.isGatewayNega.get(card)) {
                card.superFlash(Color.RED);
                this.addToBot(new DamageAction(AbstractDungeon.player, 
                        new DamageInfo(AbstractDungeon.player, 5, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Translocation();
    }
}
