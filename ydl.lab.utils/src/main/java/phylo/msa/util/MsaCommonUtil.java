package phylo.msa.util;

public class MsaCommonUtil {
    /**
     * 临时对MSA的一些操作，后面放到eGPS里面。
     *
     * @title MSAUtil
     * @createdDate 2020-10-19 21:13
     * @lastModifiedDate 2020-10-19 21:13
     * @author "yudalang"
     * @since 1.7
     *
     */
    public static class MSAUtil {

        private MSAUtil() {
        }

        public static double getPairwiseSequenceDistance(String seq1, String seq2) {
            int length = seq1.length();
            if (length != seq2.length()) {
                throw new IllegalArgumentException("Seq1 and Seq2 should be aligned!");
            }

            boolean isSeq2Insertion = false;
            boolean isSeq2Deletion = false;

            double diffCount = 0;

            for (int i = 0; i < length; i++) {
                char a = seq1.charAt(i);
                char b = seq2.charAt(i);
                if (a != b) {
                    if (a == EvolutionaryProperties.GAP_CHAR) {
                        if (!isSeq2Insertion) {
                            // you can change code here
                            diffCount++;
                            isSeq2Insertion = true;
                        }
                    } else if (b == EvolutionaryProperties.GAP_CHAR) {
                        if (!isSeq2Deletion) {
                            // you can change code here
                            diffCount++;
                            isSeq2Deletion = true;
                        }
                    } else {
                        // you can change code here
                        diffCount++;

                        if (isSeq2Insertion) {
                            isSeq2Insertion = false;
                        }
                        if (isSeq2Deletion) {
                            isSeq2Deletion = false;
                        }
                    }

                } else {

                    if (EvolutionaryProperties.GAP_CHAR == a && b == EvolutionaryProperties.GAP_CHAR) {

                    } else {
                        if (isSeq2Insertion) {
                            isSeq2Insertion = false;
                        }
                        if (isSeq2Deletion) {
                            isSeq2Deletion = false;
                        }
                    }

                }
            }

            return diffCount;
        }

        /**
         *
         * <pre>
         *          ref: ATG
         *  aligned seq: AT-G
         * </pre>
         *
         * If input 1, output3.
         *
         * Of course, JAVA is 0 based!
         *
         * @title getNextRefGenomePositionInAlignment
         * @createdDate 2020-10-25 08:55
         * @lastModifiedDate 2020-10-25 08:55
         * @author yudalang
         * @since 1.7
         *
         * @param length  : The length of aligned sequence with gap.
         * @param currSeq : Aligned sequence with gap
         * @param currPos
         * @return
         * @return int
         */
        public static int getNextRefGenomePositionInAlignment(int length, String currSeq, int currPos) {
            int ret = -1;

            currPos++;

            while (currPos < length) {
                char charAt = currSeq.charAt(currPos);

                if (charAt != EvolutionaryProperties.GAP_CHAR) {
                    return currPos;
                }

                currPos++;
            }

            return ret;
        }

        /**
         *
         * <pre>
         *          ref: ATG
         *  aligned seq: AT-G
         * </pre>
         *
         * If input 1, output0.
         *
         * Of course, JAVA is 0 based!
         *
         * @title getPreviousRefGenomePositionInAlignment
         * @createdDate 2020-10-25 08:59
         * @lastModifiedDate 2020-10-25 08:59
         * @author yudalang
         * @since 1.7
         *
         * @param currSeq : Aligned sequence with gap
         * @param currPos
         * @return int
         */
        public static int getPreviousRefGenomePositionInAlignment(String currSeq, int currPos) {
            int ret = -1;

            currPos--;

            while (currPos > -1) {
                char charAt = currSeq.charAt(currPos);

                if (charAt != EvolutionaryProperties.GAP_CHAR) {
                    return currPos;
                }

                currPos--;
            }

            return ret;
        }

        //	public static double getDistanceOfTwoDiffType(SNPAndInsertionDiffTypeAspect diffType1,
        //			SNPAndInsertionDiffTypeAspect diffType2) {
        //
        //		double ret = 0;
        //
        //		// insitu place
        //		if (diffType1.isDeletion()) {
        //			if (diffType2.isDeletion()) {
        //				if (diffType1.isIfFirstDeletion() && diffType2.isIfFirstDeletion()) {
        //					if (diffType1.getLengthOfDeletionRefer2RefSeq() == diffType2.getLengthOfDeletionRefer2RefSeq()) {
        //						ret++;
        //					}
        //				}
        //			} else {
        //				if (diffType1.isIfFirstDeletion()) {
        //					ret++;
        //				}
        //			}
        //		} else {
        //			if (diffType2.isDeletion()) {
        //				if (diffType2.isIfFirstDeletion()) {
        //					ret++;
        //				}
        //			} else {
        //				ret += QuickDistUtil.getTwoSNPCharDifferenceWithAmbiguousBaseAccording2IntArray(
        //						diffType1.getInsituSite(), diffType2.getInsituSite());
        //			}
        //		}
        //
        //		// insetion String
        //		boolean type1IsNotRight = diffType1.getInsertionContent().isEmpty() && diffType1.isDeletion();
        //		boolean type2IsNotRight = diffType2.getInsertionContent().isEmpty() && diffType2.isDeletion();
        //
        //		if (type1IsNotRight || type2IsNotRight) {
        //			// 其中之一有问题都不行
        //		} else {
        ////			if (!diffType1.getInsertionContent().equals(diffType2.getInsertionContent())) {
        ////				ret ++;
        ////			}
        //			// 考虑模糊碱基
        //			if (!QuickDistUtil.judgeTwoAllelesIdentities(diffType1.getInsertionContent(),
        //					diffType2.getInsertionContent())) {
        //				ret++;
        //			}
        //		}
        //		return ret;
        //	}


    }
}
