package game.stage;

import java.util.List;
import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.Serializable;

import game.physics.Ledge;
import game.physics.AABB;
import game.Player;
import game.Images;
import game.physics.Vector;
import game.screen.BattleScreen;

public class Stage implements Serializable {
   public List<Ledge> ledges;
   protected List<Platform> platforms;
   protected List<CustomStageObject> stageObjects = new ArrayList<>();
   protected List<Platform> deadPlatforms = new ArrayList<>();
   protected List<CustomStageObject> deadStageObjects = new ArrayList<>();
   protected String backgroundImage;
   protected String name;
   protected String musicCredit;
   protected String musicFile;
   protected Vector p1SpawnOffset = new Vector(0, 0);
   protected float maxZoomOut = 1;
   protected Vector p2SpawnOffset = new Vector(0, 0);
   public BattleScreen battleScreen;
   protected AABB safeBlastZone;
   protected AABB unsafeBlastZone;

   public float getMaxZoomOut() {
      return maxZoomOut;
   }

   public AABB getSafeBlastZone() {
      return safeBlastZone;
   }

   public AABB getUnsafeBlastZone() {
      return unsafeBlastZone;
   }

   public void preUpdate() {
   }

   public final void update() {
      preUpdate();
      for (Platform platform : platforms) {
         if (platform.updates()) {
            UpdatingPlatform updatingPlatform = (UpdatingPlatform) platform;
            updatingPlatform.update();
         }
      }
      for (CustomStageObject stageObject : stageObjects) {
         stageObject.update();
      }
      for (Platform platform : deadPlatforms) {
         platforms.remove(platform);
      }
      for (CustomStageObject stageObject : deadStageObjects) {
         stageObjects.remove(stageObject);
      }

      postUpdate();
   }

   public void postUpdate() {
   }

   public void render(Graphics g, ImageObserver target) {
      for (Platform platform : platforms) {
         battleScreen.renderObject(g, Images.getImage(platform.getImage()), platform.getHitbox().getPosition(),
               (int) platform.getHitbox().width, (int) platform.getHitbox().height, false, target);
      }

      for (CustomStageObject stageObject : stageObjects) {
         battleScreen.renderObject(g, Images.getImage(stageObject.image), stageObject.position,
               (int) stageObject.width, (int) stageObject.height, false, target);
      }

      postRender(g, target);
   }

   public void postRender(Graphics g, ImageObserver target) {
   }

   public void selectScreenRender(Graphics g, ImageObserver target) {
      for (Platform platform : platforms) {
         g.drawImage(Images.getImage(platform.getImage()), (int) platform.getHitbox().getPosition().x,
               (int) platform.getHitbox().getPosition().y, (int) platform.getHitbox().width,
               (int) platform.getHitbox().height, target);
      }

      for (CustomStageObject object : stageObjects) {
         g.drawImage(Images.getImage(object.image), (int) object.position.x,
               (int) object.position.y, (int) object.width,
               (int) object.height, target);
      }
   }

   public void setBattleScreen(BattleScreen battleScreen) {
      this.battleScreen = battleScreen;
   }

   public Vector getSpawnOffset(int playerID) {
      if (playerID == 0) {
         return p1SpawnOffset;
      } else if (playerID == 1) {
         return p2SpawnOffset;
      }
      return null;
   }

   public String getMusicCredit() {
      return musicCredit;
   }

   public String getMusic() {
      return musicFile;
   }

   public List<AABB> getCollisions(AABB hitbox) {
      List<AABB> collisions = new ArrayList<>();

      for (Platform platform : platforms) {
         if (platform.getHitbox().overlaps(hitbox) && !platform.canJumpThrough())
            collisions.add(platform.getHitbox());
      }

      return collisions;
   }

   public List<Platform> getPlatformCollisions(AABB hitbox) {
      List<Platform> collisions = new ArrayList<>();

      for (Platform platform : platforms) {
         if (platform.getHitbox().overlaps(hitbox) && !platform.canJumpThrough())
            collisions.add(platform);
      }

      return collisions;
   }

   public List<AABB> getHitboxes() {
      List<AABB> hitboxes = new ArrayList<>();

      for (Platform platform : platforms) {
         hitboxes.add(platform.getHitbox());
      }

      return hitboxes;
   }

   public List<AABB> getNonJumpThroughHitboxes() {
      List<AABB> hitboxes = new ArrayList<>();

      for (Platform platform : platforms) {
         if (!platform.canJumpThrough)
            hitboxes.add(platform.getHitbox());
      }

      return hitboxes;
   }

   public boolean collideWithPlatformsY(Player player, double dy) {
      boolean grounded = false;
      boolean movingUp = false;
      for (Platform platform : platforms) {
         if (player.hitbox.overlaps(platform.getHitbox())) {
            if (dy < 0 || player.hitbox.getY2() > platform.getHitbox().getY2()
                  || player.noPassablePlatformsFor > 0 && platform.canJumpThrough()) {
               movingUp = true;
            } else if (dy > 0) {
               grounded = true;
            }
         }
         if (!movingUp || !platform.canJumpThrough())
            player.hitbox.resolveY(dy, platform.getHitbox());
      }
      return grounded;
   }
   
   public void collideWithPlatformsX(Player player, double dx) {
      for (Platform platform : platforms) {
         if (!platform.canJumpThrough())
            player.hitbox.resolveX(dx, platform.getHitbox());
      }
   }

   public boolean colliding(AABB other) {
      for (Platform platform : platforms) {
         if (other.overlaps(platform.getHitbox()) && !platform.canJumpThrough()) {
            return true;
         }
      }
      return false;
   }

   public Ledge isGrabbing(AABB hitbox) {
      for (Ledge ledge : ledges) {
         if (ledge.grabbing(hitbox) != null)
            return ledge.grabbing(hitbox);
      }
      return null;
   }

   public List<Platform> getPlatforms() {
      return platforms;
   }

   public List<CustomStageObject> getStageObjects() {
      return stageObjects;
   }

   public String getBackground() {
      return backgroundImage;
   }

   public String getName() {
      return name;
   }

   public void preRenderUI(Graphics g, ImageObserver target) {
   }

   public Stage copy() {
      try {
         return (Stage) this.getClass().getConstructors()[0].newInstance((Object[]) null);
      } catch (Exception e) {
         System.out.println("YOUR STAGE SHOULD NOT HAVE INPUTS FOR ITS CONSTRUCTOR");
         System.exit(69);
      }
      return null;
   }
}