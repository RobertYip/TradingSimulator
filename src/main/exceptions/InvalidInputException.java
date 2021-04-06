package exceptions;

// Exception for invalid quantity value
public class InvalidInputException extends Exception {
    public InvalidInputException() {
        System.out.println("Invalid price or quantity input exception");
    }
}
