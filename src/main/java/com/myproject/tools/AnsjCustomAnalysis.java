package com.myproject.tools;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by wei.wang on 2018/3/13.
 * 加载ansj内部词典，用于es关键字检索前期分词
 */
public class AnsjCustomAnalysis {

//private static final Logger logger = LoggerFactory.getLogger(AnsjCustomAnalysis.class);

 static List<String> ansjWords;

 private static Set<String> set;

 static{
   ansjWords = new ArrayList<String>();
   set = new HashSet<String>();
   InputStream in = AnsjCustomAnalysis.class.getClassLoader().getResourceAsStream("data/userAnalysis");
   BufferedReader br;
   InputStreamReader reader = null;
   try{
     reader = new InputStreamReader(in,"UTF-8");
   }catch(Exception e){
//     logger.info("加载ansj词典失败");
   }
   br = new BufferedReader(reader);
   try{
     for(String line = br.readLine();
         line != null; line = br.readLine()){
       set.add(line);
     }
   }catch(Exception e){
//   logger.error("读取ansj词典失败"+e.getMessage());
   }
   for(String str : set){
     ansjWords.add(str);
   }
 }

  static Forest forest = null;
  static {
    NlpAnalysis.parse("");// 加载词典
    try {
      forest = Library.makeForest(AnsjCustomAnalysis.class.getClassLoader().getResourceAsStream("data/userLibrary"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param conditions 检索条件
   * @param length 词典中查出的关键词长度
   * @param wordSize 关键词的最大
   */
   public static void getWords(List<String> conditions,int length,int strLength,int wordSize){
     Set<String> set = new HashSet<>();
     for(String keyWords : conditions){
       String[] keyWord  = keyWords.split(": ",-1);
       if(keyWord.length == 1){
         if(keyWord[0].length()>wordSize){
           continue;
         }
         List<String> word_analysis = NLPAnalysis(keyWord[0]);
         if(word_analysis.size() == 0){
           continue;
         }
         if(word_analysis.size()>strLength){
           word_analysis = word_analysis.subList(0,strLength);
         }
         //根据词典选取其中可能会出现的关键词
         for(String str : ansjWords) {
           for (String word : word_analysis) {
             if (str.contains(word) && !str.equals(word)) {
               if (str.length() < length) {
                 set.add(str);
               }
             }
           }
         }
       }
     }
     conditions.addAll(set);
   }

  /**
   * 最细颗粒分词
   * @return
   */
  private static List<String> NLPAnalysis(String word){
       Result result =  IndexAnalysis.parse(word,forest);
       List<Term> list =  result.getTerms();
       if(list.size() == 0){
         return new ArrayList<>();
       }
       return list.stream().map(s->
       s.getName()
       ).collect(Collectors.toList());
   }


}
