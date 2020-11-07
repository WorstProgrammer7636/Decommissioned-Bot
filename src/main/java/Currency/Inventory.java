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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Inventory extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] split = event.getMessage().getContentRaw().split("\\s+");
        if (split[0].equalsIgnoreCase("-inv") && !event.getAuthor().isBot()) {
            Guild guild = event.getGuild();
            int length = split.length;
            if (length >= 2) {
                boolean found = false;
                String name = "";
                for (int j = 1; j < length; j++) {
                    name += split[j];
                    if (j < length - 1) {
                        name += " ";
                    }
                }
                int size = event.getGuild().getMembersByEffectiveName(name, true).size();
                found = false;
                for (int i = 0; i < size; i++) {

                    found = true;
                    try {
                        getInventory(guild, event.getGuild().getMembersByEffectiveName(name, true).get(0).getIdLong(),
                                event.getChannel(), event.getGuild().getMembersByEffectiveName(name, true).get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (found == false) {
                    event.getChannel()
                            .sendMessage("Please type in the nickname of someone in the server! (Don't ping!)").queue();
                }

            } else {
                try {
                    long member = event.getMember().getIdLong();

                    getInventory(guild, member, event.getChannel(), event.getMember());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getInventory(Guild guild, long memberID, TextChannel channel, Member member) throws IOException {
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> bullets = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("ShotgunRounds"));
        StringTokenizer st = null;
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            if (z == 0) {

            } else {
                MemberIDS.add(x);
                bullets.add(z);
            }
        }

        myReader.close();

        boolean found = false;
        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == memberID) {
                found = true;

                EmbedBuilder XP = new EmbedBuilder();
                XP.setAuthor(member.getEffectiveName() + "'s Inventory", null, member.getUser().getAvatarUrl());
                int amount = bullets.get(i);

                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                Color color = new Color(R, G, B); // random color, but can be bright or dull

                Random random = new Random();
                final float hue = random.nextFloat();
                final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
                final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
                color = Color.getHSBColor(hue, saturation, luminance);
                XP.addField("Inventory Information:", "Shotgun: **" + amount + "** bullets.", false);
                XP.setColor(color);
                channel.sendMessage(XP.build()).queue();
                break;
            }

        }
        if (found == false) {
            channel.sendMessage("Error: User Does Not Have Anything In Their Inventory!").queue();
        }
    }
}