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
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Shop extends ListenerAdapter {
    EventWaiter waiter = new EventWaiter();

    public Shop(EventWaiter waiter) {
        this.waiter = waiter;
    }

    private HashMap<User, Integer> shopTimer = new HashMap<>();
    private int n = 0;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User org = null;
        EmbedBuilder eb = new EmbedBuilder();
        try {
            org = event.getMember().getUser();
        } catch (NullPointerException e) {

        }

        checkshopTimer();
        if (event.getMessage().getContentRaw().equalsIgnoreCase("-shop") && !org.isBot()) {
            if (!canGetShop(org) && event.getMessage().getContentRaw().equalsIgnoreCase("-shop")) {
                eb.setTitle("Processing...");
                eb.setDescription("The shopkeeper is still processing your last transaction. Wait **"
                        + shopTimer.get(org) + "** seconds.");

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
            } else {
                eb.setTitle("Shop");
                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                Color color = new Color(R, G, B);
                Random random = new Random();
                final float hue = random.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                color = Color.getHSBColor(hue, saturation, luminance);
                eb.addField("Consumables:",
                        "Coffee (500 coins) :coffee: \n **Items:** \n Shotgun Round (1000 coins) :gun: \n *More Items Coming Soon* \n",
                        false);
                eb.setFooter("You have ten seconds to pick an item! Simply type in an object's name to get it!");
                eb.setColor(color);
                event.getChannel().sendMessage(eb.build()).queue();
                setPlayerShopTime(org, 15);

                checkshopTimer();

                shop(event.getChannel().getIdLong(), event.getMember().getIdLong(), event.getJDA().getShardManager(),
                        event.getMember(), event.getChannel());

            }
        }
    }

    private void shop(long channel, long member, ShardManager shardmanager, Member user, TextChannel sendchannel) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, (event) -> {
            long nchannel = event.getChannel().getIdLong();
            long nuser = event.getMember().getUser().getIdLong();
            return (channel == nchannel && member == nuser);
        }, (event) -> {
            String item = event.getMessage().getContentRaw();
            ArrayList<Long> MemberIDS = new ArrayList<Long>();
            ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
            BufferedReader myReader = null;
            try {
                myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberMoney"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            StringTokenizer st = null;
            String line;
            try {
                while ((line = myReader.readLine()) != null) {
                    st = new StringTokenizer(line);

                    long x = Long.parseLong(st.nextToken());
                    int z = Integer.parseInt(st.nextToken());
                    MemberIDS.add(x);
                    MemberMoney.add(z);
                }
                myReader.close();

            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }

            boolean found = false;
            for (int i = 0; i < MemberIDS.size(); i++) {
                if (MemberIDS.get(i) == member) {
                    if (item.equalsIgnoreCase("coffee")) {
                        if (MemberMoney.get(i) >= 500) {
                            MemberMoney.set(i, MemberMoney.get(i) - 500);
                            sendchannel.sendMessage("You spent 500 coins on a coffee. It is very delicous!").queue();

                            found = true;
                            break;

                        } else {
                            found = true;
                            sendchannel.sendMessage(
                                    "You need at least 500 coins to buy this item! I know you are broke, don't try to rob me!")
                                    .queue();
                        }
                        // SHOTGUN
                    } else if (item.equalsIgnoreCase("Shotgun Round")) {
                        if (MemberMoney.get(i) >= 1000) {
                            found = true;

                            ArrayList<Long> MemberIDSshotgun = new ArrayList<Long>();
                            ArrayList<Integer> MemberBullets = new ArrayList<Integer>();
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
                                    if (MemberIDSshotgun.contains(member)) {
                                        for (int l = 0; l < MemberIDSshotgun.size(); l++) {
                                            if (MemberIDSshotgun.get(l) == member) {
                                                if (MemberBullets.get(l) < 8) {
                                                    sendchannel.sendMessage(
                                                            "You spent 1000 coins on a shotgun round! Run -kill [@user] to make someone lose their life savings! "
                                                                    + "If they survive, however, you will lose yours!")
                                                            .queue();
                                                    MemberMoney.set(i, MemberMoney.get(i) - 1000);
                                                    MemberBullets.set(l, MemberBullets.get(l) + 1);
                                                } else {
                                                    sendchannel.sendMessage(
                                                            "Your shotgun is full! You can't buy any more bullets.")
                                                            .queue();
                                                }
                                            }
                                        }
                                    } else {
                                        sendchannel.sendMessage(
                                                "You spent 1000 coins on a shotgun round! Run -kill [@user] to make someone lose their life savings! "
                                                        + "If the survive, however, you will lose yours!")
                                                .queue();
                                        MemberIDSshotgun.add(member);
                                        MemberBullets.add(1);
                                    }
                                } catch (NumberFormatException | IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                PrintWriter shotgunwriter = new PrintWriter(
                                        new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/ShotgunRounds")));
                                for (int m = 0; m < MemberIDSshotgun.size(); m++) {
                                    String value = String.valueOf(MemberIDSshotgun.get(m));
                                    String bullets = String.valueOf(MemberBullets.get(m));
                                    shotgunwriter.print(value);
                                    shotgunwriter.print(" ");
                                    shotgunwriter.print(bullets);
                                    shotgunwriter.println();
                                }
                                shotgunwriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            found = true;
                            sendchannel.sendMessage(
                                    "You need at least 1000 coins to buy this item! I know you are broke, don't try to rob me!")
                                    .queue();
                        }
                    } else {
                        found = true;
                        sendchannel.sendMessage("Hey, that's not something you can buy!").queue();
                    }

                }

            }
            if (found == false) {
                MemberIDS.add(member);
                MemberMoney.add(0);
                sendchannel.sendMessage("New account registered!").queue();

            }
            PrintWriter myWriter = null;
            try {
                myWriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberMoney")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < MemberIDS.size(); i++) {

                String value = String.valueOf(MemberIDS.get(i));
                String money = String.valueOf(MemberMoney.get(i));

                myWriter.print(value);
                myWriter.print(" ");
                myWriter.print(money);
                myWriter.println();

            }
            myWriter.close();

        }, 10, TimeUnit.SECONDS, () -> {
            sendchannel.sendMessage(
                    "You didn't respond with an object to buy in time! Retry the command if you want to buy something")
                    .queue();
            ;
        });
    }

    public void checkshopTimer() {
        if (n == 0) {
            n = 1;
            startshopTimer();
        } else {

        }
    }

    public void startshopTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<User> remove = new ArrayList<User>();

                for (User member : shopTimer.keySet()) {
                    setPlayerShopTime(member, getPlayerShopTime(member) - 1);
                    if (getPlayerShopTime(member) == 0) {
                        remove.add(member);
                    }

                }
                for (int i = 0; i < remove.size(); i++) {
                    shopTimer.remove(remove.get(i));
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private int getPlayerShopTime(User member) {

        return shopTimer.get(member);
    }

    protected void setPlayerShopTime(User member, int num) {
        shopTimer.put(member, num);
    }

    protected boolean canGetShop(User member) {
        return !shopTimer.containsKey(member);
    }
}