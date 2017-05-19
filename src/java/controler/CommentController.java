package controler;

import bean.Comment;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.CommentFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("commentController")
@SessionScoped
public class CommentController implements Serializable {
    
    @EJB
    private service.CommentFacade ejbFacade;
    private List<Comment> items = null;
    private List<Comment> itemsFound;
    private Comment selected;
    
    public void like() {
        selected.setLikes(selected.getLikes() + 1);
        ejbFacade.edit(selected);
        JsfUtil.addSuccessMessage("comment Liked!!!");
    }

    public void send() {
        ejbFacade.create(selected);
        JsfUtil.addSuccessMessage("Merci pour votre message!!!");
    }
    
    public CommentController() {
    }
    
    public void destroy(Comment comment) {
        ejbFacade.remove(comment);
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void delete(Comment item) {
        ejbFacade.remove(item);
    }
    
    public List<Comment> getItemsFound() {
        if (itemsFound == null) {
            itemsFound = new ArrayList<>();
        }
        return itemsFound;
    }
    
    public void setItemsFound(List<Comment> itemsFound) {
        this.itemsFound = itemsFound;
    }
    
    public Comment getSelected() {
        if (selected == null) {
            selected = new Comment();
        }
        return selected;
    }
    
    public void setSelected(Comment selected) {
        this.selected = selected;
    }
    
    protected void setEmbeddableKeys() {
    }
    
    protected void initializeEmbeddableKey() {
    }
    
    private CommentFacade getFacade() {
        return ejbFacade;
    }
    
    public Comment prepareCreate() {
        selected = new Comment();
        initializeEmbeddableKey();
        return selected;
    }
    
    public void create() {
        selected.setDateComment(new Date());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CommentCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CommentUpdated"));
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CommentDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public List<Comment> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public Comment getComment(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    public List<Comment> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }
    
    public List<Comment> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    @FacesConverter(forClass = Comment.class)
    public static class CommentControllerConverter implements Converter {
        
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommentController controller = (CommentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commentController");
            return controller.getComment(getKey(value));
        }
        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Comment) {
                Comment o = (Comment) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Comment.class.getName()});
                return null;
            }
        }
        
    }
    
}
