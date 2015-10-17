package de.ovgu.variantsync.presentationlayer.view.mergeprocess;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;
import de.ovgu.variantsync.presentationlayer.controller.FeatureController;

public class FeatureViewHandler extends AbstractHandler {

	FeatureController fc = ControllerHandler.getInstance()
			.getFeatureController();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String featureExpressions[] = fc.getFeatureExpressions().getFeatureExpressions().toArray(new String[]{});

		FeatureView fv = new FeatureView(new Shell(Display.getCurrent(),
				SWT.APPLICATION_MODAL | SWT.SHEET), 0, featureExpressions);
		fv.open();
		return null;
	}
}
