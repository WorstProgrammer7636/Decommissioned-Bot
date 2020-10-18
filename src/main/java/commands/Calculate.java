package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Array;

public class Calculate extends ListenerAdapter {


    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        String[] message = event.getMessage().getContentRaw().split(" ");


        if (message[0].equalsIgnoreCase("-calculate") && message.length == 1){
            event.getChannel().sendMessage("To use this command, type(without brackets): !calculate " +
                    "[add/sub/mult/divide] [firstnum] [secondnum]").queue();
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("add")){
            double numberOne = 0;
            double numberTwo = 0;
            try {
                numberOne = Double.parseDouble(message[2]);
                numberTwo = Double.parseDouble(message[3]);
                event.getChannel().sendMessage("The answer is " + (numberOne + numberTwo)).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the two numbers you want me to use?").queue();
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
            }
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("sub")){
            double numberOne = 0;
            double numberTwo = 0;
            try {
                numberOne = Double.parseDouble(message[2]);
                numberTwo = Double.parseDouble(message[3]);
                event.getChannel().sendMessage("The answer is " + (numberOne - numberTwo)).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the two numbers you want me to use?").queue();
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
            }
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("mult")){
            double numberOne = 0;
            double numberTwo = 0;
            try {
                numberOne = Double.parseDouble(message[2]);
                numberTwo = Double.parseDouble(message[3]);
                event.getChannel().sendMessage("The answer is " + (numberOne * numberTwo)).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the two numbers you want me to use?").queue();
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
            }
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("divide")){
            double numberOne = 0;
            double numberTwo = 0;
            try {
                numberOne = Double.parseDouble(message[2]);
                numberTwo = Double.parseDouble(message[3]);
                if (numberTwo == 0){
                    event.getChannel().sendMessage("Can you stop?").queue();
                } else {
                    event.getChannel().sendMessage("The answer is " + (numberOne / numberTwo)).queue();
                }

            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Please type two numbers?").queue();
            } catch (ArithmeticException e){
                event.getChannel().sendMessage("Don't do that").queue();
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
            }
        }
    }

}
