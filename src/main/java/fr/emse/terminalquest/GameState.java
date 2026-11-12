package fr.emse.terminalquest;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** Mutable state of one adventure; Jackson serializes this object into JSON. */
public class GameState {
    private int health = 50;
    private int potions = 3;
    private int gold = 0;
    private int roomsCleared = 0;
    private boolean won = false;
    private Monster monster;

    public GameState() { }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getPotions() { return potions; }
    public void setPotions(int potions) { this.potions = potions; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public int getRoomsCleared() { return roomsCleared; }
    public void setRoomsCleared(int roomsCleared) { this.roomsCleared = roomsCleared; }
    public boolean isWon() { return won; }
    public void setWon(boolean won) { this.won = won; }
    public Monster getMonster() { return monster; }
    public void setMonster(Monster monster) { this.monster = monster; }
    @JsonIgnore // Derived gameplay status: not a separate field in the JSON save.
    public boolean isDead() { return health <= 0; }
}
