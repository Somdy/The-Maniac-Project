package TheManiac.cards.maniac_blue.attack;

import TheManiac.TheManiac;
import TheManiac.actions.ExterminatorAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class Exterminator extends AbstractManiacCard {
    public static final String ID = "maniac:Exterminator";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/exterminator.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 2;
    private static final int baseDmg = 8;
    private static final int upgrade_Dmg = 2;
    private List<TooltipInfo> tips;
    
    public Exterminator() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = baseDmg;
        this.magicNumber = this.baseMagicNumber = 1;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[4], EXTENDED_DESCRIPTION[5]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.updateCost(-1);
        }
        AbstractDungeon.actionManager.addToBottom(new ExterminatorAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.rawDescription = DESCRIPTION;
        initializeDescription();
        
        if (enchanted) {
            if (this.enchantment == 1) {
                if (m.getPower(StrengthPower.POWER_ID) != null) {
                    if (m.getPower(ArtifactPower.POWER_ID) != null) {
                        this.addToBot(new ReducePowerAction(m, p, ArtifactPower.POWER_ID, 1));
                    } else {
                        int amount = m.getPower(StrengthPower.POWER_ID).amount;
                        this.addToBot(new RemoveSpecificPowerAction(m, p, StrengthPower.POWER_ID));
                        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, amount), amount));
                    }
                }
            } else {
                this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.enchantNumber), -this.enchantNumber));
            }
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
    public void applyPowers() {
        super.applyPowers();
        
        int count = 0;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            count++;
        }
        
        this.rawDescription = DESCRIPTION;
        this.rawDescription += EXTENDED_DESCRIPTION[0] + count + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        if (this.cost != COST && !this.isCostModified) {
            this.cost = COST;
        }
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 2)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[3];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(0);
        } else {
            this.modifyEnchants(2);
        }
        initializeDescription();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            if (TheManiac.leisureMode) {
                this.upgradeDamage(upgrade_Dmg + 6);
            }
            else if (TheManiac.challengerMode) {
                this.upgradeDamage(upgrade_Dmg);
            }
            else {
                this.upgradeDamage(upgrade_Dmg + 2);
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Exterminator();
    }
}
