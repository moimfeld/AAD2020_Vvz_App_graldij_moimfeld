package com.example.aad2020_vvz_app_graldij_moimfeld;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parse extends AsyncTask<String, Void, Document> {


    @Override
    public Document doInBackground(String... params) {

        Document doc;
        try {

            doc = Jsoup.connect(params[0]).get();
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return doc;
        return null;
    }

}
