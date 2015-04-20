package com.rubean.beantable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import com.rubean.debug.Debug;
import com.rubean.domain.ClientInfo;
import com.rubean.domain.dto.AbstractDTO;
import com.rubean.domain.dto.DTOField;
import com.rubean.ejb.SessionObjectFactory;
import com.rubean.ejb.SessionObjectFactoryException;
import com.rubean.ejb.SessionObjectRemoveException;

// Referenced classes of package com.rubean.beantable:
//            BeanTable, Column, BeanTableException, BeanProvider, 
//            BeanResolver

public class PermataDTOTable extends BeanTable
{
    private class DTOProvider
        implements BeanProvider
    {

        public Collection provideData()
            throws BeanTableException
        {
            try
            {
                ArrayList arraylist = new ArrayList();
                Object obj = getDTOFactory();
                Object obj1 = null;
                try
                {
                    obj1 = obj.getClass().getMethod(finderName, finderParamTypes).invoke(obj, finderParams);
                }
                finally
                {
                    removeDTOFactory(obj);
                }
                if(obj1 != null)
                    if(obj1 instanceof Object[])
                        arraylist.addAll(Arrays.asList((Object[])obj1));
                    else
                        arraylist.add(obj1);
                return arraylist;
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                throw new BeanTableException("error providing data", invocationtargetexception.getTargetException());
            }
            catch(Exception exception)
            {
                throw new BeanTableException("error providing data", exception);
            }
        }

        private DTOProvider()
        {
        }

    }

    private class DTOResolver
        implements BeanResolver
    {

        public void insertBean(Object obj)
            throws BeanTableException
        {
            try
            {
                ((AbstractDTO)obj).validate();
                Object obj1 = getDTOFactory();
                try
                {
                    obj1.getClass().getMethod("insert", new Class[] {
                        obj.getClass(), com.rubean.domain.ClientInfo.class
                    }).invoke(obj1, new Object[] {
                        obj, createClientInfo()
                    });
                }
                finally
                {
                    removeDTOFactory(obj1);
                }
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                throw new BeanTableException("error creating entity", invocationtargetexception.getTargetException());
            }
            catch(Exception exception1)
            {
                throw new BeanTableException("error creating entity", exception1);
            }
        }

        public void updateBean(Object obj, Object obj1)
            throws BeanTableException
        {
            try
            {
                ((AbstractDTO)obj1).validate();
                Object obj2 = getDTOFactory();
                try
                {
                    obj2.getClass().getMethod("update", new Class[] {
                        obj1.getClass(), com.rubean.domain.ClientInfo.class
                    }).invoke(obj2, new Object[] {
                        obj1, createClientInfo()
                    });
                }
                finally
                {
                    removeDTOFactory(obj2);
                }
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                throw new BeanTableException("error updating entity", invocationtargetexception.getTargetException());
            }
            catch(Exception exception1)
            {
                throw new BeanTableException("error updating entity", exception1);
            }
        }

        public void removeBean(Object obj)
            throws BeanTableException
        {
            try
            {
                Object obj1 = obj.getClass().getMethod("createPrimaryKey", null).invoke(obj, null);
                Object obj2 = getDTOFactory();
                try
                {
                    obj2.getClass().getMethod("delete", new Class[] {
                        obj1.getClass(), com.rubean.domain.ClientInfo.class
                    }).invoke(obj2, new Object[] {
                        obj1, createClientInfo()
                    });
                }
                finally
                {
                    removeDTOFactory(obj2);
                }
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                throw new BeanTableException("error deleting entity", invocationtargetexception.getTargetException());
            }
            catch(Exception exception)
            {
                throw new BeanTableException("error deleting entity", exception);
            }
        }

        private DTOResolver()
        {
        }

    }


    public PermataDTOTable()
    {
        finderName = "findAll";
        dtoFields = new HashMap();
        setProvider(new DTOProvider());
        setResolver(new DTOResolver());
    }

    public static void setDefaultTerminal(String s)
    {
        defaultTerminal = s;
    }

    public static void setDefaultUser(String s)
    {
        defaultUser = s;
    }

    public void setTerminal(String s)
    {
        terminal = s;
    }

    public String getTerminal()
    {
        return terminal;
    }

    public void setUser(String s)
    {
        user = s;
    }

    public String getUser()
    {
        return user;
    }

    private ClientInfo createClientInfo()
    {
        return new ClientInfo(user == null ? defaultUser : user, terminal == null ? defaultTerminal : terminal);
    }

    public void setFactoryHomeClassName(String s)
        throws ClassNotFoundException
    {
        homeClassName = s;
        homeClass = Class.forName(s);
    }

    public String getFactoryHomeClassName()
    {
        return homeClassName;
    }

    public int getPrecision(String s)
    {
        DTOField dtofield = (DTOField)dtoFields.get(s);
        if(dtofield != null && dtofield.getPrecision() != null)
            return dtofield.getPrecision().intValue();
        else
            return -1;
    }

    public int getScale(String s)
    {
        DTOField dtofield = (DTOField)dtoFields.get(s);
        if(dtofield != null && dtofield.getScale() != null)
            return dtofield.getScale().intValue();
        else
            return -1;
    }

    public void setBeanClassName(String s)
        throws Exception
    {
        if(s != null)
        {
            Class class1 = Class.forName(s);
            DTOField adtofield[] = ((AbstractDTO)class1.newInstance()).getFields();
            dtoFields.clear();
            for(int i = 0; i < adtofield.length; i++)
                dtoFields.put(adtofield[i].getName(), adtofield[i]);

        }
        super.setBeanClassName(s);
    }

    protected Column createColumn(PropertyDescriptor propertydescriptor)
    {
        Column column = super.createColumn(propertydescriptor);
        DTOField dtofield = (DTOField)dtoFields.get(propertydescriptor.getName());
        if(dtofield != null)
        {
            column.setRequired(dtofield.isRequired());
            if(dtofield.getPrecision() != null)
                column.setPrecision(dtofield.getPrecision().intValue());
            if(dtofield.getScale() != null)
                column.setScale(dtofield.getScale().intValue());
            column.setRowId(dtofield.isRowId());
        }
        return column;
    }

    public void setFinderName(String s)
    {
        finderName = s;
    }

    public String getFinderName()
    {
        return finderName;
    }

    public void setFinderParamTypes(Class aclass[])
    {
        finderParamTypes = aclass;
    }

    public Class[] getFinderParamTypes()
    {
        return finderParamTypes;
    }

    public void setFinderParams(Object aobj[])
    {
        finderParams = aobj;
    }

    public Object[] getFinderParams()
    {
        return finderParams;
    }

    private Object getDTOFactory()
        throws SessionObjectFactoryException
    {
        return SessionObjectFactory.createSessionObject(homeClass);
    }

    private void removeDTOFactory(Object obj)
    {
        try
        {
            SessionObjectFactory.removeSessionObject(obj);
        }
        catch(SessionObjectRemoveException sessionobjectremoveexception)
        {
            Debug.exception(sessionobjectremoveexception);
        }
    }

    private String homeClassName;
    private Class homeClass;
    private String finderName;
    private Class finderParamTypes[];
    private Object finderParams[];
    private HashMap dtoFields;
    private static String defaultTerminal;
    private static String defaultUser;
    private String terminal;
    private String user;






}


