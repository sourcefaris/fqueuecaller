package com.rubean.beantable;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.*;


public class BeanTable
 implements EditorListener
{

 public BeanTable()
 {
     beans = new ArrayList();
     propertyDescriptors = new HashMap();
     columns = new HashMap();
     calculatedColumnMap = new HashMap();
     navigationListeners = new ArrayList();
     statusListeners = new ArrayList();
     errorListeners = new ArrayList();
     dataChangeListeners = new ArrayList();
     changedEditors = new HashSet();
 }

 public void addErrorListener(ErrorListener errorlistener)
 {
     errorListeners.add(errorlistener);
 }

 public void removeErrorListener(ErrorListener errorlistener)
 {
     errorListeners.remove(errorlistener);
 }

 protected void fireErrorEvent(BeanTableException beantableexception)
 {
     for(int i = 0; i < errorListeners.size(); i++)
         ((ErrorListener)errorListeners.get(i)).error(new ErrorEvent(this, beantableexception));

 }

 public void addNavigationListener(NavigationListener navigationlistener)
 {
     navigationListeners.add(navigationlistener);
 }

 public void removeNavigationListener(NavigationListener navigationlistener)
 {
     navigationListeners.remove(navigationlistener);
 }

 public void fireNavigationEvent()
 {
     for(int i = 0; i < navigationListeners.size(); i++)
         ((NavigationListener)navigationListeners.get(i)).navigated(new NavigationEvent(this));

 }

 public void addStatusListener(StatusListener statuslistener)
 {
     statusListeners.add(statuslistener);
 }

 public void removeStatusListener(StatusListener statuslistener)
 {
     statusListeners.remove(statuslistener);
 }

 private void fireStatusEvent(int i)
 {
     for(int j = 0; j < statusListeners.size(); j++)
         ((StatusListener)statusListeners.get(j)).statusChanged(new StatusEvent(this, i));

 }

 private void fireStatusEvent(int i, int j)
 {
     for(int k = 0; k < statusListeners.size(); k++)
         ((StatusListener)statusListeners.get(k)).statusChanged(new StatusEvent(this, i, j));

 }

 public void addDataChangeListener(DataChangeListener datachangelistener)
 {
     dataChangeListeners.add(datachangelistener);
 }

 public void removeDataChangeListener(DataChangeListener datachangelistener)
 {
     dataChangeListeners.remove(datachangelistener);
 }

 private void fireDataChangeEvent()
 {
     for(int i = 0; i < dataChangeListeners.size(); i++)
         ((DataChangeListener)dataChangeListeners.get(i)).dataChanged(new DataChangeEvent(this));

 }

 private void fireDataChangeEvent(int i, String s, int j)
 {
     for(int k = 0; k < dataChangeListeners.size(); k++)
         ((DataChangeListener)dataChangeListeners.get(k)).dataChanged(new DataChangeEvent(this, i, s, j));

 }

 public void registerEditor(Editor editor)
 {
     editor.addEditorListener(this);
 }

 public void unregisterEditor(Editor editor)
 {
     editor.removeEditorListener(this);
 }

 public void setSortDescriptor(SortDescriptor sortdescriptor)
 {
     if(inserting || editing)
         throw new IllegalStateException("cannot sort when editing or inserting");
     sortDescriptor = sortdescriptor;
     if(open && sort())
         fireDataChangeEvent();
 }

 public SortDescriptor getSortDescriptor()
 {
     return sortDescriptor;
 }

 private boolean sort()
 {
     if(sortDescriptor == null)
         return false;
     boolean flag = false;
     for(int i = 0; i < sortDescriptor.getSortColumns().length; i++)
     {
         Column column = getColumn(sortDescriptor.getSortColumns()[i]);
         if(column.getType() == (java.lang.String.class))
             flag = true;
         if(!column.getType().isPrimitive() && !(java.lang.Comparable.class).isAssignableFrom(column.getType()) || column.getType() == Boolean.TYPE)
             return false;
     }

     Object aobj[][] = new Object[beans.size()][sortDescriptor.getSortColumns().length + 1];
     for(int j = 0; j < beans.size(); j++)
     {
         for(int k = 0; k < sortDescriptor.getSortColumns().length; k++)
             try
             {
                 aobj[j][k] = getValue(sortDescriptor.getSortColumns()[k], j);
             }
             catch(BeanTableException beantableexception)
             {
                 return false;
             }

         aobj[j][sortDescriptor.getSortColumns().length] = beans.get(j);
     }

     final Collator collator = flag ? Collator.getInstance() : null;
     Arrays.sort(((Object []) (aobj)), new Comparator() {

         public int compare(Object obj1, Object obj2)
         {
             Object aobj1[] = (Object[])obj1;
             Object aobj2[] = (Object[])obj2;
             int j1 = 0;
             for(int k1 = 0; k1 < aobj1.length - 1; k1++)
             {
                 if(aobj1[k1] == null && aobj2[k1] == null)
                     j1 = 0;
                 else
                 if(aobj1[k1] == null)
                     j1 = 1;
                 else
                 if(aobj2[k1] == null)
                     j1 = -1;
                 else
                 if(aobj1[k1] instanceof String)
                     j1 = collator.compare(aobj1[k1], aobj2[k1]);
                 else
                     j1 = ((Comparable)aobj1[k1]).compareTo(aobj2[k1]);
                 if(j1 != 0)
                     return j1;
             }

             return j1;
         }

     });
     boolean flag1 = false;
     for(int l = 0; l < aobj.length; l++)
     {
         int i1 = sortDescriptor.isSortDescending() ? aobj.length - 1 - l : l;
         Object obj = aobj[i1][sortDescriptor.getSortColumns().length];
         if(beans.get(l) != obj)
         {
             beans.set(l, obj);
             flag1 = true;
         }
     }

     return flag1;
 }

 public void open()
     throws BeanTableException
 {
     if(!open)
     {
         if(provider == null)
             exception(new BeanTableException("no provider available"));
         java.util.Collection collection = null;
         try
         {
             collection = provider.provideData();
         }
         catch(BeanTableException beantableexception)
         {
             exception(beantableexception);
         }
         beans.clear();
         beans.addAll(collection);
         open = true;
         sort();
         fireStatusEvent(1);
     }
 }

 public void close()
 {
     if(open)
     {
         beans.clear();
         open = false;
         editing = false;
         inserting = false;
         currentRow = 0;
         backupBean = null;
         fireStatusEvent(2);
     }
 }

 public void setProvider(BeanProvider beanprovider)
 {
     provider = beanprovider;
 }

 public BeanProvider getProvider()
 {
     return provider;
 }

 public void setResolver(BeanResolver beanresolver)
 {
     resolver = beanresolver;
 }

 public BeanResolver getResolver()
 {
     return resolver;
 }

 public void setBeanClassName(String s)
     throws Exception
 {
     if(s == null)
     {
         beanInfo = null;
         propertyDescriptors.clear();
         columns.clear();
     } else
     {
         beanInfo = Introspector.getBeanInfo(Class.forName(s));
         propertyDescriptors.clear();
         columns.clear();
         PropertyDescriptor apropertydescriptor[] = beanInfo.getPropertyDescriptors();
         for(int i = 0; i < apropertydescriptor.length; i++)
             if(apropertydescriptor[i].getReadMethod() != null)
             {
                 propertyDescriptors.put(apropertydescriptor[i].getName(), apropertydescriptor[i]);
                 columns.put(apropertydescriptor[i].getName(), createColumn(apropertydescriptor[i]));
             }

         fireStatusEvent(8);
     }
     beanClassName = s;
 }

 protected Column createColumn(PropertyDescriptor propertydescriptor)
 {
     Column column = new Column();
     column.setName(propertydescriptor.getName());
     column.setType(propertydescriptor.getPropertyType());
     column.setRequired(false);
     column.setPrecision(-1);
     column.setScale(-1);
     column.setRowId(false);
     return column;
 }

 public String getBeanClassName()
 {
     return beanClassName;
 }

 public Column getColumn(String s)
 {
     Column column = (Column)columns.get(s);
     if(column == null)
         column = (Column)calculatedColumnMap.get(s);
     return column;
 }

 private boolean isCalculated(Column column)
 {
     return calculatedColumnMap.get(column.getName()) != null;
 }

 public int getSize()
 {
     return beans.size();
 }

 public void setValue(String s, Object obj)
     throws BeanTableException
 {
     if(!editing && !inserting)
         exception(new BeanTableException("not in edit state"));
     Column column = getColumn(s);
     if(column == null)
         exception(new BeanTableException("column \"" + s + "\" not found"));
     if(isCalculated(column))
     {
         if(calcColumnHandler == null)
             exception(new BeanTableException("calculated column handler not available"));
         else
             try
             {
                 calcColumnHandler.setValue(this, s, obj);
             }
             catch(BeanTableException beantableexception)
             {
                 exception(beantableexception);
             }
     } else
     {
         Method method = ((PropertyDescriptor)propertyDescriptors.get(s)).getWriteMethod();
         if(method == null)
             exception(new BeanTableException("setter not found: property \"" + s + "\""));
         try
         {
             method.invoke(beans.get(currentRow), new Object[] {
                 obj
             });
         }
         catch(InvocationTargetException invocationtargetexception)
         {
             exception(new BeanTableException("error writing property \"" + s + "\"", invocationtargetexception.getTargetException()));
         }
         catch(Exception exception1)
         {
             exception(new BeanTableException("error writing property \"" + s + "\"", exception1));
         }
     }
     if(!settingValue)
         fireDataChangeEvent(currentRow, null, -1);
 }

 public Object getValue(String s)
     throws BeanTableException
 {
     return getValue(s, currentRow);
 }

 public Object getValue(String s, int i)
     throws BeanTableException
 {
     if(i < 0 || i >= beans.size())
         exception(new BeanTableException("invalid row"));
     Column column = getColumn(s);
     if(column == null)
         exception(new BeanTableException("column \"" + s + "\" not found"));
     if(isCalculated(column))
     {
         if(calcColumnHandler == null)
             exception(new BeanTableException("calculated column handler not available"));
         else
             try
             {
                 return calcColumnHandler.calcValue(this, s, i);
             }
             catch(BeanTableException beantableexception)
             {
                 exception(beantableexception);
             }
     } else
     {
         PropertyDescriptor propertydescriptor = (PropertyDescriptor)propertyDescriptors.get(s);
         try
         {
             return propertydescriptor.getReadMethod().invoke(beans.get(i), null);
         }
         catch(InvocationTargetException invocationtargetexception)
         {
             exception(new BeanTableException("error reading property \"" + s + "\"", invocationtargetexception.getTargetException()));
         }
         catch(Exception exception1)
         {
             exception(new BeanTableException("error reading property \"" + s + "\"", exception1));
         }
     }
     return null;
 }

 public int getCurrentRow()
 {
     return currentRow;
 }

 public Object getRow()
 {
     return getRow(currentRow);
 }

 public Object getRow(int i)
 {
     return beans.get(i);
 }

 public void setCurrentRow(int i)
     throws BeanTableException
 {
     if(editing || inserting)
         return; //??? exception(new BeanTableException("cannot navigate when editing"));
     if(currentRow != i)
     {
         if(i < 0 || i >= beans.size())
             exception(new BeanTableException("invalid row"));
         currentRow = i;
         fireNavigationEvent();
     }
 }

 public boolean isOpen()
 {
     return open;
 }

 public boolean isEditing()
 {
     return editing;
 }

 public boolean isInserting()
 {
     return inserting;
 }

 private Object cloneBean(Object obj)
     throws BeanTableException
 {
     try
     {
         if((java.lang.Cloneable.class).isAssignableFrom(obj.getClass()))
             return obj.getClass().getMethod("clone", null).invoke(obj, null);
         Object obj1 = obj.getClass().newInstance();
         String as[] = (String[])propertyDescriptors.keySet().toArray(new String[0]);
         for(int i = 0; i < as.length; i++)
         {
             PropertyDescriptor propertydescriptor = (PropertyDescriptor)propertyDescriptors.get(as[i]);
             if(propertydescriptor.getReadMethod() != null && propertydescriptor.getWriteMethod() != null)
                 propertydescriptor.getWriteMethod().invoke(obj1, new Object[] {
                     propertydescriptor.getReadMethod().invoke(obj, null)
                 });
         }

         return obj1;
     }
     catch(InvocationTargetException invocationtargetexception)
     {
         exception(new BeanTableException("error cloning bean", invocationtargetexception.getTargetException()));
     }
     catch(Exception exception1)
     {
         exception(new BeanTableException("error cloning bean", exception1));
     }
     return null;
 }

 public void startEdit()
     throws BeanTableException
 {
     if(!editing)
     {
         if(!open)
             exception(new BeanTableException("table is closed"));
         if(inserting)
             exception(new BeanTableException("inserting new row"));
         if(beans.size() == 0)
             exception(new BeanTableException("table is empty"));
         backupBean = beans.get(currentRow);
         beans.set(currentRow, cloneBean(backupBean));
         editing = true;
         changedEditors.clear();
         fireStatusEvent(3, currentRow);
     }
 }

 public void delete()
     throws BeanTableException
 {
     if(resolver == null)
         exception(new BeanTableException("no resolver available"));
     if(!open)
         exception(new BeanTableException("table is closed"));
     if(inserting)
         exception(new BeanTableException("inserting new row"));
     if(editing)
         exception(new BeanTableException("editing row"));
     if(beans.size() == 0)
         exception(new BeanTableException("table is empty"));
     int i = currentRow;
     try
     {
         resolver.removeBean(beans.get(currentRow));
     }
     catch(BeanTableException beantableexception)
     {
         exception(beantableexception);
     }
     beans.remove(currentRow);
     correctPostion();
     fireDataChangeEvent(i, null, 1);
 }

 public void insert(int i)
     throws BeanTableException
 {
     if(!inserting)
     {
         if(!open)
             exception(new BeanTableException("table is closed"));
         if(editing)
             exception(new BeanTableException("editing row"));
         if(i < 0 || i > beans.size())
             exception(new BeanTableException("invalid row"));
         try
         {
             Object obj = beanInfo.getBeanDescriptor().getBeanClass().newInstance();
             beans.add(i, obj);
             inserting = true;
             currentRow = i;
             changedEditors.clear();
         }
         catch(Exception exception1)
         {
             exception(new BeanTableException("error creating new bean", exception1));
         }
         fireStatusEvent(4, i);
     }
 }

 public void save()
     throws BeanTableException
 {
     if(resolver == null)
         exception(new BeanTableException("no resolver available"));
     Iterator iterator = changedEditors.iterator();
     HashMap hashmap = new HashMap();
     while(iterator.hasNext()) 
     {
         Editor editor = (Editor)iterator.next();
         try
         {
             hashmap.put(editor.getColumnName(), editor.getValue());
         }
         catch(Exception exception1)
         {
             exception(new BeanTableException("error getting value from editor", exception1));
         }
     }
     for(Iterator iterator1 = hashmap.entrySet().iterator(); iterator1.hasNext();)
     {
         java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
         try
         {
             settingValue = true;
             setValue((String)entry.getKey(), entry.getValue());
         }
         finally
         {
             settingValue = false;
         }
     }

     if(editing)
     {
         try
         {
             resolver.updateBean(backupBean, beans.get(currentRow));
         }
         catch(BeanTableException beantableexception)
         {
             exception(beantableexception);
         }
         editing = false;
         fireStatusEvent(7, currentRow);
     } else
     if(inserting)
     {
         try
         {
             resolver.insertBean(beans.get(currentRow));
         }
         catch(BeanTableException beantableexception1)
         {
             exception(beantableexception1);
         }
         inserting = false;
         fireStatusEvent(7, currentRow);
     }
     Object obj = beans.get(currentRow);
     if(sort())
     {
         fireDataChangeEvent();
         for(int i = 0; i < beans.size(); i++)
         {
             if(beans.get(i) != obj)
                 continue;
             setCurrentRow(i);
             break;
         }

     }
 }

 private void correctPostion()
 {
     if(beans.size() > 0 && currentRow >= beans.size())
         currentRow = beans.size() - 1;
 }

 public void cancel()
     throws BeanTableException
 {
     if(editing)
     {
         beans.set(currentRow, backupBean);
         editing = false;
         fireStatusEvent(5, currentRow);
     } else
     if(inserting)
     {
         beans.remove(currentRow);
         correctPostion();
         inserting = false;
         fireStatusEvent(6, currentRow);
     }
 }

 public void valueChanged(EditorEvent editorevent)
 {
     if(open && !editing && !inserting)
         if(getSize() > 0)
             try
             {
                 startEdit();
             }
             catch(BeanTableException beantableexception) { }
         else
             try
             {
                 insert(0);
             }
             catch(BeanTableException beantableexception1) { }
     changedEditors.add(editorevent.getSource());
 }

 private void exception(BeanTableException beantableexception)
     throws BeanTableException
 {
     fireErrorEvent(beantableexception);
     throw beantableexception;
 }

 public void setCalculatedColumns(Column acolumn[])
 {
     calculatedColumns = acolumn;
     calculatedColumnMap.clear();
     if(acolumn != null)
     {
         for(int i = 0; i < acolumn.length; i++)
             calculatedColumnMap.put(acolumn[i].getName(), acolumn[i]);

     }
     fireStatusEvent(8);
 }

 public Column[] getCalculatedColumns()
 {
     return calculatedColumns;
 }

 public void setCalcColumnHandler(CalcColumnHandler calccolumnhandler)
 {
     calcColumnHandler = calccolumnhandler;
 }

 public CalcColumnHandler getCalcColumnHandler()
 {
     return calcColumnHandler;
 }

 private String beanClassName;
 protected BeanInfo beanInfo;
 private ArrayList beans;
 private HashMap propertyDescriptors;
 private HashMap columns;
 private HashMap calculatedColumnMap;
 private Column calculatedColumns[];
 private int currentRow;
 private ArrayList navigationListeners;
 private ArrayList statusListeners;
 private ArrayList errorListeners;
 private ArrayList dataChangeListeners;
 private boolean open;
 private boolean editing;
 private boolean inserting;
 private boolean settingValue;
 private BeanProvider provider;
 private BeanResolver resolver;
 private Object backupBean;
 private HashSet changedEditors;
 private CalcColumnHandler calcColumnHandler;
 private SortDescriptor sortDescriptor;
}


