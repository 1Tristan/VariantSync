package de.ovgu.variantsync.presentationlayer.controller.popup.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/*
 public void run(IAction action) {
 ISelectionService service = VariantSyncPlugin.getDefault()
 .getWorkbench().getActiveWorkbenchWindow()
 .getSelectionService();
 IStructuredSelection structured = (IStructuredSelection) service
 .getSelection("org.eclipse.jdt.ui.PackageExplorer");
 IFile file = (IFile) structured.getFirstElement();
 IPath path = file.getLocation();
 System.out.println(path.toPortableString());
 MessageDialog.openInformation(shell, "VariantSync",
 "mapping was executed.");
 }

 */
public class CodeMappingHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Code Mapping Handler");
		// must return null
		return null;
	}

}