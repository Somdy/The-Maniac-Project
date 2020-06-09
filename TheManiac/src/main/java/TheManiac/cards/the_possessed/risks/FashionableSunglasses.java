package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.SunglassesAction;
import TheManiac.relics.PossessedManuscripts;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FashionableSunglasses extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("FashionableSunglasses");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/fashionable_sunglasses.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 0;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    private double chance;
    private boolean active;
    
    public FashionableSunglasses() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.counter = 0;
        this.chance = 0.01D;
        this.active = false;
        this.damage = this.baseDamage = 10;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.active = true;
        this.counter++;
        this.chance += this.counter * 0.05D;
        this.addToBot(new DrawCardAction(1, new SunglassesAction(new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL))));
        if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof PossessedManuscripts) {
                    ((PossessedManuscripts) relic).activeEffects.set(1, true);
                    ((PossessedManuscripts) relic).modifyActiveAmount(1, this.chance);
                }
            }
        }
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (onUse && AbstractDungeon.cardRandomRng.randomBoolean() && isThrilled)
            this.addToBot(new GainEnergyAction(1));
    }

    @Override
    public void onObtain() {
        super.onObtain();
        if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof PossessedManuscripts) {
                    relic.flash();
                    ((PossessedManuscripts) relic).activeEffects.set(1, true);
                    ((PossessedManuscripts) relic).modifyActiveAmount(1, 0.1D);
                }
            }
        }
    }

    @Override
    public void onRemoveFromMasterDeck() {
        super.onRemoveFromMasterDeck();
        if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof PossessedManuscripts) {
                    relic.flash();
                    ((PossessedManuscripts) relic).activeEffects.set(1, false);
                    ((PossessedManuscripts) relic).modifyActiveAmount(1, 0D);
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FashionableSunglasses();
    }
}