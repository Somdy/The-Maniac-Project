package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import TheManiac.powers.InhibitionPower;
import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class Inhibition extends AbstractManiacCard {
    public static final String ID = "maniac:Inhibition";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/inhibition.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int POWERS = 16;
    private static final int UPGRADE_POWERS = 2;

    public Inhibition() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWERS;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 3;
        this.exhaust = true;
        this.isUnreal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isInLimbo()) {
            this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.magicNumber), this.magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InhibitionPower(p, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction("Neutral"));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.maniacExtraMagicNumber), this.maniacExtraMagicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isInLimbo()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
        else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POWERS);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Inhibition();
    }
}
