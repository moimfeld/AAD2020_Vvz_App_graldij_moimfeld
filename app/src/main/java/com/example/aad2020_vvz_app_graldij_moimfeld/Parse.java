package com.example.aad2020_vvz_app_graldij_moimfeld;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
//documentation: https://commons.apache.org/proper/commons-lang/javadocs/api-3.9/org/apache/commons/lang3/RegExUtils.html
import org.apache.commons.lang3.RegExUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;



public class Parse {

    public Lecture parse (String url, Context context){



        //create all needed variables to create a lecture object
        String name = null;
        String day = null;
        String start_time = null;
        String end_time = null;
        String lecture_code = null;
        int ECTS = -1;

        //Here I create the Lecture object
        Lecture result;
        result= new Lecture(name, day, start_time, end_time, lecture_code, ECTS);



        //Url check/manipulation
        //if there is a valid url it gets formated to the correct format
        //if there is an invalid url, the parse function returns an toast message "invalid url" and returns an "empty" lecture
        //to get the right regex pattern I used https://regex101.com/
        if(url.contains("ansicht") && url.contains("lang=")){
            url = RegExUtils.replaceAll(url, "lang=+(de|en)", "lang=en");
            url = RegExUtils.replaceAll(url, "ansicht=(.*?)&", "ansicht=ALLE&");
            //Toast.makeText(context, "url changed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "invalid url", Toast.LENGTH_SHORT).show();
            return result;
        }




        //create new AsyncTask to fetch the HTML document
        AsyncTask<String, Void, Document> task = new NewThread().execute(url);


        //to avoid NullPointerExceptions I surrounded every parse with a if statement
        try {
            Document doc = task.get();
            //get the name (this might also not be robust enough, since the lecture code could be longer than from index 0 to 13)
            //The class StringBuilder allows to manipulate strings more easily
            if (doc.getElementById("contentTop") != null) {
                Element name_element = doc.getElementById("contentTop");
                StringBuilder parsed_name = new StringBuilder(name_element.text());
                parsed_name.delete(0, 13);
                name = parsed_name.toString();
                //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            }

            //get the day
            Elements day_element_list = doc.getElementsByClass("td-small");
            if (!doc.getElementsByClass("td-small").isEmpty()) {
                Element day_element = day_element_list.get(0);
                day = day_element.text();
                //Toast.makeText(context, day, Toast.LENGTH_SHORT).show();
            }

            //get the start_time
            if (day_element_list.size() >= 1) {
                Element start_time_element = day_element_list.get(1);
                StringBuilder parsed_start_time = new StringBuilder(start_time_element.text());
                parsed_start_time.delete(2, parsed_start_time.length());
                start_time = parsed_start_time.toString();
                //Toast.makeText(context, start_time, Toast.LENGTH_SHORT).show();
            }

            //get the end_time
            if (day_element_list.size() >= 1) {
                Element end_time_element = day_element_list.get(1);
                StringBuilder parsed_end_time = new StringBuilder(end_time_element.text());
                parsed_end_time.delete(0, 3);
                end_time = parsed_end_time.toString();
                //Toast.makeText(context, end_time, Toast.LENGTH_SHORT).show();
            }

            //get the lecture_code (this line could not be robust enough, since we don't know if the lecture code has variable length or not
            if (doc.getElementById("contentTop") != null) {
                Element lecture_code_element = doc.getElementById("contentTop");
                StringBuilder parsed_lecture_code = new StringBuilder(lecture_code_element.text());
                parsed_lecture_code.delete(12, parsed_lecture_code.length());
                lecture_code = parsed_lecture_code.toString();
                //Toast.makeText(context, lecture_code, Toast.LENGTH_SHORT).show();
            }

            //get the ECTS (this is probably not robust enough since there could be more td elements which contain the word "credits")
            if (doc.select("td:contains(credits)") != null) {
                Elements ECTS_element_list = doc.select("td:contains(credits)"); //.select("td:contains(credits)" looks for any td elements in the document that contains the string "credits"
                Element ECTS_element = ECTS_element_list.get(1); //this is the line which could be not robust enough, since it could be that the wanted "amount of credits" element is not the second td:contains(credits) element in the HTML document
                String ECTS_only_number;
                if (ECTS_element.text().contains(" credits")) {
                    ECTS_only_number = ECTS_element.text().replace(" credits", "");
                    ECTS = Integer.parseInt(ECTS_only_number);
                    //Toast.makeText(context, Integer.toString(ECTS), Toast.LENGTH_SHORT).show();
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }



        //create a Lecture object with the just parsed content
        result= new Lecture(name, day, start_time, end_time, lecture_code, ECTS);


        //Toast messages to give a feedback if the lecture parsing was successful
        if (name != null && day != null && start_time != null && end_time != null && lecture_code != null && ECTS != -1) {
            Toast.makeText(context, "lecture saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error while saving lecture", Toast.LENGTH_SHORT).show();
        }


        return result;
    }
}
