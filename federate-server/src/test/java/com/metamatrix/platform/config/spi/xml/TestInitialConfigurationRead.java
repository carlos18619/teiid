/*
 * JBoss, Home of Professional Open Source.
 * Copyright (C) 2008 Red Hat, Inc.
 * Copyright (C) 2000-2007 MetaMatrix, Inc.
 * Licensed to Red Hat, Inc. under one or more contributor 
 * license agreements.  See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package com.metamatrix.platform.config.spi.xml;

import java.util.Collection;

import com.metamatrix.common.config.CurrentConfiguration;
import com.metamatrix.common.config.api.ConfigurationModelContainer;
import com.metamatrix.platform.config.BaseTest;
import com.metamatrix.platform.config.CurrentConfigHelper;


/**
*  This only test one call to the CurrentConfiguration because it assumes
*  nothing else has run to load the configuration in the VM.  
* 
*  As with TestCurrentConfiguration, it doesn't know what order the methods will
*  be called that it has to clear the cache everytime.
*/
public class TestInitialConfigurationRead extends BaseTest{  
	
	private static String PRINCIPAL = "TestInitialConfigurationRead";       //$NON-NLS-1$
        
    public TestInitialConfigurationRead(String name) {
        super(name);
       
    }

	protected void setUp() throws Exception {
		super.setUp();
			CurrentConfigHelper.initConfig(CONFIG_FILE, this.getPath(), PRINCIPAL);			    		

	}    
    
    
    /**
    */
    public void testValidateReader() {
    	printMsg("Starting TestInititaltConfigurationRead");    	 //$NON-NLS-1$

                    
      try {
      	
      		createSystemProperties("config.xml"); //$NON-NLS-1$
      		
			// do the reset after setting the system properties
     		CurrentConfiguration.getInstance().reset();
      		   		    		      	
            validConfigurationModel();
 
      } catch (Exception e) {
    		fail(e.getMessage());
    	}
    	printMsg("Completed TestInititaltConfigurationRead"); //$NON-NLS-1$
 
    }
    
    private void validConfigurationModel() throws Exception {
        ConfigurationModelContainer ccm = CurrentConfiguration.getInstance().getConfigurationModel();
        if (ccm == null) {
            fail("Configuration Model was not obtained from CurrentConfiguration"); //$NON-NLS-1$
        }
        
  	 	Collection providers = ccm.getConfiguration().getAuthenticationProviders();
        if (providers == null || providers.size() == 0) {
        	fail("no providers"); //$NON-NLS-1$
        }
        
        HelperTestConfiguration.validateModelContents(ccm);
       
        System.out.println("Providers "+ providers.size()); //$NON-NLS-1$
    } 
    
        
    
    
}

