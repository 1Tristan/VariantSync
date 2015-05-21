package de.ovgu.variantsync.presentationlayer.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import de.ovgu.variantsync.applicationlayer.AbstractModel;
import de.ovgu.variantsync.presentationlayer.view.AbstractView;
import de.ovgu.variantsync.utilitylayer.log.LogOperations;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 18.05.2015
 */
public abstract class AbstractController implements PropertyChangeListener {

	private CopyOnWriteArrayList<AbstractView> registeredViews;
	private CopyOnWriteArrayList<AbstractModel> registeredModels;

	public AbstractController() {

		registeredViews = new CopyOnWriteArrayList<AbstractView>();
		registeredModels = new CopyOnWriteArrayList<AbstractModel>();
	}

	public void addModel(AbstractModel model) {
		registeredModels.add(model);
		model.addPropertyChangeListener(this);
	}

	public void removeModel(AbstractModel model) {
		registeredModels.remove(model);
		model.removePropertyChangeListener(this);
	}

	public void addView(AbstractView view) {
		registeredViews.add(view);
	}

	public void removeView(AbstractView view) {
		registeredViews.remove(view);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		for (AbstractView view : registeredViews) {
			view.modelPropertyChange(evt);
		}
	}

	protected void setModelProperty(String propertyName) {

		for (AbstractModel model : registeredModels) {
			try {
				Method method;
				method = model.getClass().getMethod(propertyName);
				method.invoke(model);

			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LogOperations.logError(e);
				e.printStackTrace();
			}
		}
	}

	protected void setModelProperty(String propertyName, Object newValue) {

		for (AbstractModel model : registeredModels) {
			try {
				Method method;
				method = model.getClass().getMethod(propertyName,
						new Class[] { newValue.getClass() });
				method.invoke(model, newValue);
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LogOperations.logError(e);
				e.printStackTrace();
			}
		}
	}

	protected void setModelProperty(String propertyName, Object newValue,
			Object newValue2) {
		for (AbstractModel model : registeredModels) {
			try {
				Method method;
				method = model.getClass()
						.getMethod(
								propertyName,
								new Class[] { newValue.getClass(),
										newValue2.getClass() });
				method.invoke(model, newValue, newValue2);
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LogOperations.logError(e);
				e.printStackTrace();
			}
		}
	}

	protected void setModelProperty(String propertyName, Object newValue,
			Object newValue2, Object newValue3) {

		for (AbstractModel model : registeredModels) {
			try {
				Method method;
				method = model.getClass().getMethod(
						propertyName,
						new Class[] { newValue.getClass(),
								newValue2.getClass(), newValue3.getClass() });
				method.invoke(model, newValue, newValue2, newValue3);
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LogOperations.logError(e);
				e.printStackTrace();
			}
		}
	}

	protected void setModelProperty(String propertyName, Object newValue,
			Object newValue2, Object newValue3, Object newValue4) {

		for (AbstractModel model : registeredModels) {
			try {
				Method method;
				method = model.getClass().getMethod(
						propertyName,
						new Class[] { newValue.getClass(),
								newValue2.getClass(), newValue3.getClass(),
								newValue4.getClass() });
				method.invoke(model, newValue, newValue2, newValue3, newValue4);
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LogOperations.logError(e);
				e.printStackTrace();
			}
		}
	}
}
