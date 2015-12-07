package com.example.qldapm.evtranslator.models.repository;

import com.example.qldapm.evtranslator.models.entity.Sentence;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Nhat Huy (ndnhuy) on 21/11/2015.
 */
public interface SentenceRepository {
    Long add(Sentence sentence);
    List<Sentence> findAll();
    HashMap<String, String> findAllAndPutIntoMap();

    void delete(Long id);
}
