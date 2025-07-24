package module.multiseq.deversitydescriptor.calculate;

import static phylo.msa.util.EvolutionaryProperties.GAP_CHAR;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import fasta.io.FastaReader;
import org.apache.commons.lang3.tuple.Pair;

import module.evoltre.mutation.IMutation;
import module.evoltre.mutation.IMutation4Rec;
import module.evoltre.mutation.MutationOperator;

public class VariantStateObtainer {

	public List<Pair<String, VariantDescriptorElement>> getStateList(File inputFile, String refName)
			throws IOException {
		LinkedHashMap<String, String> readFastaDNASequence = FastaReader.readFastaDNASequence(inputFile, true,
				true);
		String alignedRefGenomeString = null;

		List<Pair<String, String>> alignedSequenceWithoutRefSeq = new ArrayList<>();

		for (Entry<String, String> ele : readFastaDNASequence.entrySet()) {

			String seqName = ele.getKey();
			if ( Objects.equals(seqName, refName)) {
				alignedRefGenomeString = ele.getValue().toUpperCase();
			} else {
				Pair<String, String> twoTuple = Pair.of(ele.getKey(), ele.getValue());
				alignedSequenceWithoutRefSeq.add(twoTuple);
			}
		}

		Objects.requireNonNull(alignedRefGenomeString, "Error! The input alignment should contain refernece genome!");

		String refGenome = alignedRefGenomeString.replace("-", "");
		int refNonAlignedSize = refGenome.length();

		int size = alignedRefGenomeString.length();
		List<Pair<String, VariantDescriptorElement>> ret = new ArrayList<>();
		for (Pair<String, String> twoTuple : alignedSequenceWithoutRefSeq) {
			List<IMutation4Rec> refGenome2queryListOfMutations = null;

			try {
				refGenome2queryListOfMutations = MutationOperator.getPairwiseSeqMutationsGui(refGenome,
						alignedRefGenomeString, twoTuple.getRight(), 1, refNonAlignedSize);
			} catch (InputMismatchException e) {
				System.err.println("Error in process: " + twoTuple.getLeft());
				throw e;
			}

			// 首尾两端缺失的要忽略，不比较
			int countableFromOneBase = 0, countableToOneBase = 0;
			String newString = twoTuple.getRight();
			int index = 0;
			for (int i = 0; i < size; i++) {
				char refChar = alignedRefGenomeString.charAt(i);
				char stringChar = newString.charAt(i);

				if (refChar != GAP_CHAR) {
					index++;
				}

				if (stringChar != GAP_CHAR) {
					countableFromOneBase = index;
					break;
				}
			}
			index = refGenome.length();
			for (int i = size - 1; i > 0; i--) {
				char refChar = alignedRefGenomeString.charAt(i);
				char stringChar = newString.charAt(i);

				if (stringChar != GAP_CHAR) {
					countableToOneBase = index;
					break;
				}
				if (refChar != GAP_CHAR) {
					index--;
				}
			}

			List<IMutation> imutation4Rect2Imutation = MutationOperator
					.imutation4Rect2Imutation(refGenome2queryListOfMutations);

			VariantDescriptorElement variantDescriptorElement = new VariantDescriptorElement();
			variantDescriptorElement.name = twoTuple.getLeft();
			variantDescriptorElement.countableFromOneBase = countableFromOneBase;
			variantDescriptorElement.countableToOneBase = countableToOneBase;
			variantDescriptorElement.mutations = imutation4Rect2Imutation;
			ret.add(Pair.of(twoTuple.getLeft(), variantDescriptorElement));
		}

		return ret;

	}
}
