package TheManiac.monsters.possessed_enemies;

import TheManiac.TheManiac;
import TheManiac.actions.ApplyRandomDebuffAction;
import TheManiac.actions.PossessedMonsterAction.LostSoulAction;
import TheManiac.actions.PossessedMonsterAction.RiskySunglassessAction;
import TheManiac.actions.PossessedMonsterAction.ShufflePossessedCurseAction;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import TheManiac.cards.the_possessed.possessed.BookClub;
import TheManiac.cards.the_possessed.possessed.Gifts;
import TheManiac.cards.the_possessed.possessed.Leviathans;
import TheManiac.cards.the_possessed.possessed.PossessedCurse;
import TheManiac.cards.the_possessed.risks.AbstractRisksCard;
import TheManiac.cards.the_possessed.uncertainties.AbstractUncertaintiesCard;
import TheManiac.powers.FragilePower;
import TheManiac.relics.PossessedManuscripts;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractPossessedMonster extends CustomMonster {
    public static final Logger logger = LogManager.getLogger(AbstractPossessedMonster.class.getName());
    public static final String ID = TheManiac.makeID("AbstractPossessedMonster");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    public static final int prohitbitedMoves = 70;
    public AbstractPlayer player = AbstractDungeon.player;
    public PossessedClass ownClass;
    public int turnCount;
    public int baseDmg;
    public float vanishTimer = 0F;
    public DamageInfo lethal;
    public boolean activeLethal;
    public boolean possession_curse;
    public boolean isVanishing;
    
    public AbstractPossessedMonster(String name, String id, PossessedClass ownClass, int minHp, int maxHp, int baseDmg, int lethalDmg, float hb_x, float hb_y, float hb_w, float hb_h, float offsetX, float offsetY) {
        super(name, id, maxHp, hb_x, hb_y, hb_w, hb_h, null, offsetX, offsetY);
        this.ownClass = ownClass;
        this.turnCount = 0;
        this.baseDmg = baseDmg;
        this.lethal = new DamageInfo(this, lethalDmg, DamageInfo.DamageType.NORMAL);
        this.activeLethal = true;
        this.possession_curse = true;
        setBaseHp(minHp, maxHp);
        this.damage.add(0, this.lethal);
        initialDamages(this.baseDmg);
    }

    public AbstractPossessedMonster(String name, String id, PossessedClass ownClass, int minHp, int maxHp, int baseDmg, float hb_x, float hb_y, float hb_w, float hb_h, float offsetX, float offsetY) {
        super(name, id, maxHp, hb_x, hb_y, hb_w, hb_h, null, offsetX, offsetY);
        this.ownClass = ownClass;
        this.turnCount = 0;
        this.baseDmg = baseDmg;
        this.lethal = new DamageInfo(this, 0, DamageInfo.DamageType.NORMAL);
        this.activeLethal = false;
        this.possession_curse = true;
        setBaseHp(minHp, maxHp);
        this.damage.add(0, this.lethal);
        initialDamages(this.baseDmg);
    }
    
    public void setBaseHp(int minHp, int maxHp) {
        if (AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
            PossessedManuscripts relic = (PossessedManuscripts) AbstractDungeon.player.getRelic(PossessedManuscripts.ID);
            int addition = relic.getAdditionalHp(this, minHp, maxHp);
            logger.info(this.name + "获得" + addition + "点额外生命值。");
            this.setHp(minHp + addition, maxHp + addition);
            return;
        }
        
        this.setHp(minHp, maxHp);
    }
    
    public abstract void initialDamages(int baseDamage);

    @Override
    protected void getMove(int num) {
        
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 100:
                Color color = Color.BLACK;
                if (this.ownClass == PossessedClass.Glorious) {
                    color = Color.GOLD;
                }
                else if (this.ownClass == PossessedClass.Uncertain) {
                    color = Color.PURPLE;
                }
                else if (this.ownClass == PossessedClass.Risky) {
                    color = Color.RED;
                } else {
                    logger.info("ERROR：" + this.name + " 不是范围内的分类：" + this.ownClass);
                }
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new VFXAction(new WeightyImpactEffect(player.hb.cX, player.hb.cY, color)));
                this.addToBotDamage(player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE);
                break;
            case 99:
                if (this.ownClass == PossessedClass.Glorious) {
                    int times = numRisksInDeck(ManiacRisksCard.Enum.THE_SHINIES) + this.turnCount;
                    if (times < 3) 
                        times = 3;
                    if (ascLevel() > 16)
                        times++;
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new ApplyRandomDebuffAction(this, player, 2, true, times));
                }
                else if (this.ownClass == PossessedClass.Uncertain) {
                    int amounts = 4 + AbstractDungeon.actNum + this.turnCount;
                    if (ascLevel() > 17)
                        amounts++;
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBotPower(player, this, new FragilePower(player, amounts), amounts);
                    this.addToBotPower(player, this, new FrailPower(player, amounts / 2, true), amounts / 2);
                }
                else if (this.ownClass == PossessedClass.Risky) {
                    int amounts = numRisksInDeck(ManiacRisksCard.Enum.THE_RISKS) + this.turnCount;
                    if (ascLevel() > 18)
                        amounts += 2;
                    int restoreAmt = amounts / 2;
                    if (ascLevel() > 18 && restoreAmt > 2)
                        restoreAmt--;
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBotPower(player, this, new StrengthPower(player, -amounts), -amounts);
                    this.addToBotPower(player, this, new GainStrengthPower(player, restoreAmt), restoreAmt);
                } else {
                    logger.info("ERROR：" + this.name + " 不是范围内的分类：" + this.ownClass);
                }
                break;
            case 98:
                if (this.ownClass == PossessedClass.Glorious) {
                    this.addToBot(new ShoutAction(this, DIALOG[0], 1F, 1F));
                    this.addToBot(new LostSoulAction(this, false));
                }
                else if (this.ownClass == PossessedClass.Uncertain) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new ApplyRandomDebuffAction(this, player, playerBuffs(), true, 1));
                }
                else if (this.ownClass == PossessedClass.Risky) {
                    this.addToBot(new ShoutAction(this, DIALOG[2], 1F, 1F));
                    this.addToBot(new LostSoulAction(this, ascLevel() >= 18));
                } else {
                    logger.info("ERROR：" + this.name + " 不是范围内的分类：" + this.ownClass);
                }
                break;
            case 97:
                if (this.ownClass == PossessedClass.Glorious) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBotPower(player, this, new DexterityPower(player, -fireFlies()), -fireFlies());
                }
                else if (this.ownClass == PossessedClass.Uncertain) {
                    this.addToBot(new ShoutAction(this, DIALOG[1], 1F, 1F));
                    this.addToBot(new LostSoulAction(this, ascLevel() >= 19));
                }
                else if (this.ownClass == PossessedClass.Risky) {
                    this.addToBot(new RiskySunglassessAction(99, 4 + this.turnCount));
                } else {
                    logger.info("ERROR：" + this.name + " 不是范围内的分类：" + this.ownClass);
                }
                break;
            case 96:
                if (this.ownClass == PossessedClass.Glorious) {
                    int maxOpt = ascLevel() > 17 ? 4 : 3;
                    int baseDmg = ascLevel() > 17 ? 3 : 2;
                    int baseMagics = ascLevel() > 17 ? 2 : 1;
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new ShufflePossessedCurseAction(this, maxOpt, baseDmg, baseMagics));
                }
                else if (this.ownClass == PossessedClass.Uncertain) {
                    
                }
                else if (this.ownClass == PossessedClass.Risky) {
                    int armors = ascLevel() > 19 ? 8 : 6;
                    armors += this.turnCount;
                    this.addToBotPower(this, this, new MetallicizePower(this, armors), armors);
                    this.addToBotPower(this, this, new IntangiblePower(this, 1), 1);
                } else {
                    logger.info("ERROR：" + this.name + " 不是范围内的分类：" + this.ownClass);
                }
                break;
            case 95:
                if (this.ownClass == PossessedClass.Glorious) {
                    
                }
                else if (this.ownClass == PossessedClass.Uncertain) {
                    int maxOpt = ascLevel() > 18 ? 5 : 4;
                    int baseDmg = ascLevel() > 18 ? 4 : 3;
                    int baseMagics = ascLevel() > 18 ? 4 : 3;
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new ShufflePossessedCurseAction(this, maxOpt, baseDmg, baseMagics));
                }
                else if (this.ownClass == PossessedClass.Risky) {
                    int maxOpt = ascLevel() > 19 ? 6 : 5;
                    int baseDmg = ascLevel() > 19 ? 6 : 5;
                    int baseMagics = ascLevel() > 19 ? 5 : 4;
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new ShufflePossessedCurseAction(this, maxOpt, baseDmg, baseMagics));
                }
        }
        this.turnCount++;
    }

    public boolean hasPossibleMoveSets(AbstractPossessedMonster m, int chance) {
        switch (m.ownClass) {
            case Possessor:
                return false;
            case Glorious:
                return getGlorySetMoves(AbstractDungeon.aiRng.random(99 + chance));
            case Uncertain:
                return getUncertainSetMoves(AbstractDungeon.aiRng.random(99 + chance));
            case Risky:
                return getRiskSetMoves(AbstractDungeon.aiRng.random(99 + chance));
        }
        return false;
    }
    
    private boolean getGlorySetMoves(int chance) {
        if (chance < 32) {
            if (player.currentHealth < player.maxHealth * 0.15F && !this.lastTwoMovesContain((byte)100) && this.activeLethal) {
                this.setMove(MOVES[0], (byte)100, Intent.ATTACK, this.damage.get(0).base);
                return true;
            }
            else if (playerBuffs() > 8 && !this.lastTwoMovesContain((byte)99)) {
                this.setMove((byte)99, Intent.DEBUFF);
                return true;
            }
        }
        else if (chance < 43) {
            if (lostSouls() > 0 && !this.lastTwoMovesContain((byte)98)) {
                this.setMove((byte)98, Intent.STRONG_DEBUFF);
                return true;
            }
            else if (fireFlies() > 3 && !this.lastTwoMoves((byte)97)) {
                this.setMove((byte)97, Intent.STRONG_DEBUFF);
                return true;
            }
        }
        else if (chance < 54) {
            if (hasAnyPossessions() && possession_curse) {
                if (ascLevel() >= 16 && !this.lastMoveBefore((byte)96)) {
                    this.setMove((byte)96, Intent.STRONG_DEBUFF);
                    return true;
                }
                else if (player.masterDeck != null && player.masterDeck.findCardById(PossessedCurse.ID) == null) {
                    this.setMove((byte)96, Intent.STRONG_DEBUFF);
                    return true;
                }
            }
            else if (hasAnyPossessions() && !possession_curse && !this.lastMove((byte)95)) {
                if (!this.lastMoveBefore((byte)95)) {
                    this.setMove((byte)95, Intent.DEBUFF);
                    return true;
                }
            }
        }
        
        logger.info(this.name + "没有可行的公用ai");
        return false;
    }

    private boolean getUncertainSetMoves(int chance) {
        if (chance < 34) {
            if (player.currentHealth < player.maxHealth * 0.25F && !this.lastTwoMovesContain((byte)100) && this.activeLethal) {
                this.setMove(MOVES[1], (byte)100, Intent.ATTACK, this.damage.get(0).base);
                return true;
            }
            else if (player.currentBlock > 0 && !this.lastTwoMovesContain((byte)99)) {
                this.setMove((byte)99, Intent.DEBUFF);
                return true;
            }
            else if (playerBuffs() > 6 && !this.lastTwoMovesContain((byte)98)) {
                this.setMove((byte)98, Intent.DEBUFF);
                return true;
            }
        }
        else if (chance < 45) {
            if (lostSouls() > 0 && !this.lastTwoMovesContain((byte)97)) {
                this.setMove((byte)97, Intent.STRONG_DEBUFF);
                return true;
            }
            else if (lastCardPlayedThisCombat() instanceof AbstractUncertaintiesCard && !this.lastMoveBefore((byte)96)) {
                this.setMove((byte)96, Intent.UNKNOWN);
                return true;
            }
        }
        else if (chance < 56) {
            if (hasAnyPossessions() && possession_curse) {
                if (ascLevel() >= 17 && !this.lastMoveBefore((byte)95)) {
                    this.setMove((byte)95, Intent.STRONG_DEBUFF);
                    return true;
                }
                else if (player.masterDeck != null && player.masterDeck.findCardById(PossessedCurse.ID) == null) {
                    this.setMove((byte)95, Intent.STRONG_DEBUFF);
                    return true;
                }
            }
        }

        logger.info(this.name + "没有可行的公用ai");
        return false;
    }

    private boolean getRiskSetMoves(int chance) {
        if (chance < 35) {
            if (player.currentHealth < player.maxHealth * 0.35F && !this.lastTwoMovesContain((byte)100) && this.activeLethal) {
                this.setMove(MOVES[2], (byte)100, Intent.ATTACK, this.damage.get(0).base);
                return true;
            }
            else if (numRisksInDeck(ManiacRisksCard.Enum.THE_RISKS) > 3 && !this.lastTwoMovesContain((byte)99)) {
                this.setMove((byte)99, Intent.STRONG_DEBUFF);
                return true;
            }
        }
        else if (chance < 46) {
            if (lostSouls() > 0 && !this.lastTwoMovesContain((byte)98)) {
                this.setMove((byte)98, Intent.STRONG_DEBUFF);
                return true;
            }
            else if (sunglassesOn() && !this.lastMoveBefore((byte)97)) {
                this.setMove((byte)97, Intent.UNKNOWN);
                return true;
            }
            else if (lastCardPlayedThisCombat() != null && !this.lastTwoMovesContain((byte)96)) {
                if (lastCardPlayedThisCombat().type == AbstractCard.CardType.ATTACK && lastCardPlayedThisCombat().baseDamage > this.maxHealth * 0.15F) {
                    this.setMove((byte)96, Intent.BUFF);
                    return true;
                }
            }
        }
        else if (chance < 57) {
            if (hasAnyPossessions() && possession_curse) {
                if (ascLevel() >= 18 && !this.lastMoveBefore((byte)95)) {
                    this.setMove((byte)95, Intent.STRONG_DEBUFF);
                    return true;
                }
                else if (player.masterDeck != null && player.masterDeck.findCardById(PossessedCurse.ID) == null) {
                    this.setMove((byte)95, Intent.STRONG_DEBUFF);
                    return true;
                }
            }
        }

        logger.info(this.name + "没有可行的公用ai");
        return false;
    }
    
    private boolean hasAnyPossessions() {
        if (player.masterDeck != null) {
            return player.masterDeck.findCardById(BookClub.ID) != null || player.masterDeck.findCardById(Gifts.ID) != null
                    || player.masterDeck.findCardById(Leviathans.ID) != null;
        }
        return false;
    }
    
    private int playerBuffs() {
        int num = 0;
        if (!player.powers.isEmpty()) {
            for (AbstractPower power : player.powers) {
                if (power.type == AbstractPower.PowerType.BUFF) {
                    num++;
                }
            }
        }
        return num;
    }
    
    private boolean sunglassesOn() {
        if (player.hasRelic(PossessedManuscripts.ID)) {
            PossessedManuscripts relic = (PossessedManuscripts) player.getRelic(PossessedManuscripts.ID);
            return relic.activeEffects.get(1);
        }
        
        return false;
    }
    
    private int lostSouls() {
        if (player.hasRelic(PossessedManuscripts.ID)) {
            PossessedManuscripts relic = (PossessedManuscripts) player.getRelic(PossessedManuscripts.ID);
            if (relic.activeEffects.get(3)) {
                return relic.activeAmounts.get(3).intValue();
            }
        }
        
        return 0;
    }

    private int fireFlies() {
        if (player.hasRelic(PossessedManuscripts.ID)) {
            PossessedManuscripts relic = (PossessedManuscripts) player.getRelic(PossessedManuscripts.ID);
            if (relic.activeEffects.get(0)) {
                return relic.activeAmounts.get(0).intValue();
            }
        }

        return 0;
    }
    
    protected int numRisksInDeck(AbstractCard.CardRarity rarity) {
        switch (rarity) {
            case BASIC:
            case COMMON:
            case UNCOMMON:
            case SPECIAL:
            case RARE:
            case CURSE:
                return 0;
        }
        int num = 0;
        if (player.masterDeck != null) {
            for (AbstractCard card : player.masterDeck.group) {
                if (card.rarity == rarity) {
                    num++;
                }
            }
        }
        
        return num;
    }
    
    protected AbstractCard lastCardPlayedThisCombat() {
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            return null;
        }
        
        return AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);
    }
    
    public int ascLevel() {
        return AbstractDungeon.ascensionLevel;
    }

    @Override
    public void update() {
        super.update();
        updateVanishAnimation(this.isDying);
    }

    protected void updateVanishAnimation(boolean goDeath) {
        if (isVanishing) {
            vanishTimer -= Gdx.graphics.getDeltaTime();
            if (vanishTimer < 1.8F && !this.tintFadeOutCalled) {
                this.tintFadeOutCalled = true;
                this.tint.fadeOut();
            }
        }
        
        if (vanishTimer < 0F) {
            if (goDeath)
                this.isDead = true;
            else this.escaped = true;
            if (AbstractDungeon.getMonsters().areMonstersDead() && !(AbstractDungeon.getCurrRoom()).isBattleOver &&
                    !(AbstractDungeon.getCurrRoom()).cannotLose) {
                AbstractDungeon.getCurrRoom().endBattle();
            }
            this.dispose();
            this.powers.clear();
        }
    }
    
    public void vanish(boolean goDeath) {
        this.hideHealthBar();
        this.isVanishing = true;
        if (goDeath && !this.isDying)
            this.isDying = true;
        else this.isEscaping = true;
        this.vanishTimer = 3.5F;
    }
    
    public boolean lastTwoMovesContain(byte move) {
        if (this.moveHistory.size() < 2) {
            return false;
        }

        return this.moveHistory.get(this.moveHistory.size() - 1) == move || this.moveHistory.get(this.moveHistory.size() - 2) == move;
    }
    
    public void addToBotPower(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        this.addToBot(new ApplyPowerAction(target, source, powerToApply, stackAmount, isFast, effect));
    }

    public void addToBotPower(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast) {
        this.addToBotPower(target, source, powerToApply, stackAmount, isFast, AbstractGameAction.AttackEffect.NONE);
    }

    public void addToBotPower(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount) {
        this.addToBotPower(target, source, powerToApply, stackAmount, true, AbstractGameAction.AttackEffect.NONE);
    }
    
    public void addToBotDamage(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, boolean superFast, boolean muteSfx) {
        this.addToBot(new DamageAction(target, info, effect, superFast, muteSfx));
    }

    public void addToBotDamage(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, boolean superFast) {
        this.addToBotDamage(target, info, effect, superFast, false);
    }

    public void addToBotDamage(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        this.addToBotDamage(target, info, effect, false, false);
    }
    
    public enum PossessedClass {
        Glorious, Uncertain, Risky, Possessor
    }
}