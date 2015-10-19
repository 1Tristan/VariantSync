package de.ovgu.variantsync.applicationlayer.datamodel.context;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 16.10.2015
 */
public class CodeChange {

	private List<CodeLine> baseVersion;
	private List<CodeLine> newVersion;
	private long timestamp;

	public CodeChange() {
		baseVersion = new ArrayList<CodeLine>();
		newVersion = new ArrayList<CodeLine>();
	}

	public CodeChange(List<CodeLine> baseVersion, List<CodeLine> newVersion) {
		this.baseVersion = baseVersion;
		this.newVersion = newVersion;
		this.timestamp = new Timestamp(new Date().getTime()).getTime();
	}

	public void createTimeStamp() {
		this.timestamp = new Timestamp(new Date().getTime()).getTime();
	}

	/**
	 * @return the baseVersion
	 */
	public List<CodeLine> getBaseVersion() {
		return baseVersion;
	}

	/**
	 * @param baseVersion
	 *            the baseVersion to set
	 */
	public void setBaseVersion(List<CodeLine> baseVersion) {
		this.baseVersion = baseVersion;
	}

	/**
	 * @return the newVersion
	 */
	public List<CodeLine> getNewVersion() {
		return newVersion;
	}

	/**
	 * @param newVersion
	 *            the newVersion to set
	 */
	public void setNewVersion(List<CodeLine> newVersion) {
		this.newVersion = newVersion;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CodeChange [baseVersion=" + baseVersion + ", newVersion="
				+ newVersion + ", timestamp=" + timestamp + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected CodeChange clone() throws CloneNotSupportedException {
		CodeChange copy = new CodeChange();
		copy.setTimestamp(this.timestamp);
		List<CodeLine> baseVersion = new ArrayList<CodeLine>();
		for (CodeLine line : this.baseVersion) {
			baseVersion.add(line.clone());
		}
		copy.setBaseVersion(baseVersion);
		List<CodeLine> newVersion = new ArrayList<CodeLine>();
		for (CodeLine line : this.newVersion) {
			baseVersion.add(line.clone());
		}
		copy.setNewVersion(newVersion);
		return copy;
	}

}
