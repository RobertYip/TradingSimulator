package exceptions;

// Exception for insufficient quantity in portfolio
public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException() {
        System.out.println("Insufficient Quantity in Portfolio Exception");
    }
}
