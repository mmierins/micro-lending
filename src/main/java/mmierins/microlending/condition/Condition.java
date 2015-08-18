package mmierins.microlending.condition;

public interface Condition<T> {

    boolean verify(T data);
    String getMessage();
    int getCode();

}
