package fr.epita.tf_idf;

import fr.epita.tf_idf.utils.DataUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FrenchStemmerImpl implements FrenchStemmer {

    private static final String PATTERN_VOWELS_STRING = "[aeiouyâàëéêèïîôûù]";
    private static final String PATTERN_VOWELS_2_STRING = PATTERN_VOWELS_STRING + PATTERN_VOWELS_STRING;
    private static final String PATTERN_UI_VOWELS_STRING = PATTERN_VOWELS_STRING + "[ui]" + PATTERN_VOWELS_STRING;
    private static final String PATTERN_WITH_Y_STRING = PATTERN_VOWELS_STRING + "[+y]";
    private static final String PATTERN_WITH_Y_ATER_STRING = "[+y]" + PATTERN_VOWELS_STRING;
    private static final String PATTERN_QU_STRING = "qu";
    private static final String PATTERN_REGION_STRING = PATTERN_VOWELS_STRING + "[^aeiouyâàëéêèïîôûù]";
    private static final String PATTERN_NON_VOWELS_ISSEMENT_STRING = "[^aeiouyâàëéêèïîôûù]issement[s]?";

    private static final Pattern PATTERN_VOWELS_2 = Pattern.compile(PATTERN_VOWELS_2_STRING);
    private static final Pattern PATTERN_UI_VOWELS = Pattern.compile(PATTERN_UI_VOWELS_STRING);
    private static final Pattern PATTERN_WITH_Y = Pattern.compile(PATTERN_WITH_Y_STRING);
    private static final Pattern PATTERN_WITH_Y_AFTER = Pattern.compile(PATTERN_WITH_Y_ATER_STRING);
    private static final Pattern PATTERN_WITH_QU = Pattern.compile(PATTERN_QU_STRING);
    private static final Pattern PATTERN_VOWELS = Pattern.compile(PATTERN_VOWELS_STRING);
    private static final Pattern PATTERN_NON_VOWELS = Pattern.compile(PATTERN_NON_VOWELS_ISSEMENT_STRING);
    private static final Pattern PATTERN_REGION = Pattern.compile(PATTERN_REGION_STRING);


    @Override
    public String getStemmedWord(String word) {
        word = vowelsUpperCase(word);
        WordInformation wordInformation = findRegions(word);
        boolean hasStepRemovedSuffixe;
        boolean specialSufficesFound;
        boolean doSecondStepA;
        boolean doSecondStepB = false;
        boolean doThirdStep = false;
        boolean doFourthStep = true;

        hasStepRemovedSuffixe = replaceSuffixesWithInRegion2(wordInformation, DataUtils.SUFFIXES_ISMES, DataUtils.BLANK_STRING);
        hasStepRemovedSuffixe |= removeOrRemoveSuffixesAti(wordInformation);
        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(wordInformation, DataUtils.SUFFIXES_LOG, DataUtils.SUFFIXE_LOG);
        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(wordInformation, DataUtils.SUFFIXES_TIONS, DataUtils.SUFFIXE_U);
        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(wordInformation, DataUtils.SUFFIXES_ENCE, DataUtils.SUFFIXE_ENT);
        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesEments(wordInformation);
        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesIte(wordInformation);
        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesIv(wordInformation);
        hasStepRemovedSuffixe |= replaceOrRemoveBaicSuffixes(wordInformation);
        specialSufficesFound = removeOrReplaceSpecialSuffixes(wordInformation);

        doSecondStepA = specialSufficesFound | !hasStepRemovedSuffixe;

        if (doSecondStepA) {
            doSecondStepB = !replaceSuffixesWithInPrecededWithConsonant(wordInformation, DataUtils.SUFFIXES_CONJ_I);
        }
        if (doSecondStepB) {
            if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IONS)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_IONS, DataUtils.BLANK_STRING));
                doThirdStep = true;
            }
            doThirdStep |= replaceSuffixesWithInRegion(wordInformation, DataUtils.SUFFIXES_CONJ_E);
            doThirdStep |= replaceSuffixesPrecededEWithInWordRegion(wordInformation, DataUtils.SUFFIXES_CONJ_A);


        }
        if (doThirdStep) {
            doFourthStep = doFourthStep(wordInformation);
        }

        if (doFourthStep) {
            fourthStep(wordInformation);
        }
        finalStep(wordInformation);


        return wordInformation.getNewWord();

    }

    private boolean doFourthStep(WordInformation wordInformation) {
        boolean suffixeRemains = true;
        if (wordInformation.getNewWord().endsWith("y")) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 1) + "i");
            suffixeRemains = false;
        } else if (wordInformation.getNewWord().endsWith("ç")) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 1) + "c");
            suffixeRemains = false;

        }
        return suffixeRemains;
    }

    private void fourthStep(WordInformation wordInformation) {
        if (wordInformation.getNewWord().endsWith("s") && wordInformation.getNewWord().length() > 1
                && !DataUtils.SUFFIXE_AIOUES.contains(wordInformation.getNewWord().charAt(wordInformation.getNewWord().length() - 2) + DataUtils.BLANK_STRING)) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 1));
        }

        if (wordInformation.getNewWord().endsWith("ion")) {

            if ("ts".contains(wordInformation.getNewWord().charAt(wordInformation.getNewWord().length() - 4) + DataUtils.BLANK_STRING)) {
                wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 4));
            } else {
                wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3));
            }
        }

        replaceSuffixesWithInRegion2(wordInformation, DataUtils.SUFFIXES_IER, "i");
        if (wordInformation.getNewWord().endsWith("e")) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 1));
        }
    }

    private void finalStep(WordInformation wordInformation) {
        if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_ENN)
                || wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_ONN)
                || wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_ETT)
                || wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_ELL)
                || wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_EILL)
                ) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 1));
        }

        wordInformation.setNewWord(wordInformation.getNewWord().toLowerCase());
    }

    private boolean replaceOrRemoveBaicSuffixes(WordInformation wordInformation) {
        boolean hasStepRemovedSuffixe;
        hasStepRemovedSuffixe = false;
        if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_EAUX)) {
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_EAUX, DataUtils.SUFFIXE_EAU));
            hasStepRemovedSuffixe = true;
        }
        if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_AUX) && wordInformation.getRegionFirst().endsWith(DataUtils.SUFFIXE_AUX)) {
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_AUX, "al"));
            hasStepRemovedSuffixe = true;

        }
        if (wordInformation.getRegionFirst().endsWith(DataUtils.SUFFIXE_EUSES)) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 5) + DataUtils.SUFFIXE_EUX);
            hasStepRemovedSuffixe = true;

        } else if (wordInformation.getRegionFirst().endsWith(DataUtils.SUFFIXE_EUSE)) {
            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 4) + DataUtils.SUFFIXE_EUX);
            hasStepRemovedSuffixe = true;

        }

        Matcher matcher = PATTERN_NON_VOWELS.matcher(wordInformation.getRegionFirst());
        if (matcher.find()) {
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_ISSEMENTS, DataUtils.BLANK_STRING));
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_ISSEMENT, DataUtils.BLANK_STRING));
            hasStepRemovedSuffixe = true;
        }

        return hasStepRemovedSuffixe;
    }

    private boolean removeOrReplaceSpecialSuffixes(WordInformation wordInformation) {
        Matcher matcher;
        boolean specialSufficesFound = false;

        if (wordInformation.getRegion().endsWith(DataUtils.SUFFIXE_AMMENT)) {
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_AMMENT, "ant"));
            specialSufficesFound = true;
        }
        if (wordInformation.getRegion().endsWith(DataUtils.SUFFIXE_EMMENT)) {
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_EMMENT, DataUtils.SUFFIXE_ENT));
            specialSufficesFound = true;
        }


        matcher = PATTERN_NON_VOWELS.matcher(wordInformation.getRegion());
        if (matcher.find()) {
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_MENTS, DataUtils.BLANK_STRING));
            wordInformation.setNewWord(wordInformation.getNewWord().replace(DataUtils.SUFFIXE_MENT, DataUtils.BLANK_STRING));
            specialSufficesFound = true;
        }
        return specialSufficesFound;
    }

    private WordInformation findRegions(String word) {

        Matcher vowels2 = PATTERN_VOWELS_2.matcher(word);
        Matcher vowels = PATTERN_VOWELS.matcher(word);
        Matcher region = PATTERN_REGION.matcher(word);


        WordInformation wordInformation = new WordInformation(word);
        if (word.startsWith(DataUtils.PREFIX_TAP) || word.startsWith(DataUtils.PREFIX_COL) || word.startsWith(DataUtils.PREFIX_PAR)) {
            wordInformation.setRegion(word.substring(3));
        }


        if (vowels2.find()) {
            wordInformation.setRegion(word.substring(vowels2.end()));
        } else if (vowels.find()) {
            wordInformation.setRegion(word.substring(vowels.end()));
        }

        if (region.find()) {
            wordInformation.setRegionFirst(word.substring(region.end()));
            if (region.find()) {
                wordInformation.setRegionSecond(word.substring(region.end()));
            }

        }

        return wordInformation;
    }

    private String vowelsUpperCase(String word) {

        char[] wordArray = word.toCharArray();
        Matcher uiVowels = PATTERN_UI_VOWELS.matcher(word);
        Matcher y = PATTERN_WITH_Y.matcher(word);
        Matcher yAfter = PATTERN_WITH_Y_AFTER.matcher(word);
        Matcher qu = PATTERN_WITH_QU.matcher(word);

        while (uiVowels.find()) {
            wordArray[uiVowels.start() + 1] = Character.toUpperCase(wordArray[uiVowels.start() + 1]);
        }
        while (y.find()) {
            wordArray[y.start() + 1] = Character.toUpperCase(wordArray[y.start() + 1]);
        }
        while (yAfter.find()) {
            wordArray[yAfter.start()] = Character.toUpperCase(wordArray[yAfter.start()]);
        }

        while (qu.find()) {
            wordArray[qu.start() + 1] = Character.toUpperCase(wordArray[qu.start() + 1]);
        }
        return new String(wordArray);
    }

    private boolean replaceSuffixesWithInRegion2(WordInformation wordInformation, List<String> suffixes, String replaceWith) {
        for (String suffixe : suffixes) {
            if (wordInformation.getRegionSecond().endsWith(suffixe)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, replaceWith));
                return true;
            }
        }
        return false;
    }

    private boolean replaceSuffixesWithInPrecededWithConsonant(WordInformation wordInformation, List<String> suffixes) {
        for (String suffixe : suffixes) {
            if (wordInformation.getNewWord().endsWith(suffixe)
                    && !PATTERN_VOWELS_STRING.contains(wordInformation.getNewWord().charAt(wordInformation.getNewWord().length()
                    - suffixe.length() - 1) + DataUtils.BLANK_STRING)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                return true;
            }
        }
        return false;
    }

    private boolean replaceSuffixesWithInRegion(WordInformation wordInformation, List<String> suffixes) {
        for (String suffixe : suffixes) {
            if (wordInformation.getRegionSecond().endsWith(suffixe)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                return true;
            }
        }
        return false;
    }

    private boolean replaceSuffixesPrecededEWithInWordRegion(WordInformation wordInformation, List<String> suffixes) {
        for (String suffixe : suffixes) {
            if (wordInformation.getNewWord().endsWith(suffixe)) {
                if (wordInformation.getNewWord().charAt(wordInformation.getNewWord().length() - suffixe.length()) == 'e') {
                    wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - suffixe.length() + 1));

                } else {
                    wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - suffixe.length()));
                }
                return true;
            }
        }
        return false;
    }

    private boolean removeOrRemoveSuffixesAti(WordInformation wordInformation) {
        for (String suffixe : DataUtils.SUFFIXES_AT) {
            if (wordInformation.getRegionSecond().endsWith(suffixe)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IC)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_IC + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2));
                    } else {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2) + DataUtils.SUFFIXE_iqU);
                    }

                }
                return true;
            }
        }
        return false;
    }

    private boolean replaceOrRemoveSuffixesIv(WordInformation wordInformation) {
        for (String suffixe : DataUtils.SUFFIXES_IV) {
            if (wordInformation.getRegionSecond().endsWith(suffixe)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_AT)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_AT + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2));
                    }
                    if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IC)) {
                        if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_IC + suffixe)) {
                            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2));
                        } else {
                            wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2) + DataUtils.SUFFIXE_iqU);
                        }

                    }

                }
                return true;
            }
        }
        return false;
    }


    private boolean replaceOrRemoveSuffixesIte(WordInformation wordInformation) {
        for (String suffixe : DataUtils.SUFFIXES_IT) {
            if (wordInformation.getRegionSecond().endsWith(suffixe)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_ABIL)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_ABIL + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3));
                    } else {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3) + DataUtils.SUFFIXE_ABL);
                    }
                } else if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IC)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_IC + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2));
                    } else {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2) + DataUtils.SUFFIXE_iqU);
                    }
                } else if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IV)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_IV + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2));
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean replaceOrRemoveSuffixesEments(WordInformation wordInformation) {
        for (String suffixe : DataUtils.SUFFIXES_EMENT) {
            if (wordInformation.getRegionSecond().endsWith(suffixe)) {
                wordInformation.setNewWord(wordInformation.getNewWord().replace(suffixe, DataUtils.BLANK_STRING));
                if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IV)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_IV + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 2));
                    }
                } else if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_EUS)) {
                    if (wordInformation.getRegionSecond().endsWith(DataUtils.SUFFIXE_EUS + suffixe)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3));
                    } else if (wordInformation.getRegionFirst().contains(DataUtils.SUFFIXE_EUS)) {
                        wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3) + DataUtils.SUFFIXE_EUX);
                    }
                } else if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_ABL + suffixe) || wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_iqU + suffixe)) {
                    wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3));
                } else if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IER + suffixe)) {
                    wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 3));
                } else if (wordInformation.getNewWord().endsWith(DataUtils.SUFFIXE_IERE + suffixe)) {
                    wordInformation.setNewWord(wordInformation.getNewWord().substring(0, wordInformation.getNewWord().length() - 4));
                }
                return true;
            }
        }
        return false;
    }
}
