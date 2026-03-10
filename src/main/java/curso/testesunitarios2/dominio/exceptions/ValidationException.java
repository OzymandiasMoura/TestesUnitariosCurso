package curso.testesunitarios2.dominio.exceptions;

public class ValidationException extends RuntimeException
{
    public ValidationException(String message)
    {
        super(message);
    }
}
