package Currency;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class XPLeaderboard extends ListenerAdapter {
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
        long guild = event.getGuild().getIdLong();
        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        if (event.getMessage().getContentRaw().equalsIgnoreCase(prefix + "leaderboard")) {
            try {
                buildleader(event.getChannel(), guild, event.getGuild());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void buildleader(TextChannel channel, long guild, Guild orgguild) throws IOException {
        ArrayList<Long> GuildIDS = new ArrayList<Long>();
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberXP = new ArrayList<Integer>();

        ArrayList<String> GuildMembers = new ArrayList<String>();
        ArrayList<Integer> Value = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberXP"));
        StringTokenizer st = null;
        String line;

        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            long y = Long.parseLong(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            GuildIDS.add(x);
            MemberIDS.add(y);
            MemberXP.add(z);

        }
        if (GuildIDS.contains(guild)) {
            for (int i = 0; i < GuildIDS.size(); i++) {
                if (GuildIDS.get(i) == guild) {
                    boolean found = false;

                    for (int j = 0; j < orgguild.getMemberCount(); j++) {
                        if (orgguild.getMembers().get(j).getIdLong() == MemberIDS.get(i)) {
                            found = true;
                            if (GuildMembers.size() == 0) {
                                GuildMembers.add(orgguild.getMembers().get(j).getEffectiveName());
                                Value.add(MemberXP.get(i));
                            } else {
                                boolean big = false;
                                for (int k = 0; k < GuildMembers.size(); k++) {
                                    if (MemberXP.get(i) > Value.get(k)) {
                                        GuildMembers.add(k, orgguild.getMembers().get(j).getEffectiveName());
                                        Value.add(k, MemberXP.get(i));
                                        big = true;
                                        break;
                                    }
                                }
                                if (big == false) {
                                    GuildMembers.add(orgguild.getMembers().get(j).getEffectiveName());
                                    Value.add(MemberXP.get(i));
                                }
                            }
                        }
                    }
                    if (found == false) {
                        boolean big = false;
                        for (int k = 0; k < GuildMembers.size(); k++) {
                            if (MemberXP.get(i) > Value.get(k)) {
                                GuildMembers.add(k, null);
                                Value.add(k, MemberXP.get(i));
                                big = true;
                                break;
                            }
                        }
                        if (big == false) {
                            GuildMembers.add(null);
                            Value.add(MemberXP.get(i));
                        }
                    }
                }
            }
            String leaderboard = "";
            for (int i = 0; i < GuildMembers.size() && i < 10; i++) {
                if (i == 0) {
                    leaderboard += ":star2: **";

                } else if (i == 1) {
                    leaderboard += ":gift_heart: **";
                } else if (i == 2) {
                    leaderboard += ":gift: **";
                } else {
                    leaderboard += ":christmas_tree: **";

                }
                try {
                    leaderboard += GuildMembers.get(i);

                } catch (NullPointerException e) {
                    leaderboard += ":x:";
                }
                leaderboard += "** with **";
                if (guild == Long.parseLong("770826745887850516")
                        && (GuildMembers.get(i).equals("Nard") || GuildMembers.get(i).equals("hold up"))) {
                    leaderboard += "Infinite";
                } else {

                    leaderboard += Value.get(i);
                }
                leaderboard += "** XP";

                leaderboard += "\n";
            }
            EmbedBuilder build = new EmbedBuilder();
            build.setTitle("Leaderboard");
            build.addField("Top XP Users:", leaderboard, false);
            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);
            Color color = new Color(R, G, B); // random color, but can be bright or dull

            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
            color = Color.getHSBColor(hue, saturation, luminance);
            build.setColor(color);
            build.setFooter(
                    "If you see a X on the leaderboard, that means this person has left the server. \n You will not be able to see who it is.");
            channel.sendMessage(build.build()).queue();
        }

        myReader.close();
    }
}

