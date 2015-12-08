package com.example.qldapm.evtranslator.services;

import android.content.Context;
import com.example.qldapm.evtranslator.EWord;
import com.example.qldapm.evtranslator.SentenceNLP.SentBARQ;
import com.example.qldapm.evtranslator.SentenceNLP.Sentence;
import com.example.qldapm.evtranslator.SentenceNLP.SentenceQuestion;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

/**
 * @author Nhat Huy (ndnhuy)
 */
public class TranslatorService {
    Context context;
    GlobalVariables global;

    public TranslatorService(Context ct) {
        this.context = ct;
        global = GlobalVariables.getInstance();
    }

    private TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    public Tree parseTree(String enSentence) {
        Tree tree = null;
        List<CoreLabel> tokens = tokenize(enSentence);
        try {
            LexicalizedParser parser = global.getParser();
            if(parser != null){
                tree = parser.apply(tokens);
            }
        }
        finally{
        }
        return tree;
    }

    private List<CoreLabel> tokenize(String str) {
        edu.stanford.nlp.process.Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(str));
        return tokenizer.tokenize();
    }

    public String toVietnamese(String englishSentence) {
        String vnSentence="";
        Tree tree = parseTree(englishSentence);
        if(tree != null) {
            ArrayList<EWord> eWords = setListWords(tree);
            String Sent_TYPE = tree.firstChild().label().value();
            Sentence sentence = null;
            if(Sent_TYPE.equals("S")){
                sentence = new Sentence(eWords);
            }else {
                if(Sent_TYPE.equals("SQ")){
                    sentence = new SentenceQuestion(eWords);
                }else{
                    if(Sent_TYPE.equals("SBARQ")){
                        sentence = new SentBARQ(eWords);
                    }
                }
            }
            if(sentence !=null){
                sentence.setListNounPhrase(ListNounPhrase(tree));
                sentence.arrageSequence();
                vnSentence = sentence.traslate();
            }
        }
        return vnSentence;
    }

    public ArrayList<String> ListNounPhrase(Tree root){
        ArrayList<String> res = new ArrayList<>();
        for(Tree phrase : root) {
            if (phrase.label().value().equals("NP")) {
                res.add(phrase.toString());
            }
        }
        return res;
    }
    // chuyen tung la thanh tung EWord co day du: tu tieng anh, nghia tieng viet, POS tag
    public ArrayList<EWord> setListWords(Tree root){
        ArrayList<EWord> res = new ArrayList<EWord>();
        for(Tree leaf : root){
            if(leaf.isPreTerminal()){
                EWord eword = new EWord(leaf, root);
                global.getDb_ev().addMeaming_EWord(eword);
                res.add(eword);
            }
        }
        return res;
    }
}
