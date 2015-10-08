package com.laowch.githubtrends.utils;

import android.app.Application;
import android.content.Context;

import com.laowch.githubtrends.MyApplication;
import com.laowch.githubtrends.model.Language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lao on 15/9/25.
 */
public class LanguageHelper {


    static LanguageHelper instance;

    public static synchronized LanguageHelper getInstance() {
        return instance;
    }


    public static void init(Application application) {
        instance = new LanguageHelper(application);
    }


    Language[] allLanguages;
    HashMap<String, Language> languageMap = new HashMap<>();
    List<Language> selectedLanguages = new ArrayList<>();

    Context context;

    private LanguageHelper(Context context) {
        this.context = context;

        String selectedLanguageJson = PreferenceManager.getString(context, "selected_languages", null);
        Language[] selected = MyApplication.getGson().fromJson(selectedLanguageJson, Language[].class);
        if (selected != null && selected.length > 0) {
            selectedLanguages.addAll(Arrays.asList(selected));
        } else {
            selectedLanguages.addAll(getDefaultSelectedLanguage());
        }
    }


    public Language[] getAllLanguages() {
        if (allLanguages != null) {
            return allLanguages;
        }

        try {
            StringBuilder buf = new StringBuilder();

            InputStream inputStream = context.getAssets().open("langs.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            allLanguages = MyApplication.getGson().fromJson(buf.toString(), Language[].class);
        } catch (Exception e) {
        }
        return allLanguages;
    }

    public void setSelectedLanguages(List<Language> languageList) {
        selectedLanguages = languageList;
        saveToPref();
    }

    public void addSelectedLanguages(List<Language> languageList) {
        for (Language language : languageList) {
            if (!selectedLanguages.contains(language)) {
                selectedLanguages.add(language);
            }
        }
        saveToPref();
    }

    public Language[] getSelectedLanguages() {
        return selectedLanguages.toArray(new Language[0]);
    }


    public Language[] getUnselectedLanguages() {
        List<Language> unselectedLanguages = new ArrayList<>();
        for (Language language : getAllLanguages()) {
            if (!selectedLanguages.contains(language)) {
                unselectedLanguages.add(language);
            }
        }

        return unselectedLanguages.toArray(new Language[0]);
    }


    public Language getLanguageByName(String languageName) {
        if (languageMap.size() == 0) {

            for (Language language : getAllLanguages()) {
                languageMap.put(language.name, language);
            }
        }

        return languageMap.get(languageName);

    }


    private void saveToPref() {
        Language[] languages = selectedLanguages.toArray(new Language[0]);
        String languagesJson = MyApplication.getGson().toJson(languages);
        PreferenceManager.putString(context, "selected_languages", languagesJson);
    }

    private List<Language> getDefaultSelectedLanguage() {
        String[] defaultLanguagesName = new String[]{"All Language", "JavaScript", "Java", "Go", "CSS", "Objective-C", "Python", "Swift", "HTML"};

        List<Language> defaultLanguages = new ArrayList<>();
        for (String langNAme : defaultLanguagesName) {
            defaultLanguages.add(getLanguageByName(langNAme));
        }
        return defaultLanguages;
    }
}
