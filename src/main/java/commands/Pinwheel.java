package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
//ORIGINAL SOURCE: UCSD(University of California San Diego) CSE ASSIGNMENT
public class Pinwheel extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (event.getAuthor().isBot()){
            return;
        }


        if (message[0].equalsIgnoreCase("-Pinwheel")){
            int size = 0;
            // takes size and checks if it is a valid integer
            try {
                String inputSize = message[1];
                size = Integer.parseInt(inputSize);
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("\nError: Radioactive Pinwheel size must be a single integer").queue();
                return;
            }

            if (size < 4 || size > 50) {
                event.getChannel().sendMessage("Enter a number within the bounds please").queue();
                return;
            }

            // checks if size is even
            if (size % 2 == 1) {
                event.getChannel().sendMessage("Enter an even number please").queue();
                return;
            }

            try {
                if (message[2].length() != 1 && !message[2].equals("")) {
                    event.getChannel().sendMessageFormat(PA4Strings.SINGLE_CHAR_ERR, PA4Strings.UPWARD_CHAR_NAME).queue();
                    return;
                }
            } catch (ArrayIndexOutOfBoundsException e){
                event.getChannel().sendMessageFormat(PA4Strings.SINGLE_CHAR_ERR, PA4Strings.UPWARD_CHAR_NAME).queue();
            }

            // sets upward triangle default value
            if (message[2].equals("")) {
                message[2] = "@";
            }

            boolean charValidity = false;
            char x = message[2].charAt(0);
            for (int i = 32; i < 127; i++) {
                if ((int) x == (i)) {
                    charValidity =  true;
                }
            }


            if (charValidity == false){
                event.getChannel().sendMessageFormat(PA4Strings.CHAR_RANGE_ERR, PA4Strings.UPWARD_CHAR_NAME, 32, 126).queue();
                System.exit(1);
            }

            event.getChannel().sendMessageFormat(PA4Strings.CHAR_PROMPT, PA4Strings.DOWNWARD_CHAR_NAME).queue();
            String downwardChar = message[3];

            // checks if downward arrow character is valid
            if (downwardChar.length() != 1 && !downwardChar.equals("")) {
                event.getChannel().sendMessageFormat(PA4Strings.SINGLE_CHAR_ERR, PA4Strings.DOWNWARD_CHAR_NAME).queue();
                System.exit(1);
            }

            //checks if both triangle values are identical
            if (!message[2].equals("") && !downwardChar.equals("") && message[2].equals(downwardChar)) {
                event.getChannel().sendMessage(PA4Strings.UPWARD_DOWNWARD_SAME_CHAR_ERR).queue();
                System.exit(1);
            }


            //sets downward triangle default value
            if (downwardChar.equals("")) {
                downwardChar = "`";
            }

            charValidity = false;
            x = message[3].charAt(0);
            for (int i = 32; i < 127; i++) {
                if ((int) x == (i)) {
                    charValidity =  true;
                }
            }

            if (charValidity == false){
                event.getChannel().sendMessageFormat(PA4Strings.CHAR_RANGE_ERR, PA4Strings.DOWNWARD_CHAR_NAME, 32, 126).queue();
                return;
            }

            // instantiation
            int count = 0;
            int spaces = (size / 2) - 1;
            int row = 0;
            int spaceCounter = 0;
            int upperCharCounter = 1;
            int downCharCounter = size - 1;
            int upperTemp = 0;
            int downTemp = 0;
            String upSpace = "|";
            // form the upper half of the wheel
            // Big O Notation Simplified: O(n^2) total.
            while (row < size / 2) {

                // print first set of spaces for the row
                while (spaceCounter < spaces) {
                    upSpace += " ";
                    ++spaceCounter;
                }
                spaceCounter = 0;

                // print first part of the upper triangle
                while (upperTemp < upperCharCounter) {
                    upSpace += message[2];
                    upperTemp++;
                }

                // print the downward triangle characters from corresponding row
                while (downTemp < downCharCounter) {
                    upSpace += downwardChar;
                    downTemp++;
                }

                upperTemp = 0;
                downTemp = 0;

                // print second part of the upper triangle
                while (upperTemp < upperCharCounter) {
                    upSpace += message[2];
                    upperTemp++;
                }
                upperTemp = 0;

                // print second set of spaces for the row
                while (spaceCounter < spaces) {
                    upSpace += " ";
                    ++spaceCounter;
                }
                event.getChannel().sendMessage(upSpace).queue();
                spaceCounter = 0;

                upperCharCounter += 2;
                downCharCounter -= 2;
                spaces -= 1;
                row++;
                upSpace = "|";
            }

            upSpace = "|";
            while (row < size / 2) {

                // print first set of spaces for the row
                while (spaceCounter < spaces) {
                    upSpace += " ";
                    ++spaceCounter;
                }
                spaceCounter = 0;

                // print first part of downward triangle
                while (downTemp < downCharCounter) {
                    upSpace += downwardChar;
                    downTemp++;
                }

                // print upper triangle row by row
                while (upperTemp < upperCharCounter) {
                    upSpace += message[2];
                    upperTemp++;
                }
                upperTemp = 0;
                downTemp = 0;

                // print second part of the downward triangle
                while (downTemp < downCharCounter) {
                    upSpace += downwardChar;
                    downTemp++;
                }
                downTemp = 0;

                // print second set of spaces for the row
                while (spaceCounter < spaces) {
                    upSpace += " ";
                    ++spaceCounter;
                }
                spaceCounter = 0;
                event.getChannel().sendMessage(upSpace).queue();

                upperCharCounter += 2;
                downCharCounter -= 2;
                spaces++;
                row++;
                upSpace = "|";
            }

        }
    }
}
