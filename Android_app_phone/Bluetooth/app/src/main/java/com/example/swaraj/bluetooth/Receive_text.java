package com.example.swaraj.bluetooth;

import java.util.ArrayList;

/**
 * Created by swaraj on 16/03/2016.
 */
public class Receive_text {

    private String sentence;
    private int temp_pos;
    private ArrayList<String> words = new ArrayList<String>();

    public Receive_text(){}

    public void get_message(String msg)
    {
        sentence=msg;
    }
    public void split_words()
    {
        sentence=" "+sentence+" ";
        for(int i=0;i<sentence.length();i++)
        {
            if(sentence.charAt(i)==' ' && i!=sentence.length()-1)
            {
                temp_pos=i;
                while(sentence.charAt(i+1)!=' ')
                {
                    i++;
                }
                words.add(sentence.substring(temp_pos+1, i+1).toLowerCase());
            }
        }
    }
    public ArrayList<String> return_words()
    {
        return words;
    }
    public void clear_list()
    {
        words.clear();
    }
}
