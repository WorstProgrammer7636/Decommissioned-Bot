package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.lang.reflect.Array;

public class Calculate extends ListenerAdapter {



    public void onGuildMessageReceived(GuildMessageReceivedEvent event){

        String[] message = event.getMessage().getContentRaw().split(" ");


        if (message[0].equalsIgnoreCase("-calculate") && message.length == 1){
            event.getChannel().sendMessage("To use this command, type(without brackets): !calculate " +
                    "[add/sub/mult/divide/exp/sqrt] [firstnum] [secondnum]").queue();
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("add")){

            if (message.length == 3){
                event.getChannel().sendMessage("You need to enter at least two numbers").queue();
                return;
            }
            
            try {
                int a = 2;
                double[] numbers = new double[message.length - 2];
                while (a < message.length){
                    numbers[a - 2] = Double.parseDouble(message[a]);
                    a++;
                }

                double sum = 0;
                for (int i = 0; i < numbers.length; i++){
                    sum += numbers[i];
                }
                event.getChannel().sendMessage("The answer is " + sum).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the numbers you want me to use?").queue();
                return;
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
                return;
            }



        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("sub")){
            if (message.length == 3){
                event.getChannel().sendMessage("You need to enter at least two numbers").queue();
                return;
            }

            try {
                int a = 2;
                double[] numbers = new double[message.length - 2];
                while (a < message.length){
                    numbers[a - 2] = Double.parseDouble(message[a]);
                    a++;
                }

                double answer = numbers[0];
                for (int i = 1; i < numbers.length; i++){
                    answer -= numbers[i];
                }
                event.getChannel().sendMessage("The answer is " + answer).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the numbers you want me to use?").queue();
                return;
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
                return;
            }


        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("mult")){
            if (message.length == 3){
                event.getChannel().sendMessage("You need to enter at least two numbers").queue();
                return;
            }

            try {
                int a = 2;
                double[] numbers = new double[message.length - 2];
                while (a < message.length){
                    numbers[a - 2] = Double.parseDouble(message[a]);
                    a++;
                }

                double answer = 1;
                for (int i = 0; i < numbers.length; i++){
                    answer *= numbers[i];
                }
                event.getChannel().sendMessage("The answer is " + answer).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the numbers you want me to use?").queue();
                return;
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Just stop").queue();
                return;
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
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("sqrt")){

            if (message.length > 3){
                event.getChannel().sendMessage("You can only input one number! Type in the format: -calculate sqrt [number]").queue();
                return;
            }
            try {
                double number = Double.parseDouble(message[2]);

                double answer = Math.sqrt(number);
                event.getChannel().sendMessage("The square root of " + number + " is " + answer).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the number you want me to square root?").queue();
                return;
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Please type an integer.").queue();
                return;
            }
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("exp")){

            if (message.length > 4){
                event.getChannel().sendMessage("You can only input two numbers! Type in the format: -calculate sqrt [number][exponent]").queue();
                return;
            }



            try {
                double number = Double.parseDouble(message[2]);
                double exponent = Double.parseDouble(message[3]);

                double answer = Math.pow(number,exponent);

                if (Double.isInfinite(answer)){
                    event.getChannel().sendMessage("The numbers you entered were too big,").queue();
                    return;
                }
                event.getChannel().sendMessage("The answer is " + answer).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the number you want me to power?").queue();
                return;
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Please type a number.").queue();
                return;
            }
        } else if (message[0].equalsIgnoreCase("-calculate") && message[1].equalsIgnoreCase("log")){

            if (message.length > 4){
                event.getChannel().sendMessage("You can only input two numbers! Type in the format: -calculate log [base][logNumber]").queue();
                return;
            }


            try {
                double base = Double.parseDouble(message[2]);
                double number = Double.parseDouble(message[3]);

                double answer = (Math.log(number)) / Math.log(base);

                //error cases
                if (base == 1 && number >= 1){
                    event.getChannel().sendMessage("Sorry mate you can't do that").queue();
                    return;
                }

                if (base == 0){
                    event.getChannel().sendMessage("Really? Do you really think I'm that stupid?").queue();
                    return;
                }

                if (number < 0){
                    event.getChannel().sendMessage("Really? Do you really think I'm that stupid?").queue();
                    return;
                }

                if (Double.isInfinite(answer)){
                    event.getChannel().sendMessage("The numbers you entered were too big,").queue();
                    return;
                }


                event.getChannel().sendMessage("The answer is " + answer).queue();
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessage("Can you tell me the number you want me to log? Type in the format: -calculate log [base][logNumber]").queue();
                return;
            } catch (NumberFormatException e){
                event.getChannel().sendMessage("Please type only numbers.").queue();
                return;
            }
        }
    }

}
