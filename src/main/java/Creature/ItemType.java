package Creature;

public enum ItemType {
    Rock("rock"),
    Huluwa("huluwa"), Grandpa("Grandpa"),
    BadGuy("badguy"), Snake("snake"),
    Background("background");
    private String name;
    ItemType(String nam) {
        name = nam;
    }
}