package de.ovgu.variantsync.applicationlayer.datamodel.resources;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.ovgu.variantsync.VariantSyncConstants;

/**
 * Represents a changed file. Contains file informations and patch informations.
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 15.05.2015
 */
public class ResourceChangesFilePatch implements IChangedFile {

	private String patchFileName;
	private IProject project;
	private File file;
	private boolean synchro = false;
	private long timestamp;
	private String unidiff;
	private IChangedFile parent;
	private String status;

	public ResourceChangesFilePatch(String name, IProject project) {
		this.patchFileName = name;
		this.project = project;
		String infoTxt[] = this.patchFileName.split("_");
		if (infoTxt.length > 2) {
			String time = infoTxt[infoTxt.length - 1];
			this.timestamp = Long.parseLong(time);
		}
		this.synchro = infoTxt[infoTxt.length - 2].equals("1");
	}

	public String getUnidiff() {
		return unidiff;
	}

	public void setUnidiff(String unidiff) {
		this.unidiff = unidiff;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isSynchronized() {
		return this.synchro;
	}

	@Override
	public ArrayList<IChangedFile> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return this.patchFileName;
	}

	@Override
	public IChangedFile getParent() {
		return this.parent;
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJS_WARN_TSK);
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public void addChildren(IChangedFile child) {
	}

	@Override
	public void setParent(IChangedFile parent) {
		this.parent = parent;

	}

	@Override
	public String getTime() {
		String timetxt[] = this.patchFileName.split("_");
		if (timetxt.length > 2) {
			String time = timetxt[timetxt.length - 1];
			Date date = new Date(Long.parseLong(time));
			return DateFormat.getDateTimeInstance().format(date);
		}
		return "";
	}

	public long getTimeStamp() {
		return this.timestamp;
	}

	@Override
	public void linkFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	@Override
	public String getPath() {
		if (file != null) {
			if (this.status.equals(ChangeTypes.ADDFILE)
					|| this.status.equals(ChangeTypes.REMOVEFILE)
					|| this.status.equals(ChangeTypes.CHANGE)) {
				String path = file.getPath();
				path = path.substring(path
						.indexOf(VariantSyncConstants.ADMIN_FOLDER)
						+ VariantSyncConstants.ADMIN_FOLDER.length());
				String pathtile[] = path.split("_");
				String infoText = "_" + pathtile[pathtile.length - 3] + "_"
						+ pathtile[pathtile.length - 2] + "_"
						+ pathtile[pathtile.length - 1];
				path = path.substring(0, path.indexOf(infoText));
				return path;
			} else {
				String path = file.getParentFile().getPath();
				path = path.substring(path
						.indexOf(VariantSyncConstants.ADMIN_FOLDER)
						+ VariantSyncConstants.ADMIN_FOLDER.length());
				return path;
			}
		}
		return null;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	public IProject getProject() {
		return this.project;
	}

	public String getPatchFileName() {
		return patchFileName;
	}

	public static Comparator<IChangedFile> timeComparator = new Comparator<IChangedFile>() {

		@Override
		public int compare(IChangedFile o1, IChangedFile o2) {
			if (o1 instanceof ResourceChangesFilePatch
					&& o2 instanceof ResourceChangesFilePatch) {
				return (int) (((ResourceChangesFilePatch) o1).getTimeStamp() - ((ResourceChangesFilePatch) o2)
						.getTimeStamp());
			}
			if (o1 instanceof ResourceChangesFile
					&& o2 instanceof ResourceChangesFilePatch) {
				return 1;
			}
			if (o1 instanceof ResourceChangesFolder
					&& o2 instanceof ResourceChangesFilePatch) {
				return 1;
			}
			return 0;
		}
	};

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceChangesFilePatch [patchFileName=" + patchFileName
				+ ", project=" + project + ", file=" + file + ", synchro="
				+ synchro + ", timestamp=" + timestamp + ", unidiff=" + unidiff
				+ ", parent=" + parent + ", status=" + status + "]";
	}
}
