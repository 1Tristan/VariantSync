package de.ovgu.variantsync.persistancelayer;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.ovgu.variantsync.applicationlayer.datamodel.context.Context;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 12.09.2015
 */
class JaxbOperations {

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static Context loadContext(String filename) {
		try {
			JAXBContext context = JAXBContext.newInstance(Context.class);
			Unmarshaller un = context.createUnmarshaller();
			Context c = (Context) un.unmarshal(new File(filename));
			return c;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param filename
	 */
	public static void writeContext(Context context, String filename) {
		try {
			JAXBContext c = JAXBContext.newInstance(Context.class);
			Marshaller m = c.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(context, new File(filename));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
