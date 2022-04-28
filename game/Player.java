package game;

import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import game.physics.Vector;
import game.screen.BattleScreen;
import game.character.Character;
import game.item.Item;
import game.particle.*;
import game.ai.AI;
import game.physics.Ledge;
import java.util.ArrayList;
import game.physics.AABB;
import game.projectile.*;
import modloader.ModPlayerOverride;

public class Player extends GameObject {
   public static final int UP = 0;
   public static final int DOWN = 1;
   public static final int LEFT = 2;
   public static final int RIGHT = 3;
   public static final int JUMP = 4;
   public static final int ATTACK = 5;
   public static final int PARRY = 6;

   public ArrayList<ModPlayerOverride> modPlayers;
   public boolean touchingStage;
   public boolean charging;
   public boolean render;
   public Character selectedChar;
   public AABB hitbox;
   public int width;
   public int height;
   public int direction;
   public int frozen;
   public int playerId;
   public int frame;
   public int walkTimer;
   public int respawnTimer;
   public Vector velocity = new Vector(0, 0);
   public boolean canAirJump;
   public boolean falling = false;
   public boolean tookDamage = false;
   public boolean justDied = false;
   public boolean grabbingLedge;
   public int ledgeGrabs;
   public int jumps;
   public boolean invincible;
   public int grabbedLedgeFor;
   public int damage;
   public int endLag;
   public int charge;
   public int lives;
   public Vector knockBack;
   public BattleScreen battleScreen;
   public Ledge grabbedEdge;
   public int noPassablePlatformsFor;
   public int costume;
   public int parryTimer;
   public int stunned;
   public int stuck;
   public boolean finalAss;
   public AI ai;
   public Player lastHitBy;
   public boolean dontLateUpdatePos;
   public int iFrames;

   public Item heldItem;

   public Player(int playerId, Character character, BattleScreen battleScreen, int lives, int costume, AI ai) {
      constructorBase(playerId, character, battleScreen, lives, costume);
      this.ai = ai;
   }

   public Player(int playerId, Character character, BattleScreen battleScreen, int lives, int costume) {
      constructorBase(playerId, character, battleScreen, lives, costume);
   }

   protected void constructorBase(int playerId, Character character, BattleScreen battleScreen, int lives,
         int costume) {
      ai = new AI();
      iFrames = 0;
      dontLateUpdatePos = false;
      frameTimer = 0;
      lastHitBy = null;
      stunned = 0;
      stuck = 0;
      heldItem = null;
      modPlayers = new ArrayList<>();
      knockBack = new Vector(0, 0);
      this.playerId = playerId;
      walkTimer = 0;
      direction = 1;
      touchingStage = false;
      pos.x = 0;
      pos.y = 0;
      grabbedLedgeFor = 0;
      this.costume = costume;
      endLag = 0;
      velocity.x = 0;
      velocity.y = 0;
      render = true;
      this.lives = lives;
      this.battleScreen = battleScreen;
      damage = 0;
      frame = 2;
      noPassablePlatformsFor = 0;
      grabbingLedge = false;
      ledgeGrabs = 5;
      selectedChar = character;
      width = selectedChar.width;
      height = selectedChar.height;
      jumps = selectedChar.jumps;
      selectedChar.id = playerId;
      grabbedEdge = null;
      hitbox = new AABB(pos.x, pos.y, selectedChar.hitboxWidth, selectedChar.hitboxHeight);
      selectedChar.uniqueUpdateOnGameStart(this);
   }

   public boolean[] readableKeys = {
         false, // Up
         false, // Down
         false, // Left
         false, // Right
         false, // Jump
         false, // Attack
         false // Parry
   };
   public int[] readableKeysJustPressed = {
         0, // Up
         0, // Down
         0, // Left
         0, // Right
         0, // Jump
         0, // Attack
         0 // Parry
   };
   public int frameTimer;
   public int knockBackSmokeTimer = 0;

   public void knockBack(Vector step) {
      if (step.len() > 2) {
         if (knockBackSmokeTimer >= 48) {
            battleScreen.addParticle(new Smoke(hitbox.x + width / 2 - 16, hitbox.y + height / 2 - 16, 0, 0, "p" + (playerId + 1) + "_smoke.png"));
            knockBackSmokeTimer = 0;
         }
         knockBackSmokeTimer++;
         Vector tempStep = Vector.normalize(knockBack);
         checkDeath();
         if (justDied)
            return;
         move(tempStep, true);
         knockBack(Vector.sub(step, tempStep));
      }
   }
   
   public void step(Vector step) {
      if (step.len() >= 1) {
         Vector tempStep = Vector.normalize(velocity);
         checkDeath();
         if (justDied)
            return;
         move(tempStep, true);
         step(Vector.sub(step, tempStep));
      }
   }

   public void update() {
      if (iFrames > 0) {
         invincible = true;
         iFrames--;
      }
      dontLateUpdatePos = false;
      hitbox = selectedChar.getHitbox(this);
      selectedChar.uniqueUpdatePreEverything(this);
      pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
      if (heldItem != null) {
         heldItem.preUpdate();
      }
      justDied = false;
      for (ModPlayerOverride modPlayer : modPlayers) {
         modPlayer.update(this);
      }
      progressKeys();
      if (stunned > 0) {
         stunned--;
         frame = 7;
         if (knockBack.len() > 2 && battleScreen.getGameTick() % 48 == 0) {
            battleScreen.addParticle(new Smoke(hitbox.x + width / 2 - 16, hitbox.y + height / 2 - 16, 0, 0, "p" + (playerId + 1) + "_smoke.png"));
         }
         pos = Vector.sub(new Vector(hitbox.x, hitbox.y), selectedChar.offset);
         pos.add(new Vector(16 * (Math.random() - 0.5), 16 * (Math.random() - 0.5)));
         dontLateUpdatePos = true;
         return; // don't let stunned players move
      }

      if (stuck > 0) {
         stuck--;
         frame = 0;
         pos = Vector.sub(new Vector(hitbox.x, hitbox.y), selectedChar.offset);
         return; // don't let stuck players move
      }

      if (charging && charge < 1) {
         charge = 1;
      }

      if (--respawnTimer > 0) {
         iFrames = 80;
         frame = 0;
         if (hitbox.y + hitbox.height < battleScreen.getStage().getSpawnOffset(playerId).y + 96)
            hitbox.y += 8;
         else
            for (int i = 0; i < 5; i++) {
               if (readableKeysJustPressed[i] == 1) {
                  SoundEngine.playSound("respawn");
                  velocity = velocity.mul(0);
                  knockBack = knockBack.mul(0);
                  respawnTimer = 0;
               }
            }
         jumps = selectedChar.jumps;
         pos = Vector.sub(new Vector(hitbox.x, hitbox.y), selectedChar.offset);
         invincible = true;
         grabbedEdge = null;
         if (respawnTimer > 0)
            return; // dont let the player do anything until they leave invincibility
      }
      if (frozen == 1) {
         grabbedEdge = null;
         knockBack.x = 0;
         knockBack.y = 0;
         frozen = 0;
         for (int i = 0; i < 6; i++) {
            battleScreen
                  .addParticle(new Particle(hitbox.x + width / 2 - 20, hitbox.y + height / 2 - 20,
                        (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 4, 5, 5, "ice_shard.png"));
         }
      }
      if (frozen > 0) {
         grabbedEdge = null;
         checkDeath();
         frozen--;
         charging = false;
         charge = 0;
         knockBack(new Vector(knockBack.x / 8, knockBack.y / 8));
         knockBack.y += 2;
         velocity.y = 0;
         velocity.x = 0;
         if (touchingStage) {
            knockBack.y = 0;
            knockBack.x *= 0.4;
         }
         for (int i = 0; i < 5; i++) {
            if (readableKeysJustPressed[i] == 1) {
               frozen -= 8;
               if (frozen < 1)
                  frozen = 1;
            }
         }
         pos = Vector.sub(new Vector(hitbox.x, hitbox.y), selectedChar.offset);
         return; // dont let frozen players move
      }
      if (endLag > 0 && !selectedChar.overrideAttackAnimation) {
         grabbedEdge = null;
         if (++frameTimer >= 6) {
            if (++frame >= 6) {
               frame = 4;
            }
            frameTimer = 0;
         }
      } else {
         frameTimer = 0;
      }
      checkDeath();
      airResistance();
      knockBack(knockBack);
      knockBack.x *= ((100 - selectedChar.weight) / 100) / 4 + 0.5;
      knockBack.y *= ((100 - selectedChar.weight) / 100) / 4 + 0.5;
      if (knockBack.len() > 2) {
         knockBack.y += 1;
         grabbedEdge = null;
         frame = 7;
         pos = Vector.sub(new Vector(hitbox.x, hitbox.y), selectedChar.offset);
         if (touchingStage) {
            knockBack.y *= -1;
         }
         return; // dont let knockbacked players move
      }
      grabbingLedge = false;
      checkGrabEdge();
      if (grabbedEdge != null) {
         jumps = selectedChar.jumps;
         grabbedLedgeFor++;
         if (grabbedLedgeFor == 1)
            ledgeGrabs--;
         frame = 8;
         falling = false;

         Vector grabPosition = grabbedEdge.getPos();
         hitbox.setPosition(grabPosition);

         direction = grabbedEdge.direction;

         if (direction == 1) {
            hitbox.x = hitbox.x - hitbox.width + grabbedEdge.hitbox.width - 4;
         } else {
            hitbox.x += 4;
         }
         velocity = velocity.mul(0);
         if (readableKeysJustPressed[UP] == 1 || readableKeysJustPressed[JUMP] == 1) {
            velocity.y = -selectedChar.jumpHeight;
            hitbox.y -= height + 12;
         }
         if (readableKeysJustPressed[ATTACK] == 1) {
            ledgeGrabs--;
            velocity.y = -selectedChar.jumpHeight / 2;
            hitbox.y -= height + 12;
            selectedChar.neutralAttack(this);
         }
         if (readableKeysJustPressed[DOWN] == 1 || grabbedLedgeFor > 120) {
            velocity.y = selectedChar.jumpHeight / 2;
            hitbox.y += height + 12;
         }
         pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
         return; // dont let ledge-grabbing players move
      } else {
         grabbedLedgeFor = 0;
      }
      if (readableKeys[DOWN]) {
         noPassablePlatformsFor = 1;
      } else {
         noPassablePlatformsFor = 0;
      }
      if (parryTimer > -10) {
         parryTimer--;
      }
      if (!touchingStage) {
         if (endLag > 0) {
            endLag--;
            velocity.y += selectedChar.weight / 20;
            step(velocity);
            pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
            return; // dont let endlagged players move
         }
         if (readableKeysJustPressed[4] == 1 && jumps > 0 && !falling && !charging) {
            velocity.y = -((double) selectedChar.jumpHeight * selectedChar.doubleJumpDropoff);
            canAirJump = false;
            step(velocity);
            SoundEngine.playSound("double_jump");
            jumps--;
         }
      } else {
         jumps = selectedChar.jumps;
         grabbedLedgeFor = 0;
         ledgeGrabs = 5;
         falling = false;
         friction(false);
         velocity.y = 0;
         if (endLag > 0) {
            velocity.y += selectedChar.weight / 20;
            endLag--;
            pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
            return; // dont let endlagged players move
         }
         if (readableKeysJustPressed[4] == 1 && !charging) {
            velocity.y = -selectedChar.jumpHeight;
            SoundEngine.playSound("jump");
            canAirJump = false;
         }
      }
      velocity.y += selectedChar.weight / 20;
      if (readableKeysJustPressed[5] == 0 && charging && charge > 10) {
         selectedChar.chargeAttack(this, charge);
         charging = false;
      }
      if (charging && readableKeysJustPressed[5] > 0 || charge > 0 && charge < 11) {
         frame = 5;
         charge++;
         step(new Vector(velocity.x / 2, velocity.y / 2));
         if (charge % 4 == 0)
            battleScreen.addParticle(
                  new Particle(hitbox.x + width / 2 - 8, hitbox.y + width / 2 - 8, (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 2, 4, 4, selectedChar.chargingParticle));
         pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
         return; // dont let charging players move
      }
      if (readableKeysJustPressed[PARRY] == 1 && parryTimer == -10) {
         parryTimer = 3;
      }
      selectedChar.uniqueUpdatePostFreezeCases(this);
      if (readableKeys[LEFT]) {
         if (!readableKeys[RIGHT])
            direction = -1;
         velocity.x -= selectedChar.speed;
         if (++walkTimer >= selectedChar.walkFrameTimer && !selectedChar.overrideWalkAnimation) {
            if (++frame >= 4) {
               frame = 0;
            }
            walkTimer = 0;
         }
      }
      if (readableKeys[RIGHT]) {
         if (!readableKeys[LEFT])
            direction = 1;
         velocity.x += selectedChar.speed;
         if (++walkTimer >= selectedChar.walkFrameTimer && !selectedChar.overrideWalkAnimation) {
            if (++frame >= 4) {
               frame = 0;
            }
            walkTimer = 0;
         }
      }
      if (!readableKeys[LEFT] && !readableKeys[RIGHT] || readableKeys[LEFT] && readableKeys[RIGHT]) {
         friction(true);
         if (!selectedChar.overrideWalkAnimation)
            frame = 0;
      }
      if (falling) {
         step(velocity);
         pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
         return;
      }
      if (readableKeysJustPressed[5] == 1) {
         if (readableKeys[DOWN]) {
            selectedChar.downAttack(this);
         } else if (readableKeys[UP]) {
            selectedChar.upAttack(this);
         } else if (readableKeys[LEFT] || readableKeys[RIGHT]) {
            if (heldItem == null || (heldItem != null && !heldItem.hasUseAction()))
               selectedChar.sideAttack(this);
            else
               heldItem.onUse();
         } else {
            if (finalAss) {
               selectedChar.finalAss(this);
               finalAss = false;
            } else if (heldItem == null || (heldItem != null && !heldItem.hasUseAction()))
               selectedChar.neutralAttack(this);
            else
               heldItem.onUse();
         }
      }
      if (parryTimer > 0) {
         frame = 5;
         invincible = true;
         step(new Vector(velocity.x / 2, velocity.y / 2));
         pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
         return; // dont let parrying players move
      } else {
         invincible = false;
      }
      step(velocity);
      invincible = false;
      selectedChar.uniqueUpdatePostEverything(this);
      tookDamage = false;
      pos = Vector.sub(hitbox.getPosition(), selectedChar.offset);
   }

   public void uniqueAnimations() {
      selectedChar.uniqueAnimations(this);
   }

   public void progressKeys() {
      if (!battleScreen.getGameEnded())
         ai.preUpdate(this);
      if (readableKeys[UP]) {
         readableKeysJustPressed[0]++;
      }
      if (readableKeys[DOWN]) {
         readableKeysJustPressed[1]++;
      }
      if (readableKeys[LEFT]) {
         readableKeysJustPressed[2]++;
      }
      if (readableKeys[RIGHT]) {
         readableKeysJustPressed[3]++;
      }
      if (readableKeys[JUMP]) {
         readableKeysJustPressed[4]++;
      }
      if (readableKeys[ATTACK]) {
         readableKeysJustPressed[5]++;
      }
      if (readableKeys[PARRY]) {
         readableKeysJustPressed[6]++;
      }
   }

   void checkDeath() {
      if (knockBack.len() > 2 && !battleScreen.getStage().getSafeBlastZone().overlaps(hitbox)) {
         kill();
      }
      if (!battleScreen.getStage().getUnsafeBlastZone().overlaps(hitbox)
            && !(hitbox.y < battleScreen.getStage().getUnsafeBlastZone().y)) {
         kill();
      }

   }

   void checkGrabEdge() {
      if (ledgeGrabs > 0 && knockBack.len() < 0.01) {
         grabbedEdge = battleScreen.getStage().isGrabbing(hitbox);
         if (grabbedEdge != null) {
            grabbingLedge = true;
         }
      } else {
         grabbedEdge = null;
      }
   }

   public void kill() {
      battleScreen.cameraShake(5);
      finalAss = false;
      velocity.x = 0;
      velocity.y = 0;
      knockBack.x = 0;
      knockBack.y = 0;
      charging = false;
      charge = 0;
      lives--;
      heldItem = null;
      justDied = true;
      for (int i = 0; i < 24; i++) {
         Vector twinkleVel = new Vector(25 * Math.random(), 0).rotateBy((Math.random() - 0.5) / 8).rotateBy((Vector
               .sub(new Vector(1152 / 2, 704 / 2), new Vector(center().x, center().y + ((Math.random() - 0.5) / 8)))
               .rotationOf()));
         battleScreen.addParticle(
               new Particle(hitbox.x, hitbox.y, twinkleVel.x, twinkleVel.y, 4, 4, 4, "twinkle.png"));
      }
      for (Projectile projectile : battleScreen.getProjectiles()) {
         if (projectile.owner == playerId)
            projectile.alive = false;
      }
      if (lives > 0) {
         SoundEngine.playSound("death");
         falling = false;
         frozen = 0;
         endLag = 0;
         damage = 0;
         if (playerId == 0) {
            hitbox.x = 556 - 128 - hitbox.width / 2 + battleScreen.getStage().getSpawnOffset(0).x;
            hitbox.y = -90 + battleScreen.getStage().getSpawnOffset(0).y;
         }
         if (playerId == 1) {
            hitbox.x = 556 + 128 - hitbox.width / 2 + battleScreen.getStage().getSpawnOffset(1).x;
            hitbox.y = -90 + battleScreen.getStage().getSpawnOffset(1).y;
         }
         respawnTimer = 240;
      } else {
         battleScreen.cameraShake(15);
         for (int i = 0; i < 10; i++) {
            Vector twinkleVel = new Vector(25 * (Math.random() / 2), 0).rotateBy((Math.random() - 0.5) / 6)
                  .rotateBy((Vector
                        .sub(new Vector(1152 / 2, 704 / 2),
                              new Vector(center().x, center().y + ((Math.random() - 0.5) / 8)))
                        .rotationOf()));
            battleScreen.addParticle(new Particle(hitbox.x + (Math.random() - 0.5) * 64,
                  hitbox.y + (Math.random() - 0.5) * 64, twinkleVel.x, twinkleVel.y,
                  3, 4, 4, "fire.png"));
         }
         for (int i = 0; i < 10; i++) {
            Vector twinkleVel = new Vector(25 * (Math.random() / 2), 0).rotateBy((Math.random() - 0.5) / 6)
                  .rotateBy((Vector
                        .sub(new Vector(1152 / 2, 704 / 2),
                              new Vector(center().x, center().y + ((Math.random() - 0.5) / 8)))
                        .rotationOf()));
            battleScreen.addParticle(new Particle(hitbox.x + (Math.random() - 0.5) * 64,
                  hitbox.y + (Math.random() - 0.5) * 64, twinkleVel.x, twinkleVel.y,
                  4, 4, 4, "smoke.png"));
         }
         hitbox.y = -100000000;
         hitbox.x = 100;
      }

   }

   public void move(Vector step, boolean updateTouchGround) {
      hitbox.y += step.y;
      touchingStage = battleScreen.getStage().collideWithPlatformsY(this, step.y);
      hitbox.x += step.x;
      battleScreen.getStage().collideWithPlatformsX(this, step.x);
   }

   public void friction(boolean force) {
      if (!readableKeys[LEFT] && !readableKeys[RIGHT] || force) {
         velocity.x *= 0.8;
      }
   }

   public void airResistance() {
      velocity.x *= selectedChar.drag;
      velocity.y *= selectedChar.drag;
   }

   public void hitPlayer(int damage, double kbStr, double kbDir, double kbDamageMult, Projectile projectile) {
      lastHitBy = projectile.ownerPlayer;
      MutableBoolean overrideDamage = new MutableBoolean(false);
      selectedChar.uniqueOnHit(this, overrideDamage, projectile.ownerPlayer, damage, kbStr);
      selectedChar.uniqueOnHit(this, overrideDamage, projectile.ownerPlayer);
      if (!overrideDamage.value) {
         if (parryTimer < 0) {
            hitPlayerByNonProjectile(damage, kbStr, kbDir, kbDamageMult);
         } else {
            parryTimer = 0;
            iFrames = 20;
            battleScreen.parryEffect();
            SoundEngine.playSound("parry");
            battleScreen
                  .addParticle(new Particle(hitbox.x + width / 2 - 20, hitbox.y + height / 2 - 20,
                        (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 4, 5, 5, "twinkle.png"));
            if (projectile.ownerPlayer != null)
               projectile.ownerPlayer.endLag += 15;
         }
      }
   }

   public void hitPlayerByItem(int damage, double kbStr, double kbDir, double kbDamageMult, Item item) {
      lastHitBy = item.holder;
      MutableBoolean overrideDamage = new MutableBoolean(false);
      selectedChar.uniqueOnHit(this, overrideDamage, item.holder, damage, kbStr);
      selectedChar.uniqueOnHit(this, overrideDamage, item.holder);
      if (!overrideDamage.value) {
         if (parryTimer < 0) {
            hitPlayerByNonProjectile(damage, kbStr, kbDir, kbDamageMult);
         } else {
            parryTimer = 0;
            battleScreen.parryEffect();
            SoundEngine.playSound("parry");
            battleScreen
                  .addParticle(new Particle(hitbox.x + width / 2 - 20, hitbox.y + height / 2 - 20,
                        (Math.random() - 0.5) * 3,
                        (Math.random() - 0.5) * 3, 4, 5, 5, "twinkle.png"));
            if (item.holder != null)
               item.holder.endLag += 15;
         }
      }
   }

   public void hitPlayerByNonProjectile(int damage, double kbStr, double kbDir, double kbDamageMult) {
      falling = false;
      charging = false;
      battleScreen.changeAssBallTimer(-damage);
      charge = 0;
      SoundEngine.playSound("hit");
      tookDamage = true;
      knockBack = new Vector(kbStr * 2, 0);
      knockBack = knockBack.mul(this.damage * kbDamageMult + 1);
      knockBack.rotateBy(kbDir);
      if (knockBack.len() > 240) {
         battleScreen.zoomInEffect();
      }
      this.damage += damage;
   }

   public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      if (key == KeyEvent.VK_UP && playerId == 1 || key == KeyEvent.VK_W && playerId == 0) {
         readableKeys[UP] = true;
      }
      if (key == KeyEvent.VK_RIGHT && playerId == 1 || key == KeyEvent.VK_D && playerId == 0) {
         readableKeys[RIGHT] = true;
      }
      if (key == KeyEvent.VK_DOWN && playerId == 1 || key == KeyEvent.VK_S && playerId == 0) {
         readableKeys[DOWN] = true;
      }
      if (key == KeyEvent.VK_LEFT && playerId == 1 || key == KeyEvent.VK_A && playerId == 0) {
         readableKeys[LEFT] = true;
      }
      if (key == KeyEvent.VK_N && playerId == 1 || key == KeyEvent.VK_C && playerId == 0
            || key == KeyEvent.VK_UP && playerId == 1 || key == KeyEvent.VK_W && playerId == 0) {
         readableKeys[JUMP] = true;
      }
      if (key == KeyEvent.VK_M && playerId == 1 || key == KeyEvent.VK_Q && playerId == 0
            || key == KeyEvent.VK_F && playerId == 0) {
         readableKeys[ATTACK] = true;
      }
      if (key == KeyEvent.VK_B && playerId == 1 || key == KeyEvent.VK_Z && playerId == 0) {
         readableKeys[PARRY] = true;
      }
   }

   public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_UP && playerId == 1 || key == KeyEvent.VK_W && playerId == 0) {
         readableKeys[UP] = false;
         readableKeysJustPressed[0] = 0;
      }
      if (key == KeyEvent.VK_RIGHT && playerId == 1 || key == KeyEvent.VK_D && playerId == 0) {
         readableKeys[RIGHT] = false;
         readableKeysJustPressed[3] = 0;
      }
      if (key == KeyEvent.VK_DOWN && playerId == 1 || key == KeyEvent.VK_S && playerId == 0) {
         readableKeys[DOWN] = false;
         readableKeysJustPressed[1] = 0;
      }
      if (key == KeyEvent.VK_LEFT && playerId == 1 || key == KeyEvent.VK_A && playerId == 0) {
         readableKeys[LEFT] = false;
         readableKeysJustPressed[2] = 0;
      }
      if (key == KeyEvent.VK_N && playerId == 1 || key == KeyEvent.VK_C && playerId == 0
            || key == KeyEvent.VK_UP && playerId == 1 || key == KeyEvent.VK_W && playerId == 0) {
         canAirJump = true;
         readableKeys[JUMP] = false;
         readableKeysJustPressed[4] = 0;
      }
      if (key == KeyEvent.VK_M && playerId == 1 || key == KeyEvent.VK_Q && playerId == 0
            || key == KeyEvent.VK_F && playerId == 0) {
         readableKeys[ATTACK] = false;
         readableKeysJustPressed[5] = 0;
      }
      if (key == KeyEvent.VK_B && playerId == 1 || key == KeyEvent.VK_Z && playerId == 0) {
         readableKeys[PARRY] = false;
         readableKeysJustPressed[6] = 0;
      }
   }

   public void simulateKeyPress(int readableKeyID) {
      if (readableKeyID == UP) {
         readableKeysJustPressed[UP] = 0;
         readableKeys[UP] = true;
      }
      if (readableKeyID == DOWN) {
         readableKeysJustPressed[DOWN] = 0;
         readableKeys[DOWN] = true;
      }
      if (readableKeyID == LEFT) {
         readableKeysJustPressed[LEFT] = 0;
         readableKeys[LEFT] = true;
      }
      if (readableKeyID == RIGHT) {
         readableKeysJustPressed[RIGHT] = 0;
         readableKeys[RIGHT] = true;
      }
      if (readableKeyID == JUMP) {
         readableKeysJustPressed[JUMP] = 0;
         readableKeys[JUMP] = true;
      }
      if (readableKeyID == ATTACK) {
         readableKeysJustPressed[ATTACK] = 0;
         readableKeys[ATTACK] = true;
      }
      if (readableKeyID == PARRY) {
         readableKeysJustPressed[PARRY] = 0;
         readableKeys[PARRY] = true;
      }
   }

   public void simulateKeyRelease(int readableKeyID) {
      if (readableKeyID == UP) {
         readableKeys[UP] = false;
         readableKeysJustPressed[UP] = 0;
      }
      if (readableKeyID == DOWN) {
         readableKeys[DOWN] = false;
         readableKeysJustPressed[DOWN] = 0;
      }
      if (readableKeyID == LEFT) {
         readableKeys[LEFT] = false;
         readableKeysJustPressed[LEFT] = 0;
      }
      if (readableKeyID == RIGHT) {
         readableKeys[RIGHT] = false;
         readableKeysJustPressed[RIGHT] = 0;
      }
      if (readableKeyID == JUMP) {
         readableKeys[JUMP] = false;
         readableKeysJustPressed[JUMP] = 0;
      }
      if (readableKeyID == ATTACK) {
         readableKeys[ATTACK] = false;
         readableKeysJustPressed[ATTACK] = 0;
      }
      if (readableKeyID == PARRY) {
         readableKeys[PARRY] = false;
         readableKeysJustPressed[PARRY] = 0;
      }
   }

   public boolean isFreezeCase() {
      return frozen > 0 || endLag > 0 || knockBack.len() > 2;
   }

   public void render(Graphics g, ImageObserver target) {
      selectedChar.preRender(this, g, target);

      g.setColor(new Color(0, 0, 0));
      BufferedImage image = null;
      if (costume == 0) {
         image = Images.getImage(selectedChar.fileName);
      } else {
         image = Images.getImage(
               selectedChar.fileName.substring(0, selectedChar.fileName.length() - 4) + "_alt_" + costume + ".png");
      }
      if ((iFrames + 4) / 4 % 2 == 0) {
         image = Images.alphaEffect(image, 127);
         image = Images.colorAverageEffect(image, new Color(255, 255, 255));
      }
      if (finalAss) {
         for (int i = 0; i < 4; i++) {
            Vector offset = new Vector(0, 0);
            offset.add(new Vector((Math.random() - 0.5) * 12, (Math.random() - 0.5) * 12));
            battleScreen.renderObject(g, image, Vector.add(pos, offset), width, height, frame, direction < 0, target);
         }

         battleScreen.renderObject(g, Images.getImage("ball_sack.png"), new Vector(pos.x - 32, pos.y - 32), width + 64,
               height + 64, direction < 0, target);
      }

      battleScreen.renderObject(g, image, pos, width, height, frame, direction < 0, target);

      if (frozen > 0) {
         BufferedImage ice = Images.getImage("ice.png");
         battleScreen.renderObject(g, ice, pos, width, height, false, target);
      }

      BufferedImage playerIndicator = Images.getImage(String.format("p%sarrow.png", playerId + 1));
      battleScreen.renderObject(g, playerIndicator, new Vector(pos.x + width / 2 - 8, pos.y - 16), 16, 8, false,
            target);

      if (respawnTimer > 0) {
         BufferedImage spawnPlatform = Images.getImage(String.format("p%sspawn_platform.png", playerId + 1));
         battleScreen.renderObject(g, spawnPlatform, new Vector(pos.x - 4, pos.y + height), width + 8, 32, false,
               target);
      }

      if (heldItem != null) {
         heldItem.render(g, target);
      }

      selectedChar.postRender(this, g, target);
   }

   public Vector center() {
      return new Vector(hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height / 2);
   }

   public Player lightClone() {
      return new Player(playerId, selectedChar, battleScreen, lives, costume);
   }
}