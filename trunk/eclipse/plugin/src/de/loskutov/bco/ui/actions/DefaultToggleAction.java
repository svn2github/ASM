/*******************************************************************************
 * Copyright (c) 2006 Andrei Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/bsd-license.php
 * Contributor:  Andrei Loskutov - initial API and implementation
 *******************************************************************************/
package de.loskutov.bco.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.loskutov.bco.BytecodeOutlinePlugin;


/**
 * Default action which could be used as template for "toggle" action.
 * Action image, text and tooltip will be initialized by default.
 * To use it, register IPropertyChangeListener and check for IAction.CHECKED
 * event name.
 * @author Andrei
 */
public abstract class DefaultToggleAction extends Action implements IPropertyChangeListener {

    private static final String ACTION = "action";
    boolean avoidUpdate;
    private final IPreferenceStore store;

    public DefaultToggleAction(final String id) {
        this(id, true);
    }

    public DefaultToggleAction(final String id, final boolean addPreferenceListener) {
        super();
        setId(id);
        init();

        IPreferenceStore prefStore = BytecodeOutlinePlugin.getDefault().getPreferenceStore();

        boolean isChecked = prefStore.getBoolean(id);
        setChecked(isChecked);
        if(addPreferenceListener) {
            this.store = prefStore;
            prefStore.addPropertyChangeListener(this);
        } else {
            this.store = null;
        }
    }

    public void propertyChange(final PropertyChangeEvent event){
        if(store == null){
            return;
        }
        String id = getId();
        if(!id.equals(event.getProperty())){
            return;
        }
        boolean isChecked = store.getBoolean(id);
        setChecked(isChecked);
        // The action state can be changed from preference page (therefore run()), but...
        // see http://forge.objectweb.org/tracker/?func=detail&atid=100023&aid=311888&group_id=23
        // this causes multiple unneeded re-syncs of the compare editor
        if(!avoidUpdate) {
            run(isChecked);
        }
    }

    public void dispose(){
        if(store != null) {
            store.removePropertyChangeListener(this);
        }
    }

    private void init(){
        String myId = getId();
        if(myId != null && myId.startsWith("diff_")) {
            myId = myId.substring("diff_".length());
        }
        setImageDescriptor(AbstractUIPlugin
            .imageDescriptorFromPlugin(
                BytecodeOutlinePlugin.getDefault().getBundle()
                    .getSymbolicName(),
                BytecodeOutlinePlugin
                    .getResourceString(ACTION + "." + myId + "." + IMAGE)));

        setText(BytecodeOutlinePlugin
            .getResourceString(ACTION + "." + myId + "." + TEXT));
        setToolTipText(BytecodeOutlinePlugin
            .getResourceString(ACTION + "." + myId + "." + TOOL_TIP_TEXT));
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public final void run() {
        boolean isChecked = isChecked();
        avoidUpdate = true;
        // compare dialog: we use store as global variables to remember the state
        BytecodeOutlinePlugin.getDefault().getPreferenceStore().setValue(getId(), isChecked);
        avoidUpdate = false;
        run(isChecked);
    }

    public abstract void run(boolean newState);
}
