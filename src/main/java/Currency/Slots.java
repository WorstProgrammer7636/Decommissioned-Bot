package Currency;

import java.awt.Color;
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
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Slots extends ListenerAdapter {
    private HashMap<User, Integer> playerTimer = new HashMap<User, Integer>();
    private int o = 0;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        checkTimer();
        String[] split = event.getMessage().getContentRaw().split("\\s+");
        if (split[0].equalsIgnoreCase("-slots") && canBet(event.getAuthor())) {
            if (split[0].equalsIgnoreCase("-slots") && split.length >= 2) {
                int bet = 0;
                try {
                    bet = Integer.parseInt(split[1]);
                } catch (NumberFormatException e) {
                    if (split[1].equalsIgnoreCase("max") || split[1].equalsIgnoreCase("all")) {
                        try {
                            initiateslots(bet, event.getAuthor().getIdLong(), event.getChannel(), event.getAuthor());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    } else {
                        event.getChannel().sendMessage("Bet using an integer or use max/all, please!").queue();

                    }
                    return;
                }
                if (bet < 500) {
                    event.getChannel().sendMessage("Bet at least 500 coins, please!").queue();
                } else {
                    try {
                        initiateslots(bet, event.getAuthor().getIdLong(), event.getChannel(), event.getAuthor());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (split[0].equalsIgnoreCase("-slots")) {
                event.getChannel().sendMessage("Do -slots [bet] to use the slot machine!").queue();

            }
        } else if (split[0].equalsIgnoreCase("-jackpot")) {
            try {
                jackpot(event.getChannel());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (split[0].equalsIgnoreCase("-slots")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Slow it down!");
            eb.setDescription("You don't want to break the slot machine (or go broke), so wait a bit! Wait **"
                    + playerTimer.get(event.getAuthor()) + "** seconds to use the slot machine again!");

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

    public void initiateslots(int bet, long UserID, TextChannel channel, User user) throws IOException {

        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("MemberMoney"));
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
        if (MemberIDS.contains(UserID)) {
            for (int i = 0; i < MemberIDS.size(); i++) {
                final int truebet;
                if (MemberIDS.get(i) == UserID) {
                    if (bet == 0) {
                        truebet = MemberMoney.get(i);
                    } else {
                        truebet = bet;
                    }
                    if (MemberMoney.get(i) >= truebet && truebet >= 500) {
                        boolean gotjackpot = false;
                        int jackpot = 0;
                        Random slots = new Random();
                        int first = slots.nextInt(7);
                        int second = slots.nextInt(7);
                        int third = slots.nextInt(7);
                        String[] circles = { ":heart:", ":blue_heart:", ":green_heart:", ":orange_heart:",
                                ":yellow_heart:", ":purple_heart:", ":star2:" };
                        final int win;
                        if (first == second && second == third) {
                            win = 2;
                        } else if (first == third || first == second || second == third) {
                            win = 1;
                        } else {
                            win = 0;
                        }
                        if (win == 0) {
                            MemberMoney.set(i, MemberMoney.get(i) - truebet);
                            BufferedReader JackpotReader = new BufferedReader(new FileReader("CurrentSlotsJackpot"));
                            jackpot = Integer.parseInt(JackpotReader.readLine());
                            PrintWriter JackpotWriter = new PrintWriter(
                                    new BufferedWriter(new FileWriter("CurrentSlotsJackpot")));
                            int newjackpot = jackpot + (truebet / 3);
                            JackpotWriter.write(String.valueOf(newjackpot));
                            JackpotReader.close();
                            JackpotWriter.close();
                        } else if (win == 1) {
                            MemberMoney.set(i, MemberMoney.get(i) + truebet * 3 / 2);

                        } else if (win == 2) {
                            MemberMoney.set(i, MemberMoney.get(i) + truebet * 3);
                            if (circles[first].equals(":star2:")) {
                                BufferedReader JackpotReader = new BufferedReader(
                                        new FileReader("CurrentSlotsJackpot"));
                                jackpot = Integer.parseInt(JackpotReader.readLine());
                                MemberMoney.set(i, MemberMoney.get(i) + jackpot);

                                PrintWriter JackpotWriter = new PrintWriter(
                                        new BufferedWriter(new FileWriter("CurrentSlotsJackpot")));
                                int newjackpot = 0;
                                gotjackpot = true;
                                JackpotWriter.write(String.valueOf(newjackpot));
                                JackpotReader.close();
                                JackpotWriter.close();
                            }

                        }
                        final int balance = MemberMoney.get(i);
                        EmbedBuilder slotsImage = new EmbedBuilder();
                        slotsImage.setTitle("Results");
                        slotsImage.addField("Slots", ">> :white_heart: :white_heart: :white_heart: <<", false);

                        channel.sendMessage(slotsImage.build()).queue(message -> {

                            slotsImage.getFields().remove(0);

                            if (win == 0) {
                                slotsImage.setFooter("You suck!");
                                slotsImage.setColor(0xeb3434);
                                slotsImage.addField("Loss!",
                                        "You lost **" + truebet
                                                + "** coins! Try again next time! \n Your current balance is now: **"
                                                + balance + "** coins.",
                                        false);

                            } else if (win == 1) {
                                slotsImage.setFooter("Winner!");
                                slotsImage.setColor(0x03a5fc);
                                slotsImage.addField("Win!", "You won **" + truebet * 3 / 2
                                                + "** coins! (150%) \n Your current balance is now: **" + balance + "** coins.",
                                        false);

                            } else if (win == 2) {
                                slotsImage.addField("Win!", "You won **" + truebet * 3
                                                + "** coins! (300%) \n Your current balance is now: **" + balance + "** coins.",
                                        false);
                                slotsImage.setFooter("Winner Winner!");
                                slotsImage.setColor(0x03a5fc);

                            }
                            slotsImage.addField("Slots",
                                    ">> " + circles[first] + " " + circles[second] + " " + circles[third] + " <<",
                                    false);

                            message.editMessage(slotsImage.build()).queueAfter(2, TimeUnit.SECONDS);
                            setPlayerTime(user, 10);

                        });
                        if (gotjackpot) {
                            channel.sendMessage("BONUS: Since you got three stars in a row, you won the jackpot of **"
                                    + jackpot + "** coins! Congratulations!").queueAfter(2, TimeUnit.SECONDS);

                        }
                    } else if (truebet < 500) {
                        channel.sendMessage("Hey broke man, your current worth is less than 500 coins! Go and earn some more before using the slot machine! Your "
                                + "current worth is " + MemberMoney.get(i) + " coins.").queue();
                    } else {

                        channel.sendMessage(
                                "I know you don't have that much money! You have " + MemberMoney.get(i) + " coins.")
                                .queue();
                    }
                }
            }
        } else {
            channel.sendMessage("Error: User not found!").queue();
        }
        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("MemberMoney")));

        for (int i = 0; i < MemberIDS.size(); i++) {

            String value = String.valueOf(MemberIDS.get(i));
            String money = String.valueOf(MemberMoney.get(i));

            myWriter.print(value);
            myWriter.print(" ");
            myWriter.print(money);
            myWriter.println();

        }
        myWriter.close();
    }

    public void jackpot(TextChannel channel) throws IOException {
        BufferedReader JackpotReader = new BufferedReader(new FileReader("CurrentSlotsJackpot"));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Jackpot!");
        int jackpot = Integer.parseInt(JackpotReader.readLine());
        eb.setDescription("The current jackpot is **" + jackpot
                + "** coins. \n To win the jackpot, you must get >> :star2: :star2: :star2: << in slots!");

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

        channel.sendMessage(eb.build()).queue();
        JackpotReader.close();
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

    protected boolean canBet(User member) {
        return !playerTimer.containsKey(member);
    }
}
