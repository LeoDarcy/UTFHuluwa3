package MyThread;

public enum StateType {
    STAY("stay"), MOVING("move"),
    MOVE("moving"), ATTACK("attacking"),
    DYING("dying"), DEAD("dead");
    private String name;

    StateType(String nam) {
        name = nam;
    }
}
