package com.example.qldapm.evtranslator.domain.domainservice;


import java.util.HashMap;

/**
 * @author Nhat Huy (ndnhuy)
 */
public class HistoryService {
    private HashMap<String, String> fakeHistory = new HashMap<String, String>();

    public HistoryService() {
        fakeHistory.put("I am stupid", "Tui ngu");
        fakeHistory.put("I can fly", "Tui co the bay");
        fakeHistory.put("What is it?", "Gi vay");
        fakeHistory.put("Who are you?", "Ban la ai?");
        fakeHistory.put("What are you doing?", "Dang lam gi vay?");
        fakeHistory.put("You are dog", "Ban la con cho");
        fakeHistory.put("Kill me now", "Giet tui di");
    }

    public HashMap<String, String> getHistory() {
        return fakeHistory;
    }

    public void addToHistory(String english, String vietnamese) {
        fakeHistory.put(english, vietnamese);
    }
}
