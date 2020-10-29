package entity.util;

import java.awt.Rectangle;
import java.util.ArrayList;

import effects.TextEffect;
import entity.Character;
import entity.Entity;
import entity.PlayerEnhanced;
import entity.Projectile;
import entity.enemy.Enemy;

/*
 * A master class for handling all entities in a given game.
 * Contains ArrayLists of all entities and methods for processing them.
 * 
 */
public class EntityHandler {
	
	// ENTITIES
	// player
	private PlayerEnhanced player;
	// enemies
	private ArrayList<Enemy> enemies;
	// characters
	private Character[] characters;
	// collectibles
	private ArrayList<Collectible> collectibles;
	// interactives
	private ArrayList<Interactive> interactives;
	// projectiles
	private ArrayList<Projectile> projectiles;
	// generic entities
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	// text effects
	private ArrayList<TextEffect> textEffects;
	
	// constructor
	public EntityHandler() {
		// yeah, nothing really happens here
		textEffects = new ArrayList<TextEffect>();
	}
	
	public EntityHandler(ArrayList<TextEffect> textEffects) {
		this.textEffects = textEffects;
	}
	
	// setters
	public void setPlayer(PlayerEnhanced player) { this.player = player; }
	public void setEnemies(ArrayList<Enemy> enemies) { this.enemies = enemies; }
	public void setCharacters(Character[] characters) { this.characters = characters; }
	public void setCollectibles(ArrayList<Collectible> collectibles) { this.collectibles = collectibles; }
	public void setInteractives(ArrayList<Interactive> interactives) { this.interactives = interactives; }
	public void setProjectiles(ArrayList<Projectile> projectiles) { this.projectiles = projectiles; }
	public void setEntities(ArrayList<Entity> entities) { this.entities = entities; }
	
	// getters
	public PlayerEnhanced getPlayer() { return player; }
	public ArrayList<Enemy> getEnemies() { return enemies; }
	public Character[] getCharacters() { return characters; }
	public ArrayList<Collectible> getCollectibles() { return collectibles; }
	public ArrayList<Interactive> getInteractives() { return interactives; }
	public ArrayList<Projectile> getProjectiles() { return projectiles; }
	public ArrayList<Entity> getEntities() { return entities; }
	
	// OPERATIONS
	// adding entities
	public void addEnemy(Enemy e) { enemies.add(e); }
	// adding characters
	public void addCharacter(Character c, int index) { characters[index] = c; }
	// adding collectibles
	public void addCollectible(Collectible c) { collectibles.add(c); }
	// adding interactives
	public void addInteractive(Interactive i) { interactives.add(i); }
	// adding projectiles
	public void addProjectile(Projectile p) { projectiles.add(p); }
	// adding entities
	public void addEntity(Entity e) { entities.add(e); }
	
	// UPDATE INDIVIDUAL ENTITY TYPES
	public void updatePlayer() { player.update(); }
	public void updateEnemies() { for (Enemy e : enemies) e.update(); }
	// update characters already exists
	public void updateCollectibles() { for (Collectible c : collectibles) c.update(); }
	public void updateInteractives() { for (Interactive i : interactives) i.update(); }
	public void updateEntities() { for (Entity e : entities) e.update(); }
	
	// UPDATE ALL ENTITIES
	public void update() {
		/*
		 * Update entities in the following order:
		 *     1. Player
		 *        -Check attack on enemies
		 *     2. Enemies
		 *        -Check attack on player
		 *     3. Projectiles
		 *        -Check collisions
		 *     4. Characters
		 *     5. Interactives
		 *        -Check interactions
		 *     6. Collectibles
		 *        -Check pickup
		 *     7. Entities
		 * Then, clean up any entities that need to be removed.
		 */
		// STEP 1
		player.update();
		if (player.isAttacking()) checkPlayerAttack();
		// STEP 2
		checkEnemyAttack();
		// STEP 3
		checkProjectileCollisions();
		// STEP 4
		updateCharacters();
		// STEP 5
		updateInteractives();
		checkInteractives();
		// STEP 6
		updateCollectibles();
		checkCollectibles();
		// STEP 7
		updateEntities();
		// STEP 8
		cleanUpEntities();
	}
	/*
	 * Check player melee attack on enemies.
	 */
	public void checkPlayerAttack() {
		for (int i = 0; i < enemies.size(); i++) {
			// get current enemy in list
			Enemy e = enemies.get(i);
			if (e.isDead() || e.isFlinching()) continue; // move on if it's already dead or flinching
			// get rectangle of player's attack hitbox
			Rectangle attack = player.getAttackRect();
			// check if it hits
			if (e.getRectangle().intersects(attack)) {
				// if it hits, damage the enemy
				e.damage(player.getAttackDamage());
				// check if enemy is dead
				if (e.isDead()) {
					// if it is, award points
					player.score(e.getValue());
					TextEffect t = new TextEffect("" + e.getValue(), e.getTileMap(), (int) e.getX(), (int) e.getY());
					textEffects.add(t);
				}
			}
		}
	}
	/*
	 * Check enemy melee attack on player.
	 */
	public void checkEnemyAttack() {
		for (int i = 0; i < enemies.size(); i++) {
			// get current enemy from list
			Enemy e = enemies.get(i);
			// update the enemy
			e.update();
			// check if enemy hitbox hits player (and not flinching)
			if (e.intersects(player) && !(e.isFlinching() || e.isDead())) {
				player.damage();
			}
		}
	}
	/*
	 * Update projectiles and check for their collisions.
	 */
	public void checkProjectileCollisions() {
		for (int i = 0; i < projectiles.size(); i++) {
			// get current projectile in list
			Projectile p = projectiles.get(i);
			// update the projectile
			p.update();
			p.checkTileCollision();
			// check if it hit a tile first
			if (p.hit()) continue;
			// decide what to check for collisions with
			switch (p.getSourceID()) {
			case Projectile.PLAYER:
				checkPlayerProjectile(p);
				break;
			case Projectile.ENEMY:
				checkEnemyProjectile(p);
				break;
			case Projectile.OTHER:
				checkPlayerProjectile(p);
				checkEnemyProjectile(p);
				break;
			case Projectile.OTHER_PASSIVE:
				// neither
				break;
			}
		}
	}
	/*
	 * Check for player projectile hitting enemies
	 */
	public void checkPlayerProjectile(Projectile p) {
		for (int i = 0; i < enemies.size(); i++) {
			// get current enemy in list
			Enemy e = enemies.get(i);
			// check if they collide (while enemy is not flinching)
			if (e.intersects(p) && !e.isFlinching()) {
				// they collided, damage enemy
				e.damage(p.getDamage());
				p.setHit();
			}
		}
	}
	/*
	 * Check for enemy projectile hitting player
	 */
	public void checkEnemyProjectile(Projectile p) {
		// check if player hit
		if (player.intersects(p)) {
			// they collided, damage player
			player.damage();
			p.setHit();
		}
	}
	/*
	 * Update the in-game characters.
	 */
	public void updateCharacters() {
		for (int i = 0; i < characters.length; i++) {
			if (characters[i] != null) characters[i].update();
		}
	}
	/*
	 * Update interacives, and check for their respective interactions.
	 */
	public void checkInteractives() {
		for (int i = 0; i < interactives.size(); i++) {
			// get current interactive from the list
			Interactive in = interactives.get(i);
			// interact with any entity that can interact
			in.interact(player);
			for (Enemy e : enemies) {
				if (e instanceof Interactions) in.interact((Interactions) e);
			}
		}
	}
	/*
	 * Update collectibles, and check if they have been picked up.
	 */
	public void checkCollectibles() {
		for (int i = 0; i < collectibles.size(); i++) {
			// get current collectible from list
			Collectible c = collectibles.get(i);
			// check if player hitbox collides
			if (c.getRectangle().intersects(player.getRectangle())) {
				// they collided, pick up
				player.collect(c);
				c.setRemove();
			}
		}
	}
	/*
	 * Remove any entities that need to be removed.
	 */
	public void cleanUpEntities() {
		// clean up enemies
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).remove()) {
				enemies.remove(i);
				i--;
			}
		}
		// clean up projectiles
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).remove()) {
				projectiles.remove(i);
				i--;
			}
		}
		// clean up characters
		for (int i = 0; i < characters.length; i++) {
			if (characters[i] != null && characters[i].remove()) characters[i] = null;
		}
		// clean up interactives
		for (int i = 0; i < interactives.size(); i++) {
			if (interactives.get(i).remove()) {
				interactives.remove(i);
				i--;
			}
		}
		// clean up collectibles
		for (int i = 0; i < collectibles.size(); i++) {
			if (collectibles.get(i).remove()) {
				collectibles.remove(i);
				i--;
			}
		}
	}
	
	// RENDER METHODS
	// render all
	public void render() {
		renderInteractives();
		renderCollectibles();
		renderEnemies();
		renderCharacters();
		renderPlayer();
	}
	// render individual types
	public void renderPlayer() {
		player.render();
	}
	
	public void renderEnemies() {
		for (Enemy e : enemies) {
			e.render();
		}
	}
	
	public void renderCharacters() {
		for (Character c : characters) {
			if (c != null && !c.notOnScreen()) c.render();
		}
	}
	
	public void renderCollectibles() {
		for (Collectible c : collectibles) c.render();
	}
	
	public void renderInteractives() {
		for (Interactive i : interactives) i.render();
	}
	
	public void renderEntities() {
		for (Entity e : entities) e.render();
	}
}