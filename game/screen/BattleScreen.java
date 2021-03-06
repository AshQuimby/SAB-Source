package game.screen;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.event.KeyEvent;

import game.particle.*;
import game.Player;
import game.PsuedoPlayer;
import game.stage.*;
import game.AssBall;
import modloader.ModReader;
import game.projectile.*;
import game.physics.*;
import game.ai.*;
import game.Fonts;
import game.SoundEngine;
import game.Images;
import game.character.Character;
import java.awt.image.BufferedImage;
import game.Settings;

public class BattleScreen implements Screen {
    private final Player player1;
    private final Player player2;
    private Stage stage;
    private Camera camera;

    private final List<Player> players;
    private final List<Projectile> projectiles;
    private final List<Particle> particles;
    private final List<AssBall> assBalls;

    private List<Projectile> newProjectiles;

    private int numberLives = Settings.lives();
    private int cameraShake;
    private int toCharacterSelectScreenTimer;
    private boolean showHitboxes;
    private int parryTime;
    private int slowDownTime;
    private int assBallTimer;
    private int gameTick;
    private boolean paused;
    private boolean hidePauseUI;
    private boolean warpSpeed;
    private int pauseMenuIndex;
    private int framesInSecond = 0;
    private long lastFPSCheck = 0;
    private int fps = 0;
    private int ticksInSecond = 0;
    private long lastTPSCheck = 0;
    private int tps = 0;
    private Player winner;

    private boolean gameEnded;

    private AI getAI(int aiType) {
        AI ai;

        switch (aiType) {
            case 1:
                ai = new BadAI();
                break;
            case 2:
                ai = new GodAI();
                break;
            case 3:
                ai = new TrueGodAI();
                break;
            default:
                ai = new AI();
                break;
        }

        return ai;
    }

    public BattleScreen(Character character1, Character character2, Stage stage, int costume1, int costume2) {
        pauseMenuIndex = 0;
        warpSpeed = false;
        winner = null;

        cameraShake = 0;

        gameTick = 0;

        AI player1AI = getAI(Settings.aiPlayer1());
        AI player2AI = getAI(Settings.aiPlayer2());

        player1 = new Player(0, character1, this, numberLives, costume1, player1AI);
        player2 = new Player(1, character2, this, numberLives, costume2, player2AI);
        ModReader.injectModPlayers(player1);
        ModReader.injectModPlayers(player2);
        this.stage = stage;
        stage.setBattleScreen(this);
        SoundEngine.playMusic(this.stage.getMusic());
        parryTime = 0;
        slowDownTime = 0;
        players = Collections.synchronizedList(new ArrayList<>());
        projectiles = Collections.synchronizedList(new ArrayList<>());
        particles = Collections.synchronizedList(new ArrayList<>());
        assBalls = Collections.synchronizedList(new ArrayList<>());
        paused = false;

        newProjectiles = new ArrayList<>();
        showHitboxes = false;
        players.add(player1);
        players.add(player2);
        toCharacterSelectScreenTimer = 90;
        gameEnded = false;
        positionPlayers();
        camera = new Camera(1152, 704);

        assBallTimer = 1000;
    }

    public void changeAssBallTimer(int time) {
        assBallTimer += time;
    }

    public List<Player> getPlayerList() {
        return players;
    }

    public List<AssBall> getAssBalls() {
        return assBalls;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void parryEffect() {
        parryTime = 10;
    }

    public void cameraShake(int intensity) {
        if (intensity > cameraShake)
            cameraShake = intensity;
    }

    private void positionPlayers() {
        player1.hitbox.x = 576 - 128 - player1.hitbox.width / 2 + stage.getSpawnOffset(0).x;
        player2.hitbox.x = 576 + 128 - player2.hitbox.width / 2 + stage.getSpawnOffset(1).x;
        player1.hitbox.y = 64 + stage.getSpawnOffset(0).y;
        player2.hitbox.y = 64 + stage.getSpawnOffset(1).y;
    }

    public void drawText(Vector pos, float size, String text, Color color, Graphics g, boolean lockedCenter) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setColor(color);
        g2d.setFont(Fonts.getSABFont());
        g2d.setFont(g2d.getFont().deriveFont(size));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle((int) pos.x, (int) pos.y, metrics.stringWidth(text), (int) metrics.getHeight());
        int x;
        int y;
        if (lockedCenter) {
            x = rect.x - rect.width / 2;
            y = rect.y + rect.height / 2;
        } else {
            x = rect.x - rect.width + 48;
            y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        }
        g2d.drawString(text, x, y);
    }

    private void drawLives(Graphics g, Player player, ImageObserver target) {
        for (int i = 0; i < player.lives; i++) {
            if (player.playerId != -1)
                g.drawImage(Images.getImage("life_p" + (player.playerId + 1) + ".png"),
                        576 - 48 + 24 * i + (128 * (player.playerId * 2 - 1)), 600, target);
        }
    }

    public void drawHitboxes(Graphics g) {
        for (Projectile projectile : projectiles) {
            g.setColor(Color.RED);
            Rectangle hitbox = getHitboxDrawRect(projectile.hitbox.getPosition(), (int) projectile.hitbox.width,
                    (int) projectile.hitbox.height);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            g.setColor(new Color(255, 0, 255));
            Vector drawPos = new Vector(hitbox.x +
                    hitbox.width / 2,
                    hitbox.y + hitbox.height / 2);
            Vector toPos = new Vector((hitbox.width + hitbox.height) / 2, 0).rotateBy(projectile.dir);
            g.drawLine((int) drawPos.x, (int) drawPos.y, (int) (drawPos.x + toPos.x),
                    (int) (drawPos.y + toPos.y));
        }
        g.setColor(Color.BLUE);
        for (int i = 0; i < players.size(); i++) {
            Rectangle hitbox = getHitboxDrawRect(players.get(i).hitbox.getPosition(), (int) players.get(i).hitbox.width,
                    (int) players.get(i).hitbox.height);
            g.drawRect(hitbox.x, (int) hitbox.y,
                    (int) hitbox.width, (int) hitbox.height);
        }
        g.setColor(Color.GREEN);
        for (Platform platform : stage.getPlatforms()) {
            Rectangle hitbox = getHitboxDrawRect(platform.getHitbox().getPosition(), (int) platform.getHitbox().width,
                    (int) platform.getHitbox().height);
            g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
        g.setColor(new Color(255, 128, 0));
        for (Ledge ledge : stage.ledges) {
            Rectangle hitbox = getHitboxDrawRect(ledge.hitbox.getPosition(), (int) ledge.hitbox.width,
                    (int) ledge.hitbox.height);
            g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
        g.setColor(new Color(0, 255, 125));
        // Draw camera viewport.
        AABB viewport = camera.getViewport();
        Rectangle hitbox = getHitboxDrawRect(viewport.getPosition(), (int) viewport.width, (int) viewport.height);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        
        drawText(new Vector(100, 100), 20, "FPS: " + fps, new Color(255, 255, 255), g,
                        false);
        drawText(new Vector(100, 200), 20, "TPS: " + tps, new Color(255, 255, 255), g,
                        false);  
        framesInSecond++;
    
        if (System.currentTimeMillis() >= lastFPSCheck + 1000) {
            lastFPSCheck = System.currentTimeMillis();
            fps = framesInSecond;
            framesInSecond = 0;
        }
    }

    public void triggerHitboxes() {
        showHitboxes = !showHitboxes;
    }

    @Override
    public Screen keyTyped(KeyEvent event) {
        return this;
    }

    @Override
    public Screen keyPressed(KeyEvent event) {
        if (Settings.testingMode()) {
            if (event.getKeyCode() == KeyEvent.VK_H) { // triggers hitboxes
                triggerHitboxes();
            }

            if (event.getKeyCode() == KeyEvent.VK_V) { // cheating
                assBalls.add(new AssBall(this));
            }

            if (event.getKeyCode() == KeyEvent.VK_L) { // even more cheating
                player1.lives = 999;
                player2.lives = 999;
            }

            if (event.getKeyCode() == KeyEvent.VK_K) { // murder
                player1.kill();
                player2.kill();
            }

            if (event.getKeyCode() == KeyEvent.VK_U) { // warp speed
                warpSpeed = true;
                SoundEngine.playbackSpeed = 2f;
            }
        }

        if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
            paused = !paused;
        }

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
        }

        if (paused) {
            if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W && !hidePauseUI) {
                pauseMenuIndex = Utilities.overflow(pauseMenuIndex - 1, 2, 0);
            }
            if (event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S && !hidePauseUI) {
                pauseMenuIndex = Utilities.overflow(pauseMenuIndex + 1, 2, 0);
            }

            if (event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_TAB) {
                if (pauseMenuIndex == 0) {
                    paused = false;
                } else if (pauseMenuIndex == 1) {
                    hidePauseUI = !hidePauseUI;
                } else if (pauseMenuIndex == 2) {
                    PsuedoPlayer tie = new PsuedoPlayer(this, false);
                    tie.selectedChar.fileName = "tie.png";
                    return new EndScreen(tie);
                }
            }
        }

        if (!gameEnded) {
            player1.keyPressed(event);
            player2.keyPressed(event);
        }

        return this;
    }

    @Override
    public Screen keyReleased(KeyEvent event) {
        if (!gameEnded) {
            player1.keyReleased(event);
            player2.keyReleased(event);
        }

        if (event.getKeyCode() == KeyEvent.VK_U)
            warpSpeed = false;
            SoundEngine.playbackSpeed = 1;

        return this;
    }

    public Screen bigBoyUpdate() {  
        ticksInSecond++;
    
        if (System.currentTimeMillis() >= lastTPSCheck + 1000) {
            lastTPSCheck = System.currentTimeMillis();
            tps = ticksInSecond;
            ticksInSecond = 0;
        }
        
        if (!paused) {
            updateCamera();
        }
        if (gameEnded) {
            SoundEngine.playbackSpeed = 1;
            if (toCharacterSelectScreenTimer == 90)
                SoundEngine.playSound("final_death");
        }
        if (toCharacterSelectScreenTimer <= 0) {
            if (winner == null) {
                winner = new PsuedoPlayer(this, false);
                winner.selectedChar.fileName = "tie.png";
            }
            return new EndScreen(winner);
        }
        if (parryTime % 2 == 0 && toCharacterSelectScreenTimer % 2 == 0 && slowDownTime % 8 == 0 && !paused) {
            gameTick++;
            List<Particle> deadParticles = new ArrayList<>();
            List<Projectile> deadProjectiles = new ArrayList<>();
            List<Player> deadPlayers = new ArrayList<>();
            List<AssBall> deadAssBalls = new ArrayList<>();

            for (Player player : players) {
                player.invincible = false;
                player.update();
                player.uniqueAnimations();
                if (player.lives <= 0) {
                    if (player.playerId == 0 || player.playerId == 1)
                        gameEnded = true;
                    player.kill();
                    deadPlayers.add(player);
                } else if (player.lives > 0 && gameEnded && winner == null
                        && (player.playerId == 0 || player.playerId == 1)) {
                    winner = player.lightClone();
                }
            }

            for (AssBall assBall : assBalls) {
                assBall.update();
                if (!assBall.alive)
                    deadAssBalls.add(assBall);
            }

            stage.update();

            for (Particle particle : particles) {
                particle.update();
                
                if (!particle.alive) {
                    deadParticles.add(particle);
                }
            }
            
            while (particles.size() > 50 && Settings.performanceMode()) {
               particles.remove(0);
            }

            for (Projectile projectile : projectiles) {
                for (int i = 0; i < projectile.updatesPerTick; i++) {
                    projectile.update();
                }

                if (!projectile.alive) {
                    projectile.kill();
                    deadProjectiles.add(projectile);
                }
            }
            
            if (!player1.render && player1.invincible && !player2.render && player2.invincible && stage.getClass() != Hell.class && player2.hitbox.y < 0) { // Send players to the Shadow Realm
               stage = new Hell();
               SoundEngine.playMusic("genetically_engineered_bad");
               stage.battleScreen = this;
               player1.kill();
               player2.kill();
               player1.lives++;
               player2.lives++;
            }
            
            particles.removeAll(deadParticles);
            projectiles.removeAll(deadProjectiles);
            players.removeAll(deadPlayers);
            for (AssBall assBall : deadAssBalls) {
               players.remove(assBall.fakePlayer);
            }
            assBalls.removeAll(deadAssBalls);
            projectiles.addAll(newProjectiles);
            newProjectiles = new ArrayList<>();
            if (cameraShake > 0) {
                cameraShake--;
            } else {
                cameraShake = 0;
            }
            if (assBallTimer < 0 && Settings.assBalls()) {
                assBalls.add(new AssBall(this));
                assBallTimer = 2000;
            } else {
                assBallTimer--;
            }
        }

        if (!paused) {
            if (parryTime > 0) {
                parryTime--;
            } else {
                parryTime = 0;
            }
            if (slowDownTime > 0) {
                slowDownTime--;
            } else {
                slowDownTime = 0;
            }
        }
        if (gameEnded) {
            toCharacterSelectScreenTimer--;
        }
        return this;
    }
    
    @Override
    public Screen update() {
        Screen toReturn = bigBoyUpdate();
        if (warpSpeed) {
             toReturn = bigBoyUpdate();
        }
        return toReturn;
    }

    public Vector stageCenter() {
        return stage.getSafeBlastZone().getCenter();
    }

    public Camera getCamera() {
        return camera;
    }

    public void updateCamera() {
        Vector playerDifference = Vector.sub(player1.pos, player2.pos);
        double playerDistance = Math
                .sqrt(playerDifference.x * playerDifference.x + playerDifference.y * playerDifference.y);
        camera.setTargetZoom((float) (1 / Math.exp(playerDistance / 128) + 1.5f) - ((float) playerDistance * 0.001f));
        camera.setTarget(Vector.add(player1.center(), player2.center()).div(2));
        // for (AssBall assBall : assBalls) {
        // camera.setTarget(Vector.add(camera.getTarget().mul(9), new
        // Vector(assBall.pos.x + 20, assBall.pos.y + 20)).div(10));
        // }
        camera.update();
        if (camera.getZoom() < (float) stage.getMaxZoomOut()) {
            camera.setZoom((float) stage.getMaxZoomOut());
        }
        if (camera.getZoom() > 1.5) {
            camera.setTargetZoom(1.5f);
        }
        if (slowDownTime > 0) {
            camera.setTargetZoom(1.5f);
        }
        if (Settings.fixedCamera())
            camera.setZoom(stage.getMaxZoomOut());

        AABB viewport = camera.getViewport();

        if (viewport.x < (stage.getSafeBlastZone().x)) {
            viewport.x = stage.getSafeBlastZone().x;
        }
        if (viewport.getX2() > stage.getSafeBlastZone().getX2()) {
            viewport.setX2(stage.getSafeBlastZone().getX2());
        }
        if (viewport.y < (stage.getSafeBlastZone().y)) {
            viewport.y = stage.getSafeBlastZone().y;
        }
        if (viewport.getY2() > stage.getSafeBlastZone().getY2()) {
            viewport.setY2(stage.getSafeBlastZone().getY2());
        }
        viewport.setPosition(
                viewport.getPosition().add((Math.random() - 0.5) * Math.pow((double) cameraShake / 3, 2),
                        (Math.random() - 0.5) * Math.pow((double) cameraShake / 3, 2)));

        camera.setPosition(viewport.getCenter());
    }

    public Rectangle getRenderRectangle(Vector pos, int width, int height) {
        Vector renderPosition = camera.transform(pos);
        float zoom = camera.getZoom();

        return new Rectangle((int) Math.round(renderPosition.x), (int) Math.round(renderPosition.y + 48),
                (int) Math.round(width * zoom),
                (int) Math.round(height * zoom));
    }

    public Rectangle getHitboxDrawRect(Vector pos, int width, int height) {
        float zoom = (float) stage.getMaxZoomOut();
        Vector renderPosition = camera.transform(pos, zoom);

        return new Rectangle((int) Math.round(renderPosition.x), (int) Math.round(renderPosition.y + 48),
                (int) Math.round(width * zoom),
                (int) Math.round(height * zoom));
    }

    public void renderObject(Graphics g, BufferedImage image, Vector pos, int width, int height, int frame,
            boolean flipped, ImageObserver target) {
        Rectangle drawTo = getRenderRectangle(pos, width, height);
        if (flipped) {
            g.drawImage(image, drawTo.x, drawTo.y, drawTo.x + drawTo.width, drawTo.y + drawTo.height, 0,
                    0 + frame * height, width, height * (frame + 1), target);
        } else {
            g.drawImage(image, drawTo.x + drawTo.width, drawTo.y, drawTo.x, drawTo.y + drawTo.height, 0,
                    0 + frame * height, width, height * (frame + 1), target);
        }
    }

    public void renderObject(Graphics g, BufferedImage image, Vector pos, int width, int height,
            boolean flipped, ImageObserver target) {
        Rectangle drawTo = getRenderRectangle(pos, width, height);
        if (flipped) {
            g.drawImage(image, drawTo.x, drawTo.y, drawTo.width, drawTo.height, target);
        } else {
            g.drawImage(image, drawTo.x + drawTo.width, drawTo.y, -drawTo.width, drawTo.height, target);
        }
    }

    @Override
    public void render(Graphics g, ImageObserver target) {
        if (!showHitboxes)
            g.drawImage(Images.getImage(stage.getBackground()), 0, 0, target);

        for (Projectile projectile : projectiles) {
            projectile.preRender(g, target);
        }

        for (Projectile projectile : projectiles) {
            if (!projectile.drawPriority() && !projectile.drawAfterProjectiles())
                projectile.render(g, target);
        }

        for (Projectile projectile : projectiles) {
            if (projectile.drawAfterProjectiles())
                projectile.render(g, target);
        }

        for (Player player : players) {
            if (player.render)
                player.render(g, target);
        }

        for (AssBall assBall : assBalls) {
            assBall.render(g, target);
        }

        stage.render(g, target);

        for (Projectile projectile : projectiles) {
            if (projectile.drawPriority())
                projectile.render(g, target);
        }

        for (Particle particle : particles) {
            particle.render(g, this, target);
        }

        for (Projectile projectile : projectiles) {
            projectile.lateRender(g, target);
        }

        stage.preRenderUI(g, target);

        if (players.size() > 1) {
            if (players.get(0) != null)
                drawText(new Vector(576 - 128 - 24, 640), 20, players.get(0).damage + "%", new Color(255, 255, 255), g,
                        false);
            if (players.get(1) != null)
                drawText(new Vector(576 + 128 - 24, 640), 20, players.get(1).damage + "%", new Color(255, 255, 255), g,
                        false);
        }
        for (Player player : players) {
            player.selectedChar.renderUIElements(player, g, target);
        }
        for (Player player : players)
            drawLives(g, player, target);
        if (gameEnded) {
            g.setColor(new Color(0, 0, 0, 160 + 90 - toCharacterSelectScreenTimer));
            g.fillRect(0, 0, 1152, 704);
            drawText(new Vector(576, 352), 80 - toCharacterSelectScreenTimer / 9, "GAME END",
                    new Color(255, 255, 255), g, true);
        }
        g.setColor(new Color(255, 255, 255, 0 + parryTime));
        g.fillRect(0, 0, 1152, 704);
        if (slowDownTime > 0) {
            g.drawImage(Images.getImage("screen_shatter.png"), 0, 0, target);
        }
        if (showHitboxes)
            drawHitboxes(g);

        if (paused && !hidePauseUI) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect((int) getStageBounds().x, (int) getStageBounds().y, (int) getStageBounds().width,
                    (int) getStageBounds().height);
            drawText(new Vector(1152 / 2, 64), 24, "Paused", new Color(255, 255, 255), g, true);
            g.drawImage(Images.getImage("p1_pause_UI.png"), 1152 - 192, 128, 1152, 128 + 192, 0,
                    0 + (pauseMenuIndex == 0 ? 192 : 0), 192, 192 + (pauseMenuIndex == 0 ? 192 : 0), target);
            g.drawImage(Images.getImage("p1_pause_UI.png"), 1152 - 192, 256, 1152, 256 + 192, 0,
                    0 + (pauseMenuIndex == 1 ? 192 : 0), 192, 192 + (pauseMenuIndex == 1 ? 192 : 0), target);
            g.drawImage(Images.getImage("p1_pause_UI.png"), 1152 - 192, 384, 1152, 384 + 192, 0,
                    0 + (pauseMenuIndex == 2 ? 192 : 0), 192, 192 + (pauseMenuIndex == 2 ? 192 : 0), target);
            drawText(new Vector(1152 - 64, 128 + 80), 16, "Resume", new Color(255, 255, 255), g, false);
            drawText(new Vector(1152 - 64, 256 + 80), 16, "Hide Display", new Color(255, 255, 255), g, false);
            drawText(new Vector(1152 - 64, 384 + 80), 16, "Quit", new Color(255, 255, 255), g, false);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void zoomInEffect() {
        SoundEngine.playSound("shatter");
        slowDownTime = 20;
    }

    public AABB getStageBounds() {
        return stage.getSafeBlastZone();
    }

    public Projectile addProjectile(Projectile projectile) {
        newProjectiles.add(projectile);
        projectile.battleScreen = this;
        return projectile;
    }

    public Projectile addProjectileAtCenter(Projectile projectile) {
        projectile.pos.x -= projectile.width / 2;
        projectile.pos.y -= projectile.height / 2;
        projectile.hitbox.x -= projectile.width / 2;
        projectile.hitbox.y -= projectile.height / 2;
        newProjectiles.add(projectile);
        projectile.battleScreen = this;
        return projectile;
    }

    public Projectile getProjectile(int index) {
        return projectiles.get(index);
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public int getGameTick() {
        return gameTick;
    }

    public Player getPlayers(int index) {
        return players.get(index);
    }

    public boolean getGameEnded() {
        return gameEnded;
    }
}