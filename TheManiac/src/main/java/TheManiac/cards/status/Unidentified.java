package TheManiac.cards.status;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.UnidentifiedAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.relics.PossessedManuscripts;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Unidentified extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("Unidentified");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/possessed/unidentified.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    
    public Unidentified() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        float chance;
        if (p.hasRelic(PossessedManuscripts.ID)) {
            PossessedManuscripts relic = (PossessedManuscripts) p.getRelic(PossessedManuscripts.ID);
            chance = relic.activeAmounts.get(1).floatValue();
        } else {
            chance = AbstractDungeon.cardRandomRng.random(0.2F, 0.5F);
        }
        this.addToBot(new UnidentifiedAction(chance, p, m));
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public boolean canEnchant() {
        return false;
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public AbstractCard makeCopy() {
        return new Unidentified();
    }
}
