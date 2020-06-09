package TheManiac.cards.maniac_blue.attack;

import TheManiac.actions.ImpaleFleshAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import java.util.ArrayList;
import java.util.List;

public class ImpaleFlesh extends AbstractManiacCard {
    public static final String ID = "maniac:ImpaleFlesh";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/impale_flesh.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int DAMAGE = 26;
    private static final int UPGRADED_COST = 0;
    private List<TooltipInfo> tips;

    public ImpaleFlesh() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale,
                    Settings.GOLD_COLOR.cpy()), 0.05F));
        }
        AbstractDungeon.actionManager.addToBottom(new ImpaleFleshAction(p, m, this.damage, this.enchantment == 1));
        
        if (enchanted) {
            if (this.enchantment == 2) {
                int extraDmg = 2 * this.enchantNumber;
                this.addToBot(new DamageAction(m, new DamageInfo(p, extraDmg, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                this.addToBot(new ApplyPowerAction(p, p, new BleedingPower(p, this.enchantNumber)));
            }
            else if (this.enchantment == 3) {
                if (p.currentHealth < p.maxHealth / 2) {
                    this.addToBot(new HealAction(p, p, this.enchantNumber, 0.1F));
                }
            }
        }
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 3)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                case 2:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(0);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(3);
        } else {
            this.modifyEnchants(5);
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
            this.upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ImpaleFlesh();
    }
}
