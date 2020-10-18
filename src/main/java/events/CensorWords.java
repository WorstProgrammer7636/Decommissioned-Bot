package events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class CensorWords extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String message = event.getMessage().getContentRaw();


        String[] combine = message.split(" ");
        String newstring = "";
        for(int i = 0; i < combine.length; i++){
            newstring += combine[i];
        }

        //Work on duplicate char case later
        if (newstring.contains("nigger") || newstring.contains("nigga")) {
            event.getChannel().sendMessage("yo dude don't say nasty words @" + event.getMember().getEffectiveName()).queue();
            event.getMessage().delete().queue();
        }
        else if (newstring.contains("chingchong") ) {
            event.getChannel().sendMessage("yo dude don't say nasty words @" + event.getMember().getEffectiveName()).queue();
            event.getMessage().delete().queue();
        }

    }
}

