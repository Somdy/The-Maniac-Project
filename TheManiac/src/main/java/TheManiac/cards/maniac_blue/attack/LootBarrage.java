package TheManiac.cards.maniac_blue.attack;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LootBarrage extends AbstractManiacCard {
    public static final String ID = "maniac:LootBarrage";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/loot_barrage.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int DMG_INC = 1;
    private static final int UPGRADE_AMT = 1;

    public LootBarrage() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = DMG_INC;
        this.isUnreal = true;
        this.isEnchanter = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int counts = 0;
        for (AbstractRelic relic : p.relics) {
            if (isInLimbo()) {
                counts += 1 + this.magicNumber;
            } else {
                counts++;
            }
        }
        this.damage += counts;
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
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
    public void applyPowers() {
        super.applyPowers();
        int counts = 0;
        int totalDmg;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (isInLimbo()) {
                counts += 1 + this.magicNumber;
            } else {
                counts++;
            }
        }
        totalDmg = this.damage + counts;
        this.rawDescription = DESCRIPTION;
        this.rawDescription += EXTENDED_DESCRIPTION[0] + totalDmg + EXTENDED_DESCRIPTION[1];
        initializeDescription();
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
            this.upgradeDamage(UPGRADE_AMT);
            this.upgradeMagicNumber(UPGRADE_AMT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LootBarrage();
    }
}
