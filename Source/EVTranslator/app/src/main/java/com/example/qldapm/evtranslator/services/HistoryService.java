package com.example.qldapm.evtranslator.services;


import com.example.qldapm.evtranslator.models.entity.Sentence;
import com.example.qldapm.evtranslator.models.repository.SentenceRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nhat Huy (ndnhuy)
 */
public class HistoryService {
    private HashMap<String, String> fakeHistory = new HashMap<String, String>();

    private SentenceRepository sentenceRepo;

    public HistoryService(SentenceRepository sentenceRepo) {

        this.sentenceRepo = sentenceRepo;
        fakeHistory.put("I am stupid", "Tui ngu");
        fakeHistory.put("I can fly", "Tui co the bay");
        fakeHistory.put("What is it?", "Gi vay");
        fakeHistory.put("Who are you?", "Ban la ai?");
        fakeHistory.put("What are you doing?", "Dang lam gi vay?");
        fakeHistory.put("You are dog", "Ban la con cho");
        fakeHistory.put("Kill me now", "Giet tui di");
    }

    public HashMap<String, String> getHistory() {
        return sentenceRepo.findAllAndPutIntoMap();
    }

    public void addToHistory(String english, String vietnamese) {
        sentenceRepo.add(new Sentence(english, vietnamese));
    }

    public List<Sentence> getAllSentences() {
        List<Sentence> sentences = sentenceRepo.findAll();
        Collections.reverse(sentences);
        return sentences;
    }

    public void deleteSentenceById(Long id) {
        sentenceRepo.delete(id);
    }

}
