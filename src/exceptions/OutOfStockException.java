package exceptions;

public class OutOfStockException extends Exception{
    public OutOfStockException(String mensaje) {
        super(mensaje);
    }
}