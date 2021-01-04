package listeners;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class MyListener implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String msg = event.getMessageContent();
        if (event.getMessageAuthor().isBotUser()) {
            if(msg.startsWith("Activity: ")){
                event.getMessage().addReaction("\u2705");
                event.getMessage().addReaction("\u274C");
                //if anyone reacts to check
            }
        }
        else {
        if (msg.equalsIgnoreCase("!assemble")) {
            event.getChannel().sendMessage("How to use:" +
                    "\n!assemble " +
                    "\"<Title>\" " +
                    "\"<DateTime(DD/MM-hh:mm)>\" " +
                    "\"<Observation(Optional)>\" " +
                    "\"<Party Size(2-12)>\" " +
                    "\"<Reserves(0-4)>\"" +
                    "\nExample:" +
                    "\n!assemble \"Raid: Garden of Salvation\" \"05/01-20:00\" \"\" \"6\" \"2\" ");
        } else if (msg.startsWith("!assemble ")) {
            int counter = 0;
            char verify = '\"';

            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) == verify) {
                    counter++;
                }
            }
            if (counter == 10) {
                boolean result = separateArguments(event.getMessageContent(), event);
                if (!result) {
                    event.getChannel().sendMessage("The party could not be assembled correctly." +
                            "\nPlease check if the values were filled correctly.");
                }
            } else {
                event.getChannel().sendMessage("Incorrect number of arguments." +
                        "\nPlease check the command !assemble for the correct syntax.");
            }
        }

        MyReactionAddListener myReactionAddListener = new MyReactionAddListener();
            event.getMessage().addReactionAddListener(myReactionAddListener);
    }
    }

    public boolean separateArguments(String fullMsg, MessageCreateEvent event) throws NumberFormatException {
        try{

            int firstIndex, secondIndex;
            firstIndex = fullMsg.indexOf("\"");
            secondIndex = fullMsg.indexOf("\"", firstIndex + 1);
            String title = fullMsg.substring(firstIndex + 1, secondIndex);

            firstIndex = secondIndex + 2;
            secondIndex = fullMsg.indexOf("\"", firstIndex + 1);
            String dateTime = fullMsg.substring(firstIndex + 1, secondIndex);

            firstIndex = secondIndex + 2;
            secondIndex = fullMsg.indexOf("\"", firstIndex + 1);
            String observation = fullMsg.substring(firstIndex + 1, secondIndex);

            firstIndex = secondIndex + 2;
            secondIndex = fullMsg.indexOf("\"", firstIndex + 1);
            int partySize = Integer.parseInt(fullMsg.substring(firstIndex + 1, secondIndex));

            firstIndex = secondIndex + 2;
            secondIndex = fullMsg.indexOf("\"", firstIndex + 1);
            int reserves = Integer.parseInt(fullMsg.substring(firstIndex + 1, secondIndex));

            if(argumentsAreValid(title, dateTime, observation, partySize, reserves)){
            assemblePartyMessage(title, dateTime, observation, partySize, reserves, event);
            }
            return true;

        }

        catch(NumberFormatException ex)
        {
            return false;
        }
    }

    public boolean argumentsAreValid(String title,
                                  String dateTime,
                                  String observation,
                                  int partySize,
                                  int reserves)
    {
        if (title != "" &&
                (partySize >=2 && partySize<= 12) &&
                (reserves<=4 && reserves >=0)){return true;}
        return false;
    }

    public static void assemblePartyMessage(String title,
                                            String dateTime,
                                            String observation,
                                            int partySize,
                                            int reserves,
                                            MessageCreateEvent event)
    {
        MessageBuilder finalMsg = new MessageBuilder();
        finalMsg.append("Activity: "+title)
                .append("\nDate/Time: "+dateTime)
                .append("\n"+observation+"\nSquad:\n")
                .append("1 - <@" + event.getMessageAuthor().getId()+">\n");
        for(int i = 2; i <= partySize; i++)
        {
            finalMsg.append(i+" - <OPEN>\n");
        }
        if(reserves>0 && reserves<=4){
            finalMsg.append("Reserves:\n");
            for(int j = 1; j <= reserves; j++)
            {
                finalMsg.append(j+" - <OPEN>\n");
            }
        }
        finalMsg.send(event.getChannel());
    }
}
