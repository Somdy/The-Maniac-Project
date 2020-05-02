package TheManiac.cards.colorless.attack;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.vfx.BladesEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Blades extends AbstractManiacCard {
    public static final String ID = "maniac:Blades";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTEND_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/colorless/attack/blades.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 0;
    private static final int DAMAGE = 2;
    private int USES = 1;
    
    public Blades() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.USES; i++) {
            this.addToBot(new VFXAction(new BladesEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F));
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        }
    }
    
    public void updateUses(int amount) {
        if (!upgraded) {
            this.USES = 1 + amount;
        } else {
            this.USES = 2 + amount;
        }
        
        if (this.USES > 2) {
            this.rawDescription = EXTEND_DESCRIPTION[0] + this.USES + EXTEND_DESCRIPTION[1];
        }
        else if (this.USES == 2) {
            this.rawDescription = UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = DESCRIPTION;
        }
        
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.USES += 1;
            if (this.USES == 2) {
                this.rawDescription = UPGRADE_DESCRIPTION;
            }
            else if (this.USES > 2) {
                this.rawDescription = EXTEND_DESCRIPTION[0] + this.USES + EXTEND_DESCRIPTION[1];
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blades();
    }
}
