package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
//documentation: https://commons.apache.org/proper/commons-lang/javadocs/api-3.9/org/apache/commons/lang3/RegExUtils.html
import org.apache.commons.lang3.RegExUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class Parse {

    public Course parse (String url, Context context){

        //this is a auxiliary variables
        int index;
        Element link_td_element;
        Element link_element;
        String endOf_link;
        String link;
        String hours;
        String dates_raw;
        int start_time_int;
        int end_time_int;


        //create all needed variables to create a lecture object
        String name=null;

        //these are the new ArrayLists which need to be filled / parsed
        ArrayList<Appointment> lectures = new ArrayList<>();
        ArrayList<Appointment> exercises = new ArrayList<>();
        ArrayList<Appointment> labs = new ArrayList<>();
        //these are the arguments which need to be filled in order to parse an
        String day = null;
        String periodicity;
        ArrayList<Integer> time = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        //these three are old arguments which won't be used after the new structure is fully implemented
        String start_time=null;
        String end_time=null;

        String course_code=null;
        int ECTS=-1;

        //Here I create the Lecture object
//        Lecture result;
//        result= new Lecture(null, null, null, null, null, -1);
        Course result;
        result= new Course();

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
                parsed_name.delete(0, 13);//delete code
                name = parsed_name.toString();
                //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            }
            //get the course_code (this line could not be robust enough, since we don't know if the lecture code has variable length or not
            if (doc.getElementById("contentTop") != null) {
                Element lecture_code_element = doc.getElementById("contentTop");
                StringBuilder parsed_lecture_code = new StringBuilder(lecture_code_element.text());
                parsed_lecture_code.delete(12, parsed_lecture_code.length());
                course_code = parsed_lecture_code.toString();
                //Toast.makeText(context, course_code, Toast.LENGTH_SHORT).show();
            }

            //get the lecture Appointments
            //get the Url for the next web page to parse from
            Elements td_element_list = doc.select("td");
            StringBuilder lecture_code_building = new StringBuilder(course_code);
            lecture_code_building = lecture_code_building.delete(11, 12);
            String lecture_code = "td:contains(" + lecture_code_building.toString() + " V)";
            if (!td_element_list.select(lecture_code).isEmpty()) {
                Elements td_lecture_code_elements = td_element_list.select(lecture_code);
                Element lecture_code_element = td_lecture_code_elements.get(0);
                index = td_element_list.indexOf(lecture_code_element);
                link_td_element = td_element_list.get(index + 3);
                link_element = link_td_element.getElementsByClass("td-small").first();
                link_element = link_element.select("a").first();
                endOf_link = link_element.attr("href");
                link = "http://www.vvz.ethz.ch" + endOf_link;
                //get the html document of the just parsed link
                AsyncTask<String, Void, Document> lectureTask = new NewThread().execute(link);
                Document docLecture = lectureTask.get();
                //here we get all tr elements (since in the tr elements there are all the infos for the appointments
                Elements appointment_elements = docLecture.select("tr");
                Element appointment_element;
                //this for loop iterates over all elements of appointment_elements and forms a appointment object for each element
                for(int i = 1; i < appointment_elements.size(); i++){
                    //parse all the info for each appointment object
                    appointment_element = appointment_elements.get(i);
                    Elements appointment_content = appointment_element.select("td");
                    day = appointment_content.get(0).text();
                    hours = appointment_content.get(1).text();
                    StringBuilder hours_edit = new StringBuilder(hours);
                    hours_edit.delete(2, hours.length());
                    start_time_int = Integer.parseInt(hours_edit.toString());
                    hours_edit = new StringBuilder(hours);
                    hours_edit.delete(0, 3);
                    end_time_int = Integer.parseInt(hours_edit.toString());
                    for(int j = start_time_int;j <= end_time_int; j++){
                        time.add(j);
                    }
                    periodicity = appointment_content.get(2).text();
                    dates_raw = appointment_content.get(3).text();
                    //here we only get the raw dates, the dates should get transformed into a arraylist where all dates are one entry
                    dates.add(dates_raw);
                    //with the parsed data we can finally create an appointment object and fill that into the lectures arraylist
                    Appointment lecture = new Appointment(day, time, periodicity, dates);
                    lectures.add(lecture);
                }
            }



            //this is old code which can be deleted, after the new code is implemented
            //get the day
            Elements day_element_list = doc.getElementsByClass("td-small");
/*            if (!doc.getElementsByClass("td-small").isEmpty()) {
                Element day_element = day_element_list.get(0);
                //get href (last part of the link) from the element.
                link_element = day_element.select("a").first();
                endOf_link = link_element.attr("href");
                link = "http://www.vvz.ethz.ch" + endOf_link;

                AsyncTask<String, Void, Document> taskGetDay = new NewThread().execute(link);

                Document docGetDay = taskGetDay.get();
                if (!docGetDay.select("td:contains(Mon)").isEmpty()) {
                    day_element = docGetDay.select("td:contains(Mon)").get(0);
                    day = day_element.text();
                }

//                Toast.makeText(context, day, Toast.LENGTH_SHORT).show();
            }*/

            //this is old code which can be deleted, after the new code is implemented
            //get the start_time
            if (day_element_list.size() >= 1) {
                Element start_time_element = day_element_list.get(1);
                StringBuilder parsed_start_time = new StringBuilder(start_time_element.text());
                parsed_start_time.delete(2, parsed_start_time.length());
                start_time = parsed_start_time.toString();
                //Toast.makeText(context, start_time, Toast.LENGTH_SHORT).show();
            }

            //this is old code which can be deleted, after the new code is implemented
            //get the end_time
            if (day_element_list.size() >= 1) {
                Element end_time_element = day_element_list.get(1);
                StringBuilder parsed_end_time = new StringBuilder(end_time_element.text());
                parsed_end_time.delete(0, 3);
                end_time = parsed_end_time.toString();
                //Toast.makeText(context, end_time, Toast.LENGTH_SHORT).show();
            }

            //lectures with more than one "ECTS credits" do exist, but we will not handle them. Only first entry handled.
            if (doc.select("td") != null) {
                Elements ECTS_element_list = doc.select("td:contains(ECTS credits)"); //.select("td:contains(credits)" looks for any td elements in the document that contains the string "credits"
                Element ECTS_element_entry=ECTS_element_list.get(0); //lectures with more than one "ECTS credits" do exist, but we will not handle them. Only first entry handled.
                //overwrite
                ECTS_element_list = doc.select("td:contains(credits)"); //select all elements containing "credits
                index = ECTS_element_list.indexOf(ECTS_element_entry);//find index of the element with "ECTS credits"
                Element ECTS_element = ECTS_element_list.get(index+1); //use as credit the immediately following "credit" entry
                String ECTS_only_number;
                ECTS_only_number = ECTS_element.text().replace(" credits", "");
                ECTS = Integer.parseInt(ECTS_only_number);
//                Toast.makeText(context, ECTS_only_number, Toast.LENGTH_SHORT).show();
            }
            //create a Lecture object with the just parsed content
            result= new Course(name, lectures, exercises, labs, day, start_time, end_time, course_code, ECTS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }





        //Toast messages to give a feedback if the lecture couldn't fully get parsed
        if (result.isEmpty()) {
            Toast.makeText(context, "error while saving lecture", Toast.LENGTH_SHORT).show();
        }


        return result;
    }
}
