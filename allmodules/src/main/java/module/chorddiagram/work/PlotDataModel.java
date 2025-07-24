package module.chorddiagram.work;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import utils.string.EGPSStringUtil;
import graphic.engine.colors.EGPSColors;

public class PlotDataModel {

	int numberOfColumn = 2;

	List<ChordDiagram4WntBean> chordData = Lists.newArrayList();

	public void parseData(List<String> entityList, List<String> interactionList, int numOfColumn) {
		this.numberOfColumn = numOfColumn;

		// get a entity map
		Map<String, BioEntity> entityMap = Maps.newLinkedHashMap();
		for (String entityStr : entityList) {
			String[] parts = EGPSStringUtil.split(entityStr, '\t');
			if (parts.length != 3) {
				throw new InputMismatchException("Please check your entity info. file.");
			}
			BioEntity entity = new BioEntity();
			entity.setName(parts[0]);
			entity.setLength(Integer.parseInt(parts[1]));
			entity.setColor(EGPSColors.parseColor(parts[2]));
			entityMap.put(entity.getName(), entity);
		}

		List<List<String>> blocks = parseBlocks(interactionList);
		for (List<String> list : blocks) {

			List<BioInteraction> bioInteractions = new ArrayList<>();
			Set<String> entitySet = Sets.newHashSet();
			String chordDiagramName = null;
			for (String interactionStr : list) {
				if (interactionStr.startsWith("###")) {
					int indexOf = interactionStr.indexOf('\t');
					if (indexOf > -1) {
						chordDiagramName = interactionStr.substring(indexOf + 1);
					}
					continue;
				}
				String[] parts = EGPSStringUtil.split(interactionStr, '\t');
				if (parts.length != 6) {
					throw new InputMismatchException("Please check your interaction info. file.");
				}
				BioInteraction interaction = getBioInteraction(parts, entityMap);
				bioInteractions.add(interaction);
				entitySet.add(interaction.getSource().getName());
				entitySet.add(interaction.getTarget().getName());
			}

			List<BioEntity> bioEntities = new ArrayList<>();
			for (Entry<String, BioEntity> entry : entityMap.entrySet()) {
				if (entitySet.contains(entry.getValue().getName())) {
					bioEntities.add(entry.getValue());
				}
			}

			ChordDiagram4WntBean chordDiagram = new ChordDiagram4WntBean(bioEntities, bioInteractions);
			if (chordDiagramName != null) {
				chordDiagram.setName(chordDiagramName);
			}
			chordData.add(chordDiagram);
		}

	}

	private BioInteraction getBioInteraction(String[] parts, Map<String, BioEntity> entityMap) {
		String sourceName = parts[0];
		String targetName = parts[3];
		BioInteraction interaction = new BioInteraction();
		BioEntity source = entityMap.get(sourceName);
		if (source == null) {
			throw new InputMismatchException(
					"The entity name is not found int the entity info. file: ".concat(sourceName));
		}
		source.setName(sourceName);
		interaction.setSource(source);
		interaction.setSourceStartPosition(Integer.parseInt(parts[1]));
		interaction.setSourceEndPosition(Integer.parseInt(parts[2]));
		BioEntity target = entityMap.get(targetName);
		if (target == null) {
			throw new InputMismatchException(
					"The entity name is not found int the entity info. file: ".concat(targetName));
		}
		target.setName(targetName);
		interaction.setTarget(target);
		interaction.setTargetStartPosition(Integer.parseInt(parts[4]));
		interaction.setTargetEndPosition(Integer.parseInt(parts[5]));
		return interaction;
	}

	public List<List<String>> parseBlocks(List<String> input) {
		List<List<String>> result = new ArrayList<>();
		List<String> currentBlock = new ArrayList<>();

		for (String line : input) {
			if (line.startsWith("###")) {
				// If we encounter a new block and the current block is not empty, add it to the
				// result
				if (!currentBlock.isEmpty()) {
					result.add(new ArrayList<>(currentBlock));
					currentBlock.clear(); // Clear the current block for the next set of lines
				}
				// Add the block header (### line) to the new block
				currentBlock.add(line);
			} else if (!line.trim().isEmpty()) {
				// Add non-empty lines to the current block
				currentBlock.add(line);
			}
		}

		// Add the last block if it exists
		if (!currentBlock.isEmpty()) {
			result.add(currentBlock);
		}

		return result;
	}

	public int getNumberOfColumn() {
		return numberOfColumn;
	}

	public void setNumberOfColumn(int numberOfColumn) {
		this.numberOfColumn = numberOfColumn;
	}

	public List<ChordDiagram4WntBean> getChordData() {
		return chordData;
	}

	public void setChordData(List<ChordDiagram4WntBean> chordData) {
		this.chordData = chordData;
	}

}
