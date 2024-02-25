package com.src.srcapi.service.impl.retrieveManager.algorithm;

import com.src.srcapi.util.MatrixUtils;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

public class SmithWatermanSimilarityCalAlgorithm implements SimilarityCalAlgorithm {

    private final Integer spaceDisplaceScore = -4;

    private final Integer matchScore = 4;

    private final Integer mismatchScore = -3;

    private int[][] scoreMatrix;

    private boolean[][] isCalculated;

    private static final ThreadLocal<StringBuilder> srcSequence = new ThreadLocal<>();

    private static final ThreadLocal<StringBuilder> destSequence = new ThreadLocal<>();

    @Override
    public Float calculateSimilarity(String srcCodeAST, String destCodeAST) {
        int srcCodeASTLength = srcCodeAST.length();
        int destCodeASTLength = destCodeAST.length();
        srcSequence.set(new StringBuilder());
        destSequence.set(new StringBuilder());
        scoreMatrix = new int[srcCodeASTLength + 1][destCodeASTLength + 1];
        isCalculated = new boolean[srcCodeASTLength + 1][destCodeASTLength + 1];
        recursionCalculateScoreMatrix(srcCodeAST, destCodeAST, srcCodeASTLength, destCodeASTLength);
        Pair<Integer, Integer> maxScoreIndexPair = MatrixUtils.findMaxNumInMatrix(scoreMatrix);
        traceBack(srcCodeAST, destCodeAST, maxScoreIndexPair.getKey(), maxScoreIndexPair.getValue());
        return (float) srcSequence.get().length() / (float) srcCodeASTLength;
    }

    private void recursionCalculateScoreMatrix(String srcCodeAST, String destCodeAST, int srcASTLength, int destASTLength){
        if(srcASTLength == 0)
            scoreMatrix[srcASTLength][destASTLength] = 0;
        else if(destASTLength == 0)
            scoreMatrix[srcASTLength][destASTLength] = 0;
        else{
            if(!isCalculated[srcASTLength - 1][destASTLength - 1])
                recursionCalculateScoreMatrix(srcCodeAST, destCodeAST, srcASTLength -1, destASTLength -1);
            if(!isCalculated[srcASTLength][destASTLength - 1])
                recursionCalculateScoreMatrix(srcCodeAST, destCodeAST, srcASTLength, destASTLength -1);
            if(!isCalculated[srcASTLength - 1][destASTLength])
                recursionCalculateScoreMatrix(srcCodeAST, destCodeAST, srcASTLength -1, destASTLength);
            List<Integer> scoreArray = null;
            if(srcCodeAST.charAt(srcASTLength -1) == destCodeAST.charAt(destASTLength -1)) {
                scoreArray = Arrays.asList(scoreMatrix[srcASTLength - 1][destASTLength - 1] + matchScore, scoreMatrix[srcASTLength][destASTLength - 1] + spaceDisplaceScore, scoreMatrix[srcASTLength - 1][destASTLength] + spaceDisplaceScore);
            } else {
                scoreArray = Arrays.asList(scoreMatrix[srcASTLength - 1][destASTLength - 1] + mismatchScore, scoreMatrix[srcASTLength][destASTLength - 1] + spaceDisplaceScore, scoreMatrix[srcASTLength - 1][destASTLength] + spaceDisplaceScore);
            }
            Integer maxScore = scoreArray.stream().max(Integer::compare).get();
            scoreMatrix[srcASTLength][destASTLength] = (maxScore < 0) ? 0 : maxScore;
        }
        isCalculated[srcASTLength][destASTLength] = true;
    }

    private void traceBack(String srcCodeAST, String destCodeAST, int i, int j){
        StringBuilder srcSimilarSequence = srcSequence.get();
        StringBuilder destSimilarSequence = destSequence.get();
        if(scoreMatrix[i][j] == 0)
            return;
        if(scoreMatrix[i][j] == scoreMatrix[i -1][j] + spaceDisplaceScore) {
            srcSimilarSequence.append(srcCodeAST.charAt(i -1));
            destSimilarSequence.append('-');
            traceBack(srcCodeAST, destCodeAST, i - 1, j);
        }
        else if(scoreMatrix[i][j] == scoreMatrix[i][j -1] + spaceDisplaceScore) {
            srcSimilarSequence.append('-');
            destSimilarSequence.append(destCodeAST.charAt(j -1));
            traceBack(srcCodeAST, destCodeAST, i, j - 1);
        }
        else {
            srcSimilarSequence.append(srcCodeAST.charAt(i - 1));
            destSimilarSequence.append(destCodeAST.charAt(j -1));
            traceBack(srcCodeAST, destCodeAST, i - 1, j - 1);
        }
    }
}
