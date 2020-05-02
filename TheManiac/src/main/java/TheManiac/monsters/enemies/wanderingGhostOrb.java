package TheManiac.monsters.enemies;

import TheManiac.vfx.GhostFireEffect;
import TheManiac.vfx.GhostIgniteFireEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.GhostlyWeakFireEffect;

public class wanderingGhostOrb {
    public static final String ID = "maniac:wanderingGhostOrb";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private BobEffect effect;
    private float activateTimer;
    private float particleTimer;
    private Color color;
    public boolean isActivated;
    public boolean playedSfx;
    public boolean hidden;
    private float x;
    private float y;
    
    public wanderingGhostOrb(float x, float y, int index) {
        this.effect = new BobEffect(2F);
        this.isActivated = false;
        this.playedSfx = false;
        this.hidden = false;
        this.particleTimer = 0F;

        this.x = x * Settings.scale + MathUtils.random(-10.0F, 10.0F) * Settings.scale;
        this.y = y * Settings.scale + MathUtils.random(-10.0F, 10.0F) * Settings.scale;
        this.activateTimer = index * 0.3F;
        this.color = Color.ROYAL.cpy();
        this.color.a = 0F;
    }
    
    public void activate(float posX, float posY) {
        this.playedSfx = false;
        this.isActivated = true;
        this.hidden = false;
    }
    
    public void deactivate() {
        this.isActivated = false;
    }
    
    public void hide() {
        this.hidden = true;
    }
    
    public void update(float posX, float posY) {
        if (!this.hidden) {
            if (this.isActivated) {
                this.activateTimer -= Gdx.graphics.getDeltaTime();
                if (this.activateTimer < 0F) {
                    if (!this.playedSfx) {
                        this.playedSfx = true;
                        AbstractDungeon.effectsQueue.add(new GhostIgniteFireEffect(this.x + posX, this.y + posY));

                        if (MathUtils.randomBoolean()) {
                            CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.3F);
                        } else {
                            CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.3F);
                        }
                    }

                    this.color.a = MathHelper.fadeLerpSnap(this.color.a, 1.0F);
                    this.effect.update();
                    this.effect.update();
                    this.particleTimer -= Gdx.graphics.getDeltaTime();

                    if (this.particleTimer < 0F) {
                        AbstractDungeon.effectList.add(new GhostFireEffect(this.x + posX + this.effect.y * 2F, this.y + posY + this.effect.y * 2F));
                        this.particleTimer = 0.06F;
                    }
                }
            } else {
                this.effect.update();
                this.particleTimer -= Gdx.graphics.getDeltaTime();
                if (this.particleTimer < 0.0F) {
                    AbstractDungeon.effectList.add(new GhostlyWeakFireEffect(this.x + posX + this.effect.y * 2.0F, this.y + posY + this.effect.y * 2.0F));
                    this.particleTimer = 0.06F;
                }
            }
        } else {
            this.color.a = MathHelper.fadeLerpSnap(this.color.a, 0.0F);
        }
    }
}
