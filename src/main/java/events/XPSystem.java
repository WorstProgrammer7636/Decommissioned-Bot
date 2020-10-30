package events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class XPSystem extends ListenerAdapter {
    private HashMap<Member, Integer> playerTimer = new HashMap<>();
    private int i = 0;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        long member = event.getMember().getIdLong();
        long guild = event.getGuild().getIdLong();
        Member org = event.getMember();

        if (event.getMessage().getContentRaw().startsWith("-") || event.getAuthor().isBot() || !canGetXp(org)) {
        } else {
            try {
                checkadd(member, guild, org);
                setPlayerTime(org, 3);
                checkTimer();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getMessage().getContentRaw().startsWith("-xp")) {
            try {
                getXP(guild, member, event.getChannel());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkadd(long member, long guild, Member org) throws IOException {
        ArrayList<Long> GuildIDS = new ArrayList<Long>();
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberXP = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("MemberXP"));
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

        myReader.close();

        if (GuildIDS.contains(guild)) {
            Random random = new Random();
            boolean found = false;
            for (int i = 0; i < GuildIDS.size(); i++) {
                if (GuildIDS.get(i) == guild) {
                    if (MemberIDS.get(i) == member) {

                        MemberXP.set(i, MemberXP.get(i) + random.nextInt(15));

                        found = true;
                        break;

                    }
                }
            }
            if (found == false) {
                GuildIDS.add(guild);
                MemberIDS.add(member);
                MemberXP.add(0);
            }
        } else {
            GuildIDS.add(guild);
            MemberIDS.add(member);
            MemberXP.add(0);

        }

        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("MemberXP")));

        for (int i = 0; i < GuildIDS.size(); i++) {

            String key = String.valueOf(GuildIDS.get(i));
            String value = String.valueOf(MemberIDS.get(i));
            String xp = String.valueOf(MemberXP.get(i));

            myWriter.print(key);
            myWriter.print(" ");
            myWriter.print(value);
            myWriter.print(" ");
            myWriter.print(xp);
            myWriter.println();

        }
        myWriter.close();
    }

    private void getXP(long guild, long member, TextChannel channel) throws IOException {
        ArrayList<Long> GuildIDS = new ArrayList<Long>();
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberXP = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("MemberXP"));
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

        myReader.close();

        if (GuildIDS.contains(guild)) {
            boolean found = false;
            for (int i = 0; i < GuildIDS.size(); i++) {
                if (GuildIDS.get(i) == guild) {
                    if (MemberIDS.get(i) == member) {
                        found = true;
                        int level = MemberXP.get(i) / 100;
                        int xp = MemberXP.get(i) % 100;
                        int bluesquares = xp / 10;
                        int whitesquares = 10 - bluesquares;
                        String blue = "";
                        String white = "";
                        for (int j = 0; j < bluesquares; j++) {
                            blue += " :blue_square:";
                        }
                        for (int j = 0; j < whitesquares; j++) {
                            white += " :white_large_square:";
                        }
                        channel.sendMessage("You are Level " + level + ": \n " + xp + "/100 xp: \n" + blue + white)
                                .queue();
                        break;
                    }
                }
            }
            if (found == false) {
                channel.sendMessage("Error: User Not Found!").queue();
            }
        } else {
            channel.sendMessage("Error: User Not Found!").queue();

        }
    }

    public void checkTimer() {
        if (i == 0) {
            i = 1;
            startTimer();
        } else {

        }
    }

    public void startTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<Member> remove = new ArrayList<Member>();
                for (Member member : playerTimer.keySet()) {
                    setPlayerTime(member, getPlayerTime(member) - 1);
                    if (getPlayerTime(member) == 0) {
                        remove.add(member);
                    }

                }
                for (int i = 0; i < remove.size(); i++) {
                    playerTimer.remove(remove.get(i));
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private int getPlayerTime(Member member) {

        return playerTimer.get(member);
    }

    protected void setPlayerTime(Member member, int num) {
        playerTimer.put(member, num);
    }

    protected boolean canGetXp(Member member) {
        return !playerTimer.containsKey(member);
    }
}
