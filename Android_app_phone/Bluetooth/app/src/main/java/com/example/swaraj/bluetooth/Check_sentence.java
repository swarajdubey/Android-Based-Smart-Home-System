package com.example.swaraj.bluetooth;

import java.util.ArrayList;

/**
 * Created by swaraj on 16/03/2016.
 */
public class Check_sentence {
    private ArrayList<String> commands=new ArrayList<String>();
    private ArrayList<String> instructions=new ArrayList<String>();
    private boolean command_found;

    private String action;

    public void add_commands()
    {
        commands.add("Turn on the lights");
        commands.add("Turn off the lights");
        commands.add("Turn on the fan");
        commands.add("Turn off the fan");
        commands.add("What's the temperature");
        commands.add("Turn on the air conditioner");
        commands.add("Turn off the air conditioner");
        commands.add("Medicines");
        commands.add("Reduce the brightness");
        commands.add("increase the brightness");
        commands.add("Play some music");
        commands.add("Stop the music");

        instructions.add("Affirmative, Turning on the lights");
        instructions.add("Affirmative, Turning off the lights");
        instructions.add("Affirmative, Turning on the fan");
        instructions.add("Affirmative, Turning off the fan");
        instructions.add("The temperature is");
        instructions.add("Affirmative, Turning on the air conditioner");
        instructions.add("Affirmative, Turning off the air conditioner");
        instructions.add("The following are the medicine you have to take");
        instructions.add("Affirmative, Reducing the brightness");
        instructions.add("Affirmative, increasing the brightness");
        instructions.add("Affirmative, Playing some music");
        instructions.add("Affirmative, turning off the music");
    }

    public String check_for_matching_command(String command)
    {
        command=command.toLowerCase();
        command_found=false;
        for(int i=0;i<commands.size();i++)
        {
            if(command.equals(commands.get(i).toLowerCase()))
            {
                action=instructions.get(i);
                command_found=true;
            }
        }
        if(command_found==true)
        {
            return action;
        }
        else
        {
            command="not found";
            return command;
        }
    }

    public void clear_commands()
    {
        commands.clear();
        instructions.clear();
    }
}
