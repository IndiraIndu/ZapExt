/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright The OWASP ZAP Development Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package org.zaproxy.zap.db.sql;

import java.io.File;

import org.apache.log4j.Logger;
import org.parosproxy.paros.db.Database;



public class HsqldbDatabase extends SqlDatabase implements Database {
	
    private static final Logger log = Logger.getLogger(HsqldbDatabase.class);

	public HsqldbDatabase() {
		super();
	}
	
    /* (non-Javadoc)
	 * @see org.parosproxy.paros.db.DatabaseIF#close(boolean)
	 */
    // ZAP: Added JavaDoc.
    @Override
	public void close(boolean compact) {
        // ZAP: Moved the content of this method to the method close(boolean,
        // boolean) and changed to call that method instead.
        close(compact, true);
    }

    /* (non-Javadoc)
	 * @see org.parosproxy.paros.db.DatabaseIF#deleteSession(java.lang.String)
	 */
    @Override
	public void deleteSession(String sessionName) {
    	super.deleteSession(sessionName);
		log.debug("deleteSession " + sessionName);

		deleteDbFile(new File(sessionName));
        deleteDbFile(new File(sessionName + ".data"));
        deleteDbFile(new File(sessionName + ".script"));
        deleteDbFile(new File(sessionName + ".properties"));
        deleteDbFile(new File(sessionName + ".backup"));
        deleteDbFile(new File(sessionName + ".lobs"));
    }
    
    private void deleteDbFile (File file) {
    	log.debug("Deleting " + file.getAbsolutePath());
		if (file.exists()) {
			if (! file.delete()) {
	            log.error("Failed to delete " + file.getAbsolutePath());
			}
		}
    }

	@Override
	public void open(String path) throws ClassNotFoundException, Exception {
		log.debug("open " + path);
	    setDatabaseServer(new HsqldbDatabaseServer(path));
	    notifyListenerDatabaseOpen();
	}

    /* (non-Javadoc)
	 * @see org.parosproxy.paros.db.DatabaseIF#close(boolean, boolean)
	 */
	@Override
	public void close(boolean compact, boolean cleanup) {
		log.debug("close");
		super.close(compact, cleanup);
	    if (this.getDatabaseServer() == null) {
	    	return;
	    }
	    
	    try {
	        // shutdown
	    	((HsqldbDatabaseServer)this.getDatabaseServer()).shutdown(compact);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
	}
	
	@Override
	public boolean isFileBased () {
		return true;
	}
	
}
