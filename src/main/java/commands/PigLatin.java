package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;
import java.util.ArrayList;
public class PigLatin extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event){
        String orgmessage = event.getMessage().getContentRaw();
        String after = orgmessage.trim().replaceAll(" +", " ");

        String[] message = after.split(" ");
        if (event.getAuthor().isBot()){
            return;
        }
        if (message[0].equalsIgnoreCase("-piglatin")){
            String newMessage = "";
            String sentence = "";
            try {
                for (int i = 1; i < message.length; i++){
                    newMessage = message[i].substring(1, message[i].length());
                    newMessage += message[i].substring(0, 1);
                    newMessage += "ay";

                    sentence += newMessage += " ";
                    newMessage = "";
                }
                event.getChannel().sendMessage(sentence).queue();

            } catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e)
            {
                event.getChannel().sendMessage("This is the piglatin command! " +
                        "Enter any word after the command in this format: -piglatin [word or sentence]and the bot will" +
                        " return the phrase you requested in pig latin!").queue();;

            }


        }

    }

}

