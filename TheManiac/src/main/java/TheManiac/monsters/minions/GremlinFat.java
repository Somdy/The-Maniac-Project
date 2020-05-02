package TheManiac.monsters.minions;

import TheManiac.character.TheManiacCharacter;
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
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class GremlinFat extends AbstractFriendlyMonster {
    public static final String ID = "maniac:fatGremlin";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String GREMLIN_ATLAS = "maniacMod/images/monsters/minions/fatGremlin/skeleton.atlas";
    private static final String GREMLIN_JSON = "maniacMod/images/monsters/minions/fatGremlin/skeleton.json";
    private AbstractMonster target;
    private int blunt_Dmg = 6;
    private int smash_Dmg = 12;
    private int power_Amount = 2;
    private static final int base_Hp = 14;
    private int upgrades = 0;
    
    public GremlinFat(float rot) {
        super(NAME, ID, base_Hp, 0.0F, 0.0F, 110.0F, 220.0F, (String)null, -690F - (150F * (float)Math.cos(rot)), -6F + (float)Math.sin(rot));
        if (AbstractDungeon.ascensionLevel > 10) {
            this.setHp(base_Hp, base_Hp + 6);
        } else {
            this.setHp(base_Hp, base_Hp + 3);
        }
        this.dialogY = 60.0F * Settings.scale;
        this.loadAnimation(GREMLIN_ATLAS, GREMLIN_JSON, 1.25F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        addMoves();
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 2), 2));
        } else {
            this.addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 1), 1));
        }
    }

    public void setGremlinValues(int blunt, int smash, int power) {
        this.blunt_Dmg += blunt;
        this.smash_Dmg += smash;
        this.power_Amount += power;
    }
    
    private void addMoves() {
        this.moves.addMove(getPossibleMoves(AbstractDungeon.cardRng.random(0, 1)));
        this.moves.addMove(getPossibleMoves(AbstractDungeon.cardRng.random(0, 1)));
    }
    
    private MinionMove getPossibleMoves(int index) {
        this.blunt_Dmg += getUpgradesAmount();
        this.smash_Dmg += getUpgradesAmount();
        this.power_Amount += getUpgradesAmount();
        
        System.out.println("Gremlin Fat gets upgrades: " + getUpgradesAmount());
        System.out.println("Current blunt: " + this.blunt_Dmg + ", smash: " + this.smash_Dmg + ", power: " + this.power_Amount);
        
        MinionMove smashMove = new MinionMove(DIALOG[0], this, new Texture(getSmashDamageIntents()), MOVES[0] + this.smash_Dmg + MOVES[1], 
                () -> {
            this.target = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(true);
            DamageInfo info = new DamageInfo(this, this.smash_Dmg, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, this.target);
            this.addToBot(new DamageAction(this.target, info, true));
                });
        
        MinionMove bluntMove = new MinionMove(DIALOG[1], this, new Texture("maniacMod/images/monsters/minions/intents/attackDebuff.png"), MOVES[0] + this.blunt_Dmg + MOVES[2] + this.power_Amount + MOVES[3], 
                () ->{
            this.target = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(true);
            DamageInfo info = new DamageInfo(this, this.blunt_Dmg, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, this.target);
            this.addToBot(new DamageAction(this.target, info, true));
            this.addToBot(new ApplyPowerAction(this.target, this, new WeakPower(this, this.power_Amount, true), this.power_Amount));
                });
        
        MinionMove[] moves = {smashMove, bluntMove};
        
        return moves[index];
    }

    private String getSmashDamageIntents() {
        String DmgIntent;

        try {
            if (this.smash_Dmg < 10) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_2.png";
            }
            else if (this.smash_Dmg < 20) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_3.png";
            }
            else if (this.smash_Dmg < 30) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_4.png";
            }
            else if (this.smash_Dmg < 40) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_5.png";
            }
            else if (this.smash_Dmg < 50) {
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
            System.out.println("Unable to upgrade Gremlin Fat. Catching: ");
            e.printStackTrace();
        }

        return this.upgrades;
    }
}
