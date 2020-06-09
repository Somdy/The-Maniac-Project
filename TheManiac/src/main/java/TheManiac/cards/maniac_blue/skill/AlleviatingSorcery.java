package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

import java.util.ArrayList;
import java.util.List;

public class AlleviatingSorcery extends AbstractManiacCard {
    public static final String ID = "maniac:AlleviatingSorcery";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/alleviating_sorcery.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;
    private static final int LIFE_COST = 5;
    private List<TooltipInfo> tips;

    public AlleviatingSorcery() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = LIFE_COST;
        this.exhaust = true;
        this.isUnreal = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OfferingEffect(), 0.05f));
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(p));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new HealAction(p, p, 4, 0.05F));
            } else {
                this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 2), 2));
            }
        }
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 2)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
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
        return new AlleviatingSorcery();
    }
}
