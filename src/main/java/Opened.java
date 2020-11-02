import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Opened extends ListenerAdapter {
    int i = 0;
    long sendChannel = Long.parseLong("772197435644444672");
    long sendGuild = Long.parseLong("770826745887850516");

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        JDA bot = event.getJDA();
        if (i == 0) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            bot.getGuildById(sendGuild).getTextChannelsByName("bot-status", true).get(0)
                    .sendMessage("Bot is on! Bot was turned on at " + dtf.format(now)).queue();
            i++;
        }
    }
}