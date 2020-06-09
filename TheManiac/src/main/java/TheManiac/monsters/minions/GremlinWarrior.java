package TheManiac.monsters.minions;

import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class GremlinWarrior extends AbstractFriendlyMonster {
    public static final String ID = "maniac:angryGremlin";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String GREMLIN_ATLAS = "maniacMod/images/monsters/minions/angryGremlin/skeleton.atlas";
    private static final String GREMLIN_JSON = "maniacMod/images/monsters/minions/angryGremlin/skeleton.json";
    private AbstractMonster target;
    private int base_Dmg = 4;
    private int power_Amount = 1;
    private int bleeding_Dmg = 2;
    private int bleeding = 2;
    private static final int base_Hp = 20;
    private int upgrades = 0;
    public GremlinWarrior(float rot) {
        super(NAME, ID, base_Hp, 0.0F, 0.0F, 130.0F, 194.0F, (String)null, -690F - (150F * (float)Math.cos(rot)), 0F + (float)Math.sin(rot));
        if (AbstractDungeon.ascensionLevel > 10) {
            this.setHp(base_Hp, base_Hp + 6);
        } else {
            this.setHp(base_Hp, base_Hp + 4);
        }
        this.dialogY = 60.0F * Settings.scale;
        this.loadAnimation(GREMLIN_ATLAS, GREMLIN_JSON, 1.25F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        addMoves();
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.addToBot(new ApplyPowerAction(this, this, new AngryPower(this, 2)));
            this.increaseMaxHp(4, true);
        } else {
            this.addToBot(new ApplyPowerAction(this, this, new AngryPower(this, 1)));
        }
    }
    
    public void setGremlinValues(int baseDmg, int power, int bleeding, int bleedingDmg) {
        this.base_Dmg += baseDmg;
        this.power_Amount += power;
        this.bleeding += bleeding;
        this.bleeding_Dmg += bleedingDmg;
    }

    public void addMoves() {
        this.moves.addMove(getPossibleMoves(0));
        this.moves.addMove(getPossibleMoves(AbstractDungeon.cardRng.random(1, 2)));
    }
    
    private MinionMove getPossibleMoves(int index) {
        this.base_Dmg += getUpgradesAmount();
        this.power_Amount += getUpgradesAmount();
        this.bleeding += getUpgradesAmount();
        this.bleeding_Dmg += getUpgradesAmount();
        
        System.out.println("Gremlin Warrior gets upgrades: " + getUpgradesAmount());
        System.out.println("Current baseDmg: " + this.base_Dmg + ", enrage: " + this.power_Amount + ", bleeding: " + this.bleeding + ", bleedingDmg: " + this.bleeding_Dmg);
        
        MinionMove clawMove = new MinionMove(DIALOG[0], this, new Texture(getBaseDamageIntents()), MOVES[0] + this.base_Dmg + MOVES[1], 
                () -> {
            this.target = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(true);
            DamageInfo info = new DamageInfo(this, this.base_Dmg, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, this.target);
            this.addToBot(new DamageAction(this.target, info, true));
                });
        
        MinionMove enrageMove = new MinionMove(DIALOG[1], this, new Texture("maniacMod/images/monsters/minions/intents/buff1.png"), MOVES[2] + this.power_Amount + MOVES[3], 
                () -> this.addToBot(new ApplyPowerAction(this, this, new AngryPower(this, this.power_Amount), this.power_Amount)));
        
        MinionMove scratchMove = new MinionMove(DIALOG[2], this, new Texture(getBleedingDamageIntents()), MOVES[0] + this.bleeding_Dmg + MOVES[4] + this.bleeding + MOVES[5], 
                () -> {
            this.target = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(true);
            DamageInfo info = new DamageInfo(this, this.base_Dmg, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, this.target);
            this.addToBot(new DamageAction(this.target, info, true));
            this.addToBot(new ApplyPowerAction(this.target, this, new BleedingPower(this.target, this.bleeding), this.bleeding));
                });
        
        MinionMove[] moves = {enrageMove, clawMove, scratchMove};
        
        return moves[index];
    }

    private String getBaseDamageIntents() {
        String DmgIntent;

        try {
            if (this.base_Dmg < 10) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_2.png";
            }
            else if (this.base_Dmg < 20) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_3.png";
            }
            else if (this.base_Dmg < 30) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_4.png";
            }
            else if (this.base_Dmg < 40) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_5.png";
            }
            else if (this.base_Dmg < 50) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_6.png";
            }
            else {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_7.png";
            }
        } catch (Exception e) {
            DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_2.png";
            System.out.println("Unable to get correct Intent Img. Catching: ");
            e.printStackTrace();
        }

        return DmgIntent;
    }

    private String getBleedingDamageIntents() {
        String DmgIntent;

        try {
            if (this.bleeding_Dmg < 10) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_2.png";
            }
            else if (this.bleeding_Dmg < 20) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_3.png";
            }
            else if (this.bleeding_Dmg < 30) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_4.png";
            }
            else if (this.bleeding_Dmg < 40) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_5.png";
            }
            else if (this.bleeding_Dmg < 50) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_6.png";
            }
            else {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_7.png";
            }
        } catch (Exception e) {
            DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_2.png";
            System.out.println("Unable to get correct Intent Img. Catching: ");
            e.printStackTrace();
        }

        return DmgIntent;
    }

    private int getUpgradesAmount() {
        try {
            if (AbstractDungeon.player instanceof TheManiacCharacter) {
                if (((TheManiacCharacter) AbstractDungeon.player).minionsUpgrades > 0) {
                    this.upgrades += ((TheManiacCharacter) AbstractDungeon.player).minionsUpgrades;
                }

                if (AbstractDungeon.ascensionLevel > 15) {
                    this.upgrades += 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to upgrade Gremlin Defender. Catching: ");
            e.printStackTrace();
        }

        return this.upgrades;
    }
}
