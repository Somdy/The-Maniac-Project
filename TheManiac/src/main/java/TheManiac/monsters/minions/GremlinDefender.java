package TheManiac.monsters.minions;

import TheManiac.character.TheManiacCharacter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class GremlinDefender extends AbstractFriendlyMonster {
    public static final String ID = "maniac:femaleGremlin";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String GREMLIN_ATLAS = "maniacMod/images/monsters/minions/femaleGremlin/skeleton.atlas";
    private static final String GREMLIN_JSON = "maniacMod/images/monsters/minions/femaleGremlin/skeleton.json";
    private AbstractMonster target;
    private int block_Amount = 5;
    private int bash_Dmg = 4;
    private int power_Amount = 2;
    private static final int base_Hp = 10;
    private int upgrades = 0;
    private boolean renderChecker = false;
    
    public GremlinDefender(float rot) {
        super(NAME, ID, base_Hp, 0.0F, 0.0F, 120.0F, 200.0F, (String)null, -690F - (150F * (float)Math.cos(rot)), 6F + (float)Math.sin(rot));
        if (AbstractDungeon.ascensionLevel > 10) {
            this.setHp(base_Hp, base_Hp + 7);
        } else {
            this.setHp(base_Hp, base_Hp + 5);
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
            this.addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, 4)));
            this.addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 1)));
        } else {
            this.addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 4)));
        }
    }

    public void setGremlinValues(int additionalBlock, int Dmg, int additionalPower) {
        this.block_Amount += additionalBlock;
        this.bash_Dmg += Dmg;
        this.power_Amount += additionalPower;
    }

    private void addMoves() {
        this.moves.addMove(getPossibleMoves(AbstractDungeon.cardRng.random(0, 1)));
        this.moves.addMove(getPossibleMoves(AbstractDungeon.cardRng.random(1, 2)));
    }

    private MinionMove getPossibleMoves(int index) {
        this.block_Amount += getUpgradesAmount();
        this.bash_Dmg += getUpgradesAmount();
        this.power_Amount += getUpgradesAmount();
        
        System.out.println("Gremlin Defender gets upgrades: " + getUpgradesAmount());
        System.out.println("Current block: " + this.block_Amount + ", bash: " + this.bash_Dmg + ", power: " + this.power_Amount);
        
        MinionMove blockMove = new MinionMove(DIALOG[1], this, new Texture("maniacMod/images/monsters/minions/intents/defend.png"), MOVES[0] + this.block_Amount + MOVES[1], 
                () -> this.addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block_Amount)));
        MinionMove bashMove = new MinionMove(DIALOG[0], this, new Texture(getDamageIntents()), MOVES[2] + this.bash_Dmg + MOVES[3],
                () -> {
            this.target = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(true);
            DamageInfo info = new DamageInfo(this, this.bash_Dmg, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, this.target);
            this.addToBot(new DamageAction(this.target, info, true));
                });
        MinionMove wardMove = new MinionMove(DIALOG[2], this, new Texture("maniacMod/images/monsters/minions/intents/buff1.png"), MOVES[4] + this.power_Amount + MOVES[5],
                () -> this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new PlatedArmorPower(AbstractDungeon.player, this.power_Amount), this.power_Amount)));
        
        MinionMove[] moves = {blockMove, bashMove, wardMove};
        
        return moves[index];
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!renderChecker) {
            System.out.println("===Gremlin Defender works on render===");
            renderChecker = true;
        }
        super.render(sb);
    }

    private String getDamageIntents() {
        String DmgIntent;
        
        try {
            if (this.bash_Dmg < 10) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_2.png";
            }
            else if (this.bash_Dmg < 20) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_3.png";
            }
            else if (this.bash_Dmg < 30) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_4.png";
            }
            else if (this.bash_Dmg < 40) {
                DmgIntent = "maniacMod/images/monsters/minions/intents/attacks/attack_intent_5.png";
            }
            else if (this.bash_Dmg < 50) {
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
