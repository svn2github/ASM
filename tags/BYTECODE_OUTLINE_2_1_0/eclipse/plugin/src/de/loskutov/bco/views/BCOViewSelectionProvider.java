package de.loskutov.bco.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;

import de.loskutov.bco.BytecodeOutlinePlugin;

/**
 * Adapter for different selection provider in one view - text control and table control.
 *  See <a href="http://forge.objectweb.org/tracker/?func=detail&atid=100023&aid=304424&group_id=23">bug 304424</a>
 *  The main problem is, that
 *  <pre>
 *  getSite().setSelectionProvider(viewSelectionProvider);
 *  </pre>
 *  could be set only once per view, so that we cannot switch existing
 *  selection provider on the fly (or I have no idea how to do this simplier way).
 * @author Andrei
 */
class BCOViewSelectionProvider implements IPostSelectionProvider {

    private IPostSelectionProvider realProvider;
    private List selProviders;
    private ISelection selection;

    public BCOViewSelectionProvider(){
        super();
        selProviders = new ArrayList();
    }

    public void setCurrentSelectionProvider(IPostSelectionProvider provider){
        if(!selProviders.contains(provider)){
            BytecodeOutlinePlugin.log(
                new Exception(
                    "Current selection provider is not registered yet"),
                IStatus.WARNING);
            return;
        }
        realProvider = provider;
        if(selection != null) {
            realProvider.setSelection(selection);
        }
    }

    public void registerSelectionProvider(IPostSelectionProvider provider) {
        if(!selProviders.contains(provider)){
            selProviders.add(provider);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IPostSelectionProvider#addPostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
        for (int i = 0; i < selProviders.size(); i++) {
            IPostSelectionProvider provider = (IPostSelectionProvider) selProviders.get(i);
            provider.addPostSelectionChangedListener(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IPostSelectionProvider#removePostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
        for (int i = 0; i < selProviders.size(); i++) {
            IPostSelectionProvider provider = (IPostSelectionProvider) selProviders.get(i);
            provider.removePostSelectionChangedListener(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        for (int i = 0; i < selProviders.size(); i++) {
            IPostSelectionProvider provider = (IPostSelectionProvider) selProviders.get(i);
            provider.addSelectionChangedListener(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    public ISelection getSelection() {
        return realProvider != null? realProvider.getSelection() : null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        for (int i = 0; i < selProviders.size(); i++) {
            IPostSelectionProvider provider = (IPostSelectionProvider) selProviders.get(i);
            provider.removeSelectionChangedListener(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    public void setSelection(ISelection selection) {
        this.selection = selection;
        if(realProvider != null) {
            realProvider.setSelection(selection);
        }
    }

}