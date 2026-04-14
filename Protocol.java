public class Protocol {
  
    //the join message formatting
    public static String JoinMsg(String clientName)
    {
        return clientName + "|"+ "Join";
    }

    //the welcome message formatting
    public static String AckMsg(String clientName)
    {
        return "ACK" + "|" + "Welcome" + clientName;
    }

    //the calculation request message formatting
    public static String CalcRequestMsg(String Expression)
    {
        return "CalcRequest" + "|" + Expression;
    }

    //the calculation response message formatting
    public static String CalcSolutionMsg(String Expression,String Answer)
    {
        return "CalcSolution" + "|" + Expression + "=" + Answer;
    }
    
    //the disconnect message formatting
    public static String DisconnectMsg(String ClientName)
    {
        return "Disconnect" + "|" + "See you later" + ClientName + "!";
    }


    //the error message formatting
    public static String ErrorMsg(String Reason)
    {
        return "Error" + "|" + Reason;
    }

    //parses a raw message input to a readable format
     public static String[] parse(String raw) {                                                                                                                                                                    
          int idx = raw.indexOf("|");                                                                                                                                                                               
          if (idx == -1) {
              return new String[]{ "Error", raw };                                                                                                                                                                      
          }                                                                                                                                                                                                         
          String typeOf    = raw.substring(0, idx);
          String payload = raw.substring(idx + 1);                                                                                                                                                                    
          return new String[]{ typeOf, payload };
      }

         // get just the type from a raw message                                                                                                                                                                     
      public static String getType(String raw) {
          return parse(raw)[0];                                                                                                                                                                                       
      }                                                                                                                                                                                                             

      // get just the payload from a raw message
      public static String getPayload(String raw) {
          return parse(raw)[1];                                                                                                                                                                                       
      }                                                                                                                                                                                                               
         
}
