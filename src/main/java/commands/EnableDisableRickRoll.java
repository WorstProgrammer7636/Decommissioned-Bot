package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EnableDisableRickRoll extends ListenerAdapter {
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
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());
        } catch (NumberFormatException | IOException f) {
            f.printStackTrace();
        }
        long ID = event.getGuild().getIdLong();
        if (event.getMessage().getContentRaw().startsWith(prefix + "toggle")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)){
                event.getChannel().sendMessage("Sorry! You do not have permission to perform this command.").queue();
                return;
            }

            try {
                enable(ID, event.getChannel());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enable(long ID, TextChannel channel) throws IOException {
        ArrayList<Long> GuildIDS = new ArrayList<Long>();
        ArrayList<Boolean> Enable = new ArrayList<Boolean>();
        BufferedReader myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/enable.in"));
        StringTokenizer st = null;
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            GuildIDS.add(x);
            boolean y = Boolean.parseBoolean(st.nextToken());
            Enable.add(y);

        }
        myReader.close();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/enable.in")));
        for(int i=0; i<GuildIDS.size();i++) {
            if(GuildIDS.get(i) == ID) {
                if(Enable.get(i) == false) {
                    Enable.set(i, true);
                    channel.sendMessage("Rickroll enabled!").queue();
                } else {
                    Enable.set(i, false);
                    channel.sendMessage("Rickroll disabled!").queue();;
                }
            }
            pw.println(GuildIDS.get(i) + " " + Enable.get(i));
        }
        pw.close();
    }

}
