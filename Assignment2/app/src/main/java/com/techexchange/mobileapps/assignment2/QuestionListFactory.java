package com.techexchange.mobileapps.assignment2;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuestionListFactory {

    private static final String TAG = QuestionListFactory.class.getSimpleName();
    private static ArrayList<Question> sQuestionList;

    private QuestionListFactory(){

    }

    public static ArrayList<Question> get(Context context){
        if (sQuestionList == null){
            sQuestionList = createQuestions(context);
        }
        return sQuestionList;
    }

    private static ArrayList createQuestions(Context context){
        Log.d(TAG, "createQuestions: called");
        InputStream ins = context.getResources().openRawResource(context.getResources().getIdentifier("country_list", "raw", context.getPackageName()));
        BufferedReader file = new BufferedReader(new InputStreamReader(ins));
        sQuestionList = new ArrayList<>();

        try{
            ArrayList<String> countryName = new ArrayList<>();
            ArrayList<String> capitalName = new ArrayList<>();
            file.readLine();
            int i = 0;
            while (file.ready()) {
                String[] line = file.readLine().split("\",");
                countryName.add(line[0].substring(1));
                capitalName.add(line[1].substring(1));
                //Log.d(TAG, countryName.get(i) + ", " + capitalName.get(i));
                i++;
            }
            for (int j = 0; j < countryName.size(); j++){
                int[] randomIndices = threeUniqueIndices(j, countryName.size());
                sQuestionList.add(new Question(
                        capitalName.get(j),
                        countryName.get(j),
                        countryName.get(randomIndices[0]),
                        countryName.get(randomIndices[1]),
                        countryName.get(randomIndices[2])));
            }

        } catch (IOException e) {
            Log.d(TAG, "Exception");
        }

        Collections.shuffle(sQuestionList);

        return sQuestionList;
    }

    private static int[] threeUniqueIndices(int i, int size){
        Random rand = new Random();
        int randIndex1 = i;
        int randIndex2 = i;
        int randIndex3 = i;
        while (randIndex1 == i){
            randIndex1 = rand.nextInt(size);
        }
        while(randIndex2 == i || randIndex2 == randIndex1){
            randIndex2 = rand.nextInt(size);
        }
        while(randIndex3 == i || randIndex3 == randIndex1 || randIndex3 == 1){
            randIndex3 = rand.nextInt(size);
        }
        return new int[] {randIndex1, randIndex2, randIndex3};
    }
}