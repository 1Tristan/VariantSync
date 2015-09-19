package de.ovgu.variantsync.presentationlayer.view.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.EditorPart;

import de.ovgu.variantsync.applicationlayer.context.IContextOperations;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaClass;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerHandler;
import de.ovgu.variantsync.presentationlayer.view.codemapping.MarkerInformation;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 17.09.2015
 */
public class PartAdapter implements IPartListener {

	private IContextOperations contextOp;

	public PartAdapter(IContextOperations contextOp) {
		this.contextOp = contextOp;
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if (part instanceof IEditorPart) {
			if (((IEditorPart) part).getEditorInput() instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) ((EditorPart) part)
						.getEditorInput()).getFile();
				System.out
						.println("\n====== LOCATION OF ACTIVE FILE IN EDITOR ======");
				System.out.println(file.getLocation());
				System.out
						.println("===============================================");
				String projectName = file.getProject().getName();
				String fileName = file.getName();
				Map<String, List<JavaClass>> classes = contextOp.findJavaClass(
						projectName, fileName);

				MarkerHandler.getInstance().clearAllMarker(file);
				List<MarkerInformation> markers = new ArrayList<MarkerInformation>();
				Set<Entry<String, List<JavaClass>>> set = classes.entrySet();
				Iterator<Entry<String, List<JavaClass>>> it = set.iterator();
				while (it.hasNext()) {
					Entry<String, List<JavaClass>> entry = it.next();
					List<JavaClass> listClasses = entry.getValue();
					for (JavaClass c : listClasses) {
						List<CodeLine> cls = c.getCodeLines();
						int i = 0;
						List<CodeLine> tmp = new ArrayList<CodeLine>();
						for (CodeLine cl : cls) {
							tmp.add(cl);
							if (cls.size() > i + 1
									&& cls.get(i + 1).getLine() == cl.getLine() + 1) {
								tmp.add(cls.get(i + 1));
							} else {
								MarkerInformation mi = new MarkerInformation(0,
										tmp.get(0).getLine(), tmp.get(
												tmp.size() - 1).getLine(), 0, 0);
								mi.setFeature(entry.getKey());
								mi.setColor(contextOp.findColor(entry.getKey()));
								markers.add(mi);
								tmp.clear();
							}
							i++;
						}
					}
					try {
						MarkerHandler.getInstance().setMarker(file, markers);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
	}

	@Override
	public void partClosed(IWorkbenchPart arg0) {
	}

	@Override
	public void partDeactivated(IWorkbenchPart arg0) {
	}

	@Override
	public void partOpened(IWorkbenchPart arg0) {
	}

}
