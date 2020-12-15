package com.example.aad2020_vvz_app_graldij_moimfeld.Utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
/**
 used to download the HTML code of the website as a background task (it is not allowed to do downloads in the main thread);
 **/
public class NewThread extends AsyncTask<String, Void, Document> {


    @Override
    public Document doInBackground(String... params) {

        Document doc;
        try {

            doc = Jsoup.connect(params[0]).get();
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
