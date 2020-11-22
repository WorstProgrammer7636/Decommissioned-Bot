package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class Avatar extends ListenerAdapter {
    public String prefix(long id) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/Prefixes"));
        StringTokenizer st = null;
        String line;
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);
            if (id == Long.parseLong(st.nextToken())) {
                br.close();
                String prefix = st.nextToken();

                return prefix;
            }
        }
        br.close();
        return "ERROR";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        String prefix = "";
        try {
            prefix = prefix(e.getGuild().getIdLong());
        } catch (NumberFormatException | IOException f) {
            f.printStackTrace();
        }
        if (e.getMessage().getContentRaw().startsWith(prefix + "av")){
            List<Member> mentionedMembers = e.getMessage().getMentionedMembers();
            if (mentionedMembers.isEmpty()){
                e.getChannel().sendMessage(createEmbed(e.getAuthor())).queue();
            } else {
                e.getChannel().sendMessage(createEmbed(mentionedMembers.get(0).getUser())).queue();
            }
        }

    }

    public MessageEmbed createEmbed(User user){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setImage(user.getAvatarUrl());
        return builder.build();
    }
}
