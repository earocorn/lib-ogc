/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;


/**
 * <p><b>Title:</b><br/>
 * Stream Demultiplexer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Concrete implementations of this interface are responsible for
 * registering a DataStreamParser for each type of cluster. Thus
 * a given parser will be used for a given cluster ID.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Aug 12, 2005
 * @version 1.0
 */
public interface StreamDemux
{
	public void addDataParser(String clusterID, DataStreamParser dataParser);
	
	
	public void removeDataParser(String clusterID);
	
	
	public DataStreamParser getDataParser(String clusterID);
}