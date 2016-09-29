package com.example.swaraj.bluetooth;

import java.util.ArrayList;

/**
 * Created by swaraj on 16/03/2016.
 */
public class Word_checking {

    private ArrayList<String> object=new ArrayList<String>();
    private ArrayList<String> object_alt=new ArrayList<String>();
    private ArrayList<String> actions=new ArrayList<String>();

    private ArrayList<String> instructions=new ArrayList<String>();

    private boolean object_found;
    private boolean object_alt_found;
    private boolean actions_found;

    private String Object,action;


    public Word_checking()
    {
        object_found=false;
        object_alt_found=false;
        actions_found=false;
    }

    public void add_objects_and_actions()
    {
        object.add("light");
        object.add("light");
        object.add("fan");
        object.add("fan");
        object.add("temperature");
        object.add("air conditioner");
        object.add("air conditioner");
        object.add("medicine");
        object.add("brightness");
        object.add("brightness");
        object.add("music");
        object.add("music");

        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("hot,humid,cold");
        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("-");
        object_alt.add("songs");
        object_alt.add("songs");

        actions.add("on");
        actions.add("off");
        actions.add("on");
        actions.add("off");
        actions.add("hot,humid,cold,temperature");
        actions.add("on");
        actions.add("off");
        actions.add("medicine");
        actions.add("reduce,lower");
        actions.add("increase,more");
        actions.add("play/music");
        actions.add("stop/off");

        instructions.add("Affirmative, Turning on the lights");
        instructions.add("Affirmative, Turning off the lights");
        instructions.add("Affirmative, Turning on the fan");
        instructions.add("Affirmative, Turning off the fan");
        instructions.add("The temperature is");
        instructions.add("Affirmative, Turning on the air conditioner");
        instructions.add("Affirmative, Turning off the air conditioner");
        instructions.add("The following are the medicine you have to take");
        instructions.add("Affirmative, Reducing the brightness off");
        instructions.add("Affirmative, increasing the brightness on");
        instructions.add("Affirmative, Playing some music");
        instructions.add("Affirmative, turning off the music ");
    }

    public boolean look_for_objects(ArrayList<String> words)
    {
        for(int i=0;i<words.size();i++)
        {
            for(int j=0;j<object.size();j++)
            {
                if(words.get(i).contains(object.get(j).toLowerCase()))
                {
                    object_found=true;
                    Object=object.get(j);
                }
            }
        }

        return object_found;
    }

    public boolean look_for_alt_objects(ArrayList<String> words)
    {
        for(int i=0;i<words.size();i++)
        {
            for(int j=0;j<object_alt.size();j++)
            {
                if(( words.get(i).contains(object_alt.get(j).toLowerCase()) ) || (object_alt.get(j).toLowerCase().contains(words.get(i))) )
                {
                    object_alt_found=true;
                    Object=object_alt.get(j);
                }
            }
        }

        return object_alt_found;


    }

    public boolean looks_for_actions(ArrayList<String> words)
    {
        for(int i=0;i<words.size();i++)
        {
            for(int j=0;j<actions.size();j++)
            {
                if(( words.get(i).contains(actions.get(j).toLowerCase()) ) || ( actions.get(j).contains(words.get(i).toLowerCase()) ) )
                {
                    actions_found=true;
                    action=actions.get(j);
                }
            }
        }
        return actions_found;
    }

    public String say_command()
    {
        String instruction = null;
        for(int i=0;i<instructions.size();i++)
        {
            if(( object.get(i)==Object || object_alt.get(i)==Object ) && actions.get(i)==action)
            {
                instruction=instructions.get(i);
            }
        }
        return instruction;
    }
    public void clear_objects_and_actions()
    {
        object.clear();
        object_alt.clear();
        actions.clear();
        instructions.clear();
    }
}
