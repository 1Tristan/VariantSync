package de.ovgu.variantsync.applicationlayer.merging;

import java.util.ArrayList;
import java.util.List;

import de.ovgu.variantsync.applicationlayer.AbstractModel;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import difflib.Delta;

/**
 * Receives controller invocation as part of MVC implementation and encapsulates
 * functionality of its package.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 18.05.2015
 */
public class MergeOperationProvider extends AbstractModel implements
		IMergeOperations {

	private MergeCalculation mergeCalculation;

	public MergeOperationProvider() {
		this.mergeCalculation = new MergeCalculation();
	}

	@Override
	public boolean checkConflict(List<Delta> d12, List<Delta> d13) {
		return mergeCalculation.checkConflict(d12, d13);
	}

	@Override
	public List<String> performThreeWayMerge(List<String> fList1,
			List<String> fList2, List<String> fList3) {
		return mergeCalculation.performThreeWayMerge(fList1, fList2, fList3);
	}

	@Override
	public List<CodeLine> doAutoSync(List<CodeLine> newCode,
			List<CodeLine> target) {
		List<String> quell = new ArrayList<String>();
		for (CodeLine cl : newCode) {
			if (cl.isNew())
				quell.add(cl.getCode());
		}
		List<String> targetCode = new ArrayList<String>();
		for (CodeLine cl : target) {
			if (cl.isNew())
				targetCode.add(cl.getCode());
		}
		JDimeWrapper.merge(quell, targetCode);
		List<CodeLine> syncResult = new ArrayList<CodeLine>();
		return syncResult;
	}
}