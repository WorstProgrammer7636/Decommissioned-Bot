package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RickRoll extends ListenerAdapter {
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

        Message message = event.getMessage();
        String content = message.getContentRaw();
        String[] spliced = content.split("\\s+");
        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        try {
            EnableDisable(event.getGuild().getIdLong());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (spliced[0].equalsIgnoreCase(prefix + "rickroll") && spliced.length == 1) {
            try {
                if (check(event.getGuild().getIdLong())) {
                    event.getChannel().sendMessage("Who do you want to send the video to?").queue();
                    event.getChannel().sendMessage(
                            "Type in this format: -rickroll [@user]. You have to do this if you want to send the video to yourself.")
                            .queue();
                } else {
                    event.getChannel().sendMessage("This command is unavailable because a server administrator has disabled it. Please contact " +
                            "them if you think this is a mistake").queue();
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        } else if (spliced[0].equalsIgnoreCase(prefix + "rickroll") && spliced.length == 2
                && (spliced[1].equalsIgnoreCase("@everyone") || spliced[1].equalsIgnoreCase("@here"))) {
            try {
                if (check(event.getGuild().getIdLong())) {

                    event.getChannel().sendMessage(
                            "Please choose a specific person to send the video to. I don't wanna ping everyone.")
                            .queue();
                } else {
                    event.getChannel().sendMessage("This command is unavailable because a server administrator has disabled it. Please contact " +
                            "them if you think this is a mistake").queue();
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        } else if (spliced[0].equalsIgnoreCase(prefix + "rickroll") && spliced.length == 2 && spliced[1].startsWith("<@")) {
            List<Member> mentionedMembers = message.getMentionedMembers();
            try {
                if (check(event.getGuild().getIdLong())) {
                    for (Member m : mentionedMembers) {
                        try {
                            Member me = mentionedMembers.get(0);
                            me.getUser().openPrivateChannel().complete().sendMessage(
                                    "-Unlisted- Inutile Developer Face Reveal Video: \n <https://www.youtube.com/watch?v=NfSGm9DDQ3o>")
                                    .queue();
                        } catch (UnsupportedOperationException e) {
                            event.getChannel().sendMessage("Don't even try").queue();
                        }
                    }
                } else {
                    event.getChannel().sendMessage("This command is unavailable because a server administrator has disabled it. Please contact " +
                            "them if you think this is a mistake").queue();
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void EnableDisable(long GuildID) throws IOException {
        ArrayList<Long> GuildIDS = new ArrayList<Long>();
        ArrayList<Boolean> Enable = new ArrayList<Boolean>();

        BufferedReader myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/enable.in"));
        StringTokenizer st = null;
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            boolean y = Boolean.parseBoolean(st.nextToken());
            GuildIDS.add(x);
            Enable.add(y);

        }

        myReader.close();

        if (GuildIDS.contains(GuildID)) {

        } else {
            GuildIDS.add(GuildID);
            Enable.add(true);
        }

        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/enable.in")));

        for (int i = 0; i < Enable.size(); i++) {

            String key = String.valueOf(GuildIDS.get(i));
            String value = String.valueOf(Enable.get(i));

            myWriter.print(key);
            myWriter.print(" ");
            myWriter.print(value);
            myWriter.println();

        }
        myWriter.close();
    }

    private boolean check(long ID) throws NumberFormatException, IOException {

        BufferedReader myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/enable.in"));
        StringTokenizer st = null;
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            boolean y = Boolean.parseBoolean(st.nextToken());
            if (x == ID) {
                myReader.close();

                return y;
            }

        }
        myReader.close();
        return true;

    }
}