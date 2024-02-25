package com.src.srcapi.service.impl.retrieveManager.algorithm;

public interface SimilarityCalAlgorithm {
    Float calculateSimilarity(String srcCodeAST, String destCodeAST);
}
