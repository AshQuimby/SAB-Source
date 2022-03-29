/*
 * import java.awt.*;
 * import java.awt.event.*;
 * import java.util.ArrayList;
 * import java.io.File;
 * import java.awt.image.BufferedImage;
 * import javax.swing.*;
 * import java.io.IOException;
 * import javax.imageio.ImageIO;
 * 
 * public class Board extends JPanel implements ActionListener, KeyListener {
 * 
 * // controls the delay between each tick in ms
 * private final int DELAY = 25;
 * // controls the size of the board
 * public static final int TILE_SIZE = 32;
 * public static final int ROWS = 22;
 * public static final int COLUMNS = 36;
 * // suppress serialization warning
 * private static boolean showHitboxes = false;
 * // keep a reference to the timer object that triggers actionPerformed() in
 * // case we need access to it in another method
 * private Timer timer;
 * public static Stage stage = new Stage();
 * // objects that appear on the game board
 * static ArrayList<Projectile> projectiles = new ArrayList<>(50);
 * static ArrayList<Player> players = new ArrayList<>(2);
 * static ArrayList<Particle> particles = new ArrayList<>(100);
 * Player player1 = new Player(0, new Marvin());
 * Player player2 = new Player(1, new Gus());
 * 
 * public Board() {
 * // set the game board size
 * setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
 * players.add(player1);
 * players.add(player2);
 * // set the game board background color
 * setBackground(new Color(232, 232, 232));
 * players.get(0).pos.x = 556 - 128 - players.get(0).selectedChar.width / 2;
 * players.get(1).pos.x = 556 + 128 - players.get(1).selectedChar.width / 2;
 * // this timer will call the actionPerformed() method every DELAY ms
 * timer = new Timer(DELAY, this);
 * timer.start();
 * }
 * 
 * public static void newProjectile(Projectile projectile) {
 * projectiles.add(projectile);
 * }
 * 
 * @Override
 * public void actionPerformed(ActionEvent e) {
 * // this method is called by the timer every DELAY ms.
 * // use this space to update the state of your game or animation
 * // before the graphics are redrawn.
 * players.get(0).update();
 * players.get(1).update();
 * for (int i = 0; i < projectiles.size(); i++) {
 * for (int j = 0; j < projectiles.get(i).updatesPerTick; j++)
 * projectiles.get(i).update();
 * if (!projectiles.get(i).alive) {
 * projectiles.get(i).kill();
 * projectiles.remove(i);
 * }
 * }
 * for (int i = 0; i < particles.size(); i++) {
 * particles.get(i).update();
 * if (!particles.get(i).alive) {
 * particles.remove(i);
 * }
 * }
 * 
 * // calling repaint() will trigger paintComponent() to run again,
 * // which will refresh/redraw the graphics.
 * repaint();
 * }
 * 
 * @Override
 * public void paintComponent(Graphics g) {
 * super.paintComponent(g);
 * // when calling g.drawImage() we can use "this" for the ImageObserver
 * // because Component implements the ImageObserver interface, and JPanel
 * // extends from Component. So "this" Board instance, as a Component, can
 * // react to imageUpdate() events triggered by g.drawImage()
 * 
 * // draw our graphics.
 * 
 * drawBackground(g);
 * if (!showHitboxes)
 * drawImageBackground(g);
 * for (int i = 0; i < projectiles.size(); i++) {
 * if (projectiles.get(i).draw)
 * drawProjectiles(g, projectiles.get(i));
 * }
 * drawPlayer(players.get(0), g);
 * drawPlayer(players.get(1), g);
 * drawScore(g);
 * for (int i = 0; i < particles.size(); i++) {
 * drawParticles(g, particles.get(i));
 * }
 * drawStage(g);
 * if (showHitboxes) {
 * drawHitboxes(g);
 * }
 * // this smooths out animations on some systems
 * Toolkit.getDefaultToolkit().sync();
 * }
 * 
 * public void drawParticles(Graphics g, Particle particle) {
 * g.drawImage(particle.img, (int) particle.pos.x, (int) particle.pos.y,
 * (int) (particle.width * 4 * particle.scale),
 * (int) (particle.height * 4 * particle.scale), this);
 * }
 * 
 * public void drawProjectiles(Graphics g, Projectile projectile) {
 * BufferedImage img = null;
 * img = projectile.image;
 * if (projectile.direction < 0) {
 * g.drawImage(img, (int) projectile.pos.x, (int) projectile.pos.y, (int)
 * projectile.pos.x + projectile.width,
 * (int) projectile.pos.y + projectile.height, 0,
 * 0 + projectile.frame * projectile.height, projectile.width,
 * projectile.height * (projectile.frame + 1), this);
 * } else {
 * g.drawImage(img, (int) projectile.pos.x + projectile.width, (int)
 * projectile.pos.y, (int) projectile.pos.x,
 * (int) projectile.pos.y + projectile.height, 0,
 * 0 + projectile.frame * projectile.height, projectile.width,
 * projectile.height * (projectile.frame + 1), this);
 * }
 * }
 * 
 * public void drawHitboxes(Graphics g) {
 * for (int i = 0; i < projectiles.size(); i++) {
 * g.setColor(Color.RED);
 * g.drawRect((int) projectiles.get(i).hurtbox.pos.x, (int)
 * projectiles.get(i).hurtbox.pos.y,
 * projectiles.get(i).hurtbox.width, projectiles.get(i).hurtbox.height);
 * g.setColor(new Color(255, 0, 255));
 * Vector drawPos = new Vector(projectiles.get(i).hurtbox.pos.x +
 * projectiles.get(i).width / 2,
 * projectiles.get(i).pos.y + projectiles.get(i).height / 2);
 * Vector toPos = new Vector((projectiles.get(i).width +
 * projectiles.get(i).height) / 2, 0)
 * .rotateBy(projectiles.get(i).dir);
 * g.drawLine((int) drawPos.x, (int) drawPos.y, (int) (drawPos.x + toPos.x),
 * (int) (drawPos.y + toPos.y));
 * }
 * g.setColor(Color.BLUE);
 * for (int i = 0; i < 2; i++) {
 * g.drawRect((int) players.get(i).hitbox.pos.x, (int)
 * players.get(i).hitbox.pos.y,
 * players.get(i).hitbox.width, players.get(i).hitbox.height);
 * }
 * g.setColor(Color.GREEN);
 * g.drawRect((int) stage.pos.x, (int) stage.pos.y, stage.width, stage.height);
 * }
 * 
 * public void triggerHitboxes() {
 * showHitboxes = !showHitboxes;
 * }
 * 
 * @Override
 * public void keyTyped(KeyEvent e) {
 * }
 * 
 * @Override
 * public void keyPressed(KeyEvent e) {
 * // react to key down events
 * if (e.getKeyCode() == KeyEvent.VK_H) { // triggers hitboxes
 * triggerHitboxes();
 * }
 * players.get(0).keyPressed(e);
 * players.get(1).keyPressed(e);
 * }
 * 
 * @Override
 * public void keyReleased(KeyEvent e) {
 * // react to key up events
 * players.get(0).keyReleased(e);
 * players.get(1).keyReleased(e);
 * }
 * 
 * private void drawBackground(Graphics g) {
 * // draw a checkered background
 * g.setColor(new Color(214, 214, 214));
 * for (int row = 0; row < ROWS; row++) {
 * for (int col = 0; col < COLUMNS; col++) {
 * // only color every other tile
 * if ((row + col) % 2 == 1) {
 * // draw a square tile at the current row/column position
 * g.fillRect(
 * col * TILE_SIZE,
 * row * TILE_SIZE,
 * TILE_SIZE,
 * TILE_SIZE);
 * }
 * }
 * }
 * }
 * 
 * private void drawScore(Graphics g) {
 * String text = "%" + players.get(0).damage + "     %" + players.get(1).damage;
 * Graphics2D g2d = (Graphics2D) g;
 * g2d.setRenderingHint(
 * RenderingHints.KEY_TEXT_ANTIALIASING,
 * RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 * g2d.setRenderingHint(
 * RenderingHints.KEY_RENDERING,
 * RenderingHints.VALUE_RENDER_QUALITY);
 * g2d.setRenderingHint(
 * RenderingHints.KEY_FRACTIONALMETRICS,
 * RenderingHints.VALUE_FRACTIONALMETRICS_ON);
 * g2d.setColor(new Color(150, 150, 150));
 * g2d.setFont(new Font("Lato", Font.BOLD, 25));
 * FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
 * Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE *
 * COLUMNS, TILE_SIZE);
 * int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
 * int y = rect.y + ((rect.height - metrics.getHeight()) / 2) +
 * metrics.getAscent();
 * g2d.drawString(text, x, y);
 * }
 * 
 * private void drawPlayer(Player p, Graphics g) {
 * 
 * }
 * 
 * private void drawStage(Graphics g) {
 * BufferedImage img = null;
 * try {
 * img = ImageIO.read(new File("last_location.png"));
 * } catch (IOException e) {
 * }
 * g.setColor(new Color(0, 0, 0));
 * g.drawImage(img, (int) stage.pos.x, (int) stage.pos.y, this);
 * }
 * 
 * private void drawImageBackground(Graphics g) {
 * BufferedImage img = null;
 * try {
 * img = ImageIO.read(new File("background.png"));
 * } catch (IOException e) {
 * }
 * g.setColor(new Color(0, 0, 0));
 * g.drawImage(img, 0, 0, this);
 * }
 * }
 */