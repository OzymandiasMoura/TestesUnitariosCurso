package curso.testesunitarios2.services.exceptions;

public class UserNotFound extends RuntimeException
{
    public UserNotFound(String message)
    {
        super(message);
    }
}
