import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Terminate extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("-terminate")
                && (event.getMember().getIdLong() == Long.parseLong("388905161566978049")
                || event.getMember().getIdLong() == Long.parseLong("499053898439458836"))) {
            JDA bot = event.getJDA();

            long sendGuild = Long.parseLong("770826745887850516");
            event.getChannel().sendMessage("Bot has been terminated!").queue();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            bot.getGuildById(sendGuild).getTextChannelsByName("bot-status", true).get(0)
                    .sendMessage("Bot is off! Bot was turned off at " + dtf.format(now)).queue();
            startTimer();
        } else if (event.getMessage().getContentRaw().equals("-terminate")) {
            event.getChannel().sendMessage("You can't do that! You aren't an Inutile developer.").queue();
        }
    }

    public void startTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        timer.schedule(task, 1000, 1000);
    }
}