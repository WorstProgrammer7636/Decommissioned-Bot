package events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CustomPrefix extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            check(event.getGuild().getIdLong());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        String[] split = event.getMessage().getContentRaw().split("\\s+");
        if ((split[0].equalsIgnoreCase(prefix + "changeprefix")) && (!event.getAuthor().isBot())) {
            if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {

                if (split.length == 1) {
                    event.getChannel().sendMessage(
                            "What would you like to change the prefix to? (This can be multiple characters long) Redo the command, and then type your new prefix behind it.")
                            .queue();

                } else if (split.length == 2) {

                    String newprefix = "";
                    newprefix = split[1];
                    try {

                        change(event.getGuild().getIdLong(), newprefix);
                        event.getChannel()
                                .sendMessage("Your server prefix has been changed from **" + prefix + "** to **"
                                        + newprefix
                                        + "**. \n You can always check your server's prefix by typing in 'prefix'!")
                                .queue();

                    } catch (NumberFormatException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    event.getChannel().sendMessage(
                            "Please make your server prefix one word/character! (Do you really want to make your custom prefix that long anyway??)")
                            .queue();
                }

            } else {
                event.getChannel().sendMessage(
                        "You do not have permission to use this command! You must have `Manage Server` to use this command!")
                        .queue();
            }
        } else if (split[0].equalsIgnoreCase("prefix"))

        {
            event.getChannel().sendMessage("Your server's prefix is '" + prefix + "'").queue();
        }
    }

    public void check(long id) throws IOException {
        ArrayList<Long> IDS = new ArrayList<Long>();
        ArrayList<String> prefixes = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/Prefixes"));
        StringTokenizer st = null;
        String line;
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);
            IDS.add(Long.parseLong(st.nextToken()));
            prefixes.add(st.nextToken());
        }
        if (IDS.contains(id)) {

        } else {
            IDS.add(id);
            prefixes.add("-");
        }
        br.close();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/Prefixes")));
        for (int i = 0; i < IDS.size(); i++) {
            pw.print(String.valueOf(IDS.get(i)));
            pw.print(" ");
            pw.print(String.valueOf(prefixes.get(i)));
            pw.println();

        }
        pw.close();
    }

    public void change(long id, String newprefix) throws NumberFormatException, IOException {
        ArrayList<Long> IDS = new ArrayList<Long>();
        ArrayList<String> prefixes = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/Prefixes"));
        StringTokenizer st = null;
        String line;
        boolean found = false;
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);
            IDS.add(Long.parseLong(st.nextToken()));
            if (IDS.contains(id) && found == false) {
                prefixes.add(newprefix);
                found = true;
            } else {
                prefixes.add(st.nextToken());

            }
        }

        br.close();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/Prefixes")));
        for (int i = 0; i < IDS.size(); i++) {
            pw.print(String.valueOf(IDS.get(i)));
            pw.print(" ");
            pw.print(String.valueOf(prefixes.get(i)));
            pw.println();

        }
        pw.close();
    }

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
}
