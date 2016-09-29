package com.example.swaraj.bluetooth;

import java.util.ArrayList;

/**
 * Created by swaraj on 16/03/2016.
 */
public class Speech_to_text_algorithm {

    private ArrayList<String> words=new ArrayList<String>();
    private String speak_instruction;

    public String speech_to_text_processing(String sentence)
    {
        String original_sentence=sentence;
        Receive_text rt=new Receive_text();
        rt.get_message(sentence);
        rt.split_words();
        words=rt.return_words();

        Check_sentence cs = new Check_sentence();
        cs.add_commands();
        sentence=cs.check_for_matching_command(original_sentence);
        cs.clear_commands();

        if(sentence=="not found")
        {
            Word_checking wc = new Word_checking();
            wc.add_objects_and_actions();

            if(wc.look_for_objects(words)!=true)
            {
                if(wc.look_for_alt_objects(words)==true)
                {
                    if(wc.looks_for_actions(words)==false)
                    {
                        speak_instruction="Sorry, I can't understand";
                    }
                    else{
                        speak_instruction=wc.say_command();
                    }
                }
                else{
                    speak_instruction="Sorry, I can't understand";
                }

            }
            if(wc.look_for_objects(words)!=false)
            {
                if(wc.looks_for_actions(words)==false)
                {
                    speak_instruction="Sorry, I can't understand";
                }
                else{
                    speak_instruction=wc.say_command();
                }
            }
            wc.clear_objects_and_actions();

        }

        if(sentence!="not found"){
            speak_instruction=sentence;
        }

        words.clear();
        rt.clear_list();

        return speak_instruction; //return the appripriate string to be said
    }
}
