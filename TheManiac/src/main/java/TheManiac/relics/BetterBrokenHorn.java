package TheManiac.relics;

import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.GremlinLeaderPower;
import TheManiac.powers.GremlinLeaderUpgradePower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static TheManiac.character.TheManiacCharacter.Enums.THE_MANIAC;

public class BetterBrokenHorn extends CustomRelic {
    public static final String ID = "maniac:BetterBrokenHorn";
    private static final String IMG_PATH = "maniacMod/images/relics/betterbrokenHorn.png";
    private static final String OUTLINE = "maniacMod/images/relics/outline/brokenHorn_outline.png";
    private final AbstractPlayer player = AbstractDungeon.player;
    private boolean NotGremlinUpgradeModified = true;
    
    public BetterBrokenHorn() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE), RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            if (this.player instanceof TheManiacCharacter) {
                if (this.player.chosenClass == THE_MANIAC) {
                    ((TheManiacCharacter) this.player).changeState("Hit");
                }
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (card.type == AbstractCard.CardType.ATTACK && card.damage > 10 && !card.purgeOnUse) {
            if (this.player instanceof TheManiacCharacter) {
                if (this.player.chosenClass == THE_MANIAC) {
                    ((TheManiacCharacter) this.player).changeState("Attack");
                }
            }
        }
    }

    @Override
    public void atBattleStart() {
        try {
            this.addToBot(new ApplyPowerAction(this.player, this.player, new GremlinLeaderUpgradePower()));
        } catch (Exception e) {
            System.out.println("Unable to apply Gremlin Upgraded Power -- catching " + e + ". Report this if you see it.");
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        try {
            if (NotGremlinUpgradeModified) {
                if (!this.player.hasPower(GremlinLeaderUpgradePower.POWER_ID)) {
                    this.addToBot(new ApplyPowerAction(this.player, this.player, new GremlinLeaderUpgradePower()));
                    NotGremlinUpgradeModified = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to apply Gremlin Upgraded Power -- catching " + e + ". Report this if you see it.");
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) {
            this.flash();
            this.counter++;
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(BrokenHorn.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i ++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(BrokenHorn.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
            try {
                if (AbstractDungeon.player.hasPower(GremlinLeaderPower.POWER_ID)) {
                    this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, GremlinLeaderPower.POWER_ID));
                }
                if (!AbstractDungeon.player.hasPower(GremlinLeaderUpgradePower.POWER_ID)) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GremlinLeaderUpgradePower()));
                }
            } catch (Exception e) {
                System.out.println("Unable to switch Gremlin Power. Report this if you see it. " + e);
            }
        }
        else {
            super.obtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(BrokenHorn.ID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BetterBrokenHorn();
    }
}
