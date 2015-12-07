package com.example.qldapm.evtranslator.models.entity;

/**
 * Created by Nhat Huy (ndnhuy) on 21/11/2015.
 */
public class Sentence {
    private Long id;
    private String englishSentence;
    private String vietnameseSentence;

    public Sentence() {}

    public Sentence(Long id, String englishSentence, String vietnameseSentence) {
        this.id = id;
        this.englishSentence = englishSentence;
        this.vietnameseSentence = vietnameseSentence;
    }

    public Sentence(String englishSentence, String vietnameseSentence) {
        this.englishSentence = englishSentence;
        this.vietnameseSentence = vietnameseSentence;
    }

    public String getEnglishSentence() {
        return englishSentence;
    }

    public void setEnglishSentence(String englishSentence) {
        this.englishSentence = englishSentence;
    }

    public String getVietnameseSentence() {
        return vietnameseSentence;
    }

    public void setVietnameseSentence(String vietnameseSentence) {
        this.vietnameseSentence = vietnameseSentence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Sentence) && ((Sentence) o).englishSentence.equals(this.englishSentence);
    }

    @Override
    public int hashCode() {
        if (englishSentence != null) {
            return englishSentence.hashCode();
        }

        return super.hashCode();
    }


}
