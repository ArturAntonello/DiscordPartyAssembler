package listeners;

import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

public class MyReactionAddListener implements ReactionAddListener {
    @Override
    public void onReactionAdd(ReactionAddEvent event) {
        if (event.getEmoji().equalsEmoji("\u2705")){
            addUserToSquad(event.getUserId(), event);
        }
        else if(event.getEmoji().equalsEmoji("\u274C")){
            removeUserFromSquad(event.getUserId(), event);
        }
    }

    public void addUserToSquad(long reactionUser, ReactionAddEvent event)
    {
            //check if the squad has any open slot
            String fullMsg = event.getMessageContent().toString();
            int openSlotIndex = fullMsg.indexOf("<OPEN>");
            //user is not already on the squad
            if (openSlotIndex != -1 &&
                    reactionUser != 795209682368331797L &&
                    !fullMsg.contains(Long.toString(reactionUser))) {
                String messageStart = fullMsg.substring(0, openSlotIndex).replace("Optional[", "");
                String messageEnd = fullMsg.substring(openSlotIndex + 6, fullMsg.length() - 1);
                event.editMessage(messageStart + "<@" + reactionUser + ">" + messageEnd);
            }
    }

    public void removeUserFromSquad(long reactionUser, ReactionAddEvent event)
    {
        String fullMsg = event.getMessageContent().toString();
        fullMsg = fullMsg.replace("Optional[", "")
                .replace("<@"+reactionUser+">","<OPEN>");
        if (fullMsg.length()-2 > 0){
           fullMsg = fullMsg.substring(0,fullMsg.length() -1);
        }
        event.editMessage(fullMsg);
        //.substring(0,fullMsg.length() -2)
    }
}
