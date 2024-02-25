package com.src.srcapi.util;

import javafx.util.Pair;

public class MatrixUtils {
    public static int[][] fillLevenshteinMatrix(char[] s1, char[] s2) {
        int[][] matrix = new int[s1.length + 1][s2.length + 1];

        for (int i = 0; i < s1.length + 1; i++) {
            matrix[i][0] = i;
        }

        for (int j = 0; j < s2.length + 1; j++) {
            matrix[0][j] = j;
        }

        return matrix;
    }

    public static Pair<Integer, Integer> findMaxNumInMatrix(int[][] matrix){
        int nowMax = matrix[0][0];
        Pair<Integer, Integer> res = new Pair<>(0, 0);
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] > nowMax) {
                    nowMax = matrix[i][j];
                    res = new Pair<>(i, j);
                }
            }
        }
        return res;
    }
}
