package TheManiac.cards.maniac_blue.skill;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Infuriate extends AbstractManiacCard {
    public static final Logger logger = LogManager.getLogger(Infuriate.class.getName());
    public static final String ID = "maniac:Infuriate";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/infuriate.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int HP_PER_STR = 16;

    public Infuriate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = HP_PER_STR;
        this.exhaust = true;
        this.isUnreal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int totalStr = (p.maxHealth - p.currentHealth) / this.maniacExtraMagicNumber;
        logger.info("Infuriate current total strength: " + totalStr);
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.magicNumber += 1;
        }
        logger.info("Infuriate current strengths to apply: " + this.magicNumber);
        AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_CHAMP_CHARGE"));
        AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TheManiacCharacter.charStrings.TEXT[6], 1.0f, 2.0f));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new InflameEffect(p), 0.25F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new InflameEffect(p), 0.25F));
        for (int i = 0; i < totalStr; i++) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int powersToApply = this.magicNumber;
        int counts = 0;
        int totalStr = (AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth) / this.maniacExtraMagicNumber;
        logger.info("Infuriate current total strength update: " + totalStr);
        if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
            powersToApply += 1;
        }
        for (int i = 0; i < totalStr; i++) {
            counts += powersToApply;
        }
        this.rawDescription = DESCRIPTION;
        this.rawDescription += EXTENDED_DESCRIPTION[0] + counts + EXTENDED_DESCRIPTION[1];
        initializeDescription();
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
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            if (TheManiac.leisureMode) {
                this.upgradeManiacExtraMagicNumber(-11);
            }
            else if (TheManiac.challengerMode) {
                this.upgradeManiacExtraMagicNumber(-6);
            }
            else {
                this.upgradeManiacExtraMagicNumber(-8);
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Infuriate();
    }
}
