/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.tagging.disambiguation.rules.uk;

import java.io.IOException;

import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.TestTools;
import org.languagetool.language.Ukrainian;
import org.languagetool.tagging.disambiguation.Disambiguator;
import org.languagetool.tagging.disambiguation.MultiWordChunker;
import org.languagetool.tagging.disambiguation.rules.DisambiguationRuleTest;
import org.languagetool.tagging.disambiguation.uk.UkrainianHybridDisambiguator;
import org.languagetool.tagging.disambiguation.xx.DemoDisambiguator;
import org.languagetool.tagging.uk.UkrainianTagger;
import org.languagetool.tokenizers.SRXSentenceTokenizer;
import org.languagetool.tokenizers.uk.UkrainianWordTokenizer;

public class UkrainianDisambiguationRuleTest extends DisambiguationRuleTest {
  
  private UkrainianTagger tagger;
  private UkrainianWordTokenizer tokenizer;
  private SRXSentenceTokenizer sentenceTokenizer;
  private UkrainianHybridDisambiguator disambiguator;
  private DemoDisambiguator demoDisambiguator;
  private Disambiguator chunker;

  @Override
  public void setUp() {
    tagger = new UkrainianTagger();
    tokenizer = new UkrainianWordTokenizer();
    sentenceTokenizer = new SRXSentenceTokenizer(new Ukrainian());
    disambiguator = new UkrainianHybridDisambiguator();
    demoDisambiguator = new DemoDisambiguator();
    chunker = new MultiWordChunker("/uk/multiwords.txt", true);
  }

  public void testRules() throws Exception {
    testDisambiguationRulesFromXML();
  }

  public void testDisambiguator() throws IOException {

    TestTools.myAssert("Танцювати до впаду", 
      "/[null]SENT_START Танцювати/[танцювати]verb:inf:imperf  /[null]null до/[до впаду]<adv>|до/[до]prep:rv_rod  /[null]null " +
      "впаду/[впасти]verb:futr:s:1:perf:v-u|впаду/[до впаду]</adv>",
      tokenizer, sentenceTokenizer, tagger, disambiguator);
    
    TestTools.myAssert("Прийшла Люба додому.", 
      "/[null]SENT_START Прийшла/[прийти]verb:past:f:perf  /[null]null Люба/[Люба]noun:f:v_naz:anim:fname|Люба/[любий]adj:f:v_naz  /[null]null додому/[додому]adv ./[null]null",
       tokenizer, sentenceTokenizer, tagger, demoDisambiguator);

    TestTools.myAssert("Прийшла Люба додому.", 
      "/[null]SENT_START Прийшла/[прийти]verb:past:f:perf  /[null]null Люба/[Люба]noun:f:v_naz:anim:fname  /[null]null додому/[додому]adv ./[null]null",
       tokenizer, sentenceTokenizer, tagger, disambiguator);
      
  }
  
  public void testChunker() throws Exception {
    JLanguageTool lt = new JLanguageTool(new Ukrainian());
    AnalyzedSentence analyzedSentence = lt.getAnalyzedSentence("Для  годиться.");
    AnalyzedSentence disambiguated = chunker.disambiguate(analyzedSentence);
    AnalyzedTokenReadings[] tokens = disambiguated.getTokens();
    
    assertTrue(tokens[1].getReadings().toString().contains("<adv>"));
    assertTrue(tokens[4].getReadings().toString().contains("</adv>"));
  }
  

}


