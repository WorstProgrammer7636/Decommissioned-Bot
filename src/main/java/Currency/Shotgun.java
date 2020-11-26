package Currency;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Shotgun extends ListenerAdapter {
    private HashMap<User, Integer> playerTimer = new HashMap<User, Integer>();
    private int o = 0;

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
        String[] split = event.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = event.getChannel();
        checkTimer();
        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());
        } catch (NumberFormatException | IOException f) {
            f.printStackTrace();
        }
        if (split[0].equalsIgnoreCase(prefix + "kill") && !event.getAuthor().isBot() && canKill(event.getAuthor())) {
            if (split.length == 2) {
                if (split[1].startsWith("<@")) {
                    User shooter = event.getAuthor();
                    User target = event.getMessage().getMentionedMembers().get(0).getUser();
                    long shooterID = shooter.getIdLong();
                    long targetID = target.getIdLong();
                    if (canKill(shooter)) {
                        try {
                            Kill(shooter, target, channel, shooterID, targetID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("The police are looking for you!");
                        eb.setDescription(
                                "You just murdered someone, so wait for the police to stop looking for you! Wait **"
                                        + playerTimer.get(shooter) + "** seconds to use your shotgun again!");

                        int R = (int) (Math.random() * 256);
                        int G = (int) (Math.random() * 256);
                        int B = (int) (Math.random() * 256);
                        Color color = new Color(R, G, B);
                        Random random = new Random();
                        final float hue = random.nextFloat();
                        final float saturation = 0.9f;
                        final float luminance = 1.0f;
                        color = Color.getHSBColor(hue, saturation, luminance);
                        eb.setColor(color);
                        event.getChannel().sendMessage(eb.build()).queue();
                    }
                } else {
                    channel.sendMessage("You have to MENTION someone you want to kill!").queue();
                }
            } else {
                channel.sendMessage("Run the command again, but this time mention someone to kill!").queue();
            }
        } else if (split[0].equalsIgnoreCase(prefix + "kill")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("The police are looking for you!");
            eb.setDescription("You just murdered someone, so wait for the police to stop looking for you! Wait **"
                    + playerTimer.get(event.getAuthor()) + "** seconds to use your shotgun again!");

            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);
            Color color = new Color(R, G, B);
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            color = Color.getHSBColor(hue, saturation, luminance);
            eb.setColor(color);
            event.getChannel().sendMessage(eb.build()).queue();
        }

    }

    public void Kill(User shooter, User target, TextChannel channel, long shooterID, long targetID) throws IOException {
        ArrayList<Long> MemberIDSshotgun = new ArrayList<Long>();
        ArrayList<Integer> MemberBullets = new ArrayList<Integer>();
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberMoney"));
        StringTokenizer st = null;
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            MemberIDS.add(x);
            MemberMoney.add(z);
        }

        myReader.close();
        try {
            BufferedReader shotgunreader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/ShotgunRounds"));
            try {
                while ((line = shotgunreader.readLine()) != null) {
                    st = new StringTokenizer(line);

                    long x = Long.parseLong(st.nextToken());
                    int z = Integer.parseInt(st.nextToken());
                    MemberIDSshotgun.add(x);
                    MemberBullets.add(z);
                }
                shotgunreader.close();
                if (MemberIDSshotgun.contains(shooterID)) {
                    for (int l = 0; l < MemberIDSshotgun.size(); l++) {
                        if (MemberIDSshotgun.get(l) == shooterID && MemberBullets.get(l) > 0) {
                            if (MemberIDS.contains(targetID)) {
                                for (int m = 0; m < MemberIDS.size(); m++) {
                                    if (MemberIDS.get(m) == targetID) {
                                        Random chancetokill = new Random();
                                        int chance = chancetokill.nextInt(4);
                                        chance += 1;
                                        if (chance > MemberBullets.get(l)) {
                                            for (int n = 0; n < MemberIDS.size(); n++) {
                                                if (MemberIDS.get(n) == shooterID) {
                                                    int profit = MemberMoney.get(n) / 2;
                                                    MemberBullets.set(l, 0);

                                                    MemberMoney.set(n, MemberMoney.get(n) / 2);
                                                    MemberMoney.set(m, MemberMoney.get(m) + profit);
                                                    channel.sendMessage(
                                                            "Nice aim, you used **all** your bullets and the player you tried to kill still escaped and called the police! You were nearly killed by police, losing **"
                                                                    + profit
                                                                    + "** coins after that massive fail! The police gave this money to the victim!")
                                                            .queue();
                                                    setPlayerTime(shooter, 120);
                                                }
                                            }

                                        } else {

                                            channel.sendMessage("You stole **" + MemberMoney.get(m) / 2
                                                    + "** coins after killing " + target.getName()
                                                    + ", for a net profit of **"
                                                    + ((MemberMoney.get(m) / 2) - 1000 * chance
                                                    + "** (Shotgun Rounds cost 1000 coins each). You used **"
                                                    + chance + "** bullet(s) to secure the kill."))
                                                    .queue();
                                            setPlayerTime(shooter, 120);
                                            MemberBullets.set(l, MemberBullets.get(l) - chance);

                                            int profit = MemberMoney.get(m) / 2;
                                            MemberMoney.set(m, (MemberMoney.get(m) * 1) / 2);
                                            for (int n = 0; n < MemberIDS.size(); n++) {
                                                if (MemberIDS.get(n) == shooterID) {
                                                    MemberMoney.set(n, MemberMoney.get(n) + profit);
                                                }
                                            }

                                        }
                                    }
                                }
                            } else {
                                channel.sendMessage("This person doesn't even use the currency system!").queue();
                            }
                        } else if (MemberIDSshotgun.get(l) == shooterID && MemberBullets.get(l) > 0) {
                            channel.sendMessage("You don't have any bullets! Run -shop to buy some!").queue();

                        }
                    }
                } else {
                    MemberIDSshotgun.add(shooterID);
                    MemberBullets.add(0);
                    channel.sendMessage("You don't have any bullets! Run -shop to buy some!").queue();
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberMoney")));

        for (int i = 0; i < MemberIDS.size(); i++) {

            String value = String.valueOf(MemberIDS.get(i));
            String money = String.valueOf(MemberMoney.get(i));

            myWriter.print(value);
            myWriter.print(" ");
            myWriter.print(money);
            myWriter.println();

        }
        myWriter.close();
        PrintWriter shotGunWriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/ShotgunRounds")));

        for (int i = 0; i < MemberIDSshotgun.size(); i++) {

            String value = String.valueOf(MemberIDSshotgun.get(i));
            String money = String.valueOf(MemberBullets.get(i));

            shotGunWriter.print(value);
            shotGunWriter.print(" ");
            shotGunWriter.print(money);
            shotGunWriter.println();

        }
        shotGunWriter.close();
    }

    public void checkTimer() {
        if (o == 0) {
            o = 1;
            startTimer();
        } else {

        }
    }

    public void startTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<User> remove = new ArrayList<User>();

                for (User member : playerTimer.keySet()) {
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

    private int getPlayerTime(User member) {

        return playerTimer.get(member);
    }

    protected void setPlayerTime(User member, int num) {
        playerTimer.put(member, num);
    }

    protected boolean canKill(User member) {
        return !playerTimer.containsKey(member);
    }
}
