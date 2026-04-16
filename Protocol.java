public class Protocol {
  
    //the join message formatting
    public static String JoinMsg(String name)
    {
        return "JOIN|" + name;
    }

    //the welcome message formatting
    public static String AckMsg(String name)
    {
        return "ACK|Welcome " + name;
    }

    //the calculation request message formatting
    public static String CalcRequestMsg(String op, String num1, String num2)
    {
        return "CALC|" + op + "|" + num1 + "|" + num2;
    }

    //the calculation response message formatting
    public static String CalcSolutionMsg(String value)
    {
        return "RESULT|" + value;
    }
    
    //the disconnect message formatting
    public static String DisconnectMsg(String name)
    {
        return "BYE|Goodbye " + name;
    }

    //the error message formatting
    public static String ErrorMsg(String reason)
    {
        return "ERROR|" + reason;
    }

    //parses a raw message input to a readable format
    public static String getType(String message) {
        return message.split("\\|")[0];
    }

    public static String[] getParts(String message) {
        return message.split("\\|");
    }
}