package com.src.srcapi.service.impl.retrieveManager.algorithm;

import static com.src.srcapi.util.MatrixUtils.fillLevenshteinMatrix;
import static com.src.srcapi.util.StringUtils.stringToChar;

public class LevenshteinDistanceSimilarityCalAlgorithm implements SimilarityCalAlgorithm {
    @Override
    public Float calculateSimilarity(String srcCodeAST, String destCodeAST) {
        int srcCodeASTLength = srcCodeAST.length();
        int destCodeASTLength = destCodeAST.length();

        char[] srcCodeASTCharArr = stringToChar(srcCodeAST.toLowerCase());
        char[] destCodeASTCharArr = stringToChar(destCodeAST.toLowerCase());
        int[][] distanceMatrix = fillLevenshteinMatrix(srcCodeASTCharArr, destCodeASTCharArr);

        for (int i = 1; i < srcCodeASTLength + 1; i++) {
            for (int j = 1; j < destCodeASTLength + 1; j++) {
                if (srcCodeASTCharArr[i - 1] == destCodeASTCharArr[j - 1]) {
                    distanceMatrix[i][j] = distanceMatrix[i - 1][j - 1];
                } else {
                    int tmp = Math.min((distanceMatrix[i - 1][j] + 1), (distanceMatrix[i][j -  1] + 1));
                    distanceMatrix[i][j] = Math.min(tmp, (distanceMatrix[i - 1][j - 1] + 1));
                }
            }
        }

        int levenshteinDistance = distanceMatrix[srcCodeASTLength][destCodeASTLength];
        return Math.abs((1.0f - (levenshteinDistance / ((float) Math.max(srcCodeASTLength, destCodeASTLength)))) * 100.0f) / 100.0f;
    }
}
