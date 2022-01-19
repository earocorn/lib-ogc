/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.Asserts;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.impl.AbstractFeatureImpl;


/**
 * <p>
 * Generic implementation of a GML feature.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 20, 2007
 * */
public class GenericFeatureImpl extends AbstractFeatureImpl implements GenericFeature
{
    private static final long serialVersionUID = -5445631329812411360L;
    protected QName qname;
    protected Map<QName, Object> properties;


    // constructor to be called by subclasses when overriding getQName directly
    protected GenericFeatureImpl()
    {
        this.properties = new LinkedHashMap<>();
    }
    
    
    public GenericFeatureImpl(QName qname)
    {
        this.qname = Asserts.checkNotNull(qname, QName.class);
        this.properties = new LinkedHashMap<>();
    }
    
    
    protected GenericFeatureImpl(QName qname, Map<QName, Object> properties)
    {
        this.qname = Asserts.checkNotNull(qname, QName.class);
        this.properties = Asserts.checkNotNull(properties, Map.class);
    }


    @Override
    public QName getQName()
    {
        return qname;
    }
    
    @Override
    public String getType()
    {
        if (properties != null)
        {
            // look for a 'type' property of type String
            for (var prop: properties.entrySet())
            {
                if ("type".equals(prop.getKey().getLocalPart()))
                {
                    if (prop.getValue() instanceof String)
                        return (String)prop.getValue();
                    else if (prop.getValue() instanceof IXlinkReference<?>)
                        return ((IXlinkReference<?>)prop.getValue()).getHref();
                }
            }
        }
        
        return super.getType();
    }

    
    @Override
    public Map<QName, Object> getProperties()
    {
        return properties;
    }
  
    
    @Override
    public void setProperty(QName qname, Object prop)
    {
        if (prop != null)
        {
            properties.put(qname, prop);
            
            // also set as geometry if prop is of geometry type
            if (prop instanceof AbstractGeometry)
                setGeometry((AbstractGeometry)prop);
        }
    }
    
    
    @Override
    public Object getProperty(QName qname)
    {
        return properties.get(qname);
    }
    
    
    @Override
    public void setProperty(String name, Object prop)
    {
        if (prop != null)
            properties.put(new QName(name), prop);
    }
    
    
    @Override
    public Object getProperty(String name)
    {
        return properties.get(new QName(name));
    }
}
