package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.shinies.CorruptingMist;
import TheManiac.relics.PossessedManuscripts;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class NatureNourish extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("NatureNourish");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/nature_nourish.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 3;
    
    public NatureNourish() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.magicNumber = this.baseMagicNumber = 14;
        this.isUnreal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, this.magicNumber, 0.05F));
        
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 2) {
            if (AbstractDungeon.actionManager.cardsPlayedThisCombat.get
                    (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 2).cardID.equals(CorruptingMist.ID)) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new NatureCorrupt(), (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            }

            if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof PossessedManuscripts && !((PossessedManuscripts) relic).activeEffects.get(4)) {
                        ((PossessedManuscripts) relic).activeEffects.set(4, true);
                        ((PossessedManuscripts) relic).updateEffectDescription();
                    }
                }
            }
        }
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeMagicNumber(level);
    }

    @Override
    public AbstractCard makeCopy() {
        return new NatureNourish();
    }
}
