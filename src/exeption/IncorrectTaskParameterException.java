package exeption;

public class IncorrectTaskParameterException extends Exception{

    private final String parameter;

    public IncorrectTaskParameterException(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getMessage() {
        return "Parameter \""+ parameter+ "\" is incorrect";
    }
}
