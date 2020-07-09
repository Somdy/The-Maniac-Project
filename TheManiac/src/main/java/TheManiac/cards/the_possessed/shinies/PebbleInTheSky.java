package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.actions.ExhaustAndDrawAction;
import TheManiac.powers.FragilePower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class PebbleInTheSky extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("PebbleInTheSky");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/attack/pebble_in_the_sky.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private int opt;
    
    public PebbleInTheSky() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.damage = this.baseDamage = 1;
        this.isMultiDamage = true;
        initData();
    }
    
    public void initData() {
        opt = 0;
        this.magicNumber = this.baseMagicNumber = 0;
        this.block = this.baseBlock = 0;
        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new CleaveEffect()));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE, true));
        
        switch (opt) {
            case 1:
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
                break;
            case 2:
                this.addToBot(new GainBlockAction(p, p, this.block));
                this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber));
                break;
            case 3:
                this.addToBot(new GainBlockAction(p, p, this.block));
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (!mo.isDeadOrEscaped()) 
                        this.addToBot(new ApplyPowerAction(mo, p, new FragilePower(mo, this.magicNumber), this.magicNumber));
                }
                break;
            case 4:
                this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, 2), 2));
                this.addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, 2), 2));
                break;
            case 5:
                this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
                break;
            case 6:
                this.addToBot(new ExhaustAndDrawAction(this.magicNumber));
                break;
            default:
                break;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.superFlash(Color.GOLD.cpy());
        opt = AbstractDungeon.cardRandomRng.random(1, 6);
        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[opt];
        if (opt == 1) this.magicNumber = this.baseMagicNumber = 2;
        else if (opt == 2) {
            this.block = this.baseBlock = 12;
            this.magicNumber = this.baseMagicNumber = 4;
        }
        else if (opt == 3) {
            this.block = this.baseBlock = 6;
            this.magicNumber = this.baseMagicNumber = 4;
        }
        else if (opt == 6) {
            this.magicNumber = this.baseMagicNumber = 3;
        }
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PebbleInTheSky();
    }
}