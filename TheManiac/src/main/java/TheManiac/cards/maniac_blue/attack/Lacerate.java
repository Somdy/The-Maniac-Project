package TheManiac.cards.maniac_blue.attack;

import TheManiac.TheManiac;
import TheManiac.actions.LacerateAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import TheManiac.powers.WeaknessPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;

import java.util.UUID;

public class Lacerate extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("Lacerate");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/lacerate.png";
    private static final String SHROUD_IMG = "maniacMod/images/1024portraits/maniac_blue/attack/dissever.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 2;
    
    public Lacerate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 8;
        this.isShroud = true;
        
        this.cardsToPreview = new Lacerate(null, this.upgraded);
    }

    public Lacerate(boolean upgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 8;
        this.isShroud = true;

        if (upgraded) {
            this.upgrade();
        }
    }

    public Lacerate(UUID corUUID, boolean upgraded) {
        super(ID, EXTENDED_DESCRIPTION[1], SHROUD_IMG, COST, EXTENDED_DESCRIPTION[0], TYPE, COLOR, RARITY, CardTarget.ENEMY);
        this.damage = this.baseDamage = 6;
        this.magicNumber = this.baseMagicNumber = 4;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 6;
        this.isShroud = true;
        this.shrouded = true;
        this.storedUUID = corUUID;
        
        if (corUUID != null) {
            this.cardsToPreview = new Lacerate(upgraded);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!shrouded) {
            this.addToBot(new LacerateAction(new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
        }
        
        if (shrouded) {
            if (m != null) {
                this.addToBot(new VFXAction(new ScrapeEffect(m.hb.cX, m.hb.cY), 0.1F));
            }

            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            this.addToBot(new ApplyPowerAction(m, p ,new WeaknessPower(m, this.maniacExtraMagicNumber), this.maniacExtraMagicNumber));
        }
    }

    @Override
    public void triggerWhenDrawn() {
        if (shrouded) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    this.addToBot(new VFXAction(new ScrapeEffect(m.hb.cX, m.hb.cY), 0.1F));
                    this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, 
                            new BleedingPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
                }
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            if (shrouded) {
                this.upgradeDamage(1);
                this.upgradeMagicNumber(2);
                this.upgradeManiacExtraMagicNumber(1);
            }
            if (!shrouded) {
                this.cardsToPreview = new Lacerate(null, true);
                this.upgradeDamage(2);
            }
            initializeDescription();
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public AbstractCard makeCopy() {
        if (shrouded) return new Lacerate(null, false);
        return new Lacerate();
    }

    @Override
    public AbstractCard makeShroudCopy() {
        AbstractCard card = new Lacerate(this.uuid, this.upgraded);

        if (upgraded) card.upgrade();

        return card;
    }
}
