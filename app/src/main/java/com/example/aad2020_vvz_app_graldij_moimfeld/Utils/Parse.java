package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
//documentation: https://commons.apache.org/proper/commons-lang/javadocs/api-3.9/org/apache/commons/lang3/RegExUtils.html
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;


//there is still a bug, where the app crashes, when there is only an hour category and no date/time in a lecture
public class Parse {

    public ArrayList<Appointment> parseAppointments (String category, Document doc, String course_code){


        //this is a auxiliary variables
        int index;
        Element link_td_element;
        Element link_element;
        String endOf_link;
        String link;
        String category_fullString;

        //return ArrayList
        ArrayList<Appointment> r = new ArrayList<>();


        //set the category_fullstring
        switch(category){
            case "V":
                category_fullString = "Lecture";
                break;

            case "G":
                category_fullString = "Lecture of Exercise";
                break;

            case "U":
                category_fullString = "Exercise";
                break;

            case "P":
                category_fullString = "Lab";
                break;

            default:
                category_fullString = "no category defined";
                break;
        }



        Elements td_element_list = doc.select("td");
        StringBuilder category_code_builder = new StringBuilder(course_code);
        category_code_builder = category_code_builder.delete(11, 12);
        String category_code = "td:contains(" + category_code_builder.toString() + " " + category + ")";
        if (!td_element_list.select(category_code).isEmpty()) {
            Elements td_category_code_elements = td_element_list.select(category_code);
            Element category_code_element = td_category_code_elements.get(0);
            index = td_element_list.indexOf(category_code_element);
            link_td_element = td_element_list.get(index + 3);
            link_element = link_td_element.getElementsByClass("td-small").first();
            link_element = link_element.select("a").first();
            endOf_link = link_element.attr("href");
            link = "http://www.vvz.ethz.ch" + endOf_link;
            //get the html document of the just parsed link
            AsyncTask<String, Void, Document> categoryTask = new NewThread().execute(link);
            Document docCategory = null;
            try {
                docCategory = categoryTask.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            //here we get all tr elements (since in the tr elements there are all the infos for the appointments
            Elements appointment_elements = docCategory.select("tr");
            //this for loop iterates over all elements of appointment_elements and forms a appointment object for each element
            r = new ArrayList<>(appointment_elements.size()-1);
            for(int i = 1; i < appointment_elements.size(); i++){

                //get ith appointment element
                Element appointment_element = appointment_elements.get(i);
                //get all td elements (in the td elements are the wanted information about time date etc.) of the ith appointment_element
                Elements appointment_content = appointment_element.select("td");

                if (!appointment_content.get(2).text().equals("Continuous")) {
                    //create the variables for the Appointment object
                    //it is 1000% important to create new arrays for each iteration through the for loop, since we'll pass the array to the Appointment object, and we need to get new arrays for each object
                    ArrayList<Integer> time = new ArrayList<>();
                    ArrayList<String> dates = new ArrayList<>();
                    String day = null;
                    String periodicity = null;
                    //get the day of this appointment
                    day = appointment_content.get(0).text();
                    //get the hours of this appointment
                    String hours = appointment_content.get(1).text();
                    //transform the hours string into the wanted format
                    StringBuilder hours_edit = new StringBuilder(hours);
                    hours_edit.delete(2, hours.length());
                    int start_time_int = Integer.parseInt(hours_edit.toString());
                    hours_edit = new StringBuilder(hours);
                    hours_edit.delete(0, 3);
                    int end_time_int = Integer.parseInt(hours_edit.toString());
                    //use < instead, is this fine for you?
                    for(int j = start_time_int;j < end_time_int; j++){
                        time.add(j);
                }
                    periodicity = appointment_content.get(2).text();
                    String dates_raw = appointment_content.get(3).text();
                    dates.add(dates_raw);

                    //since the places of the appointments aren't clearly separated via HTML code I just parse though the HTML section by myself
                    //first I get the HTML block and remove all "&nbsp;"
                    String place_parse = StringUtils.replace(appointment_content.get(4).html(), "&nbsp;", "");
                    //places are separated by "<br>", so I split the String into an array of strings with each place in it
                    String[] placeArray = StringUtils.split(place_parse, "<br>");
                    //Sets have the property of not having duplicates, so I initialize a HashSet with my placeArray, to filter out any duplicates
                    Set<String> placeSet = new HashSet<>(Arrays.asList(placeArray));
                    String place;
                    for (String s : placeSet) {
                        place = s;
                        Appointment category_appointment = new Appointment(category_fullString, day, time, periodicity, dates, place);
                        r.add(category_appointment);
                    }
                }
            }
            return r;
        }
        return r;
    }





    public Course parse (String url, Context context){

        //this is a auxiliary variables
        int index;
        Parse parse;

        //create all needed variables to create a lecture object
        String name=null;
        ArrayList<Appointment> lectures;
        ArrayList<Appointment> lecturesAndExercises;
        ArrayList<Appointment> exercises;
        ArrayList<Appointment> labs;
        String course_code=null;
        int ECTS=-1;

        //create empty course object
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

            //get the lecture appointments
            parse = new Parse();
            lectures = parse.parseAppointments("V", doc, course_code);

            //get the exercise appointments
            lecturesAndExercises = parseAppointments("G", doc, course_code);

            //get the exercise appointments
            exercises = parseAppointments("U", doc, course_code);

            //get the lab appointments
            labs = parseAppointments("P", doc, course_code);


            //lectures with more than one "ECTS credits" do exist, but we will not handle them. Only first entry handled.
            if (doc.select("td") != null) {
                Elements ECTS_element_list = doc.select("td:contains(ECTS credits)"); //.select("td:contains(credits)" looks for any td elements in the document that contains the string "credits"
                Element ECTS_element_entry=ECTS_element_list.get(0); //lectures with more than one "ECTS credits" do exist, but we will not handle them. Only first entry handled.
                //overwrite
                ECTS_element_list = doc.select("td:contains(credit)"); //select all elements containing "credits
                index = ECTS_element_list.indexOf(ECTS_element_entry);//find index of the element with "ECTS credits"
                Element ECTS_element = ECTS_element_list.get(index+1); //use as credit the immediately following "credit" entry
                String ECTS_only_number = RegExUtils.removeAll(ECTS_element.text(), "( credits| credit)");
                ECTS = Integer.parseInt(ECTS_only_number);
                //Toast.makeText(context, ECTS_only_number, Toast.LENGTH_SHORT).show();
            }
            //create a Lecture object with the just parsed content
            result= new Course(name, lectures, lecturesAndExercises, exercises, labs, course_code, ECTS);
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
