package fr.emse.terminalquest;

/** A monster that persists as part of a saved game. */
public class Monster {
    private String name;
    private int health;
    private int attackPower;
    private int goldReward;
    private boolean boss;

    public Monster() { // Needed by Jackson when loading JSON.
    }

    public Monster(String name, int health, int attackPower, int goldReward, boolean boss) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.goldReward = goldReward;
        this.boss = boss;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getAttackPower() { return attackPower; }
    public void setAttackPower(int attackPower) { this.attackPower = attackPower; }
    public int getGoldReward() { return goldReward; }
    public void setGoldReward(int goldReward) { this.goldReward = goldReward; }
    public boolean isBoss() { return boss; }
    public void setBoss(boolean boss) { this.boss = boss; }
}
